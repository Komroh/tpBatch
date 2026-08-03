SET work_mem = '1GB';
SET maintenance_work_mem = '2GB';

DROP TABLE IF EXISTS t_ban_duplicate;
DROP TABLE IF EXISTS t_dvf_duplicate;
DROP TABLE IF EXISTS t_dvf_old;
DROP TABLE IF EXISTS t_ban_update;
DROP TABLE IF EXISTS t_ban_added;
DROP TABLE IF EXISTS t_ban_del;
DROP TABLE IF EXISTS t_dvf_update;
DROP TABLE IF EXISTS t_dvf_added;
DROP TABLE IF EXISTS t_dvf_del;
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
     hash BIGINT,
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
    hash BIGINT
);


CREATE TABLE IF NOT EXISTS t_dvf(
    id_mutation VARCHAR,
    date_mutation DATE,
    numero_disposition VARCHAR,
    nature_mutation VARCHAR,
    valeur_fonciere NUMERIC,
    adresse_numero VARCHAR,
    adresse_suffixe VARCHAR,
    adresse_nom_voie VARCHAR,
    adresse_code_voie VARCHAR,
    code_postal VARCHAR,
    code_commune VARCHAR,
    nom_commune VARCHAR,
    code_departement VARCHAR,
    ancien_code_commune VARCHAR,
    ancien_nom_commune VARCHAR,
    id_parcelle VARCHAR,
    ancien_id_parcelle VARCHAR,
    numero_volume VARCHAR,
    lot1_numero VARCHAR,
    lot1_surface_carrez NUMERIC,
    lot2_numero VARCHAR,
    lot2_surface_carrez NUMERIC,
    lot3_numero VARCHAR,
    lot3_surface_carrez NUMERIC,
    lot4_numero VARCHAR,
    lot4_surface_carrez NUMERIC,
    lot5_numero VARCHAR,
    lot5_surface_carrez NUMERIC,
    nombre_lots INTEGER,
    code_type_local VARCHAR,
    type_local VARCHAR,
    surface_reelle_bati NUMERIC,
    nombre_pieces_principales INTEGER,
    code_nature_culture VARCHAR,
    nature_culture VARCHAR,
    code_nature_culture_speciale VARCHAR,
    nature_culture_speciale VARCHAR,
    surface_terrain NUMERIC,
    longitude NUMERIC,
    latitude NUMERIC,
    hash BIGINT
);

CREATE TABLE IF NOT EXISTS t_dvf_duplicate(
    dup_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    id_mutation VARCHAR,
    date_mutation DATE,
    numero_disposition VARCHAR,
    nature_mutation VARCHAR,
    valeur_fonciere NUMERIC,
    adresse_numero VARCHAR,
    adresse_suffixe VARCHAR,
    adresse_nom_voie VARCHAR,
    adresse_code_voie VARCHAR,
    code_postal VARCHAR,
    code_commune VARCHAR,
    nom_commune VARCHAR,
    code_departement VARCHAR,
    ancien_code_commune VARCHAR,
    ancien_nom_commune VARCHAR,
    id_parcelle VARCHAR,
    ancien_id_parcelle VARCHAR,
    numero_volume VARCHAR,
    lot1_numero VARCHAR,
    lot1_surface_carrez NUMERIC,
    lot2_numero VARCHAR,
    lot2_surface_carrez NUMERIC,
    lot3_numero VARCHAR,
    lot3_surface_carrez NUMERIC,
    lot4_numero VARCHAR,
    lot4_surface_carrez NUMERIC,
    lot5_numero VARCHAR,
    lot5_surface_carrez NUMERIC,
    nombre_lots INTEGER,
    code_type_local VARCHAR,
    type_local VARCHAR,
    surface_reelle_bati NUMERIC,
    nombre_pieces_principales INTEGER,
    code_nature_culture VARCHAR,
    nature_culture VARCHAR,
    code_nature_culture_speciale VARCHAR,
    nature_culture_speciale VARCHAR,
    surface_terrain NUMERIC,
    longitude NUMERIC,
    latitude NUMERIC,
    hash BIGINT
);

CREATE TABLE IF NOT EXISTS t_transaction (
    id BIGSERIAL PRIMARY KEY,

    id_mutation VARCHAR NOT NULL,
    numero_disposition VARCHAR NOT NULL,

    date_mutation DATE,
    nature_mutation VARCHAR,

    valeur_fonciere NUMERIC,

    type_local VARCHAR,
    surface_reelle_bati INTEGER,
    nombre_pieces_principales INTEGER,

    surface_terrain INTEGER,

    adresse_numero VARCHAR,
    adresse_suffixe VARCHAR,
    adresse_nom_voie VARCHAR,
    code_postal VARCHAR,

    code_commune VARCHAR,
    nom_commune VARCHAR,

    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,

    geom geometry(Point,4326)
);

CREATE INDEX IF NOT EXISTS idx_transaction_commune
    ON t_transaction(code_commune);

CREATE INDEX IF NOT EXISTS idx_transaction_date
    ON t_transaction(date_mutation);

CREATE INDEX IF NOT EXISTS idx_transaction_type
    ON t_transaction(type_local);

CREATE INDEX IF NOT EXISTS idx_transaction_geom
    ON t_transaction
        USING GIST(geom);

CREATE TABLE IF NOT EXISTS t_commune(
    code_insee VARCHAR PRIMARY KEY,
    nom VARCHAR,
    departement VARCHAR,
    region VARCHAR,
    epci VARCHAR,
    geom geometry(MultiPolygon,4326)
);