package com.example.tpbatch.repository;

import com.example.tpbatch.entity.Dvf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DvfRepository extends JpaRepository<Dvf, String>, JpaSpecificationExecutor<Dvf> {
}
