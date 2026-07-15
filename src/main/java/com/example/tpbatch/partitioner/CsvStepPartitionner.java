package com.example.tpbatch.partitioner;

import com.example.tpbatch.utils.SplitFile;
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

    @Value("${outputDir}")
    private String outputDir;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions  = new HashMap<>();

        try {
            SplitFile.splitFile(filename,outputDir, gridSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < gridSize; i++) {

            ExecutionContext context = new ExecutionContext();

            context.putString(
                    "file",
                    outputDir +  "/ban_" + i + ".csv"
            );

            partitions.put("partition" + i, context);
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
