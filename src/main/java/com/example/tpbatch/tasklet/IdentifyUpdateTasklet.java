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
public class IdentifyUpdateTasklet implements Tasklet {


    private final JdbcTemplate jdbcTemplate;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {


        String sql = """
            INSERT INTO t_ban_update
        
                SELECT b.*
                FROM t_ban b
                JOIN t_ban_prec p ON b.id = p.id
                WHERE
                       b.id_fantoir <> p.id_fantoir
                    OR b.numero <> p.numero
                    OR b.rep <> p.rep
                    OR b.nom_voie <> p.nom_voie
                    OR b.code_postal <> p.code_postal
                    OR b.code_insee <> p.code_insee
                    OR b.nom_commune <> p.nom_commune
                    OR b.code_insee_ancienne_commune <> p.code_insee_ancienne_commune
                    OR b.nom_ancienne_commune <> p.nom_ancienne_commune
                    OR b.x <> p.x
                    OR b.y <> p.y
                    OR b.lon <> p.lon
                    OR b.lat <> p.lat
                    OR b.type_position <> p.type_position
                    OR b.alias <> p.alias
                    OR b.nom_ld <> p.nom_ld
                    OR b.libelle_acheminement <> p.libelle_acheminement
                    OR b.nom_afnor <> p.nom_afnor
                    OR b.source_position <> p.source_position
                    OR b.source_nom_voie <> p.source_nom_voie
                    OR b.certification_commune <> p.certification_commune
                    OR b.cad_parcelles <> p.cad_parcelles;
            """;

        int updated = jdbcTemplate.update(sql);

        contribution.incrementWriteCount(updated);

        return RepeatStatus.FINISHED;
    }



}
