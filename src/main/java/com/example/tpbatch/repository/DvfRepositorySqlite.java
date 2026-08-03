package com.example.tpbatch.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("sqlite")
@Repository
public interface DvfRepositorySqlite {
}
