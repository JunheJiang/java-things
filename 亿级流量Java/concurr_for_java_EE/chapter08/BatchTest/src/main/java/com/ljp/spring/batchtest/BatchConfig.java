package com.ljp.spring.batchtest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.FlowStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ljp.spring.batchtest.bean.AppSettingBean;
import com.ljp.spring.batchtest.bean.GlobalStepValueMap;
import com.ljp.spring.batchtest.bean.MessageBussinessBean;
import com.ljp.spring.batchtest.bean.MessageConfigBean;
import com.ljp.spring.batchtest.service.ApplicationService;
import com.ljp.spring.batchtest.service.MessageService;
import com.ljp.spring.batchtest.service.RedisService;
import com.ljp.spring.batchtest.util.JsonUtil;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private GlobalStepValueMap globalStepValueMap;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private RedisService redisService;

	
	@Autowired
	private ApplicationService applicationService;
	
	@Bean
	public JobRepository jobRepository(DataSource dataSource,PlatformTransactionManager transactionManager) throws Exception{
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		jobRepositoryFactoryBean.setDatabaseType("MYSQL");
		return jobRepositoryFactoryBean.getObject();
	}
	
	
	@Bean
	public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
		System.out.println(">>>>>>>>>>" + transactionManager.getClass());
		return jobLauncher;
	}
	
	
	
	//读数据
	@Bean
	@StepScope
	public ListItemReader<MessageConfigBean> firstStepReader(@Value("#{jobParameters['request']}") String request) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
	
		System.out.println("------1st step Reader--------");
		
		//get the serialized request model and do deserialize
    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
    	
    	System.out.println("request model solution id :" + requestModel.getSolution());
		
		//Get the Message Model(config type)
		MessageConfigBean msgConfigBean  = messageService.getMsgConfigBean();
		
		//Get the application settings
		List<AppSettingBean> listAppSettings = applicationService.getAppSetting();
    	
		//deal with both message model(config type) and application settings
		MessageConfigBean newMsgConfigBean = messageService.dealWithFromMsgConfigBeanToNewMsgConfigBean(msgConfigBean, listAppSettings);
		
		try{
			throw new IOException("new mark!");
		}catch(IOException e){
			
		}
		
		
		List<MessageConfigBean> listMsgCfgBean = new ArrayList<MessageConfigBean>();
		listMsgCfgBean.add(newMsgConfigBean);
    	ListItemReader reader = new ListItemReader(listMsgCfgBean);
        return reader;
	}
	
	
	@Bean
	@StepScope
	public ListItemReader<String> secondStepReader(@Value("#{jobParameters['request']}") String request) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		System.out.println("------2nd step Reader--------");
		
		//get the serialized request model and do deserialize
    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
    	
    	
    	//Get from remote redis server
    	List<String> listAudiences = redisService.getListAudiencesFromRemoveServer("audiences" + requestModel.getJob());
    	
    	ListItemReader reader = new ListItemReader(listAudiences);
        return reader;
	}
	
	
	@Bean
	@StepScope
	public ListItemReader<String> stepForTranscationReader() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		System.out.println("------tx step Reader--------");
		
		List<String> indexVals = new ArrayList<String>();
		
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		
		
		
		
		
		
		
		
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		indexVals.add("004");
		indexVals.add("005");
		indexVals.add("006");
		indexVals.add("001");
		indexVals.add("002");
		indexVals.add("003");
		
		
		
		
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		indexVals.add("007");
		indexVals.add("008008008008");
		indexVals.add("009");
		
		indexVals.add("010");
		indexVals.add("011");
		indexVals.add("012012012012");
		
    	ListItemReader reader = new ListItemReader(indexVals);
        return reader;
	}

	    
	    
    //处理数据    
    @Bean
    @StepScope
    public MsgCfgToMsgModelItemProcessor firstStepProcessor(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------1st step Processor--------");
    	//get the serialized request model and do deserialize
    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
    	
    	//construct audiences
    	List<String> listNewAudiences = redisService.getAllAudiences(requestModel.getTags(), requestModel.getAliases());
    	
    	//save to remote
    	redisService.saveToRemoveServer("audiences" + requestModel.getJob(), listNewAudiences);	
    	
    	MsgCfgToMsgModelItemProcessor m2mProcessor = new MsgCfgToMsgModelItemProcessor();
    	m2mProcessor.setJobId(requestModel.getJob());
    	
    	
    	
    	return m2mProcessor;
    }
    
    @Bean
    @StepScope
    public ItemProcessor secondStepProcessor(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------2nd step Processor--------");
    	
    	return new AliasesToFullMsgModelItemProcessor();
    }
    
    @Bean
    @StepScope
    public ItemProcessor stepForTranscationProcessor() throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------tx step Processor--------");
    	return new StringToStringDoNotingProcessor();
    }
    
    
    
    //写数据
    @Bean
    @StepScope
    public ItemWriter<MessageBussinessBean> firstStepItemWriter(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------1st step writer--------");
    	//get the serialized request model and do deserialize
    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
    	
    	MessageFullSetItemWriter writer = new MessageFullSetItemWriter();
    	writer.setRequestJobId(requestModel.getJob());
        return writer;   
    }
    
    @Bean
    @StepScope
    public ItemWriter<String> secondStepItemWriter(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------2nd step writer--------");
    	
    	//get the serialized request model and do deserialize
    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
    	
    	//Get message model from previous step
    	MessageBussinessBean msgModel = (MessageBussinessBean) globalStepValueMap.getMap().get("msgModel" + requestModel.getJob());
		
    	System.out.println("It has messageModel:" + msgModel);
    	
    	MQChannelModelItemWriter writer = new MQChannelModelItemWriter();
        return writer;   
    }

    @Bean
    @StepScope
    public ItemWriter<String> stepForTranscationWriter(JdbcTemplate jdbcTemplate) throws JsonParseException, JsonMappingException, IOException {
    	System.out.println("------tx step writer--------");
    	
    	TestTableWriter writer = new TestTableWriter();
    	writer.setJdbcTemplate(jdbcTemplate);
        return writer;   
    }
	    
	    
	    
	//---------------job & step----------------
    
    
    @Bean
    public Job messageCoreBatch(JobBuilderFactory jobs, @Qualifier("step1")Step firstStep, @Qualifier("step2")Step secondStep, @Qualifier("stepForTranscation")Step stepForTranscation, JobExecutionListener listener) {    	
//    	return jobs.get("messageCoreBatch")
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
//                .flow(firstStep).next(secondStep)
//                .end()
//                .build();
    	
    	return jobs.get("messageCoreBatch")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(firstStep).next(secondStep)
                .build();
    	
    }
    
    
    @Bean
    public Job testBatchTranscation(JobBuilderFactory jobs, @Qualifier("step1")Step firstStep, @Qualifier("step2")Step secondStep, @Qualifier("stepForTranscation")Step stepForTranscation, JobExecutionListener listener) {  
    	
    	return jobs.get("testBatchTranscation")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepForTranscation)
                .build();
    }

	    

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, @Qualifier("firstStepReader")ListItemReader<MessageConfigBean> reader,
    		@Qualifier("firstStepItemWriter")ItemWriter<MessageBussinessBean> writer,  @Qualifier("firstStepProcessor")ItemProcessor<MessageConfigBean, MessageBussinessBean> processor, StepListener stepListener) {

        return stepBuilderFactory.get("step1")
                .<MessageConfigBean, MessageBussinessBean> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer).listener(stepListener)
                .build();
    }
	    
	    
    @Bean
    public Step step2(StepBuilderFactory stepBuilderFactory, @Qualifier("secondStepReader")ListItemReader<String> reader,
    		@Qualifier("secondStepItemWriter")ItemWriter<String> writer, @Qualifier("secondStepProcessor")ItemProcessor<String, String> processor) {
        return stepBuilderFactory.get("step2")
                .<String, String> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    
    
    @Bean
    public Step stepForTranscation(StepBuilderFactory stepBuilderFactory, @Qualifier("stepForTranscationReader")ListItemReader<String> reader,
    		 @Qualifier("stepForTranscationProcessor")ItemProcessor<String, String> processor, @Qualifier("stepForTranscationWriter")ItemWriter<String> writer) {
        return stepBuilderFactory.get("stepForTranscation")
                .<String, String> chunk(100)
                .reader(reader)
                .processor(processor)
//                .writer(writer).faultTolerant().retryLimit(3).retry(DataIntegrityViolationException.class).throttleLimit(16)
                .writer(writer)//.taskExecutor(new SimpleAsyncTaskExecutor()).throttleLimit(10)
                .build();
    }
    
    
	    
    
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
    
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityMngFactory) {
        return new JpaTransactionManager(entityMngFactory);
    }


    
    
	    
    // end::jobstep[]

    @Bean
    public static JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

	
}