package com.example.tpbatch.tasklet;

import com.example.tpbatch.metrics.BanMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.tpbatch.utils.Constants.REPORT_PATH;

@Component
@RequiredArgsConstructor
public class GenerateReportTasklet implements Tasklet {
    private final BanMetrics banMetrics;
    private final MeterRegistry meterRegistry;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        var exitStatus = contribution.getStepExecution().getJobExecution().getExitStatus();
        String jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();

        String time = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("time");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.parse(time).format(timestampFormatter);

        String checksum ="";
         if(contribution.getStepExecution().getJobExecution().getExecutionContext().containsKey("checksum"))
        {
             checksum = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("checksum");
        }

        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(REPORT_PATH + "/report_"+jobName+"_"+timestamp+".txt"));
        if(exitStatus.equals(new ExitStatus("NO_INPUT_FILE"))){
            writer.write("Aucun fichier à traiter");
        }else{
            var status = contribution.getStepExecution().getJobExecution().getStatus();
            writer.write("Status: " + status + " ExitStatus: " + exitStatus.getExitCode());
            writer.newLine();
            writer.write("Checksum: " + checksum);
            writer.newLine();
            writer.write("Nombre d'éléments traités : " + banMetrics.getItemProcessed());
            writer.newLine();
            writer.write("Nombre de doublons purs : " + banMetrics.getDuplicateSame());
            writer.newLine();
            writer.write("Nombre de doublons avec champs différents : " + banMetrics.getDuplicateDiff());
        }
        writer.close();
        return RepeatStatus.FINISHED;
    }
}
