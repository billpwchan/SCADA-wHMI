package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

public class ReadConfigJson implements ReadConfigInterface {
	
	private Logger logger					= LoggerFactory.getLogger(ReadConfigJson.class.getSimpleName());
	
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
		
		Scanner scanner = null;
		String data = null;
		try  {
			scanner = new Scanner(new File(path));
			data = scanner.useDelimiter("\\Z").next();
		} catch ( FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if ( scanner != null ) {
				scanner.close();
			}
		}
logger.debug("Reading from the path[{}] elm[{}] data[{}]", new Object[]{path, elm, data});
		
		try {
			data = readFile(path, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
logger.debug("Reading from the path[{}] elm[{}] data[{}]", new Object[]{path, elm, data});
		
		dictionary.setData(data);
		
		dictionaries.add(dictionary);
		
		logger.debug("End");
		
		return dictionaries;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte [] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);	
	}

}
