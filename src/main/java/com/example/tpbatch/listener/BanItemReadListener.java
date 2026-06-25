package com.example.tpbatch.listener;

import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.repository.BanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BanItemReadListener implements ItemReadListener<Ban> {
    private static final Logger log =
            LoggerFactory.getLogger(BanItemReadListener.class);

    private final BanRepository banRepo;

    @Override
    public void afterRead(Ban input)
    {

        if (banRepo.existsById(input.getId()))
        {
            log.debug(input.getId());
        }
    }
}
