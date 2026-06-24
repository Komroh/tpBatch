DROP TABLE IF EXISTS t_ban;


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
	cad_parcelles TEXT
);