package com.example.tpbatch.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.tpbatch.utils.Constants.ARCHIVE_PATH;
import static com.example.tpbatch.utils.Constants.FILE_PATH;

@Component
public class ArchiveTasklet implements Tasklet {
    @Value("${downloadFile}")
    private Boolean DownloadFile;
    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if(DownloadFile) {
            DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            String time = contribution.getStepExecution().getJobExecution().getExecutionContext().getString("time");
            String timestamp = LocalDateTime.parse(time).format(timestampFormatter);
            String archiveFileName = timestamp + "_" + "addr.csv";
            Path fileToMove = Paths.get(FILE_PATH);
            Path targetPath = Paths.get(ARCHIVE_PATH, archiveFileName);
            Files.move(fileToMove, targetPath);
        }
        return RepeatStatus.FINISHED;
    }
}
