//package com.ljp.spring.batchtest;
//
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
//
//
//
//@SpringBootApplication(scanBasePackages={"com.ljp.spring.batchtest"})
//@EnableConfigurationProperties
//public class Start2 {
//	public static void main(String[] args) throws JobExecutionAlreadyRunningException, org.springframework.batch.core.repository.JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, SchedulerException, BeansException, JsonProcessingException{
//		ApplicationContext context = SpringApplication.run(Start.class, args); 
//	
//		
//		//---- Quartz Job reference start ----
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("jobLauncher", context.getBean("jobLauncher"));
//		jobDataMap.put("job", context.getBean("importUserJob"));
//		jobDataMap.put("myParam","001");
//	
//		JobDetail messagePlanJobDetail = JobBuilder.newJob(MessageCoreProcessor.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).build();
//		CronTrigger myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")).build();
//		
//        StdScheduler stdScheduler = (StdScheduler) context.getBean("schedulerFactoryBean");
//        stdScheduler.scheduleJob(messagePlanJobDetail, myTrigger);
//        
//        
////        Scheduler myScheduler = StdSchedulerFactory.getDefaultScheduler();
////        myScheduler.scheduleJob(jobDetailFactoryBean001, myTrigger);
//        
//        
//        
//        
//        
//        JobDetail jobDetailFactoryBean002 = JobBuilder.newJob(MyJob3.class).withIdentity("myJob3","group2").build();
//
//		Map<String,Object> map02 = new HashMap<String,Object>();
//		map02.put("jobLauncher", context.getBean("jobLauncher"));
//		map02.put("job", context.getBean("importUserJob"));
//		map02.put("myParam","002");
//		
//		JobDataMap jobDataMap02 = new JobDataMap();
//		jobDataMap02.putAll(map02);
//		
//		jobDetailFactoryBean002 = jobDetailFactoryBean002.getJobBuilder().setJobData(jobDataMap02).build();
//        CronTrigger myTrigger2 = TriggerBuilder.newTrigger().withIdentity("myTrigger002","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
//        stdScheduler.scheduleJob(jobDetailFactoryBean002, myTrigger2);
//
//        
//        
//        JobDetail jobDetailFactoryBean003 = JobBuilder.newJob(MyJob3.class).withIdentity("myJob3","group3").build();
//
//		Map<String,Object> map03 = new HashMap<String,Object>();
//		map03.put("jobLauncher", context.getBean("jobLauncher"));
//		map03.put("job", context.getBean("importUserJob"));
//		map03.put("myParam","003");
//		
//		JobDataMap jobDataMap03 = new JobDataMap();
//		jobDataMap03.putAll(map03);
//		
//		jobDetailFactoryBean003 = jobDetailFactoryBean003.getJobBuilder().setJobData(jobDataMap03).build();
//        CronTrigger myTrigger3 = TriggerBuilder.newTrigger().withIdentity("myTrigger003","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/8 * * * * ?")).build();
//        stdScheduler.scheduleJob(jobDetailFactoryBean003, myTrigger3);
//        
//        
//        
//        JobDetail jobDetailFactoryBean004 = JobBuilder.newJob(MyJob3.class).withIdentity("myJob3","group4").build();
//
//		Map<String,Object> map04 = new HashMap<String,Object>();
//		map04.put("jobLauncher", context.getBean("jobLauncher"));
//		map04.put("job", context.getBean("importUserJob"));
//		map04.put("myParam","004");
//		
//		JobDataMap jobDataMap04 = new JobDataMap();
//		jobDataMap04.putAll(map04);
//		
//		jobDetailFactoryBean004 = jobDetailFactoryBean004.getJobBuilder().setJobData(jobDataMap04).build();
//        CronTrigger myTrigger4 = TriggerBuilder.newTrigger().withIdentity("myTrigger004","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/7 * * * * ?")).build();
//        stdScheduler.scheduleJob(jobDetailFactoryBean004, myTrigger4);
//        
//        
//        
//        JobDetail jobDetailFactoryBean005 = JobBuilder.newJob(MyJob3.class).withIdentity("myJob3","group5").build();
//
//		Map<String,Object> map05 = new HashMap<String,Object>();
//		map05.put("jobLauncher", context.getBean("jobLauncher"));
//		map05.put("job", context.getBean("importUserJob"));
//		map05.put("myParam","005");
//		
//		JobDataMap jobDataMap05 = new JobDataMap();
//		jobDataMap05.putAll(map05);
//		
//		jobDetailFactoryBean005 = jobDetailFactoryBean005.getJobBuilder().setJobData(jobDataMap05).build();
//        CronTrigger myTrigger5 = TriggerBuilder.newTrigger().withIdentity("myTrigger005","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/9 * * * * ?")).build();
//        stdScheduler.scheduleJob(jobDetailFactoryBean005, myTrigger5);
//		
//        
//        
//        
//        JobDetail jobDetailFactoryBean006 = JobBuilder.newJob(MyJob3.class).withIdentity("myJob3","group6").build();
//
//		Map<String,Object> map06 = new HashMap<String,Object>();
//		map06.put("jobLauncher", context.getBean("jobLauncher"));
//		map06.put("job", context.getBean("importUserJob"));
//		map06.put("myParam","006");
//		
//		JobDataMap jobDataMap06 = new JobDataMap();
//		jobDataMap06.putAll(map06);
//		
//		jobDetailFactoryBean006 = jobDetailFactoryBean006.getJobBuilder().setJobData(jobDataMap06).build();
//        CronTrigger myTrigger6 = TriggerBuilder.newTrigger().withIdentity("myTrigger006","group1").withSchedule(CronScheduleBuilder.cronSchedule("0/6 * * * * ?")).build();
//        stdScheduler.scheduleJob(jobDetailFactoryBean006, myTrigger6);
//      ---- Quartz Job reference end ----
//	}
//}
