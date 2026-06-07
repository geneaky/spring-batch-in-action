package me.study.springbatchinaction.config;

import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobBuilderHelper;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TaskExecutor taskExecutor;

    public JobConfiguration(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("stepTaskExecutor") TaskExecutor taskExecutor) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.taskExecutor = taskExecutor;
    }

    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(flow1())
                .next(step1())
                .end()
                .build();
    }

    private Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((stepContribution, chunkContext) ->  {

                    JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
                    String name = jobParameters.getString("name");
                    Long seq = jobParameters.getLong("seq");
                    Double age = jobParameters.getDouble("age");

                    Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

                    log.info("step1");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    private Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((stepContribution, chunkContext) ->  {
                    log.info("step2");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .taskExecutor(taskExecutor)
                .build();
    }

    private Flow flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
        flowBuilder
                .start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }

}
