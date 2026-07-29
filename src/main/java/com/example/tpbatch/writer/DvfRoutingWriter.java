package com.example.tpbatch.writer;

import com.example.tpbatch.dto.DvfDto;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class DvfRoutingWriter implements ItemWriter<DvfDto> {
    private final ItemWriter<DvfDto> dvfItemWriter;
    private final ItemWriter<DvfDto> duplicateItemWriter;

    public DvfRoutingWriter(ItemWriter<DvfDto> dvfItemWriter, ItemWriter<DvfDto> duplicateItemWriter) {
        this.dvfItemWriter = dvfItemWriter;
        this.duplicateItemWriter = duplicateItemWriter;
    }

    @Override
    public void write(Chunk<? extends DvfDto> chunk) throws Exception {
        List<DvfDto> dvfs = new ArrayList<>();
        List<DvfDto> duplicates = new ArrayList<>();

        for (DvfDto dto : chunk.getItems()) {
            if (dto.getIsDuplicate()) {
                duplicates.add(dto);
            } else {
                dvfs.add(dto);
            }
        }

        if (!dvfs.isEmpty()) {
            dvfItemWriter.write(new Chunk<>(dvfs));
        }

        if (!duplicates.isEmpty()) {
            duplicateItemWriter.write(new Chunk<>(duplicates));
        }
    }
}
