package com.example.tpbatch.repository;

import com.example.tpbatch.entity.Dvf;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Profile("postgresql")
@Repository
public interface DvfRepositoryPostgresql extends DvfRepository , JpaRepository<Dvf, String>, JpaSpecificationExecutor<Dvf> {
}
