package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class ReadConfigINI implements ReadConfigInterface {
	
	private Logger logger					= LoggerFactory.getLogger(ReadConfigINI.class.getName());
	
	@Override
	public List<Dictionary> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary> getDictionary(String path, String elm) {
		
		logger.debug("Begin");
		logger.debug("Reading from the path[{}] elm[{}]", path, elm);
		
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
				
				logger.debug("getDictionary key[{}] value[{}]", key, value);
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
		
		logger.debug("End");
		
		return dictionaries;
	}

}
