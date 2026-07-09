package com.example.tpbatch.reader;

import com.example.tpbatch.Entity.Ban;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;


@Configuration
@Component
public class BanItemReader {

    @Value("${tempFile}")
    private String  file;
    @Bean
    @StepScope
    public FlatFileItemReader<Ban>  csvReader(
            @Value("#{stepExecutionContext[first_line]}") final Long firstLine,
            @Value("#{stepExecutionContext[last_line]}") final Long lastLine
    )
    {
        return new FlatFileItemReaderBuilder<Ban>()
                .name("BanCsvReader")
                .linesToSkip(Math.toIntExact(firstLine))
                .maxItemCount(Math.toIntExact(lastLine))
                .resource(new FileSystemResource(file))
                .delimited()
                .delimiter(";")
                .names("id","id_fantoir","numero","rep","nom_voie",
                        "code_postal","code_insee","nom_commune","code_insee_ancienne_commune",
                        "nom_ancienne_commune","x","y","lon","lat","type_position","alias","nom_ld",
                        "libelle_acheminement","nom_afnor","source_position","source_nom_voie","certification_commune",
                        "cad_parcelles"
                )
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Ban.class);
                }})
                .build();
    }
}
