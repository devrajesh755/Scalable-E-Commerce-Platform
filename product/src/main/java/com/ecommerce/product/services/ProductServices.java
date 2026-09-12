package com.ecommerce.product.services;



import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest)
    {
        Product product = new Product();
        mapToProduct(product,productRequest);
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProduct() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }


    public ProductResponse getProductDetails(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent())
        {
            return mapToProductResponse(optionalProduct.get());
        }
        return null;
    }

    public ProductResponse updateProduct(Long id, ProductRequest updateProductRequest)
    {
        Optional<Product> exitProduct = productRepository.findById(id);
        if(exitProduct.isPresent()){
            Product product = exitProduct.get();
            mapToProduct(product ,updateProductRequest);
            productRepository.save(product);
            return mapToProductResponse(product);
        }
        return null;
    }

    public ProductResponse deleteProduct(Long id)
    {
        Optional<Product> OptionalProduct = productRepository.findById(id);
        if(OptionalProduct.isPresent()){
            productRepository.deleteById(id);
            return mapToProductResponse(OptionalProduct.get());
        }
        return null;
    }



    private void mapToProduct(Product product , ProductRequest productRequest)
    {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }


    public Optional<ProductResponse> productExitById(String id){
       return  productRepository.findByIdAndActiveTrue(Long.valueOf(id)).
                  map(this::mapToProductResponse);

    }

    private ProductResponse mapToProductResponse(Product product) {

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCategory(product.getCategory());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setActive(product.getActive());

        return productResponse;
    }

    public List<ProductResponse> searchProduct(String Keyword){
        return productRepository.searchProduct(Keyword).stream()
                                 .map(this::mapToProductResponse)
                                  .collect(Collectors.toList());
    }


}
