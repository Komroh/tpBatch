package com.example.tpbatch.Dto;

import com.example.tpbatch.Entity.Ban;

public record BanDto(
        String id,
        String id_fantoir,
        Integer numero,
        String rep,
        String nom_voie,
        String code_postal,
        String code_insee,
        String nom_commune,
        String code_insee_ancienne_commune,
        String nom_ancienne_commune ,
        Double x,
        Double y,
        Double lon ,
        Double lat ,
        String type_position,
        String alias ,
        String nom_ld ,
        String libelle_acheminement ,
        String nom_afnor,
        String source_position,
        String source_nom_voie,
        Integer certification_commune,
        String cad_parcelles
) {

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
                    ban.getCad_parcelles());
        }
}
