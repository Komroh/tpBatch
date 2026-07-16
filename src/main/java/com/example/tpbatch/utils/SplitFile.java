package com.example.tpbatch.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SplitFile {

    public static void splitFile(String fileName,
                                 String  outputDir,
                                 Integer numberOfPart) throws IOException {
        Path outputPath = Path.of(outputDir);

        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {

            String header = reader.readLine();

            BufferedWriter[] writers = new BufferedWriter[numberOfPart];

            for (int i = 0; i < numberOfPart; i++) {
                Path output = outputPath.resolve("ban_" + i + ".csv");
                writers[i] = Files.newBufferedWriter(output);
                writers[i].write(header);
                writers[i].newLine();
            }

            String line;

            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(";", -1);

                String id = columns[0]; // première colonne = id

                int partition = Math.floorMod(id.hashCode(), numberOfPart);

                writers[partition].write(line);
                writers[partition].newLine();
            }

            for (BufferedWriter writer : writers) {
                writer.close();
            }
        }
    }
}
