PRAGMA journal_mode = WAL;
PRAGMA busy_timeout = 10000;
PRAGMA temp_store = MEMORY;
PRAGMA synchronous = NORMAL;
PRAGMA cache_size = -1000000; -- Environ 50 Mo
PRAGMA threads = 4;


DROP TABLE IF EXISTS t_ban_duplicate;
DROP TABLE IF EXISTS t_ban_update;
DROP TABLE IF EXISTS t_ban_added;
DROP TABLE IF EXISTS t_ban_del;
DROP TABLE IF EXISTS t_ban_prec;
DROP TABLE IF EXISTS address_fts;

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

CREATE TABLE IF NOT EXISTS t_ban_duplicate(
    dup_id INTEGER PRIMARY KEY AUTOINCREMENT,
    id TEXT,
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

CREATE TABLE IF NOT EXISTS t_dvf(
    id_mutation TEXT PRIMARY KEY,
    date_mutation TEXT,
    numero_disposition TEXT,
    nature_mutation TEXT,
    valeur_fonciere NUMERIC,
    adresse_numero TEXT,
    adresse_suffixe TEXT,
    adresse_nom_voie TEXT,
    adresse_code_voie TEXT,
    code_postal TEXT,
    code_commune TEXT,
    nom_commune TEXT,
    code_departement TEXT,
    ancien_code_commune TEXT,
    ancien_nom_commune TEXT,
    id_parcelle TEXT,
    ancien_id_parcelle TEXT,
    numero_volume TEXT,
    lot1_numero TEXT,
    lot1_surface_carrez NUMERIC,
    lot2_numero TEXT,
    lot2_surface_carrez NUMERIC,
    lot3_numero TEXT,
    lot3_surface_carrez NUMERIC,
    lot4_numero TEXT,
    lot4_surface_carrez NUMERIC,
    lot5_numero TEXT,
    lot5_surface_carrez NUMERIC,
    nombre_lots INTEGER,
    code_type_local TEXT,
    type_local TEXT,
    surface_reelle_bati NUMERIC,
    nombre_pieces_principales INTEGER,
    code_nature_culture TEXT,
    nature_culture TEXT,
    code_nature_culture_speciale TEXT,
    nature_culture_speciale TEXT,
    surface_terrain NUMERIC,
    longitude NUMERIC,
    latitude NUMERIC,
    hash INTEGER
);

CREATE TABLE IF NOT EXISTS t_dvf_duplicate(
    dup_id INTEGER PRIMARY KEY AUTOINCREMENT ,
    id_mutation TEXT,
    date_mutation TEXT,
    numero_disposition TEXT,
    nature_mutation TEXT,
    valeur_fonciere NUMERIC,
    adresse_numero TEXT,
    adresse_suffixe TEXT,
    adresse_nom_voie TEXT,
    adresse_code_voie TEXT,
    code_postal TEXT,
    code_commune TEXT,
    nom_commune TEXT,
    code_departement TEXT,
    ancien_code_commune TEXT,
    ancien_nom_commune TEXT,
    id_parcelle TEXT,
    ancien_id_parcelle TEXT,
    numero_volume TEXT,
    lot1_numero TEXT,
    lot1_surface_carrez NUMERIC,
    lot2_numero TEXT,
    lot2_surface_carrez NUMERIC,
    lot3_numero TEXT,
    lot3_surface_carrez NUMERIC,
    lot4_numero TEXT,
    lot4_surface_carrez NUMERIC,
    lot5_numero TEXT,
    lot5_surface_carrez NUMERIC,
    nombre_lots INTEGER,
    code_type_local TEXT,
    type_local TEXT,
    surface_reelle_bati NUMERIC,
    nombre_pieces_principales INTEGER,
    code_nature_culture TEXT,
    nature_culture TEXT,
    code_nature_culture_speciale TEXT,
    nature_culture_speciale TEXT,
    surface_terrain NUMERIC,
    longitude NUMERIC,
    latitude NUMERIC,
    hash INTEGER
);

CREATE VIRTUAL TABLE IF NOT EXISTS address_fts USING fts5(numero, nom_voie, code_postal, nom_commune, content='t_ban' )