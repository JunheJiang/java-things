package com.ljp.rmiservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IQuartzRMIFuncs extends Remote{
	
	public boolean pauseJob(String jobName, String jobGroup) throws RemoteException;

	public boolean resumeJob(String jobName, String jobGroup) throws RemoteException;
	
	public boolean deleteJob(String jobName, String jobGroup) throws RemoteException;

	public boolean runJob(String jobName, String jobGroup) throws RemoteException;
	
	public List<QuartzSimpleJobInfo> AllJobsInfo() throws RemoteException;
	
}
