package com.example.tpbatch.listener;

import com.example.tpbatch.dto.DvfDto;
import com.example.tpbatch.entity.Dvf;
import com.example.tpbatch.metrics.BanMetrics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemProcessListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class DvfItemProcessListener implements ItemProcessListener<Dvf, DvfDto> {

    private static final Logger log = LoggerFactory.getLogger(DvfItemProcessListener.class);

    private final AtomicLong counter = new AtomicLong(0);
    private final BanMetrics banMetrics;

    @Override
    public void afterProcess(Dvf item, DvfDto result) {
        long count = counter.incrementAndGet();
        banMetrics.incrementItemProcessed();
        if (count % 10_000 == 0) {
            log.info("Éléments traités : {}", count);
        }
    }

    @Override
    public void onProcessError(Dvf item, Exception e)
    {
            log.error(e.getCause().getMessage());
    }
}
