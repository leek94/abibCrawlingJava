package com.example.abibCrawlingJava;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케줄링 기능 활성화
@EnableBatchProcessing // 배치 기능 활성화
@SpringBootApplication
public class AbibCrawlingJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbibCrawlingJavaApplication.class, args);
	}

}
