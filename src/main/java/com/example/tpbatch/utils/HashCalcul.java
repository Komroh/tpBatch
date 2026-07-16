package com.example.tpbatch.utils;

import java.nio.charset.StandardCharsets;
import com.example.tpbatch.Entity.Ban;
import net.openhft.hashing.LongHashFunction;

public class HashCalcul {

    public static long calculHash(Ban b) {

        String valeur = String.join("|",
                nullToEmpty(b.getId()),
                String.valueOf(b.getNumero()),
                nullToEmpty(b.getRep()),
                nullToEmpty(b.getNomVoie()),
                nullToEmpty(b.getCodePostal()),
                nullToEmpty(b.getCodeInsee()),
                nullToEmpty(b.getNomCommune()),
                nullToEmpty(b.getCodeInseeAncienneCommune()),
                nullToEmpty(b.getNomAncienneCommune()),
                String.valueOf(b.getX()),
                String.valueOf(b.getY()),
                String.valueOf(b.getLon()),
                String.valueOf(b.getLat()),
                nullToEmpty(b.getTypePosition()),
                nullToEmpty(b.getAlias()),
                nullToEmpty(b.getNomLd()),
                nullToEmpty(b.getLibelleAcheminement()),
                nullToEmpty(b.getNomAfnor()),
                nullToEmpty(b.getSourcePosition()),
                nullToEmpty(b.getSourceNomVoie()),
                String.valueOf(b.getCertificationCommune()),
                nullToEmpty(b.getCadParcelles())
    );

        return LongHashFunction.xx3().hashBytes(valeur.getBytes(StandardCharsets.UTF_8));
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
