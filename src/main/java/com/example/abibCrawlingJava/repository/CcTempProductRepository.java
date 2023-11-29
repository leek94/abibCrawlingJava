package com.example.abibCrawlingJava.repository;

import com.example.abibCrawlingJava.entiey.CcTempProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcTempProductRepository extends JpaRepository<CcTempProduct, Long> {
}
