package com.ljp.listener;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.annotation.OnWriteError;

public class NewStepListener{

	@BeforeStep
	public void beforeStep() {
		// 写入自己的beforeStep逻辑
		
	}

	@BeforeRead
	public void afterStep() {
		// 写入自己的BeforeRead逻辑
	}

	@OnSkipInRead
	public void onSkipInRead(Throwable t) {
		// 写入自己的SkipInRead逻辑
		
	}

	@OnSkipInWrite
	public void onSkipInWrite(Object item, Throwable t) {
		// 写入自己的SkipInWrite逻辑
		
	}


	@BeforeWrite
	public void beforeWrite(List items) {
		// 写入自己的BeforeWrite逻辑
		
	}

	@AfterWrite
	public void afterWrite(List items) {
		// 写入自己的AfterWrite逻辑
		
	}

	@OnWriteError
	public void onWriteError(Exception exception, List items) {
		// 写入自己的OnWriteError逻辑
		
	}

}
