package com.thalesgroup.scadagen.whmi.config.confignav.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task_i.TaskAttribute;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class ReadConfigXML implements ReadConfigInterface {
	
	private UILogger_i logger					= UILoggerFactory.getInstance().get(this.getClass().getName());

	private ArrayList<Dictionary_i> getXMLDictionary(String path, String elm) {
//logger.debug("Reading from the path[{}] elm[{}]", path, elm);
		
		ArrayList<Dictionary_i> dictionarys = new ArrayList<Dictionary_i>();

		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            factory.setIgnoringComments (true);
            factory.setIgnoringElementContentWhitespace (true);
            factory.setValidating (false);
            DocumentBuilder builder = factory.newDocumentBuilder ();
            
            // Pass File object instead of URI to DocumentBuilder to handle "##" in path
            File f = new File(path);

            Document document = builder.parse (f);
            
            document.getDocumentElement().normalize();
            
            NodeList cfgList = document.getElementsByTagName(elm);
            
        	for (int temp = 0; temp < cfgList.getLength(); temp++) {

        		Node cfgNode = cfgList.item(temp);
        		if (cfgNode.getNodeType() == Node.ELEMENT_NODE) {
        			
        			Dictionary_i config = new Dictionary();
        			
        			Element eElement = (Element) cfgNode;
        			
        			NamedNodeMap eElementAttr = eElement.getAttributes();
        			for (int i = 0; i < eElementAttr.getLength(); ++i) {
        			    Node attr = eElementAttr.item(i);
        				String key = attr.getNodeName();
        				String value = attr.getNodeValue();
//logger.debug("key[{}] value[{}]", key, value);
						config.setAttribute(key, value);
        			}
        			
	        		NodeList nodeList = cfgNode.getChildNodes();
	        		for (int y = 0; y < nodeList.getLength(); y++) {
	        			Node node = nodeList.item(y);
	        			if ( node.getNodeType() == Node.ELEMENT_NODE ) {
	        			
//	        				int type = node.getNodeType();
	        				String name = node.getNodeName();
//	        				String value = node.getNodeValue();
	        				String content = node.getTextContent();
	        			
//logger.debug("name[{}] content[{}]", name, content);						
//		                    config.getHashMap().put(name, content);
							config.addValue(name, content);
		                    dictionarys.add(config);
	        			}
	                }       			
        		}

        	}

		} catch (ParserConfigurationException e) {
			logger.warn("\nParserConfigurationException: {}", e.toString());
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			logger.warn("\nSAXException | IOException: {}", e.toString());
			e.printStackTrace();
		} catch (NullPointerException e) {
			logger.warn("\nNullPointerException: {}", e.toString());
			e.printStackTrace();
		}
		
		return dictionarys;
	}
	
	@Override
	public ArrayList<Task> getTasks(String mappingPath, String settingPath, String tag) {
		
//logger.debug("Reading from the mappingPath[{}] settingPath[{}] tag[{}]", new Object[]{mappingPath, settingPath, tag});

		ArrayList<Task> tasks = new ArrayList<Task>();

		ArrayList<Dictionary_i> mappings = getXMLDictionary(mappingPath, "option");
		ArrayList<Dictionary_i> settings = getXMLDictionary(settingPath, "option");
		
		String strKey = "key";
		String strSetting = "setting";

		Map<String, Dictionary_i> settingMap = new HashMap<String, Dictionary_i>();
		for ( int i = 0 ; i < settings.size() ; ++i ) {
			Dictionary_i dictionary = settings.get(i);
			String key = (String) dictionary.getAttribute(strKey);
//logger.debug("Reading from the key[{}] dictionary[{}]", key, dictionary);
			settingMap.put(key, dictionary);
		}
		
		for ( int i = 0 ; i < mappings.size() ; ++i) {
			Dictionary_i dictionaryMapping = mappings.get(i);
			String key = (String) dictionaryMapping.getAttribute(strKey);
			String setting = (String) dictionaryMapping.getValue(strSetting);
			
//logger.debug("Reading from the key[{}] setting[{}]", key, setting);

			Dictionary_i dictionarySetting = settingMap.get(setting);
			
			if ( dictionarySetting != null ) {
			
//logger.debug("dictionarySetting.getAttibute({})[{}]", strKey, dictionarySetting.getAttribute(strKey));
				if ( null == key || key.length() == 0 ) continue;
				
				Task task = new Task();
				for ( TaskAttribute t : TaskAttribute.values() ) {
					String n = t.toString();
					String v = (String) dictionarySetting.getValue(n);
					task.setParameter(n, (null!=v?v:""));
				}
				task.setParameter(TaskAttribute.Key.toString(), key);
				tasks.add(task);

			} else {
				logger.warn("getTasks setting[{}] is null", setting);
			}
			
		}
		
		return tasks;
	}
}
