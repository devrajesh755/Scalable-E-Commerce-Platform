package com.ecommerce.storeServices.service;

import com.ecommerce.storeServices.dto.StoreProductRequest;
import com.ecommerce.storeServices.dto.StoreProductResponse;
import com.ecommerce.storeServices.model.Store;
import com.ecommerce.storeServices.model.StoreProduct;
import com.ecommerce.storeServices.repository.IProductRepository;
import com.ecommerce.storeServices.repository.IStoreRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductServices {

    @Autowired
    private IStoreRepository iStoreRepository;
    @Autowired
    private IProductRepository iProductRepository;

    public String addProductInStore(Long storeId, StoreProductRequest storeProductRequest)
    {
        Optional<Store> store =  iStoreRepository.findById(storeId);

        if(store.isPresent())
        {
            StoreProduct storeProduct = new StoreProduct();
            mapToStoreProduct(storeProduct,storeProductRequest,store.get());
            iProductRepository.save(storeProduct);
            return "Product Add Successfully";
        }
        return "Store Dose's Found";
    }

    public List<StoreProductResponse> getProductByName(String name)
    {
           List<StoreProduct> storeProductList = iProductRepository.findByName(name);
           if(storeProductList==null)
               return null;
           return storeProductList.stream().map(this::mapToStoreProductResponse).toList();
    }


    public List<StoreProductResponse> getProductByStoreNameAndProductName(String storeName, String storeProductName)
    {
        Store store = iStoreRepository.findByStoreName(storeName);
        if(store==null)
             return null;
       List<StoreProduct> storeProductsList =  iProductRepository.findByName(storeProductName);
       return storeProductsList.stream()
              .map(this::mapToStoreProductResponse)
              .toList();

    }

    public StoreProductResponse updateProduct(Long storeProductId,StoreProductRequest storeProductRequest)
    {
         Optional<StoreProduct> storeProduct = iProductRepository.findById(storeProductId);
         if(storeProduct.isPresent())
         {
             UpdateStoreProduct(storeProduct.get(),storeProductRequest);
             return mapToStoreProductResponse(storeProduct.get());

         }
         return null;
    }
    public StoreProductResponse deletedProductById(Long storeProductId)
    {
        Optional<StoreProduct> storeProduct = iProductRepository.findById(storeProductId);
        if(storeProduct.isEmpty())
            return null;
        iProductRepository.deleteById(storeProductId);
        return mapToStoreProductResponse(storeProduct.get());
    }






    private void UpdateStoreProduct(StoreProduct storeProduct,StoreProductRequest storeProductRequest)
    {

        storeProduct.setName(storeProductRequest.getName());
        storeProduct.setDescription(storeProductRequest.getDescription());
        storeProduct.setPrice(storeProductRequest.getPrice());
    }

    private void mapToStoreProduct(StoreProduct storeProduct,StoreProductRequest storeProductRequest,Store store)
    {

        storeProduct.setName(storeProductRequest.getName());
        storeProduct.setDescription(storeProductRequest.getDescription());
        storeProduct.setPrice(storeProductRequest.getPrice());
        storeProduct.setStore(store);
    }

    private StoreProductResponse mapToStoreProductResponse(StoreProduct storeProduct)
    {
        return new StoreProductResponse(
                storeProduct.getId(),
                storeProduct.getName(),
                storeProduct.getDescription(),
                storeProduct.getPrice(),
                storeProduct.getStore().getId(),
                storeProduct.getStore().getStoreName()
        );
    }

}