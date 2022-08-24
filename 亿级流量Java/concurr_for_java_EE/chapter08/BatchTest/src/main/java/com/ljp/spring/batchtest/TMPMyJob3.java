//package com.ljp.spring.batchtest;
//
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.support.SimpleJobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
//public class TMPMyJob3 extends QuartzJobBean{
//	public static final String COUNT = "count";
//
//
//	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
//    	JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
//	    
//    	
//    	
//    	JobLauncher jobLauncher = (JobLauncher) dataMap.get("jobLauncher");
//    	Job job = (Job) dataMap.get("job");
//	    
//	    
//	    
//	    JobParameters jobParameters = new JobParametersBuilder()
//				.addLong("time", System.currentTimeMillis())
//				.addString("myParam", dataMap.getString("myParam"))
//				.toJobParameters();
//
//	    JobExecution execution = null;
//		try {           
//			execution = jobLauncher.run(job, jobParameters);
//		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
//				| JobParametersInvalidException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	    System.out.println(execution);
//
//	    
//    }
//    
//}
