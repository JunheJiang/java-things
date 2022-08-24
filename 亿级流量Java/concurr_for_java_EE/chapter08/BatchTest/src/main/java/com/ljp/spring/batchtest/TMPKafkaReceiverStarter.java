//package com.ljp.spring.batchtest;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import org.quartz.CronScheduleBuilder;
//import org.quartz.CronTrigger;
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.SchedulerException;
//import org.quartz.TriggerBuilder;
//import org.quartz.impl.StdScheduler;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.beans.BeansException;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.ljp.spring.batchtest.util.JsonUtil;
//
//import antlr.collections.List;
//
//@SpringBootApplication(scanBasePackages={"com.ljp.spring.batchtest"})
//@EnableConfigurationProperties
//public class TMPKafkaReceiverStarter {
//	public static void main(String[] args) throws JobExecutionAlreadyRunningException, org.springframework.batch.core.repository.JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, SchedulerException, BeansException, JsonProcessingException{
//		ApplicationContext context = SpringApplication.run(TMPKafkaReceiverStarter.class, args); 
//	
//		
//		//in the scheduler program case, it will through the kafka receiver to get the request model
//		SimpleRequestModel request = new SimpleRequestModel();
//		request.setSolution("A0001");
//		//request.setCron("15 07 17 * * ? 2016");
//		request.setCron("0/8 * * * * ? 2016");
//		request.setJob("1033");
//		
//		ArrayList<String> listTags = new ArrayList<String>();
//		listTags.add("广州地区");
//		listTags.add("美食");
//		request.setTags(listTags);
//		
//		ArrayList<String> listAliases = new ArrayList<String>();
//		request.setAliases(listAliases);
//		
//		ArrayList<Object> listParameters = new ArrayList<Object>();
//		request.setParameters(listParameters);
//		
//		
//		//---- Quartz Job reference start ----
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("jobLauncher", context.getBean("jobLauncher"));
//		jobDataMap.put("job", context.getBean("importUserJob"));
//		jobDataMap.put("myParam","001");
//		//serialize request, and put it inthe JobDataMap
//		jobDataMap.put("request", JsonUtil.getJsonStringFromEntity(request));
//	
//		JobDetail messagePlanJobDetail = JobBuilder.newJob(MessageCoreBatchProcessor.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).build();
//
//		
//		CronTrigger myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule(request.getCron()).withMisfireHandlingInstructionIgnoreMisfires()).build();
//		//CronTrigger myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule(request.getCron())).build();
//		
//        StdScheduler stdScheduler = (StdScheduler) context.getBean("schedulerFactoryBean");
//        stdScheduler.scheduleJob(messagePlanJobDetail, myTrigger);
//	}
//}