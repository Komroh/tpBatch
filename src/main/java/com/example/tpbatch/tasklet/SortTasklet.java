package com.example.tpbatch.tasklet;

import com.google.code.externalsorting.ExternalSort;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class SortTasklet implements Tasklet {

    @Value("${file}")
    private  String file;
    @Value("${tempFile}")
    private  String temp;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        File input = new File(file);
        File output = new File(temp);

        List<File> files = ExternalSort.sortInBatch(input,
                Comparator.naturalOrder(),
                null,
                false,
                1);
        ExternalSort.mergeSortedFiles(files, output);

        return RepeatStatus.FINISHED;
    }





}
