package com.example.tpbatch.writer;

import com.example.tpbatch.Dto.BanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BanRoutingWriter implements ItemWriter<BanDto> {

    private final JdbcBatchItemWriter<BanDto> banWriter;
    private final JdbcBatchItemWriter<BanDto> duplicateWriter;

    @Override
    public void write(Chunk<? extends BanDto> chunk) throws Exception {
        List<BanDto> bans = new ArrayList<>();
        List<BanDto> duplicates = new ArrayList<>();

        for (BanDto dto : chunk.getItems()) {
            if (dto.getIsDuplicate()) {
                duplicates.add(dto);
            } else {
                bans.add(dto);
            }
        }

        if (!bans.isEmpty()) {
            banWriter.write(new Chunk<>(bans));
        }

        if (!duplicates.isEmpty()) {
            duplicateWriter.write(new Chunk<>(duplicates));
        }
    }
}
