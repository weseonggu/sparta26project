package com.sparta26.baemin.order.service;

import com.sparta26.baemin.dto.order.*;
import com.sparta26.baemin.dto.orderproduct.RequestOrderProductDto;
import com.sparta26.baemin.dto.orderproduct.ResponseOrderProductDto;
import com.sparta26.baemin.exception.exceptionsdefined.BadRequestException;
import com.sparta26.baemin.exception.exceptionsdefined.NotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UnauthorizedException;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.order.client.MemberClient;
import com.sparta26.baemin.order.client.ProductClient;
import com.sparta26.baemin.order.client.StoreClient;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.order.entity.OrderStatus;
import com.sparta26.baemin.order.entity.OrderType;
import com.sparta26.baemin.order.repository.OrderRepository;
import com.sparta26.baemin.orderproduct.entity.OrderProduct;
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

    private final MemberClient memberClient;
    private final StoreClient storeClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    @Transactional
    public ResponseOrderCreateDto createOrder(
            RequestOrderCreateDto request, Long userId
    ) {

        Member member = memberClient.getMemberById(userId);

        Store store = storeClient.getStoreById(request.getStoreId());

        if (UserRole.ROLE_CUSTOMER.equals(member.getRole()) &&
                OrderType.OFFLINE.name().equals(request.getOrderType())) {
            throw new UnauthorizedException("Customer can only create online orders.");
        }

        Order order = createOrder(request, member, store);

        //TODO 결제

        return ResponseOrderCreateDto.createResponseOrderCreateDto(
                order.getId().toString(),
                order.getStore().getId().toString(),
                order.getTotalPrice(),
                order.getStatus().name()
        );
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

    @Transactional(readOnly = true)
    public ResponseOrderDto getOrder(String orderId, Long userId) {

        Order order = getOrderByIdAndMemberId(orderId, userId);

        return entityToResponseOrderDto(order);
    }

    @Transactional(readOnly = true)
    public Page<ResponseOrderDto> getOrders(
            String status, String search,Pageable pageable, String email, String role
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
            RequestOrderUpdateDto request,
            String orderId, Long userId, String email, String role
    ) {

        Order order = switch (UserRole.fromString(role)) {
            case ROLE_CUSTOMER ->
                    updateOrderByCustomer(request, orderId, userId, email);
            case ROLE_OWNER ->
                    updateOrderStatus(request, orderId, email);
            case ROLE_MANAGER, ROLE_MASTER ->
                    updateOrderByMaster(request, orderId, email);
        };

        return entityToResponseOrderDto(order);
    }

    public Boolean deleteOrder(String orderId, String email) {
        Order order = getOrderById(orderId);
        order.delete(email);
        return true;
    }

    private Order getOrderById(String orderId) {
        return orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private Order getOrderByIdAndMemberId(String orderId, Long userId) {
        return orderRepository.findByIdAndMember_Id(
                UUID.fromString(orderId),
                userId
        ).orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private Order updateOrderStatus(
            RequestOrderUpdateDto request, String orderId, String email
    ) {

        Order order = getOrderById(orderId);
        order.updateStatus(OrderStatus.valueOf(request.getNewStatus()), email);
        return order;
    }

    private Order updateOrderByCustomer(
            RequestOrderUpdateDto request, String orderId, Long userId, String email
    ) {

        Order order = getOrderByIdAndMemberId(orderId, userId);
        if (request.isCancelRequest()) {
            if (LocalDateTime.now().isAfter(order.getCreatedAt().plusSeconds(300))) {
                throw new BadRequestException(
                        "Customer can only cancel your order within 5 minutes");
            }
            order.updateStatus(OrderStatus.CANCEL, email);
            return order;
        }

        order.updateOrderByCustomer(
                request.getAddress(),
                request.getOrderRequest(),
                request.getDeliveryRequest(),
                email
        );

        return order;
    }

    private Order updateOrderByMaster(
            RequestOrderUpdateDto request, String orderId, String email
    ) {

        Order order = getOrderById(orderId);
        order.updateOrderByMaster(
                request.getAddress(),
                request.getOrderRequest(),
                request.getDeliveryRequest(),
                email,
                OrderStatus.valueOf(request.getNewStatus())
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
                    productClient.getProductById(orderProductDto.getProductId());

            orderProducts.add(new OrderProduct(
                    orderProductDto.getPrice(),
                    orderProductDto.getAmount(),
                    product,
                    email
            ));
        }
        return orderProducts;
    }
}
