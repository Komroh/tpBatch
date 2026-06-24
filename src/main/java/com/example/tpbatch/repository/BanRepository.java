package com.example.tpbatch.repository;

import com.example.tpbatch.Entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanRepository extends JpaRepository<Ban, String> {
}
