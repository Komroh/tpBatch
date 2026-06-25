package com.example.tpbatch.ban;

import com.example.tpbatch.Entity.Ban;

import com.example.tpbatch.listener.BanItemReadListener;
import com.example.tpbatch.listener.JobProgressListener;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.CompositeItemProcessor;
import org.springframework.batch.infrastructure.item.validator.BeanValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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
                     CompositeItemProcessor<Ban, Ban>  compositeProcessor,
                     JpaItemWriter<Ban> writer, PlatformTransactionManager transactionManager,
                     BanItemReadListener listener)
    {
        return new StepBuilder("step", repo)
                .<Ban,Ban>chunk(1000)
                .reader(csvReader)
                .processor(compositeProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .listener(listener)
                .build();
    }

    @Bean
    public BeanValidatingItemProcessor<Ban> validatingProcessor() {
        BeanValidatingItemProcessor<Ban> processor = new
                BeanValidatingItemProcessor<>();
       processor.setFilter(true);
        return processor;
    }

    @Bean
    public CompositeItemProcessor<Ban, Ban> compositeProcessor(BanProcessor processor, BeanValidatingItemProcessor<Ban> validator) {
        CompositeItemProcessor<Ban, Ban> composite =
                new CompositeItemProcessor<>();
        composite.setDelegates(List.of(
                validator,
                 processor
        ));
        return composite;
    }


    @Bean
    public JpaItemWriter<Ban> jdbcWriter(EntityManagerFactory emf){
        return new JpaItemWriter<>(emf);
    }
	
}
