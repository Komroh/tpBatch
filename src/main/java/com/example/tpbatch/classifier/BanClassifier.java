package com.example.tpbatch.classifier;

import com.example.tpbatch.Dto.BanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.classify.Classifier;

public class BanClassifier implements Classifier<BanDto, ItemWriter<? super BanDto>> {
    private final Logger log = LoggerFactory.getLogger(BanClassifier.class);
    private final ItemWriter<BanDto> banItemWriter;
    private final ItemWriter<BanDto> duplicateItemWriter;

    public BanClassifier(ItemWriter<BanDto> banItemWriter, ItemWriter<BanDto> duplicateItemWriter) {
        this.banItemWriter = banItemWriter;
        this.duplicateItemWriter = duplicateItemWriter;
    }

    @Override
    public ItemWriter<? super BanDto> classify(BanDto address) {
        return address.getIsDuplicate() ? duplicateItemWriter : banItemWriter;
    }
}
