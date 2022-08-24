package com.ljp.rmiservice;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class QuartzRMIFuncsImpl extends UnicastRemoteObject implements IQuartzRMIFuncs {
	
	public QuartzRMIFuncsImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}


	private static final long serialVersionUID = 4077329331699640333L;
	
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
	
	public boolean pauseJob(String jobName, String jobGroup) {
		Scheduler stdScheduler = null;
		try {
			stdScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			stdScheduler.pauseJob(new JobKey(jobName, jobGroup));
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	
	public boolean resumeJob(String jobName, String jobGroup){
		Scheduler stdScheduler = null;
		try {
			stdScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			stdScheduler.resumeJob(new JobKey(jobName, jobGroup));
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	public boolean deleteJob(String jobName, String jobGroup){
		Scheduler stdScheduler = null;
		try {
			stdScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			stdScheduler.deleteJob(new JobKey(jobName, jobGroup));
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	public boolean runJob(String jobName, String jobGroup){
		Scheduler stdScheduler = null;
		try {
			stdScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			stdScheduler.triggerJob(new JobKey(jobName, jobGroup));
		}catch(Exception e){
			//log ......
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public List<QuartzSimpleJobInfo> AllJobsInfo(){
		Scheduler stdScheduler = null;
		try {
			stdScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<QuartzSimpleJobInfo> jobs = new ArrayList<QuartzSimpleJobInfo>();
		
		try {
			for (String groupName : stdScheduler.getJobGroupNames()) { 
				for (JobKey jobKey : stdScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					JobDetail jobTMP = stdScheduler.getJobDetail(jobKey);
			 
					//get job's trigger
					
					@SuppressWarnings("unchecked")
					List<Trigger> triggers = (List<Trigger>) stdScheduler.getTriggersOfJob(jobKey);
					
					for(Trigger tmpTrigger : triggers){
						Date startTime = tmpTrigger.getStartTime();
						
						System.out.println("[job别名] : " + jobTMP.getJobDataMap().get("jobAliasName") 
								+ " [分组] : " + jobGroup 
								+ " - 触发器状态:" + stdScheduler.getTriggerState(tmpTrigger.getKey())
								+ " - 启动时间:" + startTime);
						
						QuartzSimpleJobInfo jobInfo = new QuartzSimpleJobInfo();
						jobInfo.setJobGroup(jobGroup);
						jobInfo.setJobName(jobName);
						jobInfo.setState(stdScheduler.getTriggerState(tmpTrigger.getKey()).toString());
						jobInfo.setRuntime(dateFormat.format(startTime));
						
						jobs.add(jobInfo);
					}
				}

			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jobs;
	}
	
	
//	public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException{
//		System.out.print("Run...");
//        QuartzRMIFuncsImpl quartzFuncs = new QuartzRMIFuncsImpl();
//        //RMI默认是1099端口。但如果冲突，可以采用其他端口。
//        Naming.bind("rmi://172.20.177.142:11056/quartzFuncs", quartzFuncs);
//        
//        System.out.print("Ready......");
//	}



	public boolean startRMIBinding() throws RemoteException, MalformedURLException, AlreadyBoundException, InterruptedException{
		System.out.print("Run...");

        QuartzRMIFuncsImpl quartzFuncs = new QuartzRMIFuncsImpl();
        
        //创建RMI注册中心
        LocateRegistry.createRegistry(11056);
        //RMI默认是1099端口。但如果冲突，可以采用其他端口。
        Naming.bind("rmi://172.20.176.194:11056/quartzFuncs", quartzFuncs);
        
        System.out.print("Ready......");
		
		return true;
	}
	
}
