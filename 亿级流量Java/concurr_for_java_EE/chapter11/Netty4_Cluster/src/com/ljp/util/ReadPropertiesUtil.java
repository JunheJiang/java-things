package com.ljp.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesUtil {
	public static String getPropertiesValue(String filePath, String key) throws IOException{
		Properties configProperties = new Properties();
		InputStream in = new BufferedInputStream (new FileInputStream(filePath));  
		configProperties.load(in);
		String value = configProperties.getProperty(key);
		return value;
	}
}
