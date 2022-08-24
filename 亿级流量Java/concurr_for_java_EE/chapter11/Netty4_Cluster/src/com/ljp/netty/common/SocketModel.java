package com.ljp.netty.common;


import java.io.Serializable;
import java.util.List;

public class SocketModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10000L;
	
	private String type;                //socketModel的类型
    private String code;                //该类型下的分类编码。例如如果type是response类型，那么这里可以填是success或failed等类似的值或者代号。
    private List<String> message;       //消息的实际内容，可以按照列表顺序保存不同的分类信息
 
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(List<String> message) {
		this.message = message;
	}
    

    

}
