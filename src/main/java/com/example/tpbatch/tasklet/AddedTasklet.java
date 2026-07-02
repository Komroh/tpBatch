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
public class AddedTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String sql = """
                INSERT INTO t_ban_added
                SELECT b.id
                FROM t_ban b
                LEFT JOIN t_ban_prec p ON p.id = b.id
                WHERE p.id IS NULL;
                """;
        int added = jdbcTemplate.update(sql);
        contribution.incrementWriteCount(added);
        return RepeatStatus.FINISHED;
    }
}
