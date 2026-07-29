package com.example.tpbatch.ban;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Dto.DvfDto;
import com.example.tpbatch.Entity.Ban;

import com.example.tpbatch.Entity.Dvf;
import com.example.tpbatch.listener.BanItemProcessListener;
import com.example.tpbatch.listener.DownloadJobListener;
import com.example.tpbatch.listener.DvfItemProcessListener;
import com.example.tpbatch.listener.JobProgressListener;

import com.example.tpbatch.partitioner.CsvStepPartitionner;
import com.example.tpbatch.processor.BanProcessor;
import com.example.tpbatch.processor.DuplicateDvfProcessor;
import com.example.tpbatch.processor.DuplicateProcessor;

import com.example.tpbatch.tasklet.*;
import com.example.tpbatch.writer.BanRoutingWriter;
import com.example.tpbatch.writer.DvfRoutingWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;


@Configuration
public class BanToDatabaseJobConfiguration {

    private  final Logger log = LoggerFactory.getLogger(BanToDatabaseJobConfiguration.class);

    @Value("${chunkSize}")
    private Integer chunkSize;

    @Value("${numberOfThread}")
    private Integer numberOfThread;

    @Bean("myJobOperator")
    public JobOperatorFactoryBean jobOperator(JobRepository jobRepository) {
        JobOperatorFactoryBean jobOperatorFactoryBean = new JobOperatorFactoryBean();
        jobOperatorFactoryBean.setJobRepository(jobRepository);
        return jobOperatorFactoryBean;
    }

    @Bean("ProcJob")
    public Job job(JobRepository repo,
                   @Qualifier("sortStep") Step sortStep,
                   @Qualifier("initStep") Step initTableStep,
                   @Qualifier("loadBanStepPartitioner") Step loadBanStepPartitioner,
                   @Qualifier("addedStep") Step addedStep,
                   @Qualifier("deletedStep") Step deletedStep,
                   @Qualifier("updateStep") Step updateStep,
                   @Qualifier("populateStep") @Autowired(required = false) Step populateStep,
                   @Qualifier("addConstraintsStep") @Autowired(required = false) Step addConstraintsStep,
                   @Qualifier("archiveStep") Step archiveStep,
                   @Qualifier("reportStep") Step reportStep,
                   JobProgressListener listener)
    {
        JobBuilder builder = new JobBuilder("Batch proc job", repo);

                var flow = builder
                .listener(listener)
                .start(sortStep)
                .next(initTableStep)
                .next(loadBanStepPartitioner)
                .next(addedStep)
                .next(deletedStep)
                .next(updateStep);

        if (populateStep != null) {
            flow.next(populateStep);
        }
        if(addConstraintsStep != null)
        {
            flow.next(addConstraintsStep);
        }

        return flow.next(archiveStep)
                .next(reportStep)
                .build();
    }

    @Bean("DvfJob")
    public Job DvfJob(JobRepository repo,
                   @Qualifier("sortStep") Step sortStep,
                   @Qualifier("initStep") Step initTableStep,
                   @Qualifier("loadDvfStepPartitioner") Step loadDvfStepPartitioner,
                   @Qualifier("addedStep") Step addedStep,
                   @Qualifier("deletedStep") Step deletedStep,
                   @Qualifier("updateStep") Step updateStep,
                   @Qualifier("populateStep") @Autowired(required = false) Step populateStep,
                   @Qualifier("addConstraintsStep") @Autowired(required = false) Step addConstraintsStep,
                   @Qualifier("archiveStep") Step archiveStep,
                   @Qualifier("reportStep") Step reportStep,
                   JobProgressListener listener)
    {
        JobBuilder builder = new JobBuilder("Dvf job", repo);

        var flow = builder
                .listener(listener)
                .start(sortStep)
                .next(initTableStep)
                .next(loadDvfStepPartitioner)
                .next(addedStep)
                .next(deletedStep)
                .next(updateStep);

        if (populateStep != null) {
            flow.next(populateStep);
        }
        if(addConstraintsStep != null)
        {
            flow.next(addConstraintsStep);
        }

        return flow.next(archiveStep)
                .next(reportStep)
                .build();
    }
    @Bean("DownloadJob")
    public Job downloadJob(JobRepository jobRepository,
                           @Qualifier("downloadStep") Step downloadCsvStep,
                           @Qualifier("errorReportStep") Step errorReportStep,
                           @Qualifier("reportStep") Step reportStep,
                           @Qualifier("downloadJobListener") DownloadJobListener downloadJobListener,
                           @Qualifier("JobProgressListener")  JobProgressListener jobProgressListener)
    {
        return new JobBuilder("Download Job", jobRepository)
                .listener(downloadJobListener)
                .listener(jobProgressListener)
                .start(downloadCsvStep)
                    .on("MULTIPLE_FILES_FOUND")
                    .to(errorReportStep)
                        .on("*").fail()

                .from(downloadCsvStep)
                .on("WRONG_FILE_FORMAT")
                    .to(errorReportStep)
                        .on("*").fail()

                .from(downloadCsvStep)
                    .on("NO_INPUT_FILE")
                    .to(reportStep).on("*").end()
                .from(downloadCsvStep)
                    .on("COMPLETED").end().build().build();
    }

