package com.example.tpbatch.dto;

public interface TarifCommuneProjection {

    String getCodeInsee();

    String getCommune();

    Long getNombreTransactions();

    Double getPrixMoyen();

    Double getPrixMedian();

    Double getPrixM2Moyen();
}
