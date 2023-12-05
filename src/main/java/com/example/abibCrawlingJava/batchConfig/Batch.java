package com.example.abibCrawlingJava.batchConfig;

import com.example.abibCrawlingJava.service.Crawling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration // spring batch의 모든 Job은 Configuration으로 등록하여 사용
public class Batch {
    private final CustomTasklet customTasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job TaskletJob() {
        return jobBuilderFactory.get("tasklet") // tasklet으로 이름 지정
                .start(TaskStep()) // TaskStep이라는 이름으로 Batch Step 생성
                .build();


    }

    @Bean
    public Step TaskStep() {
        return stepBuilderFactory.get("taskStep") // 이름 지정
                .tasklet(customTasklet) // 기능 수행
                .build();

    }
}
