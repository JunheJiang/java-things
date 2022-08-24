package com.ljp.spring.batchtest.bean;

import java.util.Map;

/**
 * APP渠道模型
 * @author dell2
 *
 */
public class ChannelApp {
	private Map<String,Object> headers;
	
	private Map<String,Object> payload;

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}
}
