package com.ljp.spring.batchtest;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.batch.operations.JobRestartException;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ljp.listener.JobExceptionListener;
import com.ljp.listener.SyncSchedulerListener;
import com.ljp.spring.batchtest.util.JsonUtil;

@SpringBootApplication(scanBasePackages={"com.ljp.spring.batchtest"})
@EnableConfigurationProperties
@EnableTransactionManagement 
public class KafkaReceiverStarter {
	public static void main(String[] args) throws JobExecutionAlreadyRunningException, org.springframework.batch.core.repository.JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, SchedulerException, BeansException, JsonProcessingException, ParseException, InterruptedException{
		ApplicationContext context = SpringApplication.run(KafkaReceiverStarter.class, args); 
		
		//In other scheduler program case, it will through the kafka receiver to get the request model
		SimpleRequestModel request = new SimpleRequestModel();
		request.setSolution("A0001");
		request.setCron("18 44 19 27 11 ? 2016");
		request.setJob("1033");
		
		ArrayList<String> listTags = new ArrayList<String>();
		listTags.add("广州地区");
		listTags.add("美食");
		request.setTags(listTags);
		
		ArrayList<String> listAliases = new ArrayList<String>();
		listAliases.add("161670850");
		listAliases.add("161670851");
		request.setAliases(listAliases);		
		
		ArrayList<Object> listParameters = new ArrayList<Object>();
		
		JobLauncher jobLauncher = (JobLauncher)context.getBean("jobLauncher");
		SimpleJob job = (SimpleJob) context.getBean("messageCoreBatch");
		
		
		request.setParameters(listParameters);
		
		//---- Quartz Job reference start ----
		JobDataMap jobDataMap = new JobDataMap();

		//serialize request, and put it inthe JobDataMap
		jobDataMap.put("request", JsonUtil.getJsonStringFromEntity(request));
		
		jobDataMap.put("jobAliasName", "AAABBBCCC");
	
		
		
		//CronTrigger myTrigger = TriggerB uilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule(request.getCron()).withMisfireHandlingInstructionIgnoreMisfires()).build();
		//CronTrigger myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule(request.getCron())).build();
		
		Trigger myTrigger = null;
		
		//myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").withSchedule(CronScheduleBuilder.cronSchedule(request.getCron()).withMisfireHandlingInstructionIgnoreMisfires()).build();

        //StdScheduler stdScheduler = (StdScheduler) context.getBean("schedulerFactoryBean");
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		
		
		stdScheduler.getListenerManager().addSchedulerListener(new SyncSchedulerListener());
		stdScheduler.getListenerManager().addJobListener(new JobExceptionListener());
		
		
		stdScheduler.getContext().put("jobLauncher", jobLauncher);
        stdScheduler.getContext().put("springBatchJob", job);
		
		stdScheduler.start();

		
//		stdScheduler.pauseJob(new JobKey("d1b63f80-6698-4fb7-9674-5c0bb82f1831", "group1"));
//		stdScheduler.resumeJob(new JobKey("d1b63f80-6698-4fb7-9674-5c0bb82f1831", "group1"));
//		stdScheduler.triggerJob(new JobKey("d1b63f80-6698-4fb7-9674-5c0bb82f1831", "group1"));
//		stdScheduler.deleteJob(new JobKey("d1b63f80-6698-4fb7-9674-5c0bb82f1831", "group1"));
		
		
		
//		System.out.println(stdScheduler.getTriggerState(new TriggerKey("f027b7f2-ea1f-4601-81a8-c36c0b5018d3","group1")));
		
		
//		for (String groupName : stdScheduler.getJobGroupNames()) { 
//			for (JobKey jobKey : stdScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
//				String jobName = jobKey.getName();
//				String jobGroup = jobKey.getGroup();
//				
//				JobDetail jobTMP = stdScheduler.getJobDetail(jobKey);
//		 
//				//get job's trigger                           
//				List<Trigger> triggers = (List<Trigger>) stdScheduler.getTriggersOfJob(jobKey);
//				
//				Date startTime = triggers.get(0).getStartTime();
//				
//				System.out.println(jobTMP.getJobDataMap().get("myTestString"));
//				
//				System.out.println("[job名称] : " + jobName 
//						+ " [分组] : " + jobGroup 
//						+ " - 触发器状态:" + stdScheduler.getTriggerState(triggers.get(0).getKey())
//						+ " - 触发时间:" + startTime);
//		 
//			}
//		 
//		}
//		
//		
//		


        
        String jobName = UUID.randomUUID().toString();
        String jobGroup = "group1";
		jobDataMap.put("jobName", jobName);
		jobDataMap.put("jobGroup", jobGroup);
		
		JobDetail messagePlanJobDetail = JobBuilder.newJob(MessageCoreBatchJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).storeDurably(true).build();
		
		
		
		//JobDetail messagePlanJobDetail = JobBuilder.newJob(TMPExceptionJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).storeDurably(true).build();
   
        Date date1 = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	date1 = dateFormat.parse("2017-06-05 17:08:50");
    	
    	System.out.println("---simple trigger start---");
    	
    	myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires().repeatMinutelyForTotalCount(1)).build();
    	
    	
    	
//    	myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires().repeatMinutelyForTotalCount(3)).forJob(messagePlanJobDetail).build();
//    	Trigger myTrigger02 = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group2").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires().repeatMinutelyForTotalCount(2)).forJob(messagePlanJobDetail).build();
    	
    	//myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1)).build();
    	
    	
    	
    	stdScheduler.scheduleJob(messagePlanJobDetail,myTrigger);
    	
//    	stdScheduler.addJob(messagePlanJobDetail, true);
//    	stdScheduler.scheduleJob(myTrigger);
//    	stdScheduler.scheduleJob(myTrigger02);
    	
    	
    	System.out.println("---simple trigger end---");
    
    	Thread.sleep(300);
//    	
//    	JobDetail messagePlanJobDetail02 = JobBuilder.newJob(MessageCoreBatchJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).build();
//    	
//    	date1 = dateFormat.parse("2017-02-14 17:30:30");
//    	
//    	System.out.println("---simple trigger start---");
//    	Trigger myTrigger02 = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group2").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires().repeatSecondlyForTotalCount(1)).build();
//    	
//    	//myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1)).build();
//    	
//    	stdScheduler.scheduleJob(messagePlanJobDetail02, myTrigger02);
//    	
//    	System.out.println("---simple trigger end---");
//    	
//    	//--------
//    	Thread.sleep(300);
//    	
//    	JobDetail messagePlanJobDetail03 = JobBuilder.newJob(MessageCoreBatchJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap).build();
//    	
//    	date1 = dateFormat.parse("2017-02-14 17:30:35");
//    	
//    	System.out.println("---simple trigger start---");
//    	Trigger myTrigger03 = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group3").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires().repeatSecondlyForTotalCount(1)).build();
//    	
//    	//myTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group1").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1)).build();
//    	
//    	
//    	stdScheduler.scheduleJob(messagePlanJobDetail03, myTrigger03);
//    	
//    	
//    	System.out.println("---simple trigger end---");
		
    }
	
}