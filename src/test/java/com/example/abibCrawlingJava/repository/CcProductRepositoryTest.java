package com.example.abibCrawlingJava.repository;

import com.example.abibCrawlingJava.entiey.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest// 레포지토리 테스트시 사용 데이터베이스 관련 빈들만 로드하여 테스트 가능
//@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-h2.yml")
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CcProductRepositoryTest {
//    @PersistenceContext
//    private TestEntityManager entityManager;

    @Autowired
    CcProductRepository ccProductRepository;


    @Test
    @DisplayName("프로덕트 만들기")
    void findByCom() {
        // given
        Product product1 = Product.builder()
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
        Product result1 = ccProductRepository.save(product1);

        // then
        assertThat(result1.getImg()).isEqualTo(product1.getImg());
        assertThat(result1.getImg2()).isEqualTo(product1.getImg2());
        assertThat(result1.getProdCode()).isEqualTo(product1.getProdCode());
        assertThat(result1.getProdName()).isEqualTo(product1.getProdName());
        assertThat(result1.getPrice()).isEqualTo(product1.getPrice());
        assertThat(result1.getBePrice()).isEqualTo(product1.getBePrice());
        assertThat(result1.getSale()).isEqualTo(product1.getSale());
        assertThat(result1.getSoldOut()).isEqualTo(product1.getSoldOut());
        assertThat(result1.getSiteDepth1()).isEqualTo(product1.getSiteDepth1());
        assertThat(result1.getSiteDepth2()).isEqualTo(product1.getSiteDepth2());
        assertThat(result1.getSiteDepth3()).isEqualTo(product1.getSiteDepth3());
        assertThat(result1.getSiteType()).isEqualTo(product1.getSiteType());
        assertThat(result1.getSiteType()).isEqualTo("BB");
        assertThat(result1.getBrand()).isEqualTo(product1.getBrand());
        assertThat(result1.getInfoCoupang()).isEqualTo(product1.getInfoCoupang());
    }

    @Test
    @DisplayName("프로덕트의 리스트를 반환 하는지 확인")
    void ProductList() {
        // given
        Product product1 = Product.builder()
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

        Product product2 = Product.builder()
                .img("image1")
                .img2(" ")
                .info("/p/s/e/d/f")
                .prodName("잘 팔려야 하는 제품")
                .prodCode("123456")
                .price(40000)
                .bePrice(50000)
                .sale(0.8)
                .soldOut("")
                .siteDepth1("화장품")
                .siteDepth2("여성")
                .siteDepth3("*")
                .siteType("CC")
                .brand("잘팔아")
                .infoCoupang("c/o/u")
                .build();

        Product save1 = ccProductRepository.save(product1);
        Product save2 = ccProductRepository.save(product2);
        Long id = save1.getId();
        Long id2 = save2.getId();
        // when
        List<Product> result = ccProductRepository.findAll();
        System.out.println("result.size()" + result.size());

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}