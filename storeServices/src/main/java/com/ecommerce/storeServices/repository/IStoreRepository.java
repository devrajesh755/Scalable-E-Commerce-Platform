package com.ecommerce.storeServices.repository;

import com.ecommerce.storeServices.model.Store;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IStoreRepository extends JpaRepository<Store,Long>{

    Store findByAadhaarId(String aadhaarId);
    boolean existsByAadhaarId(String aadhaarId);

    void deleteByAadhaarId(String aadhaarId);

    Store findByStoreName(String storeName);
}
