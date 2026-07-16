package com.ecommerce.storeServices.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private Long id;
    private String AadhaarId;
    private String storeName;
    private String description;
    private String logoUrl;
    private String bannerUrl;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
