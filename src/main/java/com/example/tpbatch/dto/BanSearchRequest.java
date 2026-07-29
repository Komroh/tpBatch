package com.example.tpbatch.dto;

public record BanSearchRequest(
        String codePastal,
        String rue,
        String commune,
        Integer numero
) {
}
