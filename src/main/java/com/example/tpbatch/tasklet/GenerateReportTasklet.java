package com.example.tpbatch.tasklet;

import com.example.tpbatch.metrics.BanMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log =  LoggerFactory.getLogger(GenerateReportTasklet.class);

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        try {
            String jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();

            String time = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("time");
            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timestamp = LocalDateTime.parse(time).format(timestampFormatter);

            String retrieveStatus = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("retrieveStatus","");
            String checksum = "";
            if (contribution.getStepExecution().getJobExecution().getExecutionContext().containsKey("checksum")) {
                checksum = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("checksum");
            }

            try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(REPORT_PATH + "/report_" + jobName + "_" + timestamp + ".txt"))) {
            if(retrieveStatus.equals("NO_INPUT_FILE"))

                {
                    writer.write("Aucun fichier à traiter");
                } else

                {
                    var status = contribution.getStepExecution().getJobExecution().getStatus();
                    writer.write("Status: " + status + " ExitStatus: " + retrieveStatus);
                    writer.newLine();
                    writer.write("Checksum: " + checksum);
                    writer.newLine();
                    writer.write("Nombre d'éléments traités : " + banMetrics.getItemProcessed());
                    writer.newLine();
                    writer.write("Nombre de doublons purs : " + banMetrics.getDuplicateSame());
                    writer.newLine();
                    writer.write("Nombre de doublons avec champs différents : " + banMetrics.getDuplicateDiff());
                }
            }

        }catch (Exception e){
            log.error("Erreur lors de la génération du rapport: {}", e.getMessage());
        }
        return RepeatStatus.FINISHED;
    }
}
