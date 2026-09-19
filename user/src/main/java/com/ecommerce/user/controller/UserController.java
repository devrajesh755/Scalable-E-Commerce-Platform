package com.ecommerce.user.controller;



import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

//
     private final UserService userService;

     //Constructor Injection
     private UserController(UserService userService)
     {
         this.userService=userService;
     }

     @GetMapping("/getAllUser")
     public ResponseEntity<List<UserResponse>> getAllUsers()
     {
        List<UserResponse> usersList = userService.getAllUser();
         return new ResponseEntity<List<UserResponse>>(usersList,HttpStatus.OK);
     }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
        userService.registerUser(userRequest);
        return new ResponseEntity<String>("Register User Successfully",HttpStatus.CREATED);

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable String id)
    {
        UserResponse user = userService.getUserDetails(id);

        if(user==null)
            return ResponseEntity.notFound().build();
        return new ResponseEntity<UserResponse>(user,HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,@RequestBody UserRequest updateUser)
    {
        UserResponse UpdateUser = userService.updateUser(id,updateUser);

        if(UpdateUser != null){
            return new ResponseEntity<UserResponse>(UpdateUser,HttpStatus.OK);
        }
        return new ResponseEntity<String>("User With id "+id+" Dose Not Exit",HttpStatus.BAD_REQUEST);
    }
}
