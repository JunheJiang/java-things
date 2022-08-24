package com.ljp.spring.batchtest;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BatchJobListener  extends JobExecutionListenerSupport {
  
   private  JdbcTemplate jdbcTemplate;

	@Autowired
	public BatchJobListener(JdbcTemplate jdbcTemplate) {
		if(this.jdbcTemplate==null){
		this.jdbcTemplate = jdbcTemplate;
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		//you can mark some logs by after job running
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.listener.JobExecutionListenerSupport#beforeJob(org.springframework.batch.core.JobExecution)
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
	}
	
}