    @Bean("loadBanStepPartitioner")
    public Step loadBanStepPartitioner(JobRepository jobRepository,@Qualifier("BanInsertStep") Step insertBanStep, CsvStepPartitionner partitioner) {
        return new  StepBuilder("partitionStep", jobRepository)
                .partitioner("slaveStep", partitioner)
                .partitionHandler(banPartitionHandler(insertBanStep))
                .build();
    }
    @Bean("loadDvfStepPartitioner")
    public Step loadDvfStepPartitioner(JobRepository jobRepository,@Qualifier("DvfInsertStep") Step insertDvfStep, CsvStepPartitionner partitioner) {
        return new  StepBuilder("dvfPartitionStep", jobRepository)
                .partitioner("dvfSlaveStep", partitioner)
                .partitionHandler(dvfPartitionHandler(insertDvfStep))
                .build();
    }

    @Bean("banPartitionHandler")
    public PartitionHandler banPartitionHandler(
            @Qualifier("BanInsertStep") Step workerStep) {

        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(workerStep);
        handler.setGridSize(numberOfThread);
        return handler;
    }

    @Bean("dvfPartitionHandler")
    public PartitionHandler dvfPartitionHandler(
            @Qualifier("DvfInsertStep") Step workerStep) {

        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(workerStep);
        handler.setGridSize(numberOfThread);
        return handler;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(numberOfThread);
        executor.setMaxPoolSize(numberOfThread);
        executor.setQueueCapacity(0);
        executor.initialize();
        return executor;
    }

    @Bean("BanInsertStep")
    public Step insertBanStep(JobRepository repo, @Qualifier("BanReader") FlatFileItemReader<Ban> reader,
                               @Qualifier("BanCompositeProcessor") CompositeItemProcessor<Ban, BanDto>  compositeProcessor,
                               @Qualifier("BanRoutingWriter") BanRoutingWriter writer,
                               PlatformTransactionManager transactionManager,
                               BanItemProcessListener itemCountListener,
                               DuplicateProcessor addressMapListener)
    {
        return new StepBuilder("Insert step", repo)
                .<Ban,BanDto>chunk(chunkSize)
                .reader(reader)
                .processor(compositeProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .listener(addressMapListener)
                .listener(itemCountListener)
                .build();
    }
    @Bean("DvfInsertStep")
    public Step insertDvfStep(JobRepository repo, @Qualifier("DvfReader") FlatFileItemReader<Dvf> reader,
                           @Qualifier("DvfCompositeProcessor") CompositeItemProcessor<Dvf, DvfDto>  compositeProcessor,
                           @Qualifier("DvfRoutingWriter") DvfRoutingWriter writer,
                           PlatformTransactionManager transactionManager,
                           DuplicateDvfProcessor dvfMapListener,
                           DvfItemProcessListener listener)
    {
        return new StepBuilder("Insert Dvf step", repo)
                .<Dvf,DvfDto>chunk(chunkSize)
                .reader(reader)
                .processor(compositeProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .listener(dvfMapListener)
                .listener(listener)
                .build();
    }


    @Qualifier("downloadStep")
    @Bean
    public Step downloadCsvStep(RetrieveFileTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Download Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("sortStep")
    @Bean

    public Step sortStep(SortTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Sort Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("initStep")
    @Bean
    public Step initTableStep(InitTableTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Init Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("addedStep")
    @Bean
    public Step addedStep(AddedTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Added Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }
    @Qualifier("deletedStep")
    @Bean
    public Step deleteStep(DeletedTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Delete Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("updateStep")
    @Bean
    public Step updateStep(IdentifyUpdateTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Update Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }


    @Qualifier("populateStep")
    @Bean
    @Profile("sqlite")
    public Step populateStep(PopulateSearchTableTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("populate Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("addConstraintsStep")
    @Bean
    @Profile("postgresql")
    public Step addConstraintsStep(AddConstraintsTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("add Constraints Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }
    @Qualifier("archiveStep")
    @Bean
    public Step archiveStep(ArchiveTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("archive Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("reportStep")
    @Bean
    public Step reportStep(GenerateReportTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("report Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Qualifier("errorReportStep")
    @Bean
    public Step errorReportStep(GenerateReportTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("error report Step", repo)
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public BeanValidatingItemProcessor<BanDto> validatingProcessor() {
        BeanValidatingItemProcessor<BanDto> processor = new
                BeanValidatingItemProcessor<>();
        processor.setFilter(true);
        return processor;
    }

    @Bean("BanCompositeProcessor")
    public CompositeItemProcessor<Ban, BanDto> compositeProcessor(BanProcessor processor,
                                                                  BeanValidatingItemProcessor<BanDto> validator,
                                                                  DuplicateProcessor duplicationProcessor) {
        CompositeItemProcessor<Ban, BanDto> composite =
                new CompositeItemProcessor<>();
        composite.setDelegates(List.of(

                duplicationProcessor,
                validator,
                processor

        ));
        return composite;
    }

    @Bean("DvfCompositeProcessor")
    public CompositeItemProcessor<Dvf, DvfDto> compositeDvfProcessor(
                                                                  DuplicateDvfProcessor duplicationProcessor) {
        CompositeItemProcessor<Dvf, DvfDto> composite =
                new CompositeItemProcessor<>();
        composite.setDelegates(List.of(
                duplicationProcessor
        ));
        return composite;
    }

    /*@Bean
    public ClassifierCompositeItemWriter<BanDto> classifierBanCompositeItemWriter(DataSource ds, BanItemWriterConfiguration writers) throws Exception {
        ClassifierCompositeItemWriter<BanDto> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(new BanClassifier(writers.banWriter(ds), writers.duplicateBanWriter(ds)));
        return compositeItemWriter;
    }*/



}
