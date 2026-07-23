package com.example.tpbatch.service;

import com.example.tpbatch.Dto.BanSearchRequest;
import com.example.tpbatch.Entity.Ban;
import com.example.tpbatch.repository.BanRepository;
import com.example.tpbatch.specification.BanSpecification;
import com.example.tpbatch.utils.ComputeChecksum;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;

import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//import static com.example.tpbatch.specification.BanSpecification.orderByDistance;
//import static com.example.tpbatch.specification.BanSpecification.withinRange;
import static com.example.tpbatch.utils.Constants.FILE_PATH;

@Service
public class BanService {

    private final BanRepository repo;

    @Qualifier("DownloadJob")
    private final Job downloadJob;

    @Qualifier("ProcJob")
    private final Job procJob;

    @Value("${downloadFile}")
    private Boolean downloadFile;

    @Value("${file}")
    private String file;

    private final JobOperator jobOperator;

    public BanService(BanRepository repo,@Qualifier("DownloadJob") Job downloadJob, @Qualifier("ProcJob") Job procJob, JobOperator jobOperator) {
        this.repo = repo;
        this.downloadJob = downloadJob;
        this.procJob = procJob;
        this.jobOperator = jobOperator;
    }


    @Transactional(readOnly = true)
    public List<Ban> recherche(BanSearchRequest criteria)
    {
        return repo.findAll(BanSpecification.build(criteria));
    }

    @Transactional(readOnly = true)
    public Page<Ban> recherche(BanSearchRequest criteria, Pageable pageable)
    {
        return repo.findAll(BanSpecification.build(criteria), pageable);
    }

    public Page<Ban> rechercheChaine(String chaine, Pageable pageable) {

        Specification<Ban> spec = new BanSpecification().compareString(chaine);
        return repo.findAll(spec, pageable);
    }

    public Page<Ban> rechercheFullText(String chaine, Pageable pageable)
    {
        chaine = chaine.replaceAll("-", " ");
        return repo.search(chaine, pageable);
    }

    public ResponseEntity<?> lancer(String typeCriteria, String criteria) {



            if (typeCriteria == null || criteria == null) {
                typeCriteria = "";
                criteria = "";
            }
        if(downloadFile) {
            JobParameters downloadJobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExec;

            try {
                jobExec = jobOperator.start(downloadJob, downloadJobParameters);
                Path path = Path.of(FILE_PATH);
                if (jobExec.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
                    try {
                        JobParameters procJobParameters = new JobParametersBuilder()
                                .addString("typeCriteria", typeCriteria)
                                .addString("criteria", criteria)
                                .addString("checksum", jobExec.getExecutionContext().getString("checksum", ""))
                                .addString("downloadExitStatus", jobExec.getExitStatus().getExitCode())
                                .toJobParameters();
                        jobOperator.start(procJob, procJobParameters);
                    } catch (JobInstanceAlreadyCompleteException e) {
                        Files.delete(path);
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Job already completed");
                    } catch (JobExecutionAlreadyRunningException e) {
                        return ResponseEntity.status(HttpStatus.LOCKED).body("Job already running");
                    } catch (Exception e) {
                        Files.delete(path);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                    }
                } else {
                    Files.delete(path);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jobExec.getExitStatus());
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'execution du download job");
            }
            return ResponseEntity.status(HttpStatus.OK).body(jobExec.getExitStatus());
        }
        else {
            try {
                String checksum = ComputeChecksum.computeChecksum(file);
                JobParameters procJobParameters = new JobParametersBuilder()
                        .addString("typeCriteria", typeCriteria)
                        .addString("criteria", criteria)
                        .addString("checksum", checksum)
                        .addLong("timestamp", System.currentTimeMillis())
                        .toJobParameters();
                jobOperator.start(procJob, procJobParameters);
            } catch (JobInstanceAlreadyCompleteException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Job already completed");
            } catch (JobExecutionAlreadyRunningException e) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("Job already running");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).body("Proc job started");
        }
    }

    public Ban rechercheInverse(Double lat, Double lon) {
        /* Methode non spatiale :
        Specification<Ban> spec = BanSpecification.withinRange(lat, lon)
                .and(BanSpecification.orderByDistance(lat, lon));
        return repo.findAll(spec, PageRequest.of(0,1)).getContent().stream().findFirst().orElse(null);
         */
        return repo.findClosest(lat,lon, 100);
    }
}
