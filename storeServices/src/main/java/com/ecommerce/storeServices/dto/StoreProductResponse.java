package com.ecommerce.storeServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StoreProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long storeId;

    private String storeName;

}
