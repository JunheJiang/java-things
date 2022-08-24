package com.ljp.spring.batchtest;
//
//
//
//import java.net.MalformedURLException;
//import java.rmi.AlreadyBoundException;
//import java.rmi.Naming;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationContext;
//
//import com.ljp.rmiservice.QuartzRMIFuncsImpl;
//
//
//
//@SpringBootApplication(scanBasePackages={"com.ljp.spring.batchtest"})
//public class Start {
//
//	public static void main(String [] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, RemoteException, MalformedURLException, AlreadyBoundException, InterruptedException {
//		ApplicationContext context = SpringApplication.run(Start.class, args);  
//			   
//		QuartzRMIFuncsImpl quartzRMIServer = new QuartzRMIFuncsImpl();
//		quartzRMIServer.startRMIBinding();
//
//	}
//	
//}



import java.util.Date;

import org.quartz.SchedulerException;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.item.ParseException;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication(scanBasePackages={"com.ljp.spring.batchtest"})
@EnableConfigurationProperties
@EnableTransactionManagement 
public class Start {
    public static void main(String[] args) throws JobExecutionAlreadyRunningException, org.springframework.batch.core.repository.JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, SchedulerException, BeansException, JsonProcessingException, ParseException, InterruptedException{
        ApplicationContext context = SpringApplication.run(Start.class, args); 

        JobLauncher jobLauncher = (JobLauncher)context.getBean("jobLauncher");
        SimpleJob testBatchJob = (SimpleJob) context.getBean("testBatchTranscation");
        JobExecution execution = null;
        
        long startTime = new Date().getTime();
        try {           
            execution =  jobLauncher.run(testBatchJob, new JobParametersBuilder().toJobParameters());
        }catch (org.springframework.batch.core.repository.JobRestartException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = new Date().getTime();
        System.out.println("duration time:" + (endTime - startTime));

    }

}




