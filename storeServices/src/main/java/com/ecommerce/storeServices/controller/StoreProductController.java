package com.ecommerce.storeServices.controller;


import com.ecommerce.storeServices.dto.StoreProductRequest;
import com.ecommerce.storeServices.dto.StoreProductResponse;
import com.ecommerce.storeServices.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/StoreProduct")
public class StoreProductController {

    @Autowired
    private ProductServices productServices;

    @PostMapping("/addProduct/{storeId}")
    public ResponseEntity<String> addProduct(@PathVariable Long storeId, @RequestBody StoreProductRequest storeResponse)
    {
       return new ResponseEntity<String>(productServices.addProductInStore(storeId,storeResponse), HttpStatus.OK);
    }

    @GetMapping("/getAllProduct/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name)
    {
        List<StoreProductResponse> storeProductResponseList = productServices.getProductByName(name);
        if(storeProductResponseList.isEmpty())
            return new ResponseEntity<String>("Product Not Found",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<List<StoreProductResponse>>(storeProductResponseList,HttpStatus.OK);
    }


    @GetMapping("/getProductByStoreNameAndProductName/{storeName}/{productName}")
    public ResponseEntity<?> getProductByStoreNameAndProductName(@PathVariable  String storeName, @PathVariable String productName)
    {
         List<StoreProductResponse>  storeProductResponseList = productServices.getProductByStoreNameAndProductName(storeName,productName);
         if(storeProductResponseList==null)
             return new ResponseEntity<String>("Store Name or Product Not Found",HttpStatus.BAD_REQUEST);
         return new ResponseEntity<List<StoreProductResponse>>(storeProductResponseList,HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{storeProductId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long storeProductId, @RequestBody StoreProductRequest storeProductRequest)
    {
            StoreProductResponse storeProductResponse = productServices.updateProduct(storeProductId,storeProductRequest);
            if(storeProductResponse == null)
                return new ResponseEntity<String>("Product Not Found",HttpStatus.BAD_REQUEST);
            return new ResponseEntity<StoreProductResponse>(storeProductResponse,HttpStatus.OK);
    }

    @DeleteMapping("/deletedProduct/{storeProductId}")
    public ResponseEntity<?> deletedProductById(@PathVariable Long storeProductId)
    {
        StoreProductResponse storeProductResponse =  productServices.deletedProductById(storeProductId);
        if(storeProductResponse == null)
            return new ResponseEntity<String>("Id Don't Found",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<StoreProductResponse>(storeProductResponse,HttpStatus.OK);

    }
}
