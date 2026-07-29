package com.example.tpbatch.Entity;

import com.example.tpbatch.utils.Hashable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import static com.example.tpbatch.utils.HashCalcul.nullToEmpty;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Table(name = "t_dvf")
public class Dvf implements Hashable {

    @Id
    @Column(name = "id_mutation")
    private String idMutation;
    @Column(name = "date_mutation")
    private String dateMutation;
    @Column(name ="numero_disposition")
    private String numeroDisposition;
    @Column(name = "nature_mutation")
    private String natureMutation;
    @Column(name = "valeur_fonciere")
    private Double valeurFonciere;
    @Column(name = "adresse_numero")
    private String adresseNumero;
    @Column(name = "adresse_suffixe")
    private String adresseSuffixe;
    @Column(name = "adresse_nom_voie")
    private String adresseNomVoie;
    @Column(name = "adresse_code_voie")
    private String adresseCodeVoie;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "code_commune")
    private String codeCommune;
    @Column(name = "nom_commune")
    private String nomCommune;
    @Column(name = "code_departement")
    private String codeDepartement;
    @Column(name = "ancien_code_commune")
    private String ancienCodeCommune;
    @Column(name = "ancien_nom_commune")
    private String ancienNomCommune;
    @Column(name = "id_parcelle")
    private String idParcelle;
    @Column(name = "ancien_id_parcelle")
    private String ancienIdParcelle;
    @Column(name = "numero_volume")
    private String numeroVolume;
    @Column(name = "lot1_numero")
    private String lot1Numero;
    @Column(name = "lot1_surface_carrez")
    private Double lot1SurfaceCarrez;
    @Column(name = "lot2_numero")
    private String lot2Numero;
    @Column(name = "lot2_surface_carrez")
    private Double lot2SurfaceCarrez;
    @Column(name = "lot3_numero")
    private String lot3Numero;
    @Column(name = "lot3_surface_carrez")
    private Double lot3SurfaceCarrez;
    @Column(name = "lot4_numero")
    private String lot4Numero;
    @Column(name = "lot4_surface_carrez")
    private Double lot4SurfaceCarrez;
    @Column(name = "lot5_numero")
    private String lot5Numero;
    @Column(name = "lot5_surface_carrez")
    private Double lot5SurfaceCarrez;
    @Column(name = "nombre_lots")
    private Integer nombreLots;
    @Column(name = "code_type_local")
    private String codeTypeLocal;
    @Column(name = "type_local")
    private String typeLocal;
    @Column(name = "surface_reelle_bati")
    private Double surfaceReelleBati;
    @Column(name = "nombre_pieces_principales")
    private Integer nombrePiecesPrincipales;
    @Column(name = "code_nature_culture")
    private String codeNatureCulture;
    @Column(name = "nature_culture")
    private String natureCulture;
    @Column(name = "code_nature_culture_speciale")
    private String codeNatureCultureSpeciale;
    @Column(name = "nature_culture_speciale")
    private String natureCultureSpeciale;
    @Column(name = "surface_terrain")
    private Double surfaceTerrain;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "hash")
    private long hash;

    @Override
    public String HashContent() {
        return String.join("|",
                nullToEmpty(this.getNumeroDisposition()),
                nullToEmpty(this.getNatureMutation()),
                String.valueOf(this.getValeurFonciere()),
                nullToEmpty(this.getAdresseNumero()),
                nullToEmpty(this.getAdresseSuffixe()),
                nullToEmpty(this.getAdresseNomVoie()),
                nullToEmpty(this.getAdresseCodeVoie()),
                nullToEmpty(this.getCodePostal()),
                nullToEmpty(this.getCodeCommune()),
                nullToEmpty(this.getNomCommune()),
                nullToEmpty(this.getCodeDepartement()),
                nullToEmpty(this.getAncienCodeCommune()),
                nullToEmpty(this.getAncienNomCommune()),
                nullToEmpty(this.getIdParcelle()),
                nullToEmpty(this.getAncienIdParcelle()),
                nullToEmpty(this.getNumeroVolume()),
                nullToEmpty(this.getLot1Numero()),
                String.valueOf(this.getLot1SurfaceCarrez()),
                nullToEmpty(this.getLot2Numero()),
                String.valueOf(this.getLot2SurfaceCarrez()),
                nullToEmpty(this.getLot3Numero()),
                String.valueOf(this.getLot3SurfaceCarrez()),
                nullToEmpty(this.getLot4Numero()),
                String.valueOf(this.getLot4SurfaceCarrez()),
                nullToEmpty(this.getLot5Numero()),
                String.valueOf(this.getLot5SurfaceCarrez()),
                String.valueOf(this.getNombreLots()),
                nullToEmpty(this.getCodeTypeLocal()),
                nullToEmpty(this.getTypeLocal()),
                String.valueOf(this.getSurfaceReelleBati()),
                String.valueOf(this.getNombrePiecesPrincipales()),
                nullToEmpty(this.getCodeNatureCultureSpeciale()),
                String.valueOf(this.getSurfaceTerrain()),
                String.valueOf(this.getLongitude()),
                String.valueOf(this.getLatitude())
            );
    }
}
