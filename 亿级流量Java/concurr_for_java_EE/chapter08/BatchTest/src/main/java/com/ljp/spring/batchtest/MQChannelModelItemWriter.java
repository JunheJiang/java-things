package com.ljp.spring.batchtest;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ljp.spring.batchtest.service.MessageService;

public class MQChannelModelItemWriter implements ItemWriter<String> {
	@Autowired
	private MessageService messageService;
	
	
	
	@Override
	public  void write(List<? extends String> listAliases) throws Exception {
		System.out.println("-------2nd step writer--write()-------List size:" + listAliases.size());
		
		//assembling the aliases
		String audiencesString = "";
		for(String tmpAliaese : listAliases){
			//filling the aliases to DB
			messageService.fillingAliases(audiencesString);
			//deal with anonymous......
			//write it to my message......(DB)
			//write it to my unread message......(DB)
			
			audiencesString += tmpAliaese + ",";
		}
		
		audiencesString = audiencesString.substring(0, audiencesString.lastIndexOf(","));
		
//		List<String> listChannels = messageService.getMQChannels();
		
		//iterate channels
//		for (String tmpChannel : listChannels){
//			//construct channel model
//			if(tmpChannel.equalsIgnoreCase("APP")){
//				ChannelApp channelAppModel = messageService.getAppChannelModel(audiencesString);
//				mqService.sendToAppTopic();
//			}else if(tmpChannel.equalsIgnoreCase("PC")){
//				ChannelPc channelPcModel = messageService.getPCChannelModel(audiencesString);
//				mqService.sendToPcTopic();
//			}
//			//.......
//		}
		
		
	}
	

	
}