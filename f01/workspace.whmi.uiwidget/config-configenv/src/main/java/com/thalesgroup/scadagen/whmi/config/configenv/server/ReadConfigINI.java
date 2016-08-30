package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class ReadConfigINI implements ReadConfigInterface {
	
	private final String className = "ReadConfigINI";
	private final String logPrefix = "["+className+"] ";

	@Override
	public List<Dictionary> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary> getDictionary(String path, String elm) {
		final String function = "dictionaryServer";
		
		System.out.println(logPrefix+function+" Begin");
		System.out.println(logPrefix+function+" Reading from the path["+path+"] elm["+elm+"]");
		
		List<Dictionary> dictionaries = new LinkedList<Dictionary>();
		Dictionary dictionary = new Dictionary();
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try  {
			input = new FileInputStream(path);
			prop.load(input);

			Set<String> keys = prop.stringPropertyNames();
			for ( String key : keys ) {
				String value = prop.getProperty(key);
				dictionary.addValue(key, value);
				
System.out.println(logPrefix+function+" getDictionary key[" + key + "] value[" + value + "]");
			}
			
		} catch ( IOException e) {
			e.printStackTrace();
		} finally {
			if ( input != null ) {
				try {
					input.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		
		dictionaries.add(dictionary);
		
		System.out.println(logPrefix+function+" End");
		
		return dictionaries;
	}

}
