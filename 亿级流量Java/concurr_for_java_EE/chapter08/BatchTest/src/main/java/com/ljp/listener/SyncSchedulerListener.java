package com.ljp.listener;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class SyncSchedulerListener implements SchedulerListener {
	
	
	@Override
    public void jobScheduled(Trigger trigger) {
        System.out.println("Trigger(" + trigger.getKey() + ")绑定了Job(" + trigger.getJobKey() + ")时被成功监听");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
    	
    	boolean hasOtherTrigger = false;
    	
    	Scheduler stdScheduler = null;
    	try {
			stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
			
			Trigger trigger = stdScheduler.getTrigger(triggerKey);
			JobKey jobKey = trigger.getJobKey();
			
			int triggerCount = stdScheduler.getTriggersOfJob(jobKey).size();
			if(triggerCount > 1){
				hasOtherTrigger = true;
			}
		} catch (SchedulerException e) {
			//log --> 查询时出错，未能确定是否真的有其他的trigger。
			e.printStackTrace();
		}
    	
    	
    	
    	
    	System.out.println("一个Trigger(" + triggerKey + ")被解除绑定，并且删除时被成功监听");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
    	System.out.println("与Job(" + trigger.getJobKey() + ")绑定的一个Trigger(" + trigger.getKey() + ") 已经完成了相关的触发，光荣退休时被成功监听");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        System.out.println(triggerKey + "（一个触发器）被暂停时被成功监听");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        System.out.println(triggerGroup + "所在组的全部触发器被停止时被成功监听");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        System.out.println(triggerKey + "（一个触发器）被恢复时被成功监听");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        System.out.println(triggerGroup + "所在组的全部触发器被回复时被成功监听");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        System.out.println("一个JobDetail被动态添加进来");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        System.out.println("一个Job(" + jobKey + ")被删除时被成功监听");
    }

    @Override
    public void jobPaused(JobKey jobKey) {  	
        System.out.println("一个Job(" + jobKey + ")被暂停时被成功监听");
    }

    @Override
    public void jobsPaused(String jobGroup) {
        System.out.println(jobGroup + "(一组Job)被暂停时被成功监听");
    }

    @Override
    public void jobResumed(JobKey jobKey) {  	
        System.out.println("一个Job(" + jobKey + ")被恢复时被成功监听");
    }

    @Override
    public void jobsResumed(String jobGroup) {
        System.out.println(jobGroup + "(一组Job）被回复时被成功监听");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
    	System.out.println("----------msg content:" + msg);
    	System.out.println("----------cause content:" + cause.toString());
 
    	int jobGroupStartIdx = msg.indexOf("(") + 1;
    	int jobGroupEndIdx = msg.indexOf(".");
    	int jobNameStartIdx = jobGroupEndIdx  + 1;
    	int jobNameEndIdx = msg.indexOf("threw") - 1;
    	
    	int exceptionStartIdx = cause.toString().indexOf("[") + 1;
    	int exceptionEndIdx = cause.toString().indexOf("]");
    	
    	System.out.println("jobName:" + msg.substring(jobNameStartIdx, jobNameEndIdx));
    	System.out.println("jobGroup:" + msg.substring(jobGroupStartIdx, jobGroupEndIdx));
    	System.out.println("exception description:" + cause.toString().substring(exceptionStartIdx, exceptionEndIdx));
    	
    	Scheduler stdScheduler = null;
    	try {
			stdScheduler =  StdSchedulerFactory.getDefaultScheduler();
			List<JobExecutionContext> listJobs = stdScheduler.getCurrentlyExecutingJobs();
			for(JobExecutionContext jobContext : listJobs){
				JobKey jobKey = jobContext.getJobDetail().getKey();
				System.out.println(jobKey.getName() + "." + jobKey.getGroup());
				Trigger trigger = jobContext.getTrigger();
				System.out.println(trigger.getKey().getName() + "." + trigger.getKey().getGroup());
			}
		} catch (SchedulerException e) {
			//log --> 查询时出错，未能确定是否真的有其他的trigger。
			e.printStackTrace();
		}
    	
    	
        System.out.println("出现异常" + msg + "时被成功监听");
        cause.printStackTrace();
    }

    @Override
    public void schedulerInStandbyMode() {
        System.out.println("scheduler被设为standBy等候模式时被成功监听");

    }

    @Override
    public void schedulerStarted() {
        System.out.println("scheduler启动时被成功监听");

    }

    @Override
    public void schedulerStarting() {
        System.out.println("scheduler正在启动时被成功监听");

    }

    @Override
    public void schedulerShutdown() {
        System.out.println("scheduler关闭时被成功监听");
    }

    @Override
    public void schedulerShuttingdown() {
        System.out.println("scheduler正在关闭时被成功监听");

    }

    @Override
    public void schedulingDataCleared() {
        System.out.println("scheduler中所有数据包括jobs, triggers和calendars都被清空时被成功监听");
    }
	
}
