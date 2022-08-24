package com.ljp.spring.batchtest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ljp.spring.batchtest.bean.AppSettingBean;
import com.ljp.spring.batchtest.bean.ChannelApp;
import com.ljp.spring.batchtest.bean.ChannelPc;
import com.ljp.spring.batchtest.bean.MessageBussinessBean;
import com.ljp.spring.batchtest.bean.MessageConfigBean;

@Service()
public class MessageService {

	public MessageConfigBean getMsgConfigBean(){
		MessageConfigBean mcBean = new MessageConfigBean();
		return mcBean;
	}
	
	public MessageBussinessBean getMsgBussinessBean(){
		MessageBussinessBean mbBean = new MessageBussinessBean();
		return mbBean;
	}
	
	public MessageConfigBean dealWithFromMsgConfigBeanToNewMsgConfigBean(MessageConfigBean mcBean, List<AppSettingBean> appSettingBean){
		MessageConfigBean newMcBean = new MessageConfigBean();
		return newMcBean;
	}
	
	public MessageBussinessBean dealWithFromMsgConfigBeanToMessageBussinessBean(MessageConfigBean itemMsgConfig){
		MessageBussinessBean mbBean = new MessageBussinessBean();
		return mbBean;
	}
	
	public MessageBussinessBean saveMsgBussinessBean(MessageBussinessBean msgBean){
		MessageBussinessBean mbBean = new MessageBussinessBean();
		return mbBean;
	}
	
	public List<String> getTheRemindingChannels(){
		return null;
	}
	
	public MessageBussinessBean fillingAliases(String aliases){
		return null;
	}
	
	public List<String> getMQChannels(){
		List<String> listChannels = new ArrayList<String>();
		listChannels.add("APP");
		listChannels.add("PC");
		
		return null;
	}
	
	public ChannelApp getAppChannelModel(String audiencesString){
		return null;
	}
	
	public ChannelPc getPCChannelModel(String audiencesString){
		return null;
	}
	
	public boolean saveJobRunningStateTable(){
		return true;
	}
	
}
