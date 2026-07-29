package com.example.tpbatch.dto;

import com.example.tpbatch.entity.Dvf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class DvfDto {
    private String idMutation;
    private String dateMutation;
    private String numeroDisposition;
    private String natureMutation;
    private Double valeurFonciere;
    private String adresseNumero;
    private String adresseSuffixe;
    private String adresseNomVoie;
    private String adresseCodeVoie;
    private String codePostal;
    private String codeCommune;
    private String nomCommune;
    private String codeDepartement;
    private String ancienCodeCommune;
    private String ancienNomCommune;
    private String idParcelle;
    private String ancienIdParcelle;
    private String numeroVolume;
    private String lot1Numero;
    private Double lot1SurfaceCarrez;
    private String lot2Numero;
    private Double lot2SurfaceCarrez;
    private String lot3Numero;
    private Double lot3SurfaceCarrez;
    private String lot4Numero;
    private Double lot4SurfaceCarrez;
    private String lot5Numero;
    private Double lot5SurfaceCarrez;
    private Integer nombreLots;
    private String codeTypeLocal;
    private String typeLocal;
    private Double surfaceReelleBati;
    private Integer nombrePiecesPrincipales;
    private String codeNatureCulture;
    private String natureCulture;
    private String codeNatureCultureSpeciale;
    private String natureCultureSpeciale;
    private Double surfaceTerrain;
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

