package com.example.tpbatch.ban;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Entity.Ban;

import com.example.tpbatch.classifier.BanClassifier;
import com.example.tpbatch.listener.BanItemProcessListener;
import com.example.tpbatch.listener.JobProgressListener;

import com.example.tpbatch.partitioner.CsvStepPartitionner;
import com.example.tpbatch.processor.BanProcessor;
import com.example.tpbatch.processor.DuplicateProcessor;
import com.example.tpbatch.reader.BanItemReader;
import com.example.tpbatch.tasklet.*;
import com.example.tpbatch.writer.BanItemWriterConfiguration;
import com.example.tpbatch.writer.BanRoutingWriter;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.support.ClassifierCompositeItemWriter;
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

import javax.sql.DataSource;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class BanToDatabaseJobConfiguration {


    @Value("${chunkSize}")
    private Integer chunkSize;

    @Value("${numberOfThread}")
    private Integer numberOfThread;

    @Bean
    public JobOperatorFactoryBean jobOperator(JobRepository jobRepository) {
        JobOperatorFactoryBean jobOperatorFactoryBean = new JobOperatorFactoryBean();
        jobOperatorFactoryBean.setJobRepository(jobRepository);
        return jobOperatorFactoryBean;
    }

    @Bean
    public Job job(JobRepository repo,
                   @Qualifier("initStep") Step initTableStep,
                   Step loadCsvStepPartitioner,
                   @Qualifier("sortStep") Step sortStep,
                   @Qualifier("addedStep") Step addedStep,
                   @Qualifier("deletedStep") Step deletedStep,
                   @Qualifier("updateStep") Step updateStep,
                   @Qualifier("downloadStep") Step downloadCsvStep,
                   @Qualifier("populateStep") @Autowired(required = false) Step populateStep,
                   @Qualifier("addConstraintsStep") @Autowired(required = false) Step addConstraintsStep,
                   JobProgressListener listener)
    {
        JobBuilder builder = new JobBuilder("Job", repo);

                var flow = builder
                .start(downloadCsvStep)
                .next(sortStep)
                .next(initTableStep)
                .next(loadCsvStepPartitioner)
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

        return flow.listener(listener).build();

    }

    @Bean
    public Step loadCsvStepPartitioner(JobRepository jobRepository, Step insertStep, CsvStepPartitionner partitioner) {
        return new  StepBuilder("partitionStep", jobRepository)
                .partitioner("slaveStep", partitioner)
                .partitionHandler(partitionHandler(insertStep))
                .build();
    }

    @Bean
    public PartitionHandler partitionHandler(Step workerStep) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor()); // Parallel execution
        handler.setStep(workerStep); // Worker step
        handler.setGridSize(numberOfThread); // Number of partitions
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

    @Bean
    public Step insertStep(JobRepository repo, BanItemReader banReader,
                               CompositeItemProcessor<Ban, BanDto>  compositeProcessor,
                               BanRoutingWriter writer,
                               PlatformTransactionManager transactionManager,
                               BanItemProcessListener itemCountListener,
                               DuplicateProcessor addressMapListener)
    {
        return new StepBuilder("Insert step", repo)
                .<Ban,BanDto>chunk(chunkSize)
                .reader(banReader.csvReader(""))
                .processor(compositeProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .listener(addressMapListener)
                .listener(itemCountListener)
                .build();
    }


    @Qualifier("downloadStep")
    @Bean
    public Step downloadCsvStep(DownloadTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("downloadStep", repo)
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


    @Qualifier("updateStep")
    @Bean
    public Step updateStep(IdentifyUpdateTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Update Step", repo)
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

    @Qualifier("addedStep")
    @Bean
    public Step addedStep(AddedTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Added Step", repo)
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
        return new StepBuilder("addConstraintsStep Step", repo)
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

    @Bean
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

    @Bean
    public ClassifierCompositeItemWriter<BanDto> classifierBanCompositeItemWriter(DataSource ds, BanItemWriterConfiguration writers) throws Exception {
        ClassifierCompositeItemWriter<BanDto> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(new BanClassifier(writers.banWriter(ds), writers.duplicateWriter(ds)));
        return compositeItemWriter;
    }



}
