package com.ecommerce.order.controller;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.services.CartItemServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    @Autowired
    private CartItemServices cartItemServices;



    @PostMapping("/addCart")
    public ResponseEntity<String > addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request)
    {
       if(cartItemServices.addToCart(userId,request)){
           return ResponseEntity.status(HttpStatus.CREATED).build();
       }
        return ResponseEntity.badRequest().body("Product Out Of Stock or User not Found or Product not found");
    }


    @DeleteMapping("/removeCartItem/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId
    ){
       boolean delete =  cartItemServices.deleteItemFromCart(userId,productId);
       return delete ? ResponseEntity.noContent().build()
                     : ResponseEntity.notFound().build();
    }

    @GetMapping("/getAllCartItem")
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId)
    {
        return ResponseEntity.ok(cartItemServices.getCart(userId));
    }
}
