package com.example.tpbatch.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;


@Component
public class BanMetrics {

    private final Counter duplicateDiffCounter;
    private final Counter duplicateSameCounter;
    private final Counter itemProcessedCounter;
    private final MeterRegistry meterRegistry;


    public BanMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.duplicateDiffCounter = Counter.builder("duplicate.diff")
                .description("Nombre de doublons avec un champs différent")
                .register(meterRegistry);
        this.duplicateSameCounter = Counter.builder("duplicate.same")
                .description("Nombre de doublons avec les champs identiques")
                .register(meterRegistry);
        this.itemProcessedCounter = Counter.builder("item.processed")
                .description("Nombre d'éléments traités")
                .register(meterRegistry);

    }

    public void incrementDuplicateDiff() {
        this.duplicateDiffCounter.increment();
    }

    public void incrementDuplicateSame() {
        this.duplicateSameCounter.increment();
    }

    public void incrementItemProcessed() {
        this.itemProcessedCounter.increment();
    }

    public double getDuplicateDiff() {
        return this.duplicateDiffCounter.count();
    }

    public double getDuplicateSame() {
        return this.duplicateSameCounter.count();
    }

    public double getItemProcessed() {
        return this.itemProcessedCounter.count();
    }
}
