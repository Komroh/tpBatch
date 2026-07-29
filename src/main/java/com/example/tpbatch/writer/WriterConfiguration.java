package com.example.tpbatch.writer;

import com.example.tpbatch.dto.BanDto;
import com.example.tpbatch.dto.DvfDto;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterConfiguration {

    @Bean("banRoutingWriter")
    public BanRoutingWriter banRoutingWriter(
            @Qualifier("banInsert")  JdbcBatchItemWriter<BanDto> banWriter,
            @Qualifier("banDuplicateInsert") JdbcBatchItemWriter<BanDto> duplicateWriter) {

        return new BanRoutingWriter(
                banWriter,
                duplicateWriter
        );
    }

    @Bean("dvfRoutingWriter")
    public DvfRoutingWriter dvfRoutingWriter(
            @Qualifier("dvfInsert") JdbcBatchItemWriter<DvfDto> dvfItemWriter,
            @Qualifier("dvfDuplicateInsert") JdbcBatchItemWriter<DvfDto> duplicateDvfWriter) {

        return new DvfRoutingWriter(
                dvfItemWriter,
                duplicateDvfWriter
        );
    }
}

