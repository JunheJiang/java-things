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
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.ljp.spring.batchtest.service.PersonService;
//import com.ljp.spring.batchtest.util.JsonUtil;
//
//import org.springframework.batch.item.support.SingleItemPeekableItemReader;
//
//@Configuration
//@EnableBatchProcessing
//public class TMPBatchConfig {
//	
//	@Autowired
//    private PersonService personService;
//	
//	@Autowired
//	private GlobalStepValueMap globalStepValueMap;
//	
//	@Autowired
//	private Car car;
//	
//	
//	private static final String PERSON_INSERT = "INSERT INTO Person (person_name, person_age,person_sex) VALUES (:personName, :personAge,:personSex)";
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
//	@Bean
//	public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception{
//		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//		jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
//		return jobLauncher;
//	}
//	
//	
//	
//	    // tag::readerwriterprocessor[] 1.读数据
//	    @Bean
//	    @StepScope
//	    public FlatFileItemReader<Person> reader(@Value("#{jobParameters['request']}") String request,@Value("#{jobParameters['myParam']}") String myP) {
//	    	System.out.println("-----reader------------" + myP);
//	        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
//	        //加载外部文件数据 文件类型:CSV
//	        reader.setResource(new ClassPathResource("sample-data.csv"));
//	        reader.setLineMapper(new DefaultLineMapper<Person>() {{
//	            setLineTokenizer(new DelimitedLineTokenizer() {{
//	                setNames(new String[] { "person_name","person_age","person_sex" });
//	            }});
//	            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//	                setTargetType(Person.class);
//	            }});
//	        }});
//	        return reader;
//	    }
//	    
//	    
//	    // tag::readerwriterprocessor[] 1.读数据
//	    @Bean
//	    @StepScope
//	    public ListItemReader<Person> newReader(@Value("#{jobParameters['request']}") String request,@Value("#{jobParameters['myParam']}") String myP) throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {;
//	    	System.out.println("-------reader----------" + myP);
////	    	NewItemReader<Person> reader = new NewItemReader<Person>();
////	    	reader.setPersonService(personService);
////	        reader.read();
//	    	
//	    	Person person = personService.pullOnePerson();
//	    	List<Person> list = new ArrayList<Person>();
//	    	list.add(person);
//	    	list.add(person);
//	    	list.add(person);
//	    	list.add(person);
//	    	list.add(person);
//	    	
//	    	ListItemReader reader = new ListItemReader(list);
//	    	
//	        return reader;
//	    }
//	    
//	    
//	    
//	    
//	    @Bean
//	    @StepScope
//	    public FlatFileItemReader<Person> reader02(@Value("#{jobParameters['request']}") String request, @Value("#{jobParameters['myParam']}") String myP) throws JsonParseException, JsonMappingException, IOException {
//	    	
//	    	System.out.println("-----------------" + myP);
//
//	    	
//	        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
//	        //加载外部文件数据 文件类型:CSV
//	        reader.setResource(new ClassPathResource("sample-data2.csv"));
//	        SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//	    	
//	        System.out.println("------step02-CCCCCC------" + globalStepValueMap.getMap().get("MModel" + requestModel.getJob()));
//
////	        System.out.println("------step02-CCCCCC------" + car.getColor());
//	        
//	        
//	        reader.setLineMapper(new DefaultLineMapper<Person>() {{
//	            setLineTokenizer(new DelimitedLineTokenizer() {{
//	                setNames(new String[] { "person_name","person_age","person_sex" });
//	            }});
//	            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//	                setTargetType(Person.class);
//	            }});
//	        }});
//	        return reader;
//	    }
//	    
//	    
//	    
//	    
//	    
//	    
//	    //2.处理数据
//	    @Bean
//	    @StepScope
//	    public PersonItemProcessor processor(@Value("#{jobParameters['request']}") String request) throws JsonParseException, JsonMappingException, IOException {
//	    	//get the serialized request model
//	    	SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
//	    	
//	    	System.out.println("------process------ In this case, we can  get the request model  cron exp value:" + requestModel.getCron());
//	        return new PersonItemProcessor();
//	    }
//	    
//	    
//	    @Bean
//	    @StepScope
//	    public PersonBookItemProcessor processor02() throws JsonParseException, JsonMappingException, IOException {
//	        return new PersonBookItemProcessor();
//	    }
//
//	    
//	    //3.写数据
//	    @Bean
//	    @StepScope
//	    public ItemWriter<Person> writer(@Value("#{jobParameters['request']}") String request, DataSource dataSource) throws JsonParseException, JsonMappingException, IOException {
//	        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
//	        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
//	        writer.setSql(PERSON_INSERT);
//	        writer.setDataSource(dataSource);
////	        String myMeaageModle = "{AAABBBCCC}";
////	        SimpleRequestModel requestModel = JsonUtil.getObjectFromJsonString(request, SimpleRequestModel.class);
////	        globalStepValueMap.getMap().put("MModel" + requestModel.getJob(),  myMeaageModle);	        
////	        
//	        System.out.println("-----write----");
//	        
////	        car.setColor("White");
////	        System.out.println("------step01-CCCCCC------" + car.getColor());
//	        return writer;
//	    }
//	    
//	    
//	    @Bean
//	    public ItemWriter<Person> writer02(DataSource dataSource) {
//	    	System.out.println("------writer--------");
//	    	NewItemWriter writer = new NewItemWriter();
//	        return writer;   
//	    }
//	    
//	    // end::readerwriterprocessor[]
//
//	    
//	    // tag::jobstep[]
//	    @Bean
//	    public Job importUserJob(JobBuilderFactory jobs, @Qualifier("step1")Step s1, @Qualifier("step2")Step s2, JobExecutionListener listener) {
//	    	return jobs.get("importUserJob")
//	                .incrementer(new RunIdIncrementer())
//	                .listener(listener)
//	                .flow(s1)
//	                .end()
//	                .build();
//	    }
//	    
//	    
//
//	    @Bean
//	    public Step step1(StepBuilderFactory stepBuilderFactory, @Qualifier("newReader")ListItemReader<Person> reader,
//	    		@Qualifier("writer02")ItemWriter<Person> writer02, ItemProcessor<Person, Person> processor) {
//	    	
//	    	
//	        return stepBuilderFactory.get("step1")
//	                .<Person, Person> chunk(3)
//	                .reader(reader)
//	                .processor(processor)
//	                .writer(writer02)
//	                .build();
//	    }
//	    
//	    
//	    @Bean
//	    public Step step2(StepBuilderFactory stepBuilderFactory, @Qualifier("reader02")FlatFileItemReader<Person> reader02,
//	    		@Qualifier("writer02")ItemWriter<Person> writer02, ItemProcessor<Person, Person> processor) {
//	        return stepBuilderFactory.get("step2")
//	                .<Person, Person> chunk(1)
//	                .reader(reader02)
//	                .processor(processor)
//	                .writer(writer02)
//	                .build();
//	    }
//	    
//	    
//	    // end::jobstep[]
//
//	    @Bean
//	    public static JdbcTemplate jdbcTemplate(DataSource dataSource) {
//	        return new JdbcTemplate(dataSource);
//	    }
//
//	
//}