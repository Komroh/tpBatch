package com.example.tpbatch.controller;

import com.example.tpbatch.dto.BanSearchRequest;
import com.example.tpbatch.dto.TarifDto;
import com.example.tpbatch.entity.Ban;
import com.example.tpbatch.service.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/recherche/fullText")
    public List<Ban> recherchefullText(
            @RequestParam String chaine,
            @PageableDefault(size = 20, sort = "code_postal")Pageable pageable
    )
    {
        return service.rechercheFullText(chaine, pageable).getContent();
    }

    @GetMapping("/recherche/inverse")
    public Ban rechercheInverse(
            @RequestParam Double lat,
            @RequestParam Double lon
    )
    {
        return service.rechercheInverse(lat,lon);
    }

    @PostMapping("/batch/lancer")
    public ResponseEntity<?> lancer(@RequestParam (required = false) String typeCriteria, @RequestParam (required = false)  String criteria)
    {
       return service.lancer(typeCriteria, criteria);
    }

    @GetMapping(value = "/geojson")
    public ResponseEntity<Resource> getCommunes(@Value("${contourFile}") String contourFile)
    {
        Resource resource = new FileSystemResource(contourFile);
        return ResponseEntity.ok().body(resource);
    }
    @GetMapping(value="communes/{codeInsee}/tarif")
    public TarifDto tarif(
            @PathVariable String codeInsee
    ){
        return service.getTarif(codeInsee);
    }

}
