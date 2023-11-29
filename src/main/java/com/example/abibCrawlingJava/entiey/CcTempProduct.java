package com.example.abibCrawlingJava.entiey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cc_temp_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcTempProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sno")
    private Long sno;

    private String prodCode;
    private String siteType;
}
