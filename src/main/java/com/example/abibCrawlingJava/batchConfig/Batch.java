package com.example.abibCrawlingJava.batchConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Batch {
    private final CustomTasklet customTasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job TaskletJob(){
        Job crawlingJob = jobBuilderFactory.get("tasklet")
                .start(TaskStep())
                .build();

        return crawlingJob;
    }

    @Bean
    public Step TaskStep() {
        return stepBuilderFactory.get("taskStep")
                .tasklet(customTasklet)
                .build();

    }
}
