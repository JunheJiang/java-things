package com.ljp.spring.batchtest;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ljp.spring.batchtest.bean.GlobalStepValueMap;
import com.ljp.spring.batchtest.bean.MessageBussinessBean;
import com.ljp.spring.batchtest.service.MessageService;

public class MessageFullSetItemWriter implements ItemWriter<MessageBussinessBean> {
	
	@Autowired
	private GlobalStepValueMap globalStepValueMap;
	
	@Autowired
	private MessageService messageService;
	
	private String requestJobId;	

	public String getRequestJobId() {
		return requestJobId;
	}

	public void setRequestJobId(String requestJobId) {
		this.requestJobId = requestJobId;
	}



	@Override
	public  void write(List<? extends MessageBussinessBean> listMessageModels) throws Exception {
		System.out.println("-------1st step writer--write()-------List size:" + listMessageModels.size());

		for(MessageBussinessBean messageModel : listMessageModels){// In fact, there is only one model in this list.
			
			//message model is written into the DB
			MessageBussinessBean newMessageBussinessBean =  messageService.saveMsgBussinessBean(messageModel);
			
			globalStepValueMap.getMap().put("msgModel" + requestJobId, newMessageBussinessBean);
			
			//job is written into the DB
			messageService.saveJobRunningStateTable();
		}
		
	}
	
	

	
}