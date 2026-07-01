package com.example.tpbatch.ban;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Entity.Ban;

import com.example.tpbatch.classifier.BanClassifier;
import com.example.tpbatch.listener.JobProgressListener;

import com.example.tpbatch.processor.BanProcessor;
import com.example.tpbatch.processor.DuplicateProcessor;
import com.example.tpbatch.tasklet.AddedTasklet;
import com.example.tpbatch.tasklet.DeletedTasklet;
import com.example.tpbatch.tasklet.IdentifyUpdateTasklet;
import com.example.tpbatch.tasklet.InsertTasklet;
import com.example.tpbatch.writer.BanItemWriterConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class BanToDatabaseJobConfiguration {


    @Value("${chunkSize}")
    private Integer chunkSize;
    @Bean
    public JobOperatorFactoryBean jobOperator(JobRepository jobRepository) {
        JobOperatorFactoryBean jobOperatorFactoryBean = new JobOperatorFactoryBean();
        jobOperatorFactoryBean.setJobRepository(jobRepository);
        return jobOperatorFactoryBean;
    }

    @Bean
    public Job job(JobRepository repo, Step initInsertStep,
                   @Qualifier("addedStep") Step addedStep,
                   @Qualifier("deletedStep") Step deletedStep,
                   @Qualifier("updateStep") Step updateStep,
                   @Qualifier("insertStep") Step insertStep,
                   JobProgressListener listener)
    {
        return new JobBuilder("Job", repo)
                .start(initInsertStep)
                .next(addedStep)
                .next(deletedStep)
                .next(updateStep)
                .next(insertStep)
                .listener(listener)
                .build();
    }


    @Bean
    public Step initInsertStep(JobRepository repo, FlatFileItemReader<Ban> csvReader,
                               CompositeItemProcessor<Ban, BanDto>  compositeProcessor,
                               ClassifierCompositeItemWriter<BanDto> classifierBanCompositeItemWriter,
                               PlatformTransactionManager transactionManager,
                               DuplicateProcessor duplicationProcessor)
    {
        return new StepBuilder("step1", repo)
                .<Ban,BanDto>chunk(chunkSize)
                .reader(csvReader)
                .processor(compositeProcessor)
                .writer(classifierBanCompositeItemWriter)
                .transactionManager(transactionManager)
                .listener(duplicationProcessor)
                .build();
    }


    @Qualifier("insertStep")
    @Bean
    public Step insertStep(InsertTasklet tasklet, JobRepository repo, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Insert Step", repo)
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
        compositeItemWriter.setClassifier(new BanClassifier(writers.banJdbcItemWriter(ds), writers.duplicateJdbcItemWriter(ds)));
        return compositeItemWriter;
    }



}
