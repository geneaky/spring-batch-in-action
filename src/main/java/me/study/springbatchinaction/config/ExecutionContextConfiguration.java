package me.study.springbatchinaction.config;

import lombok.extern.slf4j.Slf4j;
import me.study.springbatchinaction.listener.JobRepositoryListener;
import me.study.springbatchinaction.tasklet.ExecutionContextTasklet1;
import me.study.springbatchinaction.tasklet.ExecutionContextTasklet2;
import me.study.springbatchinaction.tasklet.ExecutionContextTasklet3;
import me.study.springbatchinaction.tasklet.ExecutionContextTasklet4;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ExecutionContextConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TaskExecutor taskExecutor;
    private final ExecutionContextTasklet1 executionContextTasklet1;
    private final ExecutionContextTasklet2 executionContextTasklet2;
    private final ExecutionContextTasklet3 executionContextTasklet3;
    private final ExecutionContextTasklet4 executionContextTasklet4;
    private final JobRepositoryListener jobRepositoryListener;

    public ExecutionContextConfiguration(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("stepTaskExecutor") TaskExecutor taskExecutor, ExecutionContextTasklet1 executionContextTasklet1, ExecutionContextTasklet2 executionContextTasklet2,
            ExecutionContextTasklet3 executionContextTasklet3, ExecutionContextTasklet4 executionContextTasklet4, JobRepositoryListener jobRepositoryListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.taskExecutor = taskExecutor;
        this.executionContextTasklet1 = executionContextTasklet1;
        this.executionContextTasklet2 = executionContextTasklet2;
        this.executionContextTasklet3 = executionContextTasklet3;
        this.executionContextTasklet4 = executionContextTasklet4;
        this.jobRepositoryListener = jobRepositoryListener;
    }

    public Job job() {
        return new JobBuilder("Job", jobRepository)
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .listener(jobRepositoryListener)
                .build();
    }

    private Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(executionContextTasklet1, transactionManager)
                .build();
    }

    private Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(executionContextTasklet2, transactionManager)
                .build();
    }

    private Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet(executionContextTasklet3, transactionManager)
                .build();
    }

    private Step step4() {
        return new StepBuilder("step4", jobRepository)
                .tasklet(executionContextTasklet4, transactionManager)
                .build();
    }
}
