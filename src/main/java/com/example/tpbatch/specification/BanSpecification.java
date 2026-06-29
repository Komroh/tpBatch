package com.example.tpbatch.specification;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.stream.Stream;

public class BanSpecification {

    private static Specification<Ban> codePostal(String code)
    {
        if(code == null || code.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("codePostal"), code + "%");
        };
    }
    private static Specification<Ban> rue(String nomRue)
    {
        if(nomRue == null || nomRue.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("nomVoie"), nomRue);
        };
    }
    private static Specification<Ban> commune(String nomCommune)
    {
        if(nomCommune == null || nomCommune.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("nomCommune"), nomCommune);
        };
    }

    public static Specification<Ban> build(BanSearchRequest criteria)
    {
        return Specification.allOf(
                Stream.of(
                     codePostal(criteria.codePastal()),
                     rue(criteria.rue()),
                     commune(criteria.commune())
                )
                .filter(Objects::nonNull)
                .toList()
        );
    }
}
