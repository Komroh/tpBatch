package com.example.tpbatch.writer;

import com.example.tpbatch.Dto.BanDto;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterConfiguration {

    @Bean
    public BanRoutingWriter banRoutingWriter(
            JdbcBatchItemWriter<BanDto> banWriter,
            JdbcBatchItemWriter<BanDto> duplicateWriter) {

        return new BanRoutingWriter(
                banWriter,
                duplicateWriter
        );
    }
}
