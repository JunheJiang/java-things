package com.ljp.rmiservice;

import java.io.Serializable;

public class QuartzSimpleJobInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10010L;
	
	private String jobName;
	private String jobGroup;
	private String state;
	private String runtime;
	
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	
}
