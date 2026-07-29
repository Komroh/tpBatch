package com.example.tpbatch.Entity;

import com.example.tpbatch.utils.Hashable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.example.tpbatch.utils.HashCalcul.nullToEmpty;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Table(name="t_ban")
public class Ban implements Hashable {

    @Id
    @Pattern(regexp = "[0-9]{5}_[A-Za-z0-9]+_.*", message = "Mauvais format d'id")
    private String id;
    @Column(name = "id_fantoir")
    private String idFantoir;
    private Integer numero;
    private String rep;
    @Column(name = "nom_voie")
    private String nomVoie;
    @Column(name = "code_postal")
    private String codePostal;
    @Column(name = "code_insee")
    private String codeInsee;
    @Column(name = "nom_commune")
    private String nomCommune;
    @Column(name = "code_insee_ancienne_commune")
    private String codeInseeAncienneCommune;
    @Column(name = "nom_ancienne_commune")
    private String nomAncienneCommune ;
    private Double x;
    private Double y;
    private Double lon ;
    private Double lat ;
    @Column(name = "type_position")
    private String typePosition;
    private String alias ;
    @Column(name = "nom_ld")
    private String nomLd ;
    @Column(name = "libelle_acheminement")
    private String libelleAcheminement ;
    @Column(name = "nom_afnor")
    private String nomAfnor;
    @Column(name = "source_position")
    private String sourcePosition;
    @Column(name = "source_nom_voie")
    private String sourceNomVoie;
    @Column(name = "certification_commune")
    private Integer certificationCommune;
    @Column(name = "cad_parcelles", length = 500)
    private String cadParcelles;
    @Column(name = "hash")
    private long hash;

    @Override
    public String HashContent() {
        return String.join("|",
                nullToEmpty(this.getId()),
                String.valueOf(this.getNumero()),
                nullToEmpty(this.getRep()),
                nullToEmpty(this.getNomVoie()),
                nullToEmpty(this.getCodePostal()),
                nullToEmpty(this.getCodeInsee()),
                nullToEmpty(this.getNomCommune()),
                nullToEmpty(this.getCodeInseeAncienneCommune()),
                nullToEmpty(this.getNomAncienneCommune()),
                String.valueOf(this.getX()),
                String.valueOf(this.getY()),
                String.valueOf(this.getLon()),
                String.valueOf(this.getLat()),
                nullToEmpty(this.getTypePosition()),
                nullToEmpty(this.getAlias()),
                nullToEmpty(this.getNomLd()),
                nullToEmpty(this.getLibelleAcheminement()),
                nullToEmpty(this.getNomAfnor()),
                nullToEmpty(this.getSourcePosition()),
                nullToEmpty(this.getSourceNomVoie()),
                String.valueOf(this.getCertificationCommune()),
                nullToEmpty(this.getCadParcelles())
            );
    }
}
