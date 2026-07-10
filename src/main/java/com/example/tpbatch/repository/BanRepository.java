package com.example.tpbatch.repository;

import com.example.tpbatch.Entity.Ban;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends JpaRepository<Ban, String>, JpaSpecificationExecutor<Ban> {

    Page<Ban> findAll(Specification<Ban> spec, Pageable page);

    @Query(value = """
        SELECT a.*
        FROM t_ban a
        JOIN address_fts fts ON a.rowid = fts.rowid
        WHERE address_fts MATCH :query
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM t_ban b
        JOIN address_fts fts ON b.rowid = fts.rowid
        WHERE address_fts MATCH :query
        """,
            nativeQuery = true)
    Page<Ban> search(@Param("query") String query, Pageable page);
}
