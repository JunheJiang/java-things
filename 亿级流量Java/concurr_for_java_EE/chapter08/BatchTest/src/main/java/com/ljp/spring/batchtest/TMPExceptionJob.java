package com.ljp.spring.batchtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TMPExceptionJob  extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//		IOException exception = new IOException();
//		try {
//			throw exception;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		int i = 9;
		int j = 0;
		
		int k = i / j;
		
//		JobExecutionException exception = new JobExecutionException(new IOException("my mark exception!"));
//		throw exception;
		
	}

}
