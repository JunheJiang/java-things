package com.ljp.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;

public class JobExceptionListener implements JobListener  {

//	ReqJobManager jobManager = new ReqJobManager();
	

	

	@Override
	public void jobExecutionVetoed(JobExecutionContext arg0) {
		//System.out.println("MyJobListener.jobToBeExecuted()");
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext arg0) {
		//System.out.println("MyJobListener.jobExecutionVetoed()");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext jobExecContext, JobExecutionException jobExecException) {
		if(jobExecException != null){
			JobKey jobKey = jobExecContext.getJobDetail().getKey();
		    
			
			String jobName = jobKey.getName();
			String jobGroup = jobKey.getGroup();
			
			String exceptionDesc = jobExecException.getMessage();
			
			System.out.println("成功catch到这样的exception：(" + jobGroup + "." + jobName + ")" + exceptionDesc);
			
			
		}	
	}

	
	@Override
	public String getName() {
		return "Job Exception";
	}

}
