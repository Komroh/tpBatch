package com.example.tpbatch.specification;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.stream.Stream;

public class BanSpecification {

    private static Specification<Ban> PostalCode(String code)
    {
        if(code == null || code.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("codePostal"), code + "%");
        };
    }
    private static Specification<Ban> streetCriteria(String streetName)
    {
        if(streetName == null || streetName.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("nomVoie"), streetName);
        };
    }
    private static Specification<Ban> cityCriteria(String cityName)
    {
        if(cityName == null || cityName.isBlank()) return null;
        return (root, query, cb) ->{
            return cb.like(root.get("nomCommune"), cityName);
        };
    }

    public static Specification<Ban> build(BanSearchRequest criteria)
    {
        return Specification.allOf(
                Stream.of(
                                PostalCode(criteria.codePastal()),
                                streetCriteria(criteria.rue()),
                                cityCriteria(criteria.commune())
                        )
                        .filter(Objects::nonNull)
                        .toList()
        );
    }
}
