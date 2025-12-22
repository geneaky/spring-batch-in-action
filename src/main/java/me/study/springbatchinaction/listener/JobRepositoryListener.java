package me.study.springbatchinaction.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobRepositoryListener implements JobExecutionListener {

    private final JobRepository jobRepository;

    public JobRepositoryListener(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = jobExecution.getJobParameters();
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if(lastJobExecution != null) {
            for(StepExecution stepExecution : lastJobExecution.getStepExecutions()) {
                log.info("step status: {}", stepExecution.getStatus());
                log.info("exit status: {}", stepExecution.getExitStatus());
            }
        }
    }
}
