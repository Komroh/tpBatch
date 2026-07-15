package com.example.tpbatch.repository;

import com.example.tpbatch.Entity.Ban;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface BanRepository extends JpaRepository<Ban, String>, JpaSpecificationExecutor<Ban> {

    Page<Ban> findAll(Specification<Ban> spec, Pageable page);

    Page<Ban> search(@Param("query") String query, Pageable page);
}
