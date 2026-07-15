package com.ecommerce.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private AddressDTO address;
}
