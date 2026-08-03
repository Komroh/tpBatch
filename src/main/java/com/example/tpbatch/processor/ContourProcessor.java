package com.example.tpbatch.processor;

import com.example.tpbatch.entity.Commune;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class ContourProcessor implements ItemProcessor<Commune, Commune> {
    @Override
    public @Nullable Commune process(Commune item) throws Exception {
        return item;
    }
}
