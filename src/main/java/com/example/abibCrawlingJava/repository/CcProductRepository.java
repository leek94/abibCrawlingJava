package com.example.abibCrawlingJava.repository;

import com.example.abibCrawlingJava.entiey.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CcProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.prodCode = :prodCode AND p.siteType = :siteType AND p.siteDepth1 = :siteDepth1 AND p.siteDepth2 = :siteDepth2 AND p.siteDepth3 = :siteDepth3")
    Optional<Product> findByComplexAttributes(@Param("prodCode") String prodCode, @Param("siteType") String siteType, @Param("siteDepth1") String siteDepth1, @Param("siteDepth2") String siteDepth2, @Param("siteDepth3") String siteDepth3);

//    @Query("SELECT p FROM Product p WHERE p.prodCode = :prodCode AND p.siteType = :siteType AND p.siteDepth1 = :siteDepth1 AND p.siteDepth2 = :siteDepth2 AND p.siteDepth3 = :siteDepth3")
//    List<Product> findByComplexAttributes(@Param("prodCode") String prodCode, @Param("siteType") String siteType, @Param("siteDepth1") String siteDepth1, @Param("siteDepth2") String siteDepth2, @Param("siteDepth3") String siteDepth3);


    @Query("SELECT COUNT(p) FROM Product p WHERE p.siteType = :siteType")
    int countBySiteType(@Param("siteType") String siteType);
}

