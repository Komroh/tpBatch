package com.example.tpbatch.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Table(name="t_ban")
public class Ban{

    @Id
    @Pattern(regexp = "[0-9]{5}_[A-Za-z0-9]+_.*", message = "Mauvais format d'id")
    private String id;
    private String id_fantoir;
    private Integer numero;
    private String rep;
    private String nom_voie;
    private String code_postal;
    private String code_insee;
    private String nom_commune;
    private String code_insee_ancienne_commune;
    private String nom_ancienne_commune ;
    private Double x;
    private Double y;
    private Double lon ;
    private Double lat ;
    private String type_position;
    private String alias ;
    private String nom_ld ;
    private String libelle_acheminement ;
    private String nom_afnor;
    private String source_position;
    private String source_nom_voie;
    private Integer certification_commune;
    private String cad_parcelles;

}
