package com.example.tpbatch.tasklet;

import com.example.tpbatch.loader.SqlLoader;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionTasklet implements Tasklet {
    private final JdbcTemplate jdbcTemplate;
    private final SqlLoader loader;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        jdbcTemplate.update(loader.load("dvf/insertTransaction.sql"));
        return RepeatStatus.FINISHED;
    }
}
