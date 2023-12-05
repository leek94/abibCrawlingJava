package com.example.abibCrawlingJava.service;

import com.example.abibCrawlingJava.dto.ProductDTO;
import com.example.abibCrawlingJava.entiey.Category;
import com.example.abibCrawlingJava.entiey.Product;
import com.example.abibCrawlingJava.repository.CcProductHistoryRepository;
import com.example.abibCrawlingJava.repository.CcProductRepository;
import com.example.abibCrawlingJava.repository.CcTempProductRepository;
import com.example.abibCrawlingJava.util.Common;
import com.example.abibCrawlingJava.util.ReturnMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Crawling {
   private final ProductService productService;
   private final Common common;

   public void runCrawling() {

      String siteType = "AI";
      String url = "https://abib.com";
      WebDriver driver = common.startCrawling(siteType);
      String siteDepth1 = "";
      String siteDepth2 = "";
      String siteDepth3 = "";

      int maxRetries = 3;
      int retryCount = 0;
      HttpClient httpClient = HttpClientBuilder.create().build(); // httpClient 인스턴스를 생성

      //최대 3번 연결 시도 while문
      while(retryCount < maxRetries)
         try{
            HttpGet request = new HttpGet(url); // url 페이지로 get 요청 생성
            HttpResponse response = httpClient.execute(request); // 요청을 실행하고 응답을 받아서 저장

            if (response.getStatusLine().getStatusCode() == 200) { // 응답이 200 성공일시 break로 중단
               break;
            }
            retryCount++;  // 성공 못할시 count ++ 하여 재시도
         } catch(IOException e){
            log.info("서버와 연결이 불안정해 다시 시도합니다...." + retryCount); // 예외 발생시
            retryCount++;

            if (retryCount == 3) {
               common.sendMail(ReturnMessage.CONNECTION.getMessage(), siteType);
               System.exit(0); // 자바 프로그램 종료
            }
         }

      try {
         Document doc = Jsoup.connect(url).get();

//         Elements depth1Elements = doc.select("#category > ul > li.boldf.xans-record- > a"); // 대 분류 카테고리
         Elements depth1Elements = doc.select("#category > a"); // 소 분류 카테고리


         List<Category> categoryList = new ArrayList<>();
         List<ProductDTO> productDTOList = new ArrayList<>();

         int prodCount = productService.countBySiteType(siteType); // 값이 있으면 1, 없으면 0 반환

         for (Element depth1 : depth1Elements) {
            siteDepth1 = depth1.text();
            String siteUrl = url + depth1.attr("href");
            siteDepth2 = "*";
            siteDepth3 = "";


            categoryList.add(new Category(siteDepth1, siteDepth2, siteUrl));
         }
         categoryList.subList(0, 3).clear(); // 0,1,2 카테고리 삭제

         for (Category category : categoryList) {
            siteDepth1 = category.getSiteDepth1();
            Document doc2 = Jsoup.connect(category.getSiteLink()).get();
            Elements cateCategory = doc2.select("div.main-right > div:nth-child(3) > div:nth-child(2) > ul > li");
            for (int k = 0; k < cateCategory.size(); k++) {
               Element cateCategoryItem = cateCategory.get(k);

               Element imgElement = cateCategoryItem.selectFirst("div > a > img");
               String img = Optional.ofNullable(imgElement)
                       .map(element -> element.attr("src"))
                       .orElse("");

               Element infoElement = cateCategoryItem.selectFirst("div > a ");
               String info = Optional.ofNullable(infoElement)
                       .map(element -> element.attr("href"))
                       .orElse("");

               String brand = "abib";

               Element prodNameElement = cateCategoryItem.selectFirst("div > p > a > span");
               String prodName = common.nullCheck(prodNameElement);

               Element detailElement = cateCategoryItem.selectFirst("div > ul > li:nth-child(2) > span");
               String detail = common.nullCheck(detailElement);

               String name = prodName + " " + detail;

               Element bePriceElement = cateCategoryItem.selectFirst("div > ul > li:nth-child(3) > span:nth-child(2)");
               String bePrice = common.nullCheckPrice(bePriceElement);

               Element priceElement = cateCategoryItem.selectFirst("div > ul >li:nth-child(4) > span");
               String price = common.nullCheckPrice(priceElement);

               if(price.equals("0")){
                  price = bePrice;
                  bePrice = String.valueOf(0);
               }

               String soldOut = (price.equals("0")) ? "일시 품절" : null;

               double sale = ((!bePrice.equals("0") &&! price.equals("0"))? (common.calculateDiscountPercent(bePrice, price)) : 0);

               String prodCode = info.split("product_no=")[1].split("&")[0];

               String infoCoupang = "https://link.coupang.com/a/3IhPI";

               ProductDTO productDTO = new ProductDTO(null, img,"/uploadc/contents/image/" + siteType + "/" + prodCode + ".png","", info
               , infoCoupang, prodName, prodCode, price, bePrice, sale
               ,soldOut, siteDepth1, siteDepth2, siteDepth3, siteType,brand);

               productDTOList.add(productDTO);
               productService.processProducts(productDTOList, prodCount);
               productDTOList.clear();
            }
         }

      } catch (IOException e) {
         log.info("예외 발생 Cralwing : " + e.getMessage());
         e.printStackTrace();
      }
   }
}