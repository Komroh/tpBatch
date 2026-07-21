package com.example.tpbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component("downloadJobListener")
public class DownloadJobListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {

        jobExecution.getStepExecutions().stream()
                .filter(step -> step.getStepName().equals("Download Step"))
                .findFirst()
                .ifPresent(step -> {
                    String code = step.getExitStatus().getExitCode();

                    if(!ExitStatus.COMPLETED.getExitCode().equals(code)) {
                        jobExecution.setExitStatus(step.getExitStatus());
                    }
                });
    }
}
