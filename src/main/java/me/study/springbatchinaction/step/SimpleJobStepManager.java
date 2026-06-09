package me.study.springbatchinaction.step;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleJobStepManager {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final @Qualifier("stepTaskExecutor") TaskExecutor taskExecutor;


    public Step step1() {
//        LocalDateTime now = LocalDateTime.now().plusSeconds(5);
        return new StepBuilder("simplejob-step1", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("Executing simplejob-step1");
//                    if(LocalDateTime.now().isBefore(now)) {
//                        return RepeatStatus.CONTINUABLE;
//                    }
                    if(1==1) {
                        throw new RuntimeException("step1 was failed");
                    }

                    return null;
                }, transactionManager)
                .startLimit(3)
//                .allowStartIfComplete(true)
                .taskExecutor(taskExecutor)
                .build();
    }

    public Step step2() {
        return new StepBuilder("simplejob-step2", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("Executing simplejob-step2");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .startLimit(1)
                .allowStartIfComplete(true)
                .build();
    }

    public Step step3() {
        return new StepBuilder("simplejob-step3", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("Executing simplejob-step3");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }
}
