package com.ecommerce.storeServices.repository;


import com.ecommerce.storeServices.model.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<StoreProduct,Long> {

    List<StoreProduct> findByName(String storeProductName);
}
