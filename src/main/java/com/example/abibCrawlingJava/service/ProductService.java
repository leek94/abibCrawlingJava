package com.example.abibCrawlingJava.service;

import com.example.abibCrawlingJava.dto.ProductDTO;
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
    private final CcProductHistoryRepository ccProductHistoryRepository;
    private final Common common;

    @Transactional
    public String processProducts(List<ProductDTO> productDTOList, int productCount) {
        for(ProductDTO productDTO : productDTOList){
            try {
                ccTempProductService.insertIntoTempProduct(productDTO.getProdCode(), productDTO.getSiteType()); // cc 템플릿에 저장

                Product foundProduct = ccProductRepository.findByComplexAttributes(
                        productDTO.getProdCode(),
                        productDTO.getSiteType(),
                        productDTO.getSiteDepth1(),
                        productDTO.getSiteDepth2(),
                        productDTO.getSiteDepth3()
                );
                long randomNum = (long) (Math.random() * (10000000000L - 1000000000L) + 1000000000L);
                String randomNumber = Long.toString(randomNum);

                //현재 날짜 형식 변경
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
                String formattedDateTime = now.format(formatter);
                if (productCount == 0 || foundProduct == null) { // siteType 확인, DB 저장된 값 확인
                    if (Integer.parseInt(productDTO.getPrice()) > 0) {
                        String filePath = common.downloadImage(productDTO); // 이미지 다운로드
                        productDTO.setImg(filePath); // 이미지 경로 저장
                        Product product = ccProductRepository.save(productDTO.toEntity()); // 값 DB에 저장
                        ProductHistory productHistory = new ProductHistory();
                        productHistory.setHistoryNo(productDTO.getSiteType() + formattedDateTime + randomNumber);
                        productHistory.setProductNo(foundProduct); // id가 있는 값을 찾아서 넣어야해서 foundProduct 값을 넣음
                        productHistory.setSiteType(productDTO.getSiteType());
                        productHistory.setProdCode(productDTO.getProdCode());
                        productHistory.setPrice(Integer.parseInt(productDTO.getPrice()));
                        //제품 이력 저장
                        ccProductHistoryRepository.save(productHistory);
                        log.info("새 제품이 추가되었습니다");
                    }
                    continue;

                }
//                 들어온 값과 DB의 Img값이 다를 경우
                if (!foundProduct.getImg().equals(productDTO.getImg())) {

                    String filePath = common.downloadImage(productDTO);
                    productDTO.setImg(filePath);
                    ccProductRepository.save(productDTO.toEntity()); //DB에 다시 저장
                    // 제품 이름, 브랜드 변경시
                } else if (!foundProduct.getProdName().equals(productDTO.getProdName()) ||
                        !foundProduct.getBrand().equals(productDTO.getBrand())) {
                    ccProductRepository.save(productDTO.toEntity());

                    // getSoldOut 이 null 값이여서 equals로 비교시 에러
                } else if( foundProduct.getSoldOut() != productDTO.getSoldOut()){ // 품절, 입고시
                    ccProductRepository.save(productDTO.toEntity());

                }  else {

                    // 가격 변경시
                    if (foundProduct.getPrice() != Integer.parseInt(productDTO.getPrice())) {

                        ProductHistory productHistory = new ProductHistory();
                        productHistory.setHistoryNo(productDTO.getSiteType() + formattedDateTime + randomNumber);
                        productHistory.setProductNo(foundProduct);
                        productHistory.setSiteType(productDTO.getSiteType());
                        productHistory.setProdCode(productDTO.getProdCode());
                        productHistory.setPrice(Integer.parseInt(productDTO.getPrice()));

                        if (productDTO.getSiteType().equals("CL") && productDTO.getSoldOut().equals("일시품절")) {
                            productDTO.setPrice(String.valueOf(foundProduct.getPrice()));
                            productHistory.setPrice(foundProduct.getPrice());

                        }


                        //제품 이력 저장
                        ccProductHistoryRepository.save(productHistory);
                        ccProductRepository.save(productDTO.toEntity());
                    }
                }

            }catch (DataIntegrityViolationException e) {
                log.warn("예외가 발생했습니다"+ e.getMessage());
            }catch(Exception e){
                log.warn(" 예외 발생 : " + e.getMessage());
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