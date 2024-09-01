package com.sparta26.baemin.order.service;

import com.sparta26.baemin.dto.order.*;
import com.sparta26.baemin.dto.orderproduct.RequestOrderProductDto;
import com.sparta26.baemin.dto.orderproduct.ResponseOrderProductDto;
import com.sparta26.baemin.exception.exceptionsdefined.BadRequestException;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UnauthorizedException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.order.client.MemberServiceClient;
import com.sparta26.baemin.order.client.PaymentServiceClient;
import com.sparta26.baemin.order.client.ProductServiceClient;
import com.sparta26.baemin.order.client.StoreServiceClient;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.order.entity.OrderProduct;
import com.sparta26.baemin.order.entity.OrderStatus;
import com.sparta26.baemin.order.entity.OrderType;
import com.sparta26.baemin.order.repository.OrderProductRepository;
import com.sparta26.baemin.order.repository.OrderRepository;
import com.sparta26.baemin.payment.entity.Payment;
import com.sparta26.baemin.product.entity.Product;
import com.sparta26.baemin.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sparta26.baemin.member.entity.UserRole.ROLE_CUSTOMER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberServiceClient memberServiceClient;
    private final StoreServiceClient storeServiceClient;
    private final ProductServiceClient productServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;


    @Transactional
    public ResponseOrderCreateDto createOrder(
            RequestOrderCreateDto request, String email
    ) {

        Member member = memberServiceClient.getMemberByEmail(email);

        Store store = storeServiceClient.getStoreById(request.getStoreId());

        if (ROLE_CUSTOMER.equals(member.getRole()) &&
                OrderType.OFFLINE.name().equals(request.getOrderType())) {
            log.error(
                    "고객이 오프라인 주문을 시도 { userId : " + email +
                            ", requestOrderType : " + request.getOrderType() + " }"
            );
            throw new UnauthorizedException("Customer can only create online orders.");
        }

        // 주문 객체 생성
        Order order = createOrder(request, member, store);

        // 결제 요청
        Payment payment;
        try {
            payment = paymentServiceClient.pay(
                    request.getCardNumber(), order.getTotalPrice());

        } catch (BadRequestException e) {
            log.error(
                    "결제 실패 { userId : " + email +
                            ", errorMessage : " + e.getMessage() + " }"
            );
            throw new BadRequestException("Pay not completed.");
        }

        // 주문에 결제 데이터를 추가하고 결제 상태 'CONFIRM' 으로 변경
        order.addPayment(payment);

        // TODO 상품 재고 감량 처리

        return ResponseOrderCreateDto.createResponseOrderCreateDto(
                order.getId().toString(),
                order.getStore().getId().toString(),
                order.getTotalPrice(),
                order.getStatus().name()
        );
    }

    @Transactional(readOnly = true)
    public ResponseOrderDto getOrder(String orderId, Long userId) {

        Order order = getOrderByIdAndMemberId(orderId, userId);

        return entityToResponseOrderDto(order);
    }

    @Transactional(readOnly = true)
    public Page<ResponseOrderDto> getOrders(
            String status, String search, Pageable pageable, String email, String role
    ) {

        Page<Order> orders = orderRepository.getOrdersByRole(
                status,
                search,
                role,
                email,
                pageable
        );

        return orders.map(this::entityToResponseOrderDto);
    }

    @Transactional
    public ResponseOrderDto updateOrder(
            RequestOrderUpdateDto request, String orderId, Long userId, String role
    ) {

        if (UserRole.ROLE_OWNER.equals(UserRole.fromString(role))) {
            throw new BadRequestException("Owner can only change the order status");
        }

        Order order = getOrderByRole(orderId, userId, UserRole.fromString(role));

        order.updateOrder(
                request.getAddress(),
                request.getOrderRequest(),
                request.getDeliveryRequest()
        );

        return entityToResponseOrderDto(order);
    }

    @Transactional
    public ResponseOrderDto changeOrderStatus(
            RequestOrderStatusDto request, String orderId,
            Long userId, String role, String email
    ) {

        UserRole userRole = UserRole.fromString(role);

        if (!request.isCancelRequest() && ROLE_CUSTOMER.equals(userRole)) {
            throw new UnauthorizedException(
                    "Required permissions to access this resource");
        }

        Order order = getOrderByRole(orderId, userId, userRole);

        if (request.isCancelRequest()) {
            return cancelOrder(role, email, order);
        }

        order.updateStatus(OrderStatus.fromString(request.getNewStatus()));

        return entityToResponseOrderDto(order);
    }

    @Transactional
    public Boolean deleteOrder(String orderId, String email) {
        Order order = getOrderById(orderId);
        order.delete(email);
        return true;
    }

    private Order createOrder(RequestOrderCreateDto request, Member member, Store store) {
        Order order = Order.createOrder(
                request.getAddress(),
                OrderType.fromString(request.getOrderType()),
                request.getOrderRequest(),
                request.getDeliveryRequest(),
                member,
                store,
                member.getEmail(),
                OrderStatus.PENDING,
                requestOrderProductDtoToEntity(
                        request.getOrderProducts(), member.getEmail()
                ).toArray(new OrderProduct[0])
        );

        return orderRepository.save(order);
    }

    private Order getOrderByRole(String orderId, Long userId, UserRole userRole) {
        return switch (userRole) {
            case ROLE_CUSTOMER -> getOrderByIdAndMemberId(orderId, userId);
            case ROLE_OWNER -> getOrderByIdAndStoreId(orderId);
            case ROLE_MANAGER, ROLE_MASTER -> getOrderById(orderId);
        };
    }

    private Order getOrderById(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private Order getOrderByIdAndMemberId(String orderId, Long userId) {
        return orderRepository.findByIdAndMember_IdAndIsPublic(
                UUID.fromString(orderId),
                userId
        ).orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private Order getOrderByIdAndStoreId(String orderId) {

        // TODO createdBy 로 Store 가져오기 메서드 만들어달라고 하기

        String storeId = "";

        return orderRepository.findByIdAndStore_IdAndIsPublic(
                UUID.fromString(orderId),
                UUID.fromString(storeId)
        ).orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private ResponseOrderDto cancelOrder(String role, String email, Order order) {

        if (ROLE_CUSTOMER.equals(UserRole.fromString(role)) &&
                LocalDateTime.now().isAfter(order.getCreatedAt().plusSeconds(300))
        ) {
            log.error(
                    "고객이 주문생성 5분이 지난 후 수정 요청 { email : " + email +
                            ", orderId : " + order.getId().toString() + " }"
            );
            throw new BadRequestException(
                    "Customer can only cancel your order within 5 minutes");
        }

        try {
            paymentServiceClient.cancelPay(
                    order.getPayment().getId().toString(), "CANCEL", role, email);
        } catch (BadRequestException e) {
            log.error(
                    "결제 취소 실패 { email : " + email +
                            ", errorMessage : " + e.getMessage() + " }"
            );
            throw new BadRequestException("Pay cancel not completed.");
        }

        order.cancelOrder(OrderStatus.CANCEL);

        return entityToResponseOrderDto(order);
    }
    
    private ResponseOrderDto entityToResponseOrderDto(Order order) {
        return ResponseOrderDto.createResponseOrderDto(
                order.getId().toString(),
                order.getAddress(),
                order.getOrderType().name(),
                order.getOrderRequest(),
                order.getDeliveryRequest(),
                order.getTotalPrice(),
                order.getStore().getId().toString(),
                entityToResponseOrderProductDto(order.getOrderProducts()),
                order.getPayment().getId().toString(),
                order.getStatus().name()
        );
    }

    private List<ResponseOrderProductDto> entityToResponseOrderProductDto(
            List<OrderProduct> orderProducts
    ) {
        return orderProducts.stream()
                .map(orderProduct ->
                        ResponseOrderProductDto.createResponseOrderProductDto(
                                orderProduct.getProduct().getId().toString(),
                                orderProduct.getProduct().getName(),
                                orderProduct.getPrice(),
                                orderProduct.getAmount()
                        ))
                .toList();
    }

    private List<OrderProduct> requestOrderProductDtoToEntity(
            List<RequestOrderProductDto> request,
            String email
    ) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (RequestOrderProductDto orderProductDto : request) {
            Product product =
                    productServiceClient.getProductById(orderProductDto.getProductId());

            orderProducts.add(OrderProduct.createOrderProduct(
                    orderProductDto.getPrice(),
                    orderProductDto.getAmount(),
                    product,
                    email
            ));
        }

        orderProductRepository.saveAll(orderProducts);

        return orderProducts;
    }
}
