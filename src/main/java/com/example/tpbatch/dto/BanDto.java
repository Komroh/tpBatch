package com.example.tpbatch.dto;

import com.example.tpbatch.entity.Ban;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BanDto {
    @Pattern(regexp = "\\d{5}_[A-Za-z0-9]+_.*", message = "Mauvais format d'id")
    private String id;
    private String idFantoir;
    private Integer numero;
    private String rep;
    private String nomVoie;
    private String codePostal;
    private String codeInsee;
    private String nomCommune;
    private String codeInseeAncienneCommune;
    private String nomAncienneCommune ;
    private Double x;
    private Double y;
    private Double lon ;
    private Double lat ;
    private String typePosition;
    private String alias ;
    private String nomLd ;
    private String libelleAcheminement ;
    private String nomAfnor;
    private String sourcePosition;
    private String sourceNomVoie;
    private Integer certificationCommune;
    private String cadParcelles;
    private long hash;
    private Boolean isDuplicate;


    public static BanDto from(Ban ban) {
            return new BanDto(ban.getId(),
                    ban.getIdFantoir(),
                    ban.getNumero(),
                    ban.getRep(),
                    ban.getNomVoie(),
                    ban.getCodePostal(),
                    ban.getCodeInsee(),
                    ban.getNomCommune(),
                    ban.getCodeInseeAncienneCommune(),
                    ban.getNomAncienneCommune(),
                    ban.getX(),
                    ban.getY(),
                    ban.getLon(),
                    ban.getLat(),
                    ban.getTypePosition(),
                    ban.getAlias(),
                    ban.getNomLd(),
                    ban.getLibelleAcheminement(),
                    ban.getNomAfnor(),
                    ban.getSourcePosition(),
                    ban.getSourceNomVoie(),
                    ban.getCertificationCommune(),
                    ban.getCadParcelles(),
                    ban.getHash(),
                    false);
        }
        public Ban toBan()
        {
            return new Ban(
                    this.id,
                    this.idFantoir,
                    this.numero,
                    this.rep,
                    this.nomVoie,
                    this.codePostal,
                    this.codeInsee,
                    this.nomCommune,
                    this.codeInseeAncienneCommune,
                    this.nomAncienneCommune ,
                    this.x,
                    this.y,
                    this.lon ,
                    this.lat ,
                    this.typePosition,
                    this.alias ,
                    this.nomLd ,
                    this.libelleAcheminement ,
                    this.nomAfnor,
                    this.sourcePosition,
                    this.sourceNomVoie,
                    this.certificationCommune,
                    this.cadParcelles,
                    this.hash
            );
        }
}
