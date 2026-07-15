package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private String product_id;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
