package com.ecommerce.order.services;


import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderServices {

    private final CartItemServices cartItemServices;

    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId){

        //Validate For Cart items
        List<CartItem> cartItems = cartItemServices.getCart(userId);
        if(cartItems.isEmpty())
        {
                  return Optional.empty();
        }
//        //validate for user
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty())
//        {
//            return Optional.empty();
//        }
//
//        User user = userOptional.get();


        //calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        //create order
        Order  order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPrice(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
         Order savedOrder = orderRepository.save(order);

         //Update stockQuantity


        //clear the cart
         cartItemServices.clearCart(userId);

           return Optional.of(mapToOrderResponse(order));
    }

    private OrderResponse mapToOrderResponse(Order order)
    {
        return new OrderResponse(
                order.getId(),
                order.getPrice(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                        orderItem.getId(),
                                        orderItem.getProductId(),
                                        orderItem.getQuantity(),
                                        orderItem.getPrice(),
                                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                                ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}
