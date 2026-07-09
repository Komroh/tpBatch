package com.example.tpbatch.Dto;

public record BanSearchRequest(
        String codePastal,
        String rue,
        String commune,
        Integer numero
) {
}
