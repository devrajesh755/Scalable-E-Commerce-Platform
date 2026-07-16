package com.ecommerce.storeServices.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreRequest {

    @NotNull(message = "Aadhaar ID is mandatory")
    @Size(min=11,max = 12,message = "Id Must Be 12 Digit")
    private String AadhaarId;

    @NotBlank(message = "Store name is mandatory")
    @Size(min = 3, max = 100, message = "Store name must be between 3 and 100 characters")
    private String storeName;

    @Size(min = 3,max = 1000,message = "Description must not exceed 1000 characters")
    private String description;;

    private String logoUrl;
    private String bannerUrl;
}
