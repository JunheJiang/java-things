//package com.ljp.spring.batchtest;
//
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.JobKey;
//import org.quartz.PersistJobDataAfterExecution;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
//public class TMPMyJobTwo  extends QuartzJobBean {
//	public static final String COUNT = "count";
//	private String name;
//	
//    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
//    	JobDataMap dataMap = ctx.getJobDetail().getJobDataMap();
//    	int cnt = dataMap.getInt(COUNT);
//	    JobKey jobKey = ctx.getJobDetail().getKey();
//	    System.out.println(jobKey+": "+name+": "+ cnt);
//	    cnt++;
//	    dataMap.put(COUNT, cnt);
//    }
//    
//	public void setName(String name) {
//		this.name = name;
//	}
//}
