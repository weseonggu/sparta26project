package com.sparta26.baemin.order.service;

import com.sparta26.baemin.dto.order.*;
import com.sparta26.baemin.dto.orderproduct.RequestOrderProductDto;
import com.sparta26.baemin.dto.orderproduct.ResponseOrderProductDto;
import com.sparta26.baemin.dto.payment.ResponsePaymentInfoDto;
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
import com.sparta26.baemin.order.entity.OrderStatus;
import com.sparta26.baemin.order.entity.OrderType;
import com.sparta26.baemin.order.repository.OrderRepository;
import com.sparta26.baemin.orderproduct.entity.OrderProduct;
import com.sparta26.baemin.orderproduct.repository.OrderProductRepository;
import com.sparta26.baemin.payment.entity.Payment;
import com.sparta26.baemin.payment.entity.PaymentStatus;
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
            RequestOrderCreateDto request, Long userId
    ) {

        Member member = memberServiceClient.getMemberById(userId);

        Store store = storeServiceClient.getStoreById(request.getStoreId());

        if (UserRole.ROLE_CUSTOMER.equals(member.getRole()) &&
                OrderType.OFFLINE.name().equals(request.getOrderType())) {
            log.error(
                    "고객이 오프라인 주문을 시도 { userId : " + userId +
                            ", requestOrderType : " + request.getOrderType() + " }"
            );
            throw new UnauthorizedException("Customer can only create online orders.");
        }

        // 주문 객체 생성
        Order order = createOrder(request, member, store);

        // 결제 요청
        ResponsePaymentInfoDto responsePaymentInfoDto =
                paymentServiceClient.pay(
                        request.getCardNumber(), order.getTotalPrice()
                );
        // 결제 응답이 'COMPLETE' 가 아니면 예외 발생
        if (!PaymentStatus.COMPLETE.equals(responsePaymentInfoDto.getStatus())) {
            throw new BadRequestException("Pay not completed.");
        }

        // 주문에 저장하기 위한 결제 객체 생성
        Payment payment = Payment.builder()
                .id(UUID.fromString(responsePaymentInfoDto.getId()))
                .status(PaymentStatus.fromString(responsePaymentInfoDto.getStatus()))
                .cardNumber(responsePaymentInfoDto.getCardNumber())
                .payDate(responsePaymentInfoDto.getPayDate())
                .totalPrice(responsePaymentInfoDto.getTotalPrice())
                .build();

        // 주문에 결제 데이터를 추가하고 결제 상태 'CONFIRM' 으로 변경
        order.addPayment(payment);

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

        Order order = switch (UserRole.fromString(role)) {
            case ROLE_CUSTOMER ->
                    updateOrderByCustomer(request, orderId, userId);
            case ROLE_OWNER ->
                    updateOrderStatus(request, orderId);
            case ROLE_MANAGER, ROLE_MASTER ->
                    updateOrderByMaster(request, orderId);
        };

        return entityToResponseOrderDto(order);
    }

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

    private Order updateOrderStatus(RequestOrderUpdateDto request, String orderId) {

        Order order = getOrderById(orderId);
        order.updateStatus(OrderStatus.fromString(request.getNewStatus()));
        return order;
    }

    private Order updateOrderByCustomer(
            RequestOrderUpdateDto request, String orderId, Long userId
    ) {

        Order order = getOrderByIdAndMemberId(orderId, userId);
        if (request.isCancelRequest()) {
            if (LocalDateTime.now().isAfter(order.getCreatedAt().plusSeconds(300))) {
                log.error(
                        "고객이 주문생성 5분이 지난 후 수정 요청 { userId : " + userId +
                                ", orderId : " + orderId + " }"
                );
                throw new BadRequestException(
                        "Customer can only cancel your order within 5 minutes");
            }
            order.updateStatus(OrderStatus.CANCEL);
            return order;
        }

        order.updateOrderByCustomer(
                request.getAddress(),
                request.getOrderRequest(),
                request.getDeliveryRequest()
        );

        return order;
    }

    private Order updateOrderByMaster(RequestOrderUpdateDto request, String orderId) {

        Order order = getOrderById(orderId);
        order.updateOrderByMaster(
                request.getAddress(),
                request.getOrderRequest(),
                request.getDeliveryRequest(),
                OrderStatus.fromString(request.getNewStatus())
        );
        return order;
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
