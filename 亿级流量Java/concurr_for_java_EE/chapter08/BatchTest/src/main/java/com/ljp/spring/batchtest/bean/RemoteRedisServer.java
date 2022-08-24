package com.ljp.spring.batchtest.bean;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class RemoteRedisServer {
	private HashMap<String, Object> map = new HashMap<String, Object>();

	public HashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}	
	
}