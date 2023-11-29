package com.example.abibCrawlingJava.repository;

import com.example.abibCrawlingJava.entiey.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
}
// TODO : 구현체를 구현해서 사용하는 방법 생각해봐야함.