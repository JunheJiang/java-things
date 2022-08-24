package com.ljp.spring.batchtest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ljp.spring.batchtest.util.JsonUtil;

public class QuartzJobListShow {
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
		
		
		
		
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		
		stdScheduler.getContext().put("jobLauncher", jobLauncher);
        stdScheduler.getContext().put("springBatchJob", job);
		
		stdScheduler.start();
		

		System.out.println(stdScheduler);
		
		
		for (String groupName : stdScheduler.getJobGroupNames()) { 
			for (JobKey jobKey : stdScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				
				JobDetail jobTMP = stdScheduler.getJobDetail(jobKey);
		 
				//get job's trigger
				List<Trigger> triggers = (List<Trigger>) stdScheduler.getTriggersOfJob(jobKey);
				
				for(Trigger tmpTrigger : triggers){
					Date startTime = tmpTrigger.getStartTime();
					
					System.out.println("[job别名] : " + jobTMP.getJobDataMap().get("jobAliasName") 
							+ " [分组] : " + jobGroup 
							+ " - 触发器状态:" + stdScheduler.getTriggerState(tmpTrigger.getKey())
							+ " - 启动时间:" + startTime);
				}
			}	 
		}
		
        
        
        Date date1 = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	date1 = dateFormat.parse("2017-01-16 16:58:20");
    	
    	//---- Quartz Job reference start ----
    	JobDataMap jobDataMap = new JobDataMap();

    	//serialize request, and put it inthe JobDataMap
    	jobDataMap.put("request", JsonUtil.getJsonStringFromEntity(request));
    	jobDataMap.put("jobAliasName", "B00104");
    			
    	JobDetail messagePlanJobDetail = JobBuilder
    										.newJob(MessageCoreBatchJob.class)
    										.withIdentity(UUID.randomUUID().toString(),"group1")
    										.setJobData(jobDataMap)
    										.build();
    			

    	Trigger myTrigger = TriggerBuilder.newTrigger()
    							.withIdentity(
    								UUID.randomUUID().toString(),
    								"group1"
    							)
    							.startAt(date1)
    							.withSchedule(
    								SimpleScheduleBuilder
    									.simpleSchedule()
    									.withMisfireHandlingInstructionFireNow()
    							).build();
    	
    	
    	System.out.println("---simple trigger start---");
    	stdScheduler.scheduleJob(messagePlanJobDetail, myTrigger);
    	System.out.println("---simple trigger end---");
    	
    	//--------
    	Thread.sleep(300);
    	
    	
    	
//    	JobDataMap jobDataMap02 = new JobDataMap();
//		jobDataMap02.put("request", JsonUtil.getJsonStringFromEntity(request));
//		jobDataMap02.put("jobAliasName", "A0002");
//		
//    	JobDetail messagePlanJobDetail02 = JobBuilder.newJob(MessageCoreBatchJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap02).build();
//    	
//    	date1 = dateFormat.parse("2017-01-09 17:43:30");
//    	
//    	System.out.println("---simple trigger start---");
//    	Trigger myTrigger02 = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group2").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1).withMisfireHandlingInstructionIgnoreMisfires()).build();
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
//    	
//    	JobDataMap jobDataMap03 = new JobDataMap();
//		jobDataMap03.put("request", JsonUtil.getJsonStringFromEntity(request));
//		jobDataMap03.put("jobAliasName", "A0003");
//    	
//    	JobDetail messagePlanJobDetail03 = JobBuilder.newJob(MessageCoreBatchJob.class).withIdentity(UUID.randomUUID().toString(),"group1").setJobData(jobDataMap03).build();
//    	
//    	date1 = dateFormat.parse("2017-01-09 17:43:35");
//    	
//    	System.out.println("---simple trigger start---");
//    	Trigger myTrigger03 = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(),"group3").startAt(date1).withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(1).withMisfireHandlingInstructionIgnoreMisfires()).build();
//    	
//    	
//    	stdScheduler.scheduleJob(messagePlanJobDetail03, myTrigger03);
//    	
//    	
//    	System.out.println("---simple trigger end---");
    	
    }
	

	
	public boolean pauseJob(JobKey jobkey) throws SchedulerException{
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		try{
			stdScheduler.pauseJob(jobkey);
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	
	public boolean resumeJob(JobKey jobKey) throws SchedulerException{
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		try{
			stdScheduler.resumeJob(jobKey);
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	public boolean deleteJob(JobKey jobKey) throws SchedulerException{
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		try{
			stdScheduler.deleteJob(jobKey);
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	public boolean runJob(JobKey jobKey) throws SchedulerException{
		Scheduler stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
		try{
			stdScheduler.triggerJob(jobKey);
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
}
