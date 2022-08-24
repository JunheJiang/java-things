package com.ljp.spring.batchtest.service;

import org.springframework.stereotype.Service;

@Service()
public class MQService {
	public boolean sendToAppTopic(){
		//write the message channel model to the kafka MQ #31 topic
		return true;
	}
	
	public boolean sendToPcTopic(){
		//write the message channel model to the kafka MQ #31 topic
		return true;
	}
}
