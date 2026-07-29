package com.example.tpbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Qualifier("JobProgressListener")
@Component
public class JobProgressListener implements JobExecutionListener {

    private static final Logger log =
            LoggerFactory.getLogger(JobProgressListener.class);
    private static final String ZONE = "Europe/Paris";
    public void beforeJob(JobExecution jobExecution) {

        jobExecution.getExecutionContext().put("time", LocalDateTime.now(ZoneId.of(ZONE)).toString());
        log.info("Démarrage du job [{}] avec les paramètres : {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getJobParameters());
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime jobBegin = jobExecution.getStartTime();
        LocalDateTime jobEnd = jobExecution.getEndTime();

        if(jobEnd != null && jobBegin != null){
            log.info("Job [{}] terminé avec le statut : {} en {} ms",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus(),
                    Duration.between(
                            jobBegin.atZone(ZoneId.of(ZONE)).toInstant(),
                            jobEnd.atZone(ZoneId.of(ZONE)).toInstant()
                    ).toMillis());
        }
        else{
            log.info("Job [{}] terminé avec le statut : {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus()
            );
        }

    }
}

