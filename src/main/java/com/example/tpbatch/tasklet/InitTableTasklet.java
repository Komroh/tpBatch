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
public class InitTableTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String sql = """
                ALTER TABLE t_ban RENAME TO t_ban_prec;
                
                CREATE TABLE IF NOT EXISTS t_ban(
                    	id TEXT PRIMARY KEY,
                    	id_fantoir TEXT,
                    	numero INTEGER,
                    	rep TEXT,
                    	nom_voie TEXT,
                    	code_postal TEXT,
                    	code_insee TEXT,
                    	nom_commune TEXT,
                    	code_insee_ancienne_commune TEXT,
                    	nom_ancienne_commune TEXT,
                    	x REAL,
                    	y REAL,
                    	lon REAL,
                    	lat REAL,
                    	type_position TEXT,
                    	alias TEXT,
                    	nom_ld TEXT,
                    	libelle_acheminement TEXT,
                    	nom_afnor TEXT,
                    	source_position TEXT,
                    	source_nom_voie TEXT,
                    	certification_commune INTEGER,
                    	cad_parcelles TEXT,
                        hash INTEGER
                    );
            """;

        int inserted = jdbcTemplate.update(sql);
        contribution.incrementWriteCount(inserted);

        return RepeatStatus.FINISHED;
    }

}
