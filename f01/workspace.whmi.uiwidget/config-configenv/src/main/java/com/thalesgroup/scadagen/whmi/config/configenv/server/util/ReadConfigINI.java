package com.thalesgroup.scadagen.whmi.config.configenv.server.util;

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
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

public class ReadConfigINI implements ReadConfigInterface {
	
	private Logger logger					= LoggerFactory.getLogger(ReadConfigINI.class.getName());
	
	@Override
	public List<Dictionary_i> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary_i> getDictionary(String path, String elm) {
		
		logger.debug("Begin");
		logger.debug("Reading from the path[{}] elm[{}]", path, elm);
		
		List<Dictionary_i> dictionaries = new LinkedList<Dictionary_i>();
		Dictionary_i dictionary = new Dictionary();
		
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
					logger.warn("getDictionary IOException[{}]", e.toString());
				}
			}
		}
		
		dictionaries.add(dictionary);
		
		logger.debug("End");
		
		return dictionaries;
	}

}
