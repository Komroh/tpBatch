SET work_mem = '1GB';
SET maintenance_work_mem = '2GB';

DROP TABLE IF EXISTS t_ban_duplicate;
DROP TABLE IF EXISTS t_ban_update;
DROP TABLE IF EXISTS t_ban_added;
DROP TABLE IF EXISTS t_ban_del;
DROP TABLE IF EXISTS t_ban_prec;
DROP TABLE IF EXISTS address_fts;
CREATE EXTENSION IF NOT EXISTS postgis;

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
     hash INTEGER,
    search_vector TSVECTOR
);


CREATE TABLE IF NOT EXISTS t_ban_duplicate(
    dup_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    id VARCHAR,
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
    hash INTEGER
);

