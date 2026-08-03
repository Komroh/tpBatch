package com.example.tpbatch.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDate;

@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_mutation")
    private String idMutation;
    @Column(name = "numero_disposition")
    private String numeroDisposition;

    @Column(name = "date_mutation")
    private LocalDate dateMutation;
    @Column(name = "nature_mutation")
    private String natureMutation;

    @Column(name = "valeur_fonciere")
    private Double valeurFonciere;

    @Column(name = "type_local")
    private String typeLocal;
    @Column(name = "surface_reelle_bati")
    private Integer surfaceReelleBati;
    @Column(name = "nombre_pieces_principales")
    private Integer nombrePiecesPrincipales;

    @Column(name = "surface_terrain")
    private Integer surfaceTerrain;

    @Column(name = "adresse_numero")
    private String adresseNumero;
    @Column(name = "adresse_suffixe")
    private String adresseSuffixe;
    @Column(name = "adresse_nom_voie")
    private String adresseNomVoie;
    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "code_commune")
    private String codeCommune;
    @Column(name = "nom_commune")
    private String nomCommune;

    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;

    @Column(columnDefinition = "geometry(Point,4326)")
    Geometry geom;
}
