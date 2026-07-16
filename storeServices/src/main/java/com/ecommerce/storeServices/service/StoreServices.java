package com.ecommerce.storeServices.service;


import com.ecommerce.storeServices.dto.StoreRequest;
import com.ecommerce.storeServices.dto.StoreResponse;
import com.ecommerce.storeServices.model.Store;
import com.ecommerce.storeServices.repository.IStoreRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreServices
{

    private IStoreRepository iStoreRepository;

    public List<StoreResponse> getAllStore()
    {
         return iStoreRepository.findAll().stream()
                 .map(this::mapToStoreResponse)
                 .toList();
    }

    public StoreResponse getStoreById(Long id)
    {
        Store store = iStoreRepository.findById(id)
                                 .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
        return mapToStoreResponse(store);
    }

    public StoreResponse registerStore(StoreRequest storeRequest)
    {
       Store store = iStoreRepository.findByAadhaarId(storeRequest.getAadhaarId());

        if(store != null)
        {
            return null;
        }
        return mapToStoreResponse(iStoreRepository.save(mapToStore(store,storeRequest)));
    }

    public StoreResponse deleteStoreById(String AadhaarId)
    {
        Store store = iStoreRepository.findByAadhaarId(AadhaarId);
        if(store == null)
        {
            return null;
        }
        iStoreRepository.deleteByAadhaarId(AadhaarId);
        return mapToStoreResponse(store);
    }

    public StoreResponse UpdateStoreDetails(StoreRequest storeRequest,String AadhaarId)
    {
        if(iStoreRepository.existsByAadhaarId(AadhaarId))
        {
            Store store = iStoreRepository.findByAadhaarId(AadhaarId);
            iStoreRepository.save(store);
            return mapToStoreResponse(store);
        }
        return  null;
    }

    public List<StoreResponse> SearchStoreByName(String storeName)
    {
        List<Store> storeList = iStoreRepository.findByStoreName(storeName);

        if(storeList.isEmpty())
        {
            return null;
        }
        return storeList.stream()
                .map(this::mapToStoreResponse)
                .toList();

    }



    private StoreResponse mapToStoreResponse(Store store)
    {
        StoreResponse storeResponse = new StoreResponse();

        storeResponse.setId(store.getId());
        storeResponse.setStoreName(store.getStoreName());
        storeResponse.setDescription(store.getDescription());
        storeResponse.setBannerUrl(store.getBannerUrl());
        storeResponse.setLogoUrl(store.getLogoUrl());

        return storeResponse;

    }
    private Store mapToStore(Store store,StoreRequest storeRequest)
    {
        store.setStoreName(storeRequest.getStoreName());
        store.setLogoUrl(storeRequest.getLogoUrl());
        store.setDescription(storeRequest.getDescription());
        store.setBannerUrl(storeRequest.getBannerUrl());
        store.setAadhaarId(storeRequest.getAadhaarId());

        return store;
    }
}
