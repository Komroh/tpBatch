package com.example.tpbatch.listener;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Entity.Ban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemProcessListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class BanItemProcessListener implements ItemProcessListener<Ban, BanDto> {

    private static final Logger log = LoggerFactory.getLogger(BanItemProcessListener.class);

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public void afterProcess(Ban item, BanDto result) {
        long count = counter.incrementAndGet();

        if (count % 10_000 == 0) {
            log.info("Éléments traités : {}", count);
        }
    }

    @Override
    public void onProcessError(Ban address, Exception e)
    {

            log.error(e.getCause().getMessage());

    }
}
