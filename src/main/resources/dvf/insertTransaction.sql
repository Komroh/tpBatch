INSERT INTO t_transaction (
    id_mutation,
    numero_disposition,
    date_mutation,
    nature_mutation,
    valeur_fonciere,
    type_local,
    surface_reelle_bati,
    nombre_pieces_principales,
    surface_terrain,
    adresse_numero,
    adresse_suffixe,
    adresse_nom_voie,
    code_postal,
    code_commune,
    nom_commune,
    longitude,
    latitude,
    geom
)
SELECT
    id_mutation,
    numero_disposition,

    MIN(date_mutation),
    MIN(nature_mutation),

    MIN(valeur_fonciere),

    MAX(type_local),
    MAX(surface_reelle_bati),
    MAX(nombre_pieces_principales),

    MAX(surface_terrain),

    MAX(adresse_numero),
    MAX(adresse_suffixe),
    MAX(adresse_nom_voie),
    MAX(code_postal),

    MAX(code_commune),
    MAX(nom_commune),

    AVG(longitude),
    AVG(latitude),

    ST_SetSRID(
        ST_MakePoint(
            AVG(longitude),
            AVG(latitude)
        ),
        4326
    )
FROM t_dvf
GROUP BY id_mutation, numero_disposition;