package com.ecommerce.order.client;

import com.ecommerce.order.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient
{
    @GetExchange("/api/product/getProductById/{id}")
    ProductResponse getProductDetails(@PathVariable String id);
}
