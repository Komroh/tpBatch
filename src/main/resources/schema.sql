PRAGMA journal_mode = WAL;
PRAGMA temp_store = MEMORY;
PRAGMA synchronous = NORMAL;
PRAGMA cache_size = -1000000; -- Environ 50 Mo
PRAGMA threads = 4;


DROP TABLE IF EXISTS t_ban_duplicate;
DROP TABLE IF EXISTS t_ban_update;
DROP TABLE IF EXISTS t_ban_added;
DROP TABLE IF EXISTS t_ban_del;
DROP TABLE IF EXISTS t_ban_prec;

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


CREATE TABLE IF NOT EXISTS t_ban_update(
     id TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS t_ban_added(
     id TEXT PRIMARY KEY

);

CREATE TABLE IF NOT EXISTS t_ban_del(
     id TEXT PRIMARY KEY
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