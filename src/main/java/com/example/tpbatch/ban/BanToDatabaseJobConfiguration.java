package com.example.tpbatch.ban;

import com.example.tpbatch.Dto.BanDto;
import com.example.tpbatch.Entity.Ban;

import com.example.tpbatch.listener.BanItemProcessListener;
import com.example.tpbatch.listener.JobProgressListener;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class BanToDatabaseJobConfiguration {

    @Bean
    public JobOperatorFactoryBean jobOperator(JobRepository jobRepository) {
        JobOperatorFactoryBean jobOperatorFactoryBean = new JobOperatorFactoryBean();
        jobOperatorFactoryBean.setJobRepository(jobRepository);
        return jobOperatorFactoryBean;
    }

    @Bean
    public Job job(JobRepository repo, Step step, JobProgressListener listener)
    {
        return new JobBuilder("Job", repo)
                .start(step)
                .listener(listener)
                .build();
    }

    @Bean
    public Step step(JobRepository repo, FlatFileItemReader<Ban> csvReader,
                     CompositeItemProcessor<Ban, BanDto>  compositeProcessor,
                     ClassifierCompositeItemWriter<BanDto> classifierBanCompositeItemWriter, PlatformTransactionManager transactionManager,
                     BanItemProcessListener listener, DuplicateProcessor duplicateProcessor)
    {
        return new StepBuilder("step", repo)
                .<Ban,BanDto>chunk(10000)
                .reader(csvReader)
                .processor(compositeProcessor)
                .writer(classifierBanCompositeItemWriter)
                .transactionManager(transactionManager)
                .listener(listener)
                .listener(duplicateProcessor)
                .faultTolerant()
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
