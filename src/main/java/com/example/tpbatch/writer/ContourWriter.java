package com.example.tpbatch.writer;

import com.example.tpbatch.entity.Commune;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContourWriter {

    @Bean
    @StepScope
    public JpaItemWriter<Commune> contourItemWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriter<>(entityManagerFactory);
    }
}
