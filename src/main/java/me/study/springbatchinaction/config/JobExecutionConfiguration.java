package me.study.springbatchinaction.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobExecutionConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TaskExecutor taskExecutor;

    public JobExecutionConfiguration(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("stepTaskExecutor") TaskExecutor taskExecutor) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.taskExecutor = taskExecutor;
    }

    public Job job() {
        return new JobBuilder("Job", jobRepository)
                .start(step1())
                .next(step2())
                .build();
    }

    private Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((stepContribution, chunkContext) ->  {
                    log.info("step1");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    private Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((stepContribution, chunkContext) ->  {
                    log.info("step2 has executed");
//                    throw new RuntimeException("step2 has failed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

}
