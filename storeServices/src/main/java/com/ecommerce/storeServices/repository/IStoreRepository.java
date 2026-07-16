package com.ecommerce.storeServices.repository;

import com.ecommerce.storeServices.model.Store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IStoreRepository extends JpaRepository<Store,Long>{

    Store findByAadhaarId(String aadhaarId);
    boolean existsByAadhaarId(String aadhaarId);

    void deleteByAadhaarId(String aadhaarId);

    List<Store> findByStoreName(String storeName);
}
