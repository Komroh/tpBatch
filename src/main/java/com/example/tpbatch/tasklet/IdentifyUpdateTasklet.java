package com.example.tpbatch.tasklet;

import com.example.tpbatch.loader.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@StepScope
public class IdentifyUpdateTasklet implements Tasklet {


    private final JdbcTemplate jdbcTemplate;
    private final SqlLoader loader;

    @Value("#{jobParameters['updatedScript']}")
    private String updatedScript;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


        String sql = loader.load(updatedScript);

        int updated = jdbcTemplate.update(sql);

        contribution.incrementWriteCount(updated);

        return RepeatStatus.FINISHED;
    }



}
