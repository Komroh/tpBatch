package com.example.tpbatch.utils;

import com.example.tpbatch.Dto.BanDto;

import java.nio.charset.StandardCharsets;
import net.openhft.hashing.LongHashFunction;

public class HashCalcul {

    public static long calculHash(BanDto b) {

        String valeur = String.join("|",
                nullToEmpty(b.getId_fantoir()),
                String.valueOf(b.getNumero()),
                nullToEmpty(b.getRep()),
                nullToEmpty(b.getNom_voie()),
                nullToEmpty(b.getCode_postal()),
                nullToEmpty(b.getCode_insee()),
                nullToEmpty(b.getNom_commune()),
                nullToEmpty(b.getCode_insee_ancienne_commune()),
                nullToEmpty(b.getNom_ancienne_commune()),
                String.valueOf(b.getX()),
                String.valueOf(b.getY()),
                String.valueOf(b.getLon()),
                String.valueOf(b.getLat()),
                nullToEmpty(b.getType_position()),
                nullToEmpty(b.getAlias()),
                nullToEmpty(b.getNom_ld()),
                nullToEmpty(b.getLibelle_acheminement()),
                nullToEmpty(b.getNom_afnor()),
                nullToEmpty(b.getSource_position()),
                nullToEmpty(b.getSource_nom_voie()),
                String.valueOf(b.getCertification_commune()),
                nullToEmpty(b.getCad_parcelles())
    );

        return LongHashFunction.xx3().hashBytes(valeur.getBytes(StandardCharsets.UTF_8));
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
