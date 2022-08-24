package com.ljp.listener;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class ReqJobStateListener  implements SchedulerListener {
	
	@Override
    public void jobScheduled(Trigger trigger) {
        System.out.println("Job(" + trigger.getJobKey() + ")被部署时被成功监听");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
    	//System.out.println("Job被卸载时被成功监听");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        System.out.println("Job(" + trigger.getJobKey() + ")完成了它的使命，光荣退休时被成功监听");
        //通知server-api，trigger以及对应的job 变为COMPLETE 
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        //System.out.println(triggerKey + "（一个触发器）被暂停时被成功监听");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        //System.out.println(triggerGroup + "所在组的全部触发器被停止时被成功监听");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        //System.out.println(triggerKey + "（一个触发器）被恢复时被成功监听");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        //System.out.println(triggerGroup + "所在组的全部触发器被回复时被成功监听");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        System.out.println("一个JobDetail被动态添加进来");
        //通知server-api，job以及对应的trigger 变为 Waiting 状态
        
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        System.out.println(jobKey + "被删除时被成功监听");
        //通知server-api， job以及对应的trigger变为deleted状态
    }

    @Override
    public void jobPaused(JobKey jobKey) {  	
        System.out.println(jobKey + "被暂停时被成功监听");
        //通知server-api，job以及对应的trigger为paused状态
    }

    @Override
    public void jobsPaused(String jobGroup) {
        //System.out.println(jobGroup + "(一组Job)被暂停时被成功监听");
    }

    @Override
    public void jobResumed(JobKey jobKey) {  	
        System.out.println(jobKey + "被恢复时被成功监听");
        //通知servier-api，job以及对应的trigger状态为Waiting
    }

    @Override
    public void jobsResumed(String jobGroup) {
        //System.out.println(jobGroup + "(一组Job）被回复时被成功监听");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        System.out.println("出现异常" + msg + "时被成功监听");
        cause.printStackTrace();
    }

    @Override
    public void schedulerInStandbyMode() {
        //System.out.println("scheduler被设为standBy等候模式时被成功监听");
    }

    @Override
    public void schedulerStarted() {
        //System.out.println("scheduler启动时被成功监听");
    }

    @Override
    public void schedulerStarting() {
        //System.out.println("scheduler正在启动时被成功监听");
    }

    @Override
    public void schedulerShutdown() {
        //System.out.println("scheduler关闭时被成功监听");
    }

    @Override
    public void schedulerShuttingdown() {
        //System.out.println("scheduler正在关闭时被成功监听");

    }

    @Override
    public void schedulingDataCleared() {
        //System.out.println("scheduler中所有数据包括jobs, triggers和calendars都被清空时被成功监听");
    }
}
