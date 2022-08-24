package com.ljp.spring.batchtest;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.ljp.spring.batchtest.bean.GlobalStepValueMap;
import com.ljp.spring.batchtest.bean.MessageBussinessBean;
import com.ljp.spring.batchtest.bean.MessageConfigBean;
import com.ljp.spring.batchtest.service.MessageService;

public class MsgCfgToMsgModelItemProcessor implements ItemProcessor<MessageConfigBean, MessageBussinessBean> {

	@Autowired
	private GlobalStepValueMap globalStepValueMap;
	
	@Autowired
	private MessageService messageService;

	
	private String jobId;
	
	
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}



	@Override
	public MessageBussinessBean process(MessageConfigBean itemMsgConfig) throws Exception {
		//contruct new message model
		MessageBussinessBean mbBean = messageService.dealWithFromMsgConfigBeanToMessageBussinessBean(itemMsgConfig);
		
		globalStepValueMap.getMap().put("msgModel" + jobId, mbBean);
		return mbBean;
	}



}