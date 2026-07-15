package com.example.tpbatch.tasklet;

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
public class AddConstraintsTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        jdbcTemplate.execute("""
        ALTER TABLE t_ban_added
        ADD PRIMARY KEY (id)
    """);

        jdbcTemplate.execute("""
        ALTER TABLE t_ban_update
        ADD PRIMARY KEY (id)
    """);

        jdbcTemplate.execute("""
        ALTER TABLE t_ban_del
        ADD PRIMARY KEY (id)
    """);

        return RepeatStatus.FINISHED;
    }
}
