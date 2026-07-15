package com.ecommerce.user.dto;



import com.ecommerce.user.model.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private UserRole role;
    private AddressDTO address;
}
