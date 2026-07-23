package com.example.tpbatch.repository;

import com.example.tpbatch.Entity.Ban;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Profile("postgresql")
@Repository
public interface BanRepositoryPostgresql extends BanRepository , JpaRepository<Ban, String>, JpaSpecificationExecutor<Ban> {

    @Query(value = """
        SELECT a.*
        FROM t_ban a
        WHERE search_vector @@ plainto_tsquery('simple', :query)
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM t_ban b
        WHERE b.search_vector @@ plainto_tsquery('simple', :query)
        """,
            nativeQuery = true)
    Page<Ban> search(@Param("query") String query, Pageable page);



    @Query(value = """
    SELECT *
    FROM t_ban
    WHERE ST_DWithin(
        geom::geography,
        ST_SetSRID(ST_MakePoint(:lon,:lat),4326)::geography,
        :radius
    )
    ORDER BY geom <-> ST_SetSRID(ST_MakePoint(:lon,:lat),4326)
    LIMIT 1
    """, nativeQuery = true)
    Ban findClosest(double lat, double lon, double radius);
}
