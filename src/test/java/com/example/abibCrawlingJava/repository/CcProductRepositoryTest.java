package com.example.abibCrawlingJava.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // 레포지토리 테스트시 사용 데이터베이스 관련 빈들만 로드하여 테스트 가능
class CcProductRepositoryTest {

    @Autowired
    CcProductRepository ccProductRepository;

    @Test
    @DisplayName("맴버 조회하기")
    void findByCom() {
        // given

        // when

        // then
    }

}