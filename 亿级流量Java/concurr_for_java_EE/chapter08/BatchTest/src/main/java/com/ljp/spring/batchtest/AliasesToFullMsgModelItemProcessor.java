package com.ljp.spring.batchtest;

import org.springframework.batch.item.ItemProcessor;

import com.ljp.spring.batchtest.bean.MessageBussinessBean;
import com.ljp.spring.batchtest.bean.MessageConfigBean;

public class AliasesToFullMsgModelItemProcessor implements ItemProcessor<String, String> {

	@Override
	public String process(String item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
