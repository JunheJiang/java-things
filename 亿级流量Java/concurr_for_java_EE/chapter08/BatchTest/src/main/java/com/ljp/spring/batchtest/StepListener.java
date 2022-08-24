package com.ljp.spring.batchtest;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

@Component
public class StepListener {
	@BeforeStep
	public void testBeforeStep(){
		System.out.println("-------- before step ---------");
	}
	
	@AfterStep
	public void testBeforeRead(){
		System.out.println("-------- after step ---------");
	}
	
}
