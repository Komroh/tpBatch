package com.example.tpbatch.controller;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.service.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BanController {


    private final BanService service;

    @GetMapping("/recherche")
    public List<Ban> recherche(
            @RequestParam(required = false) String codePostal,
            @RequestParam(required = false) String rue,
            @RequestParam(required = false) String commune,
            @RequestParam(required = false) Integer numero
    )
    {
        return service.recherche(new BanSearchRequest(codePostal, rue, commune, numero));
    }

    @GetMapping("/recherche/page")
    public Page<Ban> recherche(
            @RequestParam(required = false) String codePostal,
            @RequestParam(required = false) String rue,
            @RequestParam(required = false) String commune,
            @RequestParam(required = false) Integer numero,
            @PageableDefault(size = 20, sort = "nomCommune")Pageable pageable

            )
    {
        return service.recherche(new BanSearchRequest(codePostal, rue, commune,numero),pageable);
    }

    @GetMapping("/recherche/chaine")
    public List<Ban> rechercheChaine(
            @RequestParam String chaine,
            @PageableDefault(size = 20, sort = "codePostal")Pageable pageable
    )
    {
        return service.rechercheChaine(chaine,pageable).getContent();
    }

}
