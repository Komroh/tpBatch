package com.example.tpbatch.processor;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Dto.DvfDto;
import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.Entity.Dvf;
import com.example.tpbatch.metrics.BanMetrics;
import com.example.tpbatch.repository.DvfRepository;
import com.example.tpbatch.utils.HashCalcul;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@StepScope
@RequiredArgsConstructor
public class DuplicateDvfProcessor implements ItemProcessor<Dvf, DvfDto>, ChunkListener<Ban, BanDto> {
    private static final Logger log =
            LoggerFactory.getLogger(DuplicateDvfProcessor.class);

    private final DvfRepository dvfRepo;
    private final BanMetrics banMetrics;
    private final MeterRegistry meterRegistry;

    private final Map<String, Dvf> chunkDvfs = new HashMap<>();

    @Override
    public void beforeChunk(@NonNull Chunk c) {
        chunkDvfs.clear();
    }

    @Override
    public @Nullable DvfDto process(Dvf dvf) throws Exception {
        String dvfId = dvf.getIdMutation();

        Dvf doublon = null;

        dvf.setHash(HashCalcul.calculHash(dvf));
        DvfDto dvfDto = DvfDto.from(dvf);

        if(chunkDvfs.containsKey(dvfId)) {
            doublon = chunkDvfs.get(dvfId);
        } else {
            doublon = dvfRepo.findById(dvfId).orElse(null);
        }

        if(doublon != null) {
            if (dvf.equals(doublon)) {
                log.debug("Filtrage Doublon pur : " + dvfId);
                banMetrics.incrementDuplicateSame();
                return null;
            } else {
                log.debug("Doublon avec champs différents : " + dvfId);
                banMetrics.incrementDuplicateDiff();
                dvfDto.setIsDuplicate(true);
            }
        }
        chunkDvfs.put(dvfId,dvf);
        return dvfDto;
    }
}
