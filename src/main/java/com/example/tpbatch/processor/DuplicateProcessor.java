package com.example.tpbatch.processor;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.repository.BanRepository;
import com.example.tpbatch.utils.HashCalcul;
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
public class DuplicateProcessor implements ItemProcessor<Ban, BanDto>, ChunkListener<Ban, BanDto> {
    private static final Logger log =
            LoggerFactory.getLogger(DuplicateProcessor.class);

    private final BanRepository banRepo;

    private final Map<String, Ban> chunkAddresses = new HashMap<>();

    @Override
    public void beforeChunk(@NonNull Chunk c) {
        chunkAddresses.clear();
    }

    @Override
    public @Nullable BanDto process(Ban address) throws Exception {
        String addrId = address.getId();

        Ban doublon = null;

        address.setHash(HashCalcul.calculHash(address));
        BanDto addressDto = BanDto.from(address);

        if(chunkAddresses.containsKey(addrId)) {
            doublon = chunkAddresses.get(addrId);
        } else {
            doublon = banRepo.findById(addrId).orElse(null);
        }

        if(doublon != null) {
            if (address.equals(doublon)) {
                log.debug("Filtrage Doublon pur : " + addrId);
                return null;
            } else {
                log.debug("Doublon avec champs différents : " + addrId);
                addressDto.setIsDuplicate(true);
            }
        }
        chunkAddresses.put(addrId,address);
        return addressDto;
    }
}
