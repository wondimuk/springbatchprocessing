package com.wk.batchprocess.confguration;

import com.wk.batchprocess.model.Employee;
import com.wk.batchprocess.processor.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    /*reader() creates an ItemReader.
     It looks for a file called sample-data.csv and parses each line item with enough information to turn it into a Person.*/
    @Bean
    public FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName", "address"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                    setTargetType(Employee.class);
                }})
                .build();
    }

    /*processor() creates an instance of the PersonItemProcessor that you defined earlier,
     meant to convert the data to upper case.*/
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /*writer(DataSource) creates an ItemWriter.
    This one is aimed at a JDBC destination and automatically gets a copy of the dataSource created by @EnableBatchProcessing.
    It includes the SQL statement needed to insert a single Person, driven by Java bean properties.*/
    @Bean
    public JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO employee (first_name, last_name, address) VALUES (:firstName, :lastName, :address)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Employee> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Employee, Employee> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
