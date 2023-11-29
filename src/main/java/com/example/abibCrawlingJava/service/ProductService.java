package com.example.abibCrawlingJava.service;

import com.example.abibCrawlingJava.entiey.CcTempProduct;
import com.example.abibCrawlingJava.entiey.Product;
import com.example.abibCrawlingJava.entiey.ProductHistory;
import com.example.abibCrawlingJava.repository.CcProductHistoryRepository;
import com.example.abibCrawlingJava.repository.CcProductRepository;
import com.example.abibCrawlingJava.repository.CcTempProductRepository;
import com.example.abibCrawlingJava.util.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final CcTempProductService ccTempProductService;
    private final CcProductRepository ccProductRepository;
    private final CcTempProductRepository ccTempProductRepository;
    private final CcProductHistoryRepository ccProductHistoryRepository;
    private final Common common;

    @Transactional
    public String processProducts(List<Product> productList, int productCount) {
        for(Product product : productList){
            try {
                System.out.println("1");
                ccTempProductService.insertIntoTempProduct(product.getProdCode(), product.getSiteType()); // cc 템플릿에 저장

                Product foundProduct = ccProductRepository.findByComplexAttributes(
                        product.getProdCode(),
                        product.getSiteType(),
                        product.getSiteDepth1(),
                        product.getSiteDepth2(),
                        product.getSiteDepth3()
                );
                long randomNum = (long) (Math.random() * (10000000000L - 1000000000L) + 1000000000L);
                String randomNumber = Long.toString(randomNum);

                //현재 날짜 형식 변경
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
                String formattedDateTime = now.format(formatter);

                System.out.println("2");
                if (productCount == 0 || foundProduct == null) { // siteType 확인, DB 저장된 값 확인
                    System.out.println("3");
                    if (product.getPrice() > 0) {
                        System.out.println("4");
                        String filePath = common.downloadImage(product); // 이미지 다운로드
                        System.out.println("5");
                        product.setImg(filePath); // 이미지 경로 저장
                        System.out.println("6");
                        ccProductRepository.save(product); // 값 DB에 저장
                        System.out.println("7");
                        ProductHistory productHistory = new ProductHistory();
                        productHistory.setHistoryNo(product.getSiteType() + formattedDateTime + randomNumber);
                        productHistory.setProductNo(product); // 물품 가격 변동 확인을 위해 product를 받아서 넣음
                        productHistory.setSiteType(product.getSiteType());
                        productHistory.setProdCode(product.getProdCode());
                        productHistory.setPrice(product.getPrice());

                        //제품 이력 저장
                        ccProductHistoryRepository.save(productHistory);
                        log.info("새 제품이 추가되었습니다");
                    }
                    System.out.println("10");
                    continue;
                }
//                 들어온 값과 DB의 Img값이 다를 경우
                if (!foundProduct.getImg().equals(product.getImg())) {
                    String filePath = common.downloadImage(product);
                    product.setImg(filePath);
                    ccProductRepository.save(product); //DB에 다시 저장
                    // 제품 이름, 브랜드 변경시
                } else if (!foundProduct.getProdName().equals(product.getProdName()) ||
                        !foundProduct.getBrand().equals(product.getBrand())) {
                    ccProductRepository.save(product);
                    // getSoldOut 이 null 값이여서 equals로 비교시 에러
                } else if( foundProduct.getSoldOut() != product.getSoldOut()){ // 품절, 입고시
                    ccProductRepository.save(product);
                }  else {
                    // 가격 변경시
                    if (foundProduct.getPrice() != (product.getPrice())) {
                        ProductHistory productHistory = new ProductHistory();
                        productHistory.setHistoryNo(product.getSiteType() + formattedDateTime + randomNumber);
                        productHistory.setProductNo(product);
                        productHistory.setSiteType(product.getSiteType());
                        productHistory.setProdCode(product.getProdCode());
                        productHistory.setPrice(product.getPrice());

                        if (product.getSiteType().equals("CL") && product.getSoldOut().equals("일시품절")) {
                            product.setPrice(foundProduct.getPrice());
                            productHistory.setPrice(foundProduct.getPrice());
                        }

                        //제품 이력 저장
                        ccProductHistoryRepository.save(productHistory);
                        ccProductRepository.save(product);
                    }
                }

            }catch (DataIntegrityViolationException e) {
                log.warn("예외가 발생했습니다"+ e.getMessage());
            }catch(Exception e){
                log.warn(" 예외 발생 : " + e.getMessage());
                System.out.println("0000");
            }

        } // for문 안에서 돌고 있어서 try catch에 잡혀도 에러 메세지 띄우고 다음 내용 진행됨
        return "끝남";
    }

    @Transactional
    public int countBySiteType(String siteType) {
        try {
            int count = ccProductRepository.countBySiteType(siteType);
            return (count > 0) ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}