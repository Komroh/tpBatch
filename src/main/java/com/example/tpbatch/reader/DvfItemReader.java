package com.example.tpbatch.reader;

import com.example.tpbatch.entity.Dvf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@StepScope
public class DvfItemReader {

    private final Logger log = LoggerFactory.getLogger(DvfItemReader.class);

    @Bean("dvfReader")
    @StepScope
    public FlatFileItemReader<Dvf> dvfReader(
            @Value("#{stepExecutionContext[file]}") String file
    )
    {
        log.info("DVF reader créé");
        BeanWrapperFieldSetMapper<Dvf> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Dvf.class);
        return new FlatFileItemReaderBuilder<Dvf>()
                .name("DvfCsvReader")
                .resource(new FileSystemResource(file))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("id_mutation","date_mutation",
                        "numero_disposition","nature_mutation",
                        "valeur_fonciere","adresse_numero",
                        "adresse_suffixe","adresse_nom_voie",
                        "adresse_code_voie","code_postal",
                        "code_commune","nom_commune",
                        "code_departement","ancien_code_commune",
                        "ancien_nom_commune","id_parcelle",
                        "ancien_id_parcelle","numero_volume",
                        "lot1_numero","lot1_surface_carrez",
                        "lot2_numero","lot2_surface_carrez",
                        "lot3_numero","lot3_surface_carrez",
                        "lot4_numero","lot4_surface_carrez",
                        "lot5_numero","lot5_surface_carrez",
                        "nombre_lots","code_type_local",
                        "type_local","surface_reelle_bati",
                        "nombre_pieces_principales","code_nature_culture",
                        "nature_culture","code_nature_culture_speciale",
                        "nature_culture_speciale","surface_terrain",
                        "longitude","latitude"

                )
                .fieldSetMapper(fieldSetMapper)
                .build();
    }
}
