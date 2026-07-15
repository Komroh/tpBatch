package com.example.tpbatch.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SplitFile {

    public static void splitFile(String fileName,
                                 String  outputDir,
                                 Integer numberOfPart) throws IOException {
        int totalLines = getNoOfLines(fileName) - 1;
        int linePerFile = (int) Math.ceil((double) (totalLines) / numberOfPart);

        try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName))) {
            BufferedWriter writer = null;

            int currentLine = 0;
            int currentPart = 0;

            String line;
            String header = reader.readLine();
            Path outputPath = Path.of(outputDir);
            while((line = reader.readLine()) !=null){
                if(currentLine % linePerFile == 0)
                {
                    if (writer != null) {
                        writer.close();
                    }
                    Path output = outputPath.resolve("ban_"+ currentPart + ".csv");
                    writer = Files.newBufferedWriter(output);

                    writer.write(header);
                    writer.newLine();

                    currentPart++;
                }

                writer.write(line);
                writer.newLine();

                currentLine++;
            }
            if (writer != null) {
                writer.close();
            }
        }
    }


    private static int getNoOfLines(String fileName) throws IOException {

        try (LineNumberReader reader =
                     new LineNumberReader(new FileReader(fileName))) {

            while (reader.readLine() != null) {
                // lecture jusqu'à la fin
            }

            return reader.getLineNumber();
        }
    }
}
