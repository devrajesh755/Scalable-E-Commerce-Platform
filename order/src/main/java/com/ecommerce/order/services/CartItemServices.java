package com.ecommerce.order.services;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class CartItemServices {



    private final CartItemRepository cartItemRepository;


    public boolean addToCart(String userId, CartItemRequest request){
//        //Look For Product
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if(productOpt.isEmpty())
//            return false;
//
//        Product product = productOpt.get();
//        if(product.getStockQuantity() < request.getQuantity())
//            return false;
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty())
//            return false;
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());

        if(existingCartItem!=null)
        {
            //Update The Quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
            //existingCartItem.setPrice((product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity()))));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        }
        else{
            //Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            //cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(2000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId){

//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
         CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId,productId);
        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
//            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }

        return true;
    }

    public List<CartItem> getCart(String userId){

        return  cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
