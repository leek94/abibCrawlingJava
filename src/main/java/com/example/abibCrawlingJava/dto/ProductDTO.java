package com.example.abibCrawlingJava.dto;

import com.example.abibCrawlingJava.entiey.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;
    private String imgPath;
    private String img;
    private String img2;
    private String info;
    private String infoCoupang;
    private String prodName;
    private String prodCode;
    private String price;
    private String bePrice;
    private double sale;
    private String soldOut;
    private String siteDepth1;
    private String siteDepth2;
    private String siteDepth3;
    private String siteType;
    private String brand;

    public Product toEntity(){
        return Product.builder()
                .id(null)
                .img(img)
                .img2(img2)
                .info(info)
                .infoCoupang(infoCoupang)
                .prodName(prodName)
                .prodCode(prodCode)
                .price(Integer.parseInt(price))
                .bePrice(Integer.parseInt(bePrice))
                .sale(sale)
                .soldOut(soldOut)
                .siteDepth1(siteDepth1)
                .siteDepth2(siteDepth2)
                .siteDepth3(siteDepth3)
                .siteType(siteType)
                .brand(brand)
                .build();
    }
}
