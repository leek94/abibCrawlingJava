package com.example.abibCrawlingJava.service;

import com.example.abibCrawlingJava.dto.ProductDTO;
import com.example.abibCrawlingJava.entiey.Product;
import com.example.abibCrawlingJava.repository.CcProductHistoryRepository;
import com.example.abibCrawlingJava.repository.CcProductRepository;
import com.example.abibCrawlingJava.util.Common;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    // test 주체
    ProductService productService;

    // test 협력자
    @MockBean
    CcProductRepository ccProductRepository;
    @MockBean
    CcTempProductService ccTempProductService;
    @MockBean
    CcProductHistoryRepository ccProductHistoryRepository;
    @MockBean
    Common common;

    @BeforeEach
    void setUp(){ productService = new ProductService(ccTempProductService,ccProductRepository,ccProductHistoryRepository,common);}

    @Test
    @DisplayName("맴버 생성 성공")
    void createMemberSuccess() {
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

        ReflectionTestUtils.setField(product1, "id", 1L);
        ReflectionTestUtils.setField(product2, "id", 2L);

        Mockito.when(ccProductRepository.save(product1)).thenReturn(product1);
        Mockito.when(ccProductRepository.save(product2)).thenReturn(product2);

        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);

        List<ProductDTO> productDTOList = convertToProductDTOList(productList);

        // when
        String hh = productService.processProducts(productDTOList, 1);
        // then
        assertThat(hh).isEqualTo("전체 갯수: 2");
    }
    // proudctList를 DTOList로 변환하는 메서드
    private List<ProductDTO> convertToProductDTOList(List<Product> productList){
        return productList.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
    }

    // product를 DTO로 변환하는 메서드
    private ProductDTO convertToProductDTO(Product product) {
        return ProductDTO.builder()
                .img(product.getImg())
                .img2(product.getImg2())
                .info(product.getInfo())
                .prodName(product.getProdName())
                .prodCode(product.getProdCode())
                .price(String.valueOf(product.getPrice()))
                .bePrice(String.valueOf(product.getBePrice()))
                .sale(product.getSale())
                .soldOut(product.getSoldOut())
                .siteDepth1(product.getSiteDepth1())
                .siteDepth2(product.getSiteDepth2())
                .siteDepth3(product.getSiteDepth3())
                .siteType(product.getSiteType())
                .brand(product.getBrand())
                .infoCoupang(product.getInfoCoupang())
                .build();
    }
}