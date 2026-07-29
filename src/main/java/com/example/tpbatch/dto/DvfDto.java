package com.example.tpbatch.dto;

import com.example.tpbatch.entity.Dvf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class DvfDto {
    private String id_mutation;
    private String date_mutation;
    private String numero_disposition;
    private String nature_mutation;
    private Double valeur_fonciere;
    private String adresse_numero;
    private String adresse_suffixe;
    private String adresse_nom_voie;
    private String adresse_code_voie;
    private String code_postal;
    private String code_commune;
    private String nom_commune;
    private String code_departement;
    private String ancien_code_commune;
    private String ancien_nom_commune;
    private String id_parcelle;
    private String ancien_id_parcelle;
    private String numero_volume;
    private String lot1_numero;
    private Double lot1_surface_carrez;
    private String lot2_numero;
    private Double lot2_surface_carrez;
    private String lot3_numero;
    private Double lot3_surface_carrez;
    private String lot4_numero;
    private Double lot4_surface_carrez;
    private String lot5_numero;
    private Double lot5_surface_carrez;
    private Integer nombre_lots;
    private String code_type_local;
    private String type_local;
    private Double surface_reelle_bati;
    private Integer nombre_pieces_principales;
    private String code_nature_culture;
    private String nature_culture;
    private String code_nature_culture_speciale;
    private String nature_culture_speciale;
    private Double surface_terrain;
    private Double longitude;
    private Double latitude;
    private long hash;
    private Boolean isDuplicate;

    public static DvfDto from(Dvf dvf) {
        return new DvfDto(
                dvf.getIdMutation(),
                dvf.getDateMutation(),
                dvf.getNumeroDisposition(),
                dvf.getNatureMutation(),
                dvf.getValeurFonciere(),
                dvf.getAdresseNumero(),
                dvf.getAdresseSuffixe(),
                dvf.getAdresseNomVoie(),
                dvf.getAdresseCodeVoie(),
                dvf.getCodePostal(),
                dvf.getCodeCommune(),
                dvf.getNomCommune(),
                dvf.getCodeDepartement(),
                dvf.getAncienCodeCommune(),
                dvf.getAncienNomCommune(),
                dvf.getIdParcelle(),
                dvf.getAncienIdParcelle(),
                dvf.getNumeroVolume(),
                dvf.getLot1Numero(),
                dvf.getLot1SurfaceCarrez(),
                dvf.getLot2Numero(),
                dvf.getLot2SurfaceCarrez(),
                dvf.getLot3Numero(),
                dvf.getLot3SurfaceCarrez(),
                dvf.getLot4Numero(),
                dvf.getLot4SurfaceCarrez(),
                dvf.getLot5Numero(),
                dvf.getLot5SurfaceCarrez(),
                dvf.getNombreLots(),
                dvf.getCodeTypeLocal(),
                dvf.getTypeLocal(),
                dvf.getSurfaceReelleBati(),
                dvf.getNombrePiecesPrincipales(),
                dvf.getCodeNatureCulture(),
                dvf.getNatureCulture(),
                dvf.getCodeNatureCultureSpeciale(),
                dvf.getNatureCultureSpeciale(),
                dvf.getSurfaceTerrain(),
                dvf.getLongitude(),
                dvf.getLatitude(),
                0L, // Initialize hash to 0 or any default value
                false // Initialize isDuplicate to false or any default value
        );
    }
}

