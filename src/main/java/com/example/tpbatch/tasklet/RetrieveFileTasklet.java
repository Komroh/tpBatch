package com.example.tpbatch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;

import static com.example.tpbatch.utils.ComputeChecksum.computeChecksum;
import static com.example.tpbatch.utils.Constants.*;

@Component
@StepScope
public class RetrieveFileTasklet implements Tasklet {

    @Value("#{jobParameters['url']}")
    String urlString;

    @Value("#{jobParameters['filePath']}")
    String filePath;

    @Value("#{jobParameters['valideHeader']}")
    String valideHeader;

    private static final Logger log = LoggerFactory.getLogger(RetrieveFileTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        int count = 0;
        File[] listFiles = new File(DOWNLOAD_PATH).listFiles();

        assert listFiles != null;
        for (File file : listFiles)
        {
            if(file.getName().endsWith(".csv"))
            {
                count++;
            }
        }

        if(count > 1)
        {
            log.error("Plusieurs fichiers csv trouvés");
            contribution.setExitStatus(new ExitStatus("MULTIPLE_FILES_FOUND"));
            contribution.getStepExecution().getJobExecution().getExecutionContext().put("retrieveStatus", "MULTIPLE_FILES_FOUND");
            return RepeatStatus.FINISHED;
        }

        File file = new File(filePath);
        Path zipPath = Path.of(ZIPPED_PATH);

        if (! file.exists()) {
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
            );
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(60000);

            try (InputStream in = connection.getInputStream()) {
                Files.copy(in,
                        zipPath,
                        StandardCopyOption.REPLACE_EXISTING);
            }catch(Exception e) {
                log.error("Erreur lors du téléchargement de fichier");
                contribution.setExitStatus(new ExitStatus("NO_INPUT_FILE"));
                contribution.getStepExecution().getJobExecution().getExecutionContext().put("retrieveStatus", "NO_INPUT_FILE");
                return RepeatStatus.FINISHED;
            }

            decompress();
        }
        else {
            log.info("Fichier existant");
        }

        boolean headerIsValid = checkValidity();
        if(headerIsValid) {
            contribution.getStepExecution()
                    .getJobExecution()
                    .getExecutionContext()
                    .putString("checksum", computeChecksum(filePath));
        }
        else  {
            log.error("Format du fichier non valide");
            contribution.setExitStatus(new ExitStatus("WRONG_FILE_FORMAT"));
            contribution.getStepExecution().getJobExecution().getExecutionContext().put("retrieveStatus", "WRONG_FILE_FORMAT");
        }
        contribution.getStepExecution().getJobExecution().getExecutionContext().put("retrieveStatus", "COMPLETED");
        Files.deleteIfExists(zipPath);
        return RepeatStatus.FINISHED;
    }

    private boolean checkValidity() throws IOException {
        String header;
       try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
           header = br.readLine();
       }
        return header.equals(valideHeader);
    }

    private void decompress()
    {
        byte[] buffer = new byte[1024];
        try
        {
            GZIPInputStream is =
                    new GZIPInputStream(new FileInputStream(ZIPPED_PATH));

            FileOutputStream out =
                    new FileOutputStream(filePath);

            int totalSize;
            while((totalSize = is.read(buffer)) > 0 )
            {
                out.write(buffer, 0, totalSize);
            }

            out.close();
            is.close();

            log.info("Fichier décompressé avec succès");
        }
        catch (IOException e)
        {
            log.error("Erreur lors de la décompression du fichier");
        }

    }

}