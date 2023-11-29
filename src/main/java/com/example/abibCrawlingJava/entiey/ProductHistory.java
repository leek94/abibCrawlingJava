package com.example.abibCrawlingJava.entiey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cc_product_history")
@NoArgsConstructor
@Getter
@Setter
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String historyNo;

    @ManyToOne
    @JoinColumn(name = "product_no")
    private Product productNo;
    private String siteType;
    private String prodCode;
    private int price;
    @Column(name="reg_date")
    private LocalDateTime regDate;

    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now(); // Entity가 생성될 때 현재 시간 설정
    }

    public ProductHistory(String historyNo,  Product productNo, String siteType, String prodCode, int price) {
        this.historyNo = historyNo;
        this.productNo = productNo;
        this.siteType = siteType;
        this.prodCode = prodCode;
        this.price = price;
    }
}
