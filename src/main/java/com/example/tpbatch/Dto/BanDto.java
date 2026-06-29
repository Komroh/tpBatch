package com.example.tpbatch.Dto;

import com.example.tpbatch.Entity.Ban;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BanDto {
    @Pattern(regexp = "[0-9]{5}_[A-Za-z0-9]+_.*", message = "Mauvais format d'id")
    private String id;
    private String id_fantoir;
    private Integer numero;
    private String rep;
    private String nom_voie;
    private String code_postal;
    private String code_insee;
    private String nom_commune;
    private String code_insee_ancienne_commune;
    private String nom_ancienne_commune ;
    private Double x;
    private Double y;
    private Double lon ;
    private Double lat ;
    private String type_position;
    private String alias ;
    private String nom_ld ;
    private String libelle_acheminement ;
    private String nom_afnor;
    private String source_position;
    private String source_nom_voie;
    private Integer certification_commune;
    private String cad_parcelles;
    private Boolean isDuplicate;

    public static BanDto from(Ban ban) {
            return new BanDto(ban.getId(),
                    ban.getId_fantoir(),
                    ban.getNumero(),
                    ban.getRep(),
                    ban.getNom_voie(),
                    ban.getCode_postal(),
                    ban.getCode_insee(),
                    ban.getNom_commune(),
                    ban.getCode_insee_ancienne_commune(),
                    ban.getNom_ancienne_commune(),
                    ban.getX(),
                    ban.getY(),
                    ban.getLon(),
                    ban.getLat(),
                    ban.getType_position(),
                    ban.getAlias(),
                    ban.getNom_ld(),
                    ban.getLibelle_acheminement(),
                    ban.getNom_afnor(),
                    ban.getSource_position(),
                    ban.getSource_nom_voie(),
                    ban.getCertification_commune(),
                    ban.getCad_parcelles(),
                    false);
        }
        public Ban toBan()
        {
            return new Ban(
                    this.id,
                    this.id_fantoir,
                    this.numero,
                    this.rep,
                    this.nom_voie,
                    this.code_postal,
                    this.code_insee,
                    this.nom_commune,
                    this.code_insee_ancienne_commune,
                    this.nom_ancienne_commune ,
                    this.x,
                    this.y,
                    this.lon ,
                    this.lat ,
                    this.type_position,
                    this.alias ,
                    this.nom_ld ,
                    this.libelle_acheminement ,
                    this.nom_afnor,
                    this.source_position,
                    this.source_nom_voie,
                    this.certification_commune,
                    this.cad_parcelles
            );
        }
}
