package me.study.springbatchinaction.job;

import lombok.extern.slf4j.Slf4j;
import me.study.springbatchinaction.incrementer.CustomJobParametersIncrementer;
import me.study.springbatchinaction.step.SimpleJobStepManager;
import me.study.springbatchinaction.validator.CustomJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Slf4j
@Component
public class SimpleJobManager {

    private final SimpleJobStepManager stepManager;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public SimpleJobManager(SimpleJobStepManager stepManager, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.stepManager = stepManager;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job batchJob() {
        return new JobBuilder("simplejob-test", jobRepository)
                .start(stepManager.step1())
                .next(stepManager.step2())
                .next(stepManager.step3())
//                .preventRestart()
//                .incrementer(new CustomJobParametersIncrementer())
                .incrementer(new RunIdIncrementer())
//                .validator(new CustomJobParameterValidator())
//                .validator(new DefaultJobParametersValidator(new String[]{"name"}, new String[]{"age"}))
                .build();

    }
}
