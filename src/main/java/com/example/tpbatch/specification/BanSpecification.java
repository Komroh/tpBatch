package com.example.tpbatch.specification;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.stream.Stream;

public class BanSpecification {

    private static Specification<Ban> postalCode(String code)
    {
        if(code == null || code.isBlank()) return null;
        return (root, query, cb) ->
             cb.like(root.get("codePostal"), code.trim() + "%");

    }

    private static Specification<Ban> numberCriteria(Integer number)
    {
        if(number == null) return null;
        return (root, query, cb) ->
             cb.equal(root.get("numero"), number);

    }
    private static Specification<Ban> streetCriteria(String streetName)
    {
        if(streetName == null || streetName.isBlank()) return null;
        return (root, query, cb) ->
             cb.like(cb.upper(root.get("nomVoie")), streetName.toUpperCase().trim());

    }
    private static Specification<Ban> cityCriteria(String cityName)
    {
        if(cityName == null || cityName.isBlank()) return null;
        return (root, query, cb) ->
             cb.like(cb.upper(root.get("nomCommune")), cityName.toUpperCase().trim());

    }

    public static Specification<Ban> build(BanSearchRequest criteria)
    {
        return Specification.allOf(
                Stream.of(
                                postalCode(criteria.codePastal()),
                                streetCriteria(criteria.rue()),
                                cityCriteria(criteria.commune()),
                                numberCriteria(criteria.numero())

                        )
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    public Specification<Ban> compareString(String chaine)
    {
        return (((root, query, criteriaBuilder) -> {
            Expression<String> numeroRue = criteriaBuilder.concat(criteriaBuilder.concat(root.get("numero").as(String.class)," "), root.get("nomVoie"));
            Expression<String> numeroRueCode = criteriaBuilder.concat(criteriaBuilder.concat(numeroRue, " "), root.get("codePostal"));
            Expression<String> addrExp = criteriaBuilder.concat(criteriaBuilder.concat(numeroRueCode, " "), root.get("nomCommune"));
            return criteriaBuilder.like(criteriaBuilder.upper(addrExp), chaine.toUpperCase() + "%");
        }));
    }

    public static Specification<Ban> orderByDistance(Double lat, Double lon)
    {
        return (root, query, cb) -> {
            Expression<Double> distance = cb.function("sqrt", Double.class,
                    cb.sum(
                            cb.prod(cb.diff(root.get("lat"), lat), cb.diff(root.get("lat"), lat)),
                            cb.prod(cb.diff(root.get("lon"), lon), cb.diff(root.get("lon"), lon))
                    )
            );
            query.orderBy(cb.asc(distance));
            return cb.conjunction();
        };
    }

    public static Specification<Ban> withinRange(Double lat, Double lon)
    {
        double halfSide = 50.0; // mètres

        double deltaLat = halfSide / 111320.0;
        double deltaLon = halfSide / (111320.0 * Math.cos(Math.toRadians(lat)));

        double latMin = lat - deltaLat;
        double latMax = lat + deltaLat;
        double lonMin = lon - deltaLon;
        double lonMax = lon + deltaLon;

        return (root, query, cb) -> cb.and(
                cb.between(root.get("lat"), latMin, latMax),
                cb.between(root.get("lon"), lonMin, lonMax)
        );

    }
}
