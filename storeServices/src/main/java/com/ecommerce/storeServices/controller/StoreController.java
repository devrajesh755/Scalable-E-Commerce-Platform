package com.ecommerce.storeServices.controller;

import com.ecommerce.storeServices.dto.StoreRequest;
import com.ecommerce.storeServices.dto.StoreResponse;
import com.ecommerce.storeServices.service.StoreServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/storeService")
@AllArgsConstructor
public class StoreController {


    private final StoreServices storeServices;

    @GetMapping("/getAllStoreDetails")
    public ResponseEntity<List<StoreResponse>> getAllStoreDetails()
    {
       return new ResponseEntity<List<StoreResponse>>(storeServices.getAllStore(), HttpStatus.OK);
    }

    @GetMapping("/getStore/{id}")
    public ResponseEntity<?> getStoreDetails(@PathVariable Long id)
    {
              return new ResponseEntity<>(storeServices.getStoreById(id),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> addStore(@Valid @RequestBody StoreRequest storeRequest)
    {
        StoreResponse storeResponse = storeServices.registerStore(storeRequest);

        if(storeResponse == null)
            return new ResponseEntity<String>("Store All Ready Exist",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<StoreResponse>(storeResponse,HttpStatus.CREATED);

    }

    @PutMapping("/updateStoreDetails/{AadhaarId}")
    public ResponseEntity<?> updateStoreDetails(@RequestBody StoreRequest storeRequest,@PathVariable String AadhaarId)
    {
        StoreResponse storeResponse = storeServices.UpdateStoreDetails(storeRequest,AadhaarId);
        if(storeResponse!=null)
            return new ResponseEntity<StoreResponse>(storeResponse,HttpStatus.OK);
        return new ResponseEntity<String>("Store With This AadhaarId Dose's Exist",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/searchStoreByName/{storeName}")
    public ResponseEntity<?>  searchStoreByName(@PathVariable String storeName)
    {
       StoreResponse storeResponse = storeServices.SearchStoreByName(storeName);
       if(storeResponse == null)
           return new ResponseEntity<String>("Store Not Found",HttpStatus.BAD_REQUEST);
       return new ResponseEntity<StoreResponse>(storeResponse,HttpStatus.OK);
    }

    @DeleteMapping("/deleteStore/{AadhaarId}")
    public ResponseEntity<String> deleteStore(@PathVariable String AadhaarId)
    {
         StoreResponse storeResponse = storeServices.deleteStoreById(AadhaarId);
         if(storeResponse == null)
             return new ResponseEntity<String>("Store With This AadhaarId Dose's Exist",HttpStatus.BAD_REQUEST);
         return new ResponseEntity<String>("Store Deleted Successfully",HttpStatus.OK);
    }





}
