package com.example.tpbatch.repository;

import com.example.tpbatch.dto.TarifCommuneProjection;
import com.example.tpbatch.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = """
        SELECT
            code_commune AS codeInsee,
            MAX(nom_commune) AS commune,

            COUNT(*) AS nombreTransactions,

            AVG(valeur_fonciere) AS prixMoyen,

            percentile_cont(0.5)
            WITHIN GROUP (
                ORDER BY valeur_fonciere
            ) AS prixMedian,

            AVG(
                valeur_fonciere / NULLIF(surface_reelle_bati,0)
            ) AS prixM2Moyen

        FROM t_transaction

        WHERE code_commune = :codeInsee

        GROUP BY code_commune
        """,
            nativeQuery = true)
    TarifCommuneProjection getTarif(String codeInsee);

    @Query(value = """
        SELECT
            code_commune AS codeInsee,
            MAX(nom_commune) AS commune,

            COUNT(*) AS nombreTransactions,

            AVG(valeur_fonciere) AS prixMoyen,

            percentile_cont(0.5)
            WITHIN GROUP (
                ORDER BY valeur_fonciere
            ) AS prixMedian,

            AVG(
                valeur_fonciere / NULLIF(surface_reelle_bati,0)
            ) AS prixM2Moyen

        FROM t_transaction

        GROUP BY code_commune
        """,
            nativeQuery = true)
    List<TarifCommuneProjection> getTarif();
}
