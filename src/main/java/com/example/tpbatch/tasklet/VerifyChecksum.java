package com.example.tpbatch.tasklet;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VerifyChecksum implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(VerifyChecksum.class);

    private final JobRepository jobRepo;
    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<JobInstance> instances = jobRepo.getJobInstances("Job", 0,2);
        JobExecution lastJobExecution = null;
        if(instances.size() >= 2) {
            lastJobExecution = jobRepo.getLastJobExecution(instances.get(1));
        }

        if(lastJobExecution != null && lastJobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            String lastChecksum = lastJobExecution.getExecutionContext().getString("checksum");
            String checksum = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("checksum");
            log.info("Last checksum: {}", lastChecksum);
            log.info("Current checksum: {}", checksum);
            if(checksum.equals(lastChecksum)) {
                log.info("Checksum identique, stop job");
                contribution.setExitStatus(new ExitStatus("SAME_FILE"));
            }
        }
        return RepeatStatus.FINISHED;
    }
}
