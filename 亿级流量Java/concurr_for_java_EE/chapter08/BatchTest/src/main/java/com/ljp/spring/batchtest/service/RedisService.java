package com.ljp.spring.batchtest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljp.spring.batchtest.bean.RemoteRedisServer;

@Service()
public class RedisService {
	@Autowired
	RemoteRedisServer remoteRedisServer;
	
	public List<String> getAllAudiences(List<String> listTags, List<String> listAliases){
		List<String> listNewAudiences = new ArrayList<String>();
		
		listNewAudiences.addAll(listAliases);
		
		//suppose listTags mapping 5 aliases: 1111,1112,1113,1114,1115
		listNewAudiences.add("1111");
		listNewAudiences.add("1112");
		listNewAudiences.add("1113");
		listNewAudiences.add("1114");
		listNewAudiences.add("1115");
		
		return listNewAudiences;
	}
	
	
	public boolean saveToRemoveServer(String key, List<String> listAudiences){
		remoteRedisServer.getMap().put(key, listAudiences);
		return true;
	}
	
	public List<String> getListAudiencesFromRemoveServer(String key){
		return (List<String>) remoteRedisServer.getMap().get(key);
		
	}
	
}
