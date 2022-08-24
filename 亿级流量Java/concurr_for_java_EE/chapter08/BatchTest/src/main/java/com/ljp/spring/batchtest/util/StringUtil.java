package com.ljp.spring.batchtest.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.mysql.jdbc.util.Base64Decoder;
import com.thoughtworks.xstream.core.util.Base64Encoder;

public class StringUtil {
	public static String serializeObject(Object object) throws IOException{
		if(object==null){
			throw new NullPointerException();
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(os);
		out.writeObject(object);

		byte[] byteArray = os.toByteArray();
		
		String encodeToString = new Base64Encoder().encode(byteArray);
		return encodeToString;

	}
	

	public static <T> T deserializeObject(String serializeObject) throws IOException, ClassNotFoundException{
		
		byte[] objByte = serializeObject.getBytes();
		if (objByte == null){
			throw new NullPointerException();
		}
		
		objByte =  new Base64Decoder().decode(objByte, 0, objByte.length);
		ByteArrayInputStream is = new ByteArrayInputStream(objByte);
		ObjectInputStream in = new ObjectInputStream(is);

		return (T) in.readObject();
	}
	
	
}
