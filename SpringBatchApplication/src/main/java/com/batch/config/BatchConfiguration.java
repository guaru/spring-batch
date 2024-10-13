package com.batch.config;

import com.batch.service.IPersonService;
import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterStep;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchConfiguration {

    private final PlatformTransactionManager transactionManager;
    private final ResourceLoader resourceLoader;
    private final JobRepository jobRepository;
    private final IPersonService personService;


    @Bean
    @JobScope
    public ItemDescompressStep itemDescompressStep() {
        return new ItemDescompressStep(resourceLoader);
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep(resourceLoader);
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return  new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep() {
        return new ItemWriterStep(personService);
    }

    @Bean
    public Step descompressFileStep() {
        return new StepBuilder("itemDescompressStep", jobRepository)
                .tasklet(itemDescompressStep(), transactionManager).build();
    }

    @Bean
    public Step readFileStep() {
      return new StepBuilder("itemReaderStep", jobRepository)
              .tasklet(itemReaderStep(), transactionManager).build();
    }

    @Bean
    public Step processDataStep() {
        return new StepBuilder("itemProcessorStep", jobRepository)
                .tasklet(itemProcessorStep(), transactionManager).build();
    }

    @Bean
    public Step writeDataStep() {
        return new StepBuilder("itemWriterStep", jobRepository)
                .tasklet(itemWriterStep(), transactionManager).build();
    }

    public Job readCSvJob() {
         return new JobBuilder("readCSVJob", jobRepository)
                 .start(descompressFileStep())
                 .next(readFileStep())
                 .next(processDataStep())
                 .next(writeDataStep())
                 .build();
    }

}