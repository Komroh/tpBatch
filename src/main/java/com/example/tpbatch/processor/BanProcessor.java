package com.example.tpbatch.processor;

import com.example.tpbatch.Dto.BanDto;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class BanProcessor implements ItemProcessor<BanDto, BanDto> {

    @Value("#{jobParameters['typeCriteria']}") private String typeCriteria;
    @Value("#{jobParameters['criteria']}") private String criteria;


    @Override
    public @Nullable BanDto process(@NonNull BanDto address) throws Exception {

        if(typeCriteria.isEmpty() && criteria.isEmpty())
        {
            return address;
        }
        if(!typeCriteria.isEmpty()) {
            return switch (typeCriteria) {
                case "dept" -> address.getCode_postal().startsWith(criteria) ? address : null;
                case "postal" -> address.getCode_postal().equals(criteria) ? address : null;
                case "insee" -> address.getCode_insee().equals(criteria) ? address : null;
                default -> throw new IllegalArgumentException("Invalid field argument");
            };
        }
        return null;
    }


}
