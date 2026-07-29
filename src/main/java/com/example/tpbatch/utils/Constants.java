package com.example.tpbatch.utils;

public class Constants {

    public static final String DOWNLOAD_PATH = "./data/downloads";
    public static final String ARCHIVE_PATH = "./data/archives";
    public static final String REPORT_PATH = "./data/reports";
    public static final String ZIPPED_PATH = DOWNLOAD_PATH + "/ddl.gz";
    public static final String BAN_PATH = DOWNLOAD_PATH + "/ban.csv";
    public static final String DVF_PATH = DOWNLOAD_PATH + "/dvf.csv";
    public static final String BAN_INSERT_SCRIPT_PATH = "ban/insert.sql";
    public static final String BAN_DUPLICATE_INSERT_SCRIPT_PATH = "ban/duplicate_insert.sql";
    public static final String BAN_INIT_SCRIPT_PATH = "ban/init.sql";
    public static final String BAN_ADDED_SCRIPT_PATH = "ban/added.sql";
    public static final String BAN_UPDATED_SCRIPT_PATH = "ban/updated.sql";
    public static final String BAN_DELETED_SCRIPT_PATH = "ban/deleted.sql";
    public static final String BAN_CONSTRAINTS_SCRIPT_PATH = "ban/constraints.sql";
    public static final String DVF_INSERT_SCRIPT_PATH = "dvf/insert.sql";
    public static final String DVF_DUPLICATE_INSERT_SCRIPT_PATH = "dvf/duplicate_insert.sql";
    public static final String DVF_INIT_SCRIPT_PATH = "dvf/init.sql";
    public static final String DVF_ADDED_SCRIPT_PATH = "dvf/added.sql";
    public static final String DVF_UPDATED_SCRIPT_PATH = "dvf/updated.sql";
    public static final String DVF_DELETED_SCRIPT_PATH = "dvf/deleted.sql";
    public static final String DVF_CONSTRAINTS_SCRIPT_PATH = "dvf/constraints.sql";
    public static final String BAN_HEADER="id;id_fantoir;numero;rep;nom_voie;code_postal;code_insee;nom_commune;code_insee_ancienne_commune;nom_ancienne_commune;x;y;lon;lat;type_position;alias;nom_ld;libelle_acheminement;nom_afnor;source_position;source_nom_voie;certification_commune;cad_parcelles";
    public static final String DVF_HEADER="id_mutation,date_mutation,numero_disposition,nature_mutation,valeur_fonciere,adresse_numero,adresse_suffixe,adresse_nom_voie,adresse_code_voie,code_postal,code_commune,nom_commune,code_departement,ancien_code_commune,ancien_nom_commune,id_parcelle,ancien_id_parcelle,numero_volume,lot1_numero,lot1_surface_carrez,lot2_numero,lot2_surface_carrez,lot3_numero,lot3_surface_carrez,lot4_numero,lot4_surface_carrez,lot5_numero,lot5_surface_carrez,nombre_lots,code_type_local,type_local,surface_reelle_bati,nombre_pieces_principales,code_nature_culture,nature_culture,code_nature_culture_speciale,nature_culture_speciale,surface_terrain,longitude,latitude";
}
