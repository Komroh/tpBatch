package com.example.tpbatch.tasklet;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitTableTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    private Environment environment;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        String sql="";
        String[] profiles = this.environment.getActiveProfiles();
        if (Arrays.asList(profiles).contains("postgresql")) {
            populator.addScript(new ClassPathResource("schema-postgresql.sql"));
             sql = """
                        ALTER TABLE t_ban RENAME TO t_ban_prec;
                    
                        CREATE TABLE IF NOT EXISTS t_ban(
                            	id VARCHAR PRIMARY KEY,
                            	id_fantoir VARCHAR,
                            	numero INTEGER,
                            	rep VARCHAR,
                            	nom_voie VARCHAR,
                            	code_postal VARCHAR,
                            	code_insee VARCHAR,
                            	nom_commune VARCHAR,
                            	code_insee_ancienne_commune VARCHAR,
                            	nom_ancienne_commune VARCHAR,
                            	x NUMERIC,
                            	y NUMERIC,
                            	lon NUMERIC,
                            	lat NUMERIC,
                            	type_position VARCHAR,
                            	alias VARCHAR,
                            	nom_ld VARCHAR,
                            	libelle_acheminement VARCHAR,
                            	nom_afnor VARCHAR,
                            	source_position VARCHAR,
                            	source_nom_voie VARCHAR,
                            	certification_commune INTEGER,
                            	cad_parcelles VARCHAR,
                                hash BIGINT,
                                search_vector TSVECTOR  GENERATED ALWAYS AS (
                                   to_tsvector(
                                       'simple',
                                       coalesce(numero, '0') || ' '
                                       || coalesce(nom_voie, '') || ' '
                                       || coalesce(code_postal,'') || ' '
                                       || coalesce(nom_commune,'')
                                   )
                               ) STORED
                            );
                    """;
        }else {
            populator.addScript(new ClassPathResource("schema-sqlite.sql"));
            sql = """
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
        }

        populator.setContinueOnError(false);

        populator.execute(dataSource);

        int inserted = jdbcTemplate.update(sql);
        contribution.incrementWriteCount(inserted);

        return RepeatStatus.FINISHED;
    }

}
