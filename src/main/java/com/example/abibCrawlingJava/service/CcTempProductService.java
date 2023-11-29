package com.example.abibCrawlingJava.service;

import com.example.abibCrawlingJava.entiey.CcTempProduct;
import com.example.abibCrawlingJava.repository.CcTempProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CcTempProductService {
    @Autowired
    private CcTempProductRepository ccTempProductRepository;

    @Transactional
    public void insertIntoTempProduct(String prodCode, String siteType) {
        CcTempProduct tempProduct = new CcTempProduct();
        tempProduct.setProdCode(prodCode);
        tempProduct.setSiteType(siteType);

        ccTempProductRepository.save(tempProduct);
    }
}
