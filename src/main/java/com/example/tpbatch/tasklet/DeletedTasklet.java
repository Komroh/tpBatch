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
public class DeletedTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String sql = """
                INSERT INTO t_ban_del
                SELECT p.*
                FROM t_ban_prec p
                LEFT JOIN t_ban b ON p.id = b.id
                WHERE b.id IS NULL;
            """;

        int deleted = jdbcTemplate.update(sql);

        contribution.incrementWriteCount(deleted);

        return RepeatStatus.FINISHED;
    }
}
