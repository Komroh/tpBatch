package com.example.tpbatch.tasklet;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;

import static com.example.tpbatch.utils.Constants.DECOMPRESSED_PATH;
import static com.example.tpbatch.utils.Constants.ZIPPED_PATH;

@Component
public class DownloadTasklet implements Tasklet {

    @Value("${urlCsv}")
    String urlCsv;
    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        URL url = new URI(urlCsv).toURL();

        try (InputStream in = url.openStream()) {
            Files.copy(in,
                    Path.of(ZIPPED_PATH),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        decompress();
        return RepeatStatus.FINISHED;
    }

    static void decompress()
    {
        byte[] buffer = new byte[1024];
        try
        {
            GZIPInputStream is =
                    new GZIPInputStream(new FileInputStream(ZIPPED_PATH));

            FileOutputStream out =
                    new FileOutputStream(DECOMPRESSED_PATH);

            int totalSize;
            while((totalSize = is.read(buffer)) > 0 )
            {
                out.write(buffer, 0, totalSize);
            }

            out.close();
            is.close();

            System.out.println("File Successfully decompressed");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}