package com.example.abibCrawlingJava.controller;

import com.example.abibCrawlingJava.service.Crawling;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final Crawling crawling;

    @GetMapping("/test")
    public ResponseEntity test() {

        long start = System.currentTimeMillis();
        crawling.runCrawling();
        long end = System.currentTimeMillis();
        System.out.println("걸리는 시간 : " + (end - start));
        return new ResponseEntity(HttpStatus.OK);

    }
}