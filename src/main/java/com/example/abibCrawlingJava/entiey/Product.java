package com.example.abibCrawlingJava.entiey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cc_product")
@Data
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_no")
    private Long id;
    @Column(nullable = true, columnDefinition = "VARCHAR(255)")
    private String img;
    @Column(nullable = true, columnDefinition = "VARCHAR(255)")
    private String img2;
    @Column(nullable = false, length = 255, columnDefinition = "VARCHAR(255)")
    private String info;
    @Column(name="prod_name", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String prodName;
    @Column(name="prod_code", nullable = false, length = 100, columnDefinition = "VARCHAR(50)")
    private String prodCode;
    @Column(nullable = true, columnDefinition = "INT(10)")
    private int price;
    @Column(nullable = true, columnDefinition = "INT(10)")
    private int bePrice;
    @Column(nullable = true, columnDefinition = "INT(10)")
    private double sale;
    @Column(name="sold_out",nullable = true, length = 50, columnDefinition = "VARCHAR(50)")
    private String soldOut;
    @Column(name="site_depth1", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String siteDepth1;
    @Column(name="site_depth2", nullable = false, length = 100, columnDefinition = "VARCHAR(100)")
    private String siteDepth2;
    @Column(name="site_depth3", nullable = true, length = 100, columnDefinition = "VARCHAR(100)")
    private String siteDepth3;
    @Column(name="site_type",nullable = false, length = 10)
    private String siteType;
    @Column(nullable = true, length = 255)
    private String brand;
    @Column(name ="reg_date")
    private LocalDateTime regDate;
    @Column(name ="mod_date")
    private LocalDateTime modDate;
    @Column(name="info_coupang")
    private String infoCoupang;

    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now(); // Entity가 생성될 때 현재 시간 설정
        modDate = LocalDateTime.now(); // Entity가 생성될 때도 수정일자는 생성일자와 같이 설정
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = LocalDateTime.now(); // Entity가 업데이트될 때 현재 시간으로 수정일자 갱신
    }


    public Product(Long id,String img, String img2, String info, String prodName, String prodCode, int price, int bePrice, double sale, String soldOut, String siteDepth1, String siteDepth2, String siteDepth3, String siteType, String brand, String infoCoupang) {
        this.id = null;
        this.img = img;
        this.img2 = img2;
        this.info = info;
        this.prodName = prodName;
        this.prodCode = prodCode;
        this.price = price;
        this.bePrice = bePrice;
        this.sale = sale;
        this.soldOut = soldOut;
        this.siteDepth1 = siteDepth1;
        this.siteDepth2 = siteDepth2;
        this.siteDepth3 = siteDepth3;
        this.siteType = siteType;
        this.brand = brand;
        this.infoCoupang = infoCoupang;
    }
}