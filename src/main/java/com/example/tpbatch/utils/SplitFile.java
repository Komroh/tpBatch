package com.example.tpbatch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SplitFile {

    private static final Logger log = LoggerFactory.getLogger(SplitFile.class);

    public static void splitFile(String fileName,
                                 String  outputDir,
                                 Integer numberOfPart, String delimiter) throws IOException {
        Path outputPath = Path.of(outputDir);

        log.info("Splitting file {} to {}", fileName, numberOfPart);
        long lineNb = 0;
        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName),
                StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            BufferedWriter[] writers = new BufferedWriter[numberOfPart];

            for (int i = 0; i < numberOfPart; i++) {
                Path output = outputPath.resolve("temp_" + i + ".csv");
                writers[i] = Files.newBufferedWriter(output);
                writers[i].write(header);
                writers[i].newLine();
            }

            String line;

            while ((line = reader.readLine()) != null) {
                lineNb++;
                String[] columns = line.split(delimiter, -1);

                String id = columns[0]; // première colonne = id

                int partition = Math.floorMod(id.hashCode(), numberOfPart);

                writers[partition].write(line);
                writers[partition].newLine();
            }

            for (BufferedWriter writer : writers) {
                writer.close();
            }
        }catch(MalformedInputException e){
            log.error("Erreur de lecture du fichier : {}", e.getMessage());
            throw new IOException("Erreur de lecture du fichier : " + lineNb +e.getMessage(), e);
        }
    }
}
