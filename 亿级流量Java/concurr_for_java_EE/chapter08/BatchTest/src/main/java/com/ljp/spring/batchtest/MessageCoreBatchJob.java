package com.ljp.spring.batchtest;

import javax.batch.operations.JobRestartException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class MessageCoreBatchJob extends QuartzJobBean  {
	
	protected void executeInternal(JobExecutionContext quartzJobExecContext) throws JobExecutionException {
    	JobDataMap quartzDataMap = quartzJobExecContext.getJobDetail().getJobDataMap();

    	
    	
	    
//    	JobLauncher jobLauncher = (JobLauncher) quartzDataMap.get("jobLauncher");
//    	Job springBatchJob = (Job) quartzDataMap.get("springBatchJob");
	
		JobLauncher jobLauncher = null;
		Job springBatchJob = null;
    	
    	try {
			SchedulerContext schCtx = quartzJobExecContext.getScheduler().getContext();
			jobLauncher = (JobLauncher) schCtx.get("jobLauncher");
			springBatchJob = (Job) schCtx.get("springBatchJob");
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
    	
    	//JobParameters is the 
	    JobParameters springBatchJobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.addString("request", quartzDataMap.getString("request"))  // serialized request model
				.addString("jobName", quartzDataMap.getString("jobName"))
				.addString("jobGroup", quartzDataMap.getString("jobGroup"))
				.toJobParameters();

	    JobExecution execution = null;
		try {           
			execution = jobLauncher.run(springBatchJob, springBatchJobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.springframework.batch.core.repository.JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    System.out.println(execution);

	}
}
