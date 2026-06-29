package com.example.tpbatch.ban;

import com.example.tpbatch.Dto.BanDto;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BanItemWriterConfiguration {

    @Bean
    public JdbcBatchItemWriter<BanDto> banJdbcItemWriter(DataSource ds)
    {
       return new JdbcBatchItemWriterBuilder<BanDto>()
               .dataSource(ds)
               .sql(
                       """
                               INSERT INTO t_ban(
                               id,
                               id_fantoir,
                               numero,
                               rep,
                               nom_voie,
                               code_postal,
                               code_insee,
                               nom_commune,
                               code_insee_ancienne_commune,
                               nom_ancienne_commune,
                               x,
                               y,
                               lon,
                               lat,
                               type_position,
                               alias,
                               nom_ld,
                               libelle_acheminement,
                               nom_afnor,
                               source_position,
                               source_nom_voie,
                               certification_commune,
                               cad_parcelles)
                               
                               VALUES (
                               :id,
                               :id_fantoir,
                               :numero,
                               :rep,
                               :nom_voie,
                               :code_postal,
                               :code_insee,
                               :nom_commune,
                               :code_insee_ancienne_commune,
                               :nom_ancienne_commune ,
                               :x,
                               :y,
                               :lon ,
                               :lat ,
                               :type_position,
                               :alias ,
                               :nom_ld ,
                               :libelle_acheminement ,
                               :nom_afnor,
                               :source_position,
                               :source_nom_voie,
                               :certification_commune,
                               :cad_parcelles)
                               """
               )
               .beanMapped()
               .build();

    }
    @Bean
    public JdbcBatchItemWriter<BanDto> duplicateJdbcItemWriter(DataSource ds)
    {
        return new JdbcBatchItemWriterBuilder<BanDto>()
                .dataSource(ds)
                .sql(
                        """
                                INSERT INTO t_ban_duplicate(
                                id,
                                id_fantoir,
                                numero,
                                rep,
                                nom_voie,
                                code_postal,
                                code_insee,
                                nom_commune,
                                code_insee_ancienne_commune,
                                nom_ancienne_commune,
                                x,
                                y,
                                lon,
                                lat,
                                type_position,
                                alias,
                                nom_ld,
                                libelle_acheminement,
                                nom_afnor,
                                source_position,
                                source_nom_voie,
                                certification_commune,
                                cad_parcelles)
                                
                                VALUES (
                                :id,
                                :id_fantoir,
                                :numero,
                                :rep,
                                :nom_voie,
                                :code_postal,
                                :code_insee,
                                :nom_commune,
                                :code_insee_ancienne_commune,
                                :nom_ancienne_commune ,
                                :x,
                                :y,
                                :lon ,
                                :lat ,
                                :type_position,
                                :alias ,
                                :nom_ld ,
                                :libelle_acheminement ,
                                :nom_afnor,
                                :source_position,
                                :source_nom_voie,
                                :certification_commune,
                                :cad_parcelles)
                                """
                )
                .beanMapped()
                .build();

    }
}
