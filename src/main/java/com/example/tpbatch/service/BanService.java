package com.example.tpbatch.service;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.repository.BanRepository;
import com.example.tpbatch.specification.BanSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BanService {

    private final BanRepository repo;

    @Transactional(readOnly = true)
    public List<Ban> recherche(BanSearchRequest criteria)
    {
        return repo.findAll(BanSpecification.build(criteria));
    }

    @Transactional(readOnly = true)
    public Page<Ban> recherche(BanSearchRequest criteria, Pageable pageable)
    {
        return repo.findAll(BanSpecification.build(criteria), pageable);
    }

    public Page<Ban> rechercheChaine(String chaine, Pageable pageable) {

        Specification<Ban> spec = new BanSpecification().compareString(chaine);
        return repo.findAll(spec, pageable);
    }

    public Page<Ban> rechercheFullText(String chaine, Pageable pageable)
    {
        chaine = chaine.replaceAll("-", " ");
        return repo.search(chaine, pageable);
    }
}
