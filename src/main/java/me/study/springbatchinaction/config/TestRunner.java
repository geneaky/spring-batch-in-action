package me.study.springbatchinaction.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final JobConfiguration jobConfiguration;
    private final JobExecutionConfiguration jobExecutionConfiguration;
    private final StepConfiguration stepConfiguration;
    private final StepExecutionConfiguration stepExecutionConfiguration;
    private final StepContributionConfiguration stepContributionConfiguration;
    private final ExecutionContextConfiguration executionContextConfiguration;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("seq", 2L)
                .addDouble("age", 16.5)
                .toJobParameters();

//        jobLauncher.run(jobConfiguration.job(), jobParameters);
//        jobLauncher.run(jobExecutionConfiguration.job(), jobParameters);
//        jobLauncher.run(stepConfiguration.job(), jobParameters);
//        jobLauncher.run(stepExecutionConfiguration.job(), new JobParameters());
//        jobLauncher.run(stepContributionConfiguration.job(), new JobParameters());
        jobLauncher.run(executionContextConfiguration.job(), new JobParameters());
    }
}
