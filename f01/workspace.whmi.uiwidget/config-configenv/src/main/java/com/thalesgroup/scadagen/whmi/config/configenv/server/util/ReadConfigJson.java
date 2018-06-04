package com.thalesgroup.scadagen.whmi.config.configenv.server.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class ReadConfigJson implements ReadConfigInterface {
	
	private UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	@Override
	public List<Dictionary_i> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary_i> getDictionary(String path, String elm) {
		
		logger.debug("getDictionary Begin");
		logger.debug("getDictionary Reading from the path[{}] elm[{}]", path, elm);
		
		List<Dictionary_i> dictionaries = new LinkedList<Dictionary_i>();
		
		String data = null;
		
//		Scanner scanner = null;
//		try  {
//			scanner = new Scanner(new File(path));
//			data = scanner.useDelimiter("\\Z").next();
//		} catch ( FileNotFoundException e) {
//			logger.error("Reading from the path[{}] elm[{}] IOException[{}]", new Object[]{path, elm, e.toString()});
//		} finally {
//			if ( scanner != null ) {
//				scanner.close();
//			}
//		}
//logger.debug("Reading from the path[{}] elm[{}] data[{}]", new Object[]{path, elm, data});
		
		try {
			data = readFile(path, Charset.defaultCharset());
		} catch (IOException e) {
			logger.warn("getDictionary IOException[{}]", e.toString());
		}
logger.debug("getDictionary Reading from the path[{}] elm[{}] data[{}]", new Object[]{path, elm, data});
		
		Dictionary_i dictionary = new Dictionary();
		dictionary.setData(data);
		
		dictionaries.add(dictionary);
		
		logger.debug("getDictionary End");
		
		return dictionaries;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte [] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);	
	}

}
