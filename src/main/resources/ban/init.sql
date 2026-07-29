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

ALTER TABLE t_ban
    ADD COLUMN geom geometry(Point,4326)
        GENERATED ALWAYS AS (
            ST_SetSRID(ST_MakePoint(lon, lat),4326)
            ) STORED;

DROP INDEX IF EXISTS idx_t_ban_geom;

CREATE INDEX idx_t_ban_geom
    ON t_ban
        USING GIST (geom);