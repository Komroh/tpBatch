package com.example.tpbatch.listener;

import com.example.tpbatch.Entity.Ban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
public class BanItemProcessListener implements ItemProcessListener<Ban, Ban> {

    private static final Logger log = LoggerFactory.getLogger(BanItemProcessListener.class);

    @Override
    public void onProcessError(Ban address, Exception e)
    {

            log.error(e.getCause().getMessage());

    }
}
