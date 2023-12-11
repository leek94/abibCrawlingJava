package com.example.abibCrawlingJava.entiey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("product가 생성되는지 확인하는 테스트 코드")
    void createMember() {
        // given
        Product product = Product.builder()
                .img("image")
                .img2(" ")
                .info("/p/s/e/d")
                .prodName("잘 팔리는 제품")
                .prodCode("12345")
                .price(36000)
                .bePrice(45000)
                .sale(0.8)
                .soldOut("")
                .siteDepth1("화장품")
                .siteDepth2("남성")
                .siteDepth3("*")
                .siteType("BB")
                .brand("잘팔아")
                .infoCoupang("c/o/u")
                .build();
        // when

        // then
        Assertions.assertThat(product.getImg()).isEqualTo("image");
        Assertions.assertThat(product.getImg2()).isEqualTo(" ");
        Assertions.assertThat(product.getProdName()).isEqualTo("잘 팔리는 제품");
        Assertions.assertThat(product.getProdCode()).isEqualTo("12345");
        Assertions.assertThat(product.getPrice()).isEqualTo(36000);
        Assertions.assertThat(product.getBePrice()).isEqualTo(45000);
        Assertions.assertThat(product.getSale()).isEqualTo(0.8);
        Assertions.assertThat(product.getSoldOut()).isEqualTo("");
        Assertions.assertThat(product.getSiteDepth1()).isEqualTo("화장품");
        Assertions.assertThat(product.getSiteDepth2()).isEqualTo("남성");
        Assertions.assertThat(product.getSiteDepth3()).isEqualTo("*");
        Assertions.assertThat(product.getSiteType()).isEqualTo("BB");
        Assertions.assertThat(product.getBrand()).isEqualTo("잘팔아");
        Assertions.assertThat(product.getInfoCoupang()).isEqualTo("c/o/u");
    }
}