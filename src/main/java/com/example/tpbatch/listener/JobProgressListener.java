package com.example.tpbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Qualifier("JobProgressListener")
@Component
public class JobProgressListener implements JobExecutionListener {

    private static final Logger log =
            LoggerFactory.getLogger(JobProgressListener.class);
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().put("time", LocalDateTime.now().toString());
        log.info("Démarrage du job [{}] avec les paramètres : {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getJobParameters());
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job [{}] terminé avec le statut : {} en {} ms",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                Duration.between(
                        jobExecution.getStartTime(),
                        jobExecution.getEndTime()).toMillis());
    }
}

