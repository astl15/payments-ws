package ro.astl.paymentsws.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WltMngrEnv {
	
	public static final String PROPFILE = "paymentsws.properties";
	
	public static Properties getProperties() {
		Properties properties = new Properties();
		try(InputStream input = WltMngrEnv.class.getClassLoader().getResourceAsStream(PROPFILE)){
			if(input!=null) {
				properties.load(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String key : properties.stringPropertyNames()) {
			  String value = properties.getProperty(key);
			  System.out.println(key + " => " + value);
		}
		
		return properties;
	}

}
