package com.example.tpbatch.writer;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Dto.DvfDto;
import com.example.tpbatch.loader.SqlLoader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class BanItemWriterConfiguration {


    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BanItemWriterConfiguration.class);

    @Bean("banInsert")
    @StepScope
    public JdbcBatchItemWriter<BanDto> banWriter(DataSource ds, SqlLoader loader,
                                       @Value("#{jobParameters['insertScript']}") String insertScriptPath) throws IOException {

            return new JdbcBatchItemWriterBuilder<BanDto>()
                    .dataSource(ds)
                    .sql(
                            loader.load(insertScriptPath)
                    )
                    .beanMapped()
                    .build();
    }
    @Bean("banDuplicateInsert")
    @StepScope
    public JdbcBatchItemWriter<BanDto> duplicateBanWriter(DataSource ds, SqlLoader loader,
                                            @Value("#{jobParameters['duplicateInsertScript']}") String duplicateInsertScriptPath) throws IOException
    {
        return new JdbcBatchItemWriterBuilder<BanDto>()
                .dataSource(ds)
                .sql(
                        loader.load(duplicateInsertScriptPath)
                )
                .beanMapped()
                .build();

    }

    @Bean("dvfInsert")
    @StepScope
    public JdbcBatchItemWriter<DvfDto> dvfWriter(DataSource ds, SqlLoader loader,
                                                 @Value("#{jobParameters['insertScript']}") String insertScriptPath) throws IOException {

        return new JdbcBatchItemWriterBuilder<DvfDto>()
                .dataSource(ds)
                .sql(
                        loader.load(insertScriptPath)
                )
                .beanMapped()
                .build();
    }
    @Bean("dvfDuplicateInsert")
    @StepScope
    public JdbcBatchItemWriter<DvfDto> duplicateDvfWriter(DataSource ds, SqlLoader loader,
                                                          @Value("#{jobParameters['duplicateInsertScript']}") String duplicateInsertScriptPath) throws IOException
    {
        return new JdbcBatchItemWriterBuilder<DvfDto>()
                .dataSource(ds)
                .sql(
                        loader.load(duplicateInsertScriptPath)
                )
                .beanMapped()
                .build();

    }

}
