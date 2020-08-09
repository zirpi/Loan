package org.example.camundatest;

import javax.sql.DataSource;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.core.io.Resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Configuration
public class LoanApplicationContext {

    @Bean
    public Starter starter() {
        return new Starter();
    }

    @Bean
    public CalculateClientsGoodnessService calculateClientsGoodnessService() {
        return new CalculateClientsGoodnessService();
    }

    @Bean
    public SpringProcessEngineConfiguration engineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager,
            @Value("classpath*:*.bpmn") Resource[] deploymentResources) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

        configuration.setProcessEngineName("myengine");
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setJobExecutorActivate(false);
        configuration.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
        configuration.setHistory(ProcessEngineConfiguration.HISTORY_FULL);
        configuration.setDeploymentResources(deploymentResources);

        return configuration;
    }


    @Bean
    public DataSource dataSource() {
        //DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //dataSource.setDriverClassName("org.h2.Driver");
        //dataSource.setUrl("jdbc:h2:mem:process-engine;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        //dataSource.setUsername("sa");
        //dataSource.setPassword("");
        //return dataSource;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/loan-camunda?autoReconnect=true");
        dataSource.setUsername("camunda");
        dataSource.setPassword("camunda");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean
    @Primary
    public ProcessEngineFactoryBean engineFactory(SpringProcessEngineConfiguration engineConfiguration) {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(engineConfiguration);
        return factoryBean;
    }

    @Bean
    public ProcessEngine processEngine(ProcessEngineFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }
}
