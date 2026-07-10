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
public class PopulateSearchTableTasklet implements Tasklet {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String sql = """
                INSERT INTO address_fts(address_fts)
                VALUES('rebuild')
                """;

        int inserted = jdbcTemplate.update(sql);
        contribution.incrementWriteCount(inserted);

        return RepeatStatus.FINISHED;
    }
}
