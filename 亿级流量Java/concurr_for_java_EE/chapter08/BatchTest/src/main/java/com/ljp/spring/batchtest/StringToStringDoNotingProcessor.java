package com.ljp.spring.batchtest;

import org.springframework.batch.item.ItemProcessor;

public class StringToStringDoNotingProcessor implements ItemProcessor<String, String> {

	@Override
	public String process(String item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}
}
