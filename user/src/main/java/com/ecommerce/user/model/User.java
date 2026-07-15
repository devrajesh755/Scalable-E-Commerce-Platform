package com.ecommerce.user.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;



@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;


    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String number;
    private UserRole role = UserRole.CUSTOMER;


    private Address address;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedBy
    private LocalDateTime updateAt;
}
