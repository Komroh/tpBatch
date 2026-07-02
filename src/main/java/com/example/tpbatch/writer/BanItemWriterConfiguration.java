package com.example.tpbatch.writer;

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
                               cad_parcelles,
                               hash)
                               
                               VALUES (
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?)
                               """
               )
               .itemPreparedStatementSetter(((item, ps) -> {
                   ps.setString(1, item.getId());
                   ps.setString(2, item.getId_fantoir());
                   ps.setInt(3, item.getNumero());
                   ps.setString(4, item.getRep());
                   ps.setString(5, item.getNom_voie());
                   ps.setString(6, item.getCode_postal());
                   ps.setString(7, item.getCode_insee());
                   ps.setString(8, item.getNom_commune());
                   ps.setString(9, item.getCode_insee_ancienne_commune());
                   ps.setString(10, item.getNom_ancienne_commune());
                   ps.setDouble(11, item.getX());
                   ps.setDouble(12, item.getY());
                   ps.setDouble(13, item.getLon());
                   ps.setDouble(14, item.getLat());
                   ps.setString(15,item.getType_position());
                   ps.setString(16,item.getAlias());
                   ps.setString(17,item.getNom_ld());
                   ps.setString(18,item.getLibelle_acheminement());
                   ps.setString(19,item.getNom_afnor());
                   ps.setString(20,item.getSource_position());
                   ps.setString(21,item.getSource_nom_voie());
                   ps.setInt(22,item.getCertification_commune());
                   ps.setString(23,item.getCad_parcelles());
                   ps.setLong(24,item.getHash());
               }))
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
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?,
                                ? ,
                                ?,
                                ?,
                                ? ,
                                ? ,
                                ?,
                                ? ,
                                ? ,
                                ? ,
                                ?,
                                ?,
                                ?,
                                ?,
                                ?)
                                """
                )
                .itemPreparedStatementSetter(((item, ps) -> {
                    ps.setString(1, item.getId());
                    ps.setString(2, item.getId_fantoir());
                    ps.setInt(3, item.getNumero());
                    ps.setString(4, item.getRep());
                    ps.setString(5, item.getNom_voie());
                    ps.setString(6, item.getCode_postal());
                    ps.setString(7, item.getCode_insee());
                    ps.setString(8, item.getNom_commune());
                    ps.setString(9, item.getCode_insee_ancienne_commune());
                    ps.setString(10, item.getNom_ancienne_commune());
                    ps.setDouble(11, item.getX());
                    ps.setDouble(12, item.getY());
                    ps.setDouble(13, item.getLon());
                    ps.setDouble(14, item.getLat());
                    ps.setString(15,item.getType_position());
                    ps.setString(16,item.getAlias());
                    ps.setString(17,item.getNom_ld());
                    ps.setString(18,item.getLibelle_acheminement());
                    ps.setString(19,item.getNom_afnor());
                    ps.setString(20,item.getSource_position());
                    ps.setString(21,item.getSource_nom_voie());
                    ps.setInt(22,item.getCertification_commune());
                    ps.setString(23,item.getCad_parcelles());
                }))
                .build();

    }

}
