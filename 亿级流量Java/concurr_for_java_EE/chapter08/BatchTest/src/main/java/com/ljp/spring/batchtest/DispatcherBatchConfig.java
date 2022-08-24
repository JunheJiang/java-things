//package com.ljp.spring.batchtest;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.launch.support.SimpleJobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.ljp.spring.batchtest.bean.AppSettingBean;
//import com.ljp.spring.batchtest.bean.GlobalStepValueMap;
//import com.ljp.spring.batchtest.bean.MessageBussinessBean;
//import com.ljp.spring.batchtest.bean.MessageConfigBean;
//import com.ljp.spring.batchtest.service.ApplicationService;
//import com.ljp.spring.batchtest.service.MessageService;
//import com.ljp.spring.batchtest.service.RedisService;
//import com.ljp.spring.batchtest.util.JsonUtil;
//
//@Configuration
//@EnableBatchProcessing
//public class DispatcherBatchConfig {
//	@Autowired
//	private GlobalStepValueMap globalStepValueMap;
//	
//	@Autowired
//	private MessageService messageService;
//	
//	@Autowired
//	private RedisService redisService;
//
//	
//	@Autowired
//	private ApplicationService applicationService;
//	
//	@Bean
//	public JobRepository jobRepository(DataSource dataSource,PlatformTransactionManager transactionManager) throws Exception{
//		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
//		jobRepositoryFactoryBean.setDataSource(dataSource);
//		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
//		jobRepositoryFactoryBean.setDatabaseType("MYSQL");
//		return jobRepositoryFactoryBean.getObject();
//	}
//	
//	
//	@Bean
//	public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
//		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//		jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
//		return jobLauncher;
//	}
//	
//	
//	
//	//读数据
//	@Bean
//	@StepScope
//	public ListItemReader<MessageConfigBean> firstStepReader(@Value("#{jobParameters['request']}") String request) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
//	
//		System.out.println("------1st step Reader--------");
//		
//		//get the serialized request model and do deserialize
//    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//    	
//    	System.out.println("request model solution id :" + requestModel.getSolution());
//		
//		//Get the Message Model(config type)
//		MessageConfigBean msgConfigBean  = messageService.getMsgConfigBean();
//		
//		//Get the application settings
//		List<AppSettingBean> listAppSettings = applicationService.getAppSetting();
//    	
//		//deal with both message model(config type) and application settings
//		MessageConfigBean newMsgConfigBean = messageService.dealWithFromMsgConfigBeanToNewMsgConfigBean(msgConfigBean, listAppSettings);
//		
//		List<MessageConfigBean> listMsgCfgBean = new ArrayList<MessageConfigBean>();
//		listMsgCfgBean.add(newMsgConfigBean);
//    	ListItemReader reader = new ListItemReader(listMsgCfgBean);
//        return reader;
//	}
//	
//	
//	@Bean
//	@StepScope
//	public ListItemReader<String> secondStepReader(@Value("#{jobParameters['request']}") String request) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
//		System.out.println("------2nd step Reader--------");
//		
//		//get the serialized request model and do deserialize
//    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//    	
//    	
//    	//Get from remote redis server
//    	List<String> listAudiences = redisService.getListAudiencesFromRemoveServer("audiences" + requestModel.getJob());
//    	
//    	ListItemReader reader = new ListItemReader(listAudiences);
//        return reader;
//	}
//	
//	
//
//	    
//	    
//    //处理数据    
//    @Bean
//    @StepScope
//    public MsgCfgToMsgModelItemProcessor firstStepProcessor(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
//    	System.out.println("------1st step Processor--------");
//    	//get the serialized request model and do deserialize
//    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//    	
//    	//construct audiences
//    	List<String> listNewAudiences = redisService.getAllAudiences(requestModel.getTags(), requestModel.getAliases());
//    	
//    	//save to remote
//    	redisService.saveToRemoveServer("audiences" + requestModel.getJob(), listNewAudiences);	
//    	
//    	MsgCfgToMsgModelItemProcessor m2mProcessor = new MsgCfgToMsgModelItemProcessor();
//    	m2mProcessor.setJobId(requestModel.getJob());
//    	
//    	return m2mProcessor;
//    }
//    
//    @Bean
//    @StepScope
//    public ItemProcessor secondStepProcessor(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
//    	System.out.println("------2nd step Processor--------");
//    	
//    	return new AliasesToFullMsgModelItemProcessor();
//    }
//    
//    
//    
//    
//    
//    //写数据
//    @Bean
//    @StepScope
//    public ItemWriter<MessageBussinessBean> firstStepItemWriter(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
//    	System.out.println("------1st step writer--------");
//    	//get the serialized request model and do deserialize
//    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//    	
//    	MessageFullSetItemWriter writer = new MessageFullSetItemWriter();
//    	writer.setRequestJobId(requestModel.getJob());
//        return writer;   
//    }
//    
//    @Bean
//    @StepScope
//    public ItemWriter<String> secondStepItemWriter(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
//    	System.out.println("------2nd step writer--------");
//    	
//    	//get the serialized request model and do deserialize
//    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//    	
//    	//Get message model from previous step
//    	MessageBussinessBean msgModel = (MessageBussinessBean) globalStepValueMap.getMap().get("msgModel" + requestModel.getJob());
//		
//    	System.out.println("It has messageModel:" + msgModel);
//    	
//    	MQChannelModelItemWriter writer = new MQChannelModelItemWriter();
//        return writer;   
//    }
//
//	    
//	    
//	    
//	//---------------job & step----------------
//    
//    
//    @Bean
//    public Job messageCoreBatch(JobBuilderFactory jobs, @Qualifier("step1")Step firstStep, @Qualifier("step2")Step secondStep) {    	
//    	
//    	return jobs.get("messageCoreBatch")
//                .incrementer(new RunIdIncrementer())
//                .start(firstStep).next(secondStep)
//                .build();
//
//    }
//    
//	    
//
//    @Bean
//    public Step step1(StepBuilderFactory stepBuilderFactory, @Qualifier("firstStepReader")ListItemReader<MessageConfigBean> reader,
//    		@Qualifier("firstStepItemWriter")ItemWriter<MessageBussinessBean> writer,  @Qualifier("firstStepProcessor")ItemProcessor<MessageConfigBean, MessageBussinessBean> processor) {
//
//        return stepBuilderFactory.get("step1")
//                .<MessageConfigBean, MessageBussinessBean> chunk(1)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//	    
//	    
//    @Bean
//    public Step step2(StepBuilderFactory stepBuilderFactory, @Qualifier("secondStepReader")ListItemReader<String> reader,
//    		@Qualifier("secondStepItemWriter")ItemWriter<String> writer, @Qualifier("secondStepProcessor")ItemProcessor<String, String> processor) {
//        return stepBuilderFactory.get("step2")
//                .<String, String> chunk(3)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//
//    // end::jobstep[]
//
//    @Bean
//    public static JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
