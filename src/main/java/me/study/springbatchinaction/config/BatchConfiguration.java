package me.study.springbatchinaction.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @EnableBatchProcessing
 * https://github.com/spring-projects/spring-boot/issues/33526
 * 애너테이션을 제거해야 기본 테이블이 생성된다;
 */
@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public JobLauncher jobLauncher() {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setMaxPoolSize(8);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setThreadNamePrefix("batch-job-");
        threadPoolTaskExecutor.initialize();

        jobLauncher.setTaskExecutor(threadPoolTaskExecutor);
        try {
            jobLauncher.afterPropertiesSet();
            return jobLauncher;
        } catch (Exception e) {
            throw new BatchConfigurationException("Unable to configure the default job launcher", e);
        }
    }


    @Bean
    public TaskExecutor stepTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setQueueCapacity(10);
        threadPoolTaskExecutor.setThreadNamePrefix("batch-step-");
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
