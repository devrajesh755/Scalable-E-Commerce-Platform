package com.ecommerce.product.controller;



import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.services.ProductServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {


    private final ProductServices productServices;

    private ProductController(ProductServices productServices){
        this.productServices = productServices;
    }


    @PostMapping("/addProduct")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest)
    {
        productServices.addProduct(productRequest);
        return new ResponseEntity<String>("Product Adds Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/GetAllProduct")
    public ResponseEntity<List<ProductResponse>> getAllProduct()
    {
        return new ResponseEntity<List<ProductResponse>>(productServices.getAllProduct(),HttpStatus.OK);
    }

//    @GetMapping("/getProductById/{id}")
//    public ResponseEntity<?> getProductById(@PathVariable  Long id)
//    {
//       ProductResponse productResponse = productServices.getProductDetails(id);
//       if(productResponse!=null)
//           return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
//        return new ResponseEntity<String>("Product With Id "+id+" Dose Not Exist",HttpStatus.BAD_REQUEST);
//    }


    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProductDatils(@PathVariable Long id,@RequestBody ProductRequest productRequest)
    {
        ProductResponse productResponse = productServices.updateProduct(id,productRequest);

        if(productResponse != null){
            System.out.println(1);
            return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
        }

        return new ResponseEntity<String>("Product With Id "+id+" Dose Not Exist",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteProductById/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id)
    {
        ProductResponse productResponse = productServices.deleteProduct(id);
        if(productResponse!=null)
            return new ResponseEntity<String>("Product Deleted Successfully",HttpStatus.OK);
        return new ResponseEntity<String>("Product With Id "+id+" Dose Not Found",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword)
    {
        return ResponseEntity.ok(productServices.searchProduct(keyword));
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable  String id)
    {
        return productServices.productExitById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }



}
