package com.example.abibCrawlingJava.batchConfig;

import com.example.abibCrawlingJava.service.Crawling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomTasklet implements Tasklet, StepExecutionListener {

private final Crawling crawling;

    @Override
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        log.info("======================= Crawling 시작 =======================");
    }

    @Override
    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("======================= Crawling 완료 =======================");
        return ExitStatus.COMPLETED;
    }
    // 배치에 등록해놓고 컨트롤러로 테스트시 에러가 나는 경우가 있음 --> 컨트롤러용 서비스를 따로 만들어서 테스트를 하든지
    // 스케줄링 시간을 맞춰서 테스트를 진행해야 여러개 등록 에러가 없음(DB에서 Product 찾을때 2개의 값이 찾아지는 에러)
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        try {
            crawling.runCrawling();
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            log.error("CustomTasklet - Error occurred: " + e.getMessage());
            return RepeatStatus.CONTINUABLE; // 예외 발생 시 CONTINUABLE 상태 반환
        }
    }
}
