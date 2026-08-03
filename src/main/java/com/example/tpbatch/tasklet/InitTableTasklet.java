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
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@StepScope
public class InitTableTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final SqlLoader loader;

    private final Environment environment;

    @Value("#{jobParameters['initScriptPostgres']}")
    private String initScriptPostgres;

    @Value("#{jobParameters['initScriptSqlite']}")
    private String initScriptSqlite;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        String sql;
        String[] profiles = this.environment.getActiveProfiles();
        if (Arrays.asList(profiles).contains("postgresql")) {
            populator.addScript(new ClassPathResource("schema-postgresql.sql"));
            sql = loader.load(initScriptPostgres);
            populator.execute(dataSource);
            populator.setContinueOnError(false);
        }else {
            sql = loader.load(initScriptSqlite);
        }



        jdbcTemplate.execute(sql);

        return RepeatStatus.FINISHED;
    }

}
