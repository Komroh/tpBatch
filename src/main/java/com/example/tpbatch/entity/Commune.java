package com.example.tpbatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Entity
@Getter
@Setter
@Table(name = "t_commune")
public class Commune {

    @Id
    @Column(name = "code_insee",unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String nom;

    @Column(length = 2)
    private String departement;

    @Column(length = 2)
    private String region;

    @Column(length = 9)
    private String epci;

    @Column(columnDefinition = "geometry(MultiPolygon,4326)")
    private Geometry geom;
}
