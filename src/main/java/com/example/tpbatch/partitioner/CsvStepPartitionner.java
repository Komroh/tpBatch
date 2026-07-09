package com.example.tpbatch.partitioner;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class CsvStepPartitionner implements Partitioner {
    @Value("${tempFile}")
    private String filename;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions  = new HashMap<>();

        int totalLines = 0;
        try {
            totalLines = getNoOfLines(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int partitionSize = (int) Math.ceil((double) totalLines / gridSize);

        int start = 0;
        for(int i = 0; i < gridSize && start <= totalLines; i++) {
            int end = Math.min(start + partitionSize - 1, totalLines);
            ExecutionContext context = new ExecutionContext();
            context.putLong("PartitionNumber", i);
            context.putLong("first_line", start);
            context.putLong("last_line", end);

            partitions.put("partition" + i, context);

            start = end + 1;
            }

        return partitions;
    }

    private int getNoOfLines(String fileName) throws IOException {

        try (LineNumberReader reader =
                     new LineNumberReader(new FileReader(fileName))) {

            while (reader.readLine() != null) {
                // lecture jusqu'à la fin
            }

            return reader.getLineNumber();
        }
    }

}
