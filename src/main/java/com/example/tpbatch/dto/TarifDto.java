package com.example.tpbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TarifDto {
    private String codeInsee;
    private String commune;
    private Long nombreTransactions;
    private Double prixMoyen;
    private Double prixMedian;
    private Double prixM2Moyen;
}
