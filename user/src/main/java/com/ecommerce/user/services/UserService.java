package com.ecommerce.user.services;


import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;



    public List<UserResponse> getAllUser()
    {
        return repository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void registerUser(UserRequest userRequest)
    {
         User user  = new User();
         updateUserFromRequest(user,userRequest);
         repository.save(user);
    }

    public UserResponse getUserDetails(String id) {
        Optional<User> optionalUser = repository.findById(String.valueOf(id));
        if(optionalUser.isPresent())
        {
              return mapToUserResponse(optionalUser.get());
        }
        return null;
    }


    public UserResponse updateUser(String id, UserRequest updateUserRequest) {
        Optional<User> exitUser = repository.findById(String.valueOf(id));
        if(exitUser.isPresent()){
           User user = exitUser.get();
           updateUserFromRequest(user,updateUserRequest);
           repository.save(user);
           return mapToUserResponse(user);
        }
        return null;
    }




    private UserResponse mapToUserResponse(User user)
    {
        UserResponse userResponsiveness = new UserResponse();

        userResponsiveness.setId(String.valueOf(user.getId()));
        userResponsiveness.setNumber(user.getNumber());
        userResponsiveness.setEmail(user.getEmail());
        userResponsiveness.setFirstName(user.getFirstName());
        userResponsiveness.setLastName(user.getLastName());
        userResponsiveness.setRole(user.getRole());

        if(user.getAddress() != null)
        {
            AddressDTO addressDTO = new AddressDTO();

            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setState(user.getAddress().getState());

            userResponsiveness.setAddress(addressDTO);
        }
        return userResponsiveness;
    }

    private void updateUserFromRequest(User user , UserRequest userRequest)
    {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setNumber(userRequest.getNumber());
        if(userRequest.getAddress() != null)
        {
            Address address =  new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());

            user.setAddress(address);
        }
    }


}
