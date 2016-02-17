package com.thalesgroup.scadagen.whmi.config.confignav.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;

public class ReadConfigXML implements ReadConfigInterface {
	
	@Override
	public ArrayList<String> getTags(String path) {
		ArrayList<String> configs = new ArrayList<String>();
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            factory.setIgnoringComments (true);
            factory.setIgnoringElementContentWhitespace (true);
            factory.setValidating (false);
            DocumentBuilder builder = factory.newDocumentBuilder ();
            Document document = builder.parse (path);
		    NodeList nodeList = document.getElementsByTagName("*");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        			        	
		            System.out.println(node.getNodeName());
		            configs.add(node.getNodeName());
		        }
		    }
		} catch (ParserConfigurationException e) {
System.out.println("\nParserConfigurationException:" + e.toString());
			e.printStackTrace();
		} catch (SAXException | IOException e) {
System.out.println("\nSAXException | IOException e" + e.toString());
			e.printStackTrace();
		}
		return configs;
	}
	
	private ArrayList<Dictionary> getXMLDictionary(String path, String elm) {
System.out.println("getXMLDictionary Reading from the path["+path+"] elm["+elm+"]");
		
		ArrayList<Dictionary> dictionarys = new ArrayList<Dictionary>();

		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            factory.setIgnoringComments (true);
            factory.setIgnoringElementContentWhitespace (true);
            factory.setValidating (false);
            DocumentBuilder builder = factory.newDocumentBuilder ();
            Document document = builder.parse (path);
            
            document.getDocumentElement().normalize();
            
            NodeList cfgList = document.getElementsByTagName(elm);
            
        	for (int temp = 0; temp < cfgList.getLength(); temp++) {

        		Node cfgNode = cfgList.item(temp);
        		if (cfgNode.getNodeType() == Node.ELEMENT_NODE) {
        			
        			Dictionary config = new Dictionary();
        			
        			Element eElement = (Element) cfgNode;
        			
        			NamedNodeMap eElementAttr = eElement.getAttributes();
        			for (int i = 0; i < eElementAttr.getLength(); ++i) {
        			    Node attr = eElementAttr.item(i);
        				String key = attr.getNodeName();
        				String value = attr.getNodeValue();
System.out.println("getXMLDictionary key[" + key + "] value[" + value + "]");
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
	        			
System.out.println("getXMLDictionary name[" + name + "] content[" + content + "]");						
//		                    config.getHashMap().put(name, content);
							config.setValue(name, content);
		                    dictionarys.add(config);
	        			}
	                }       			
        		}

        	}

		} catch (ParserConfigurationException e) {
System.out.println("\nParserConfigurationException:" + e.toString());
			e.printStackTrace();
		} catch (SAXException | IOException e) {
System.out.println("\nSAXException | IOException e" + e.toString());
			e.printStackTrace();
		}
		
		return dictionarys;
	}
	
	@Override
	public ArrayList<Task> getTasks(String mappingPath, String settingPath, String tag) {
		
System.out.println("getTasks Reading from the mappingPath["+mappingPath+"] settingPath["+settingPath+"] tag["+tag+"]");

		ArrayList<Task> tasks = new ArrayList<Task>();

		ArrayList<Dictionary> mappings = getXMLDictionary(mappingPath, "option");
		ArrayList<Dictionary> settings = getXMLDictionary(settingPath, "option");
		
		String strKey = "key";
		String strSetting = "setting";
		
		String strType = "type"
				, strOrder = "order"
				, strName = "name"
				, strTitle = "title"
				, strEnable = "enable"
				, strVisible = "visible"
				, strLocCat = "locCat"
				, strFunCat = "funCat"
				, strUiPanel = "uiPanel"
				, strUiScreen = "uiScreen"
				, strUiPath = "uiPath";
		
		String names [] = new String[] {
				strType, strOrder, strName, strTitle, strEnable, strVisible, strLocCat, strFunCat, strUiPanel, strUiScreen, strUiPath
		};
		
		HashMap<String, Dictionary> settingMap = new HashMap<String, Dictionary>();
		for ( int i = 0 ; i < settings.size() ; ++i ) {
			Dictionary dictionary = settings.get(i);
			String key = (String) dictionary.getAttribute(strKey);
System.out.println("getTasks Reading from the key["+key+"] dictionary["+dictionary+"]");
			settingMap.put(key, dictionary);
		}
		
		HashMap<String, String> mappingMap = new HashMap<String, String>();
		for ( int i = 0 ; i < mappings.size() ; ++i) {
			Dictionary dictionaryMapping = mappings.get(i);
			String key = (String) dictionaryMapping.getAttribute(strKey);
			String setting = (String) dictionaryMapping.getValue(strSetting);
			
System.out.println("getTasks Reading from the key["+key+"] setting["+setting+"]");
			
			mappingMap.put(key, setting);
			
			Dictionary dictionarySetting = settingMap.get(setting);
			
			if ( dictionarySetting != null ) {
			
System.out.println("getTasks dictionarySetting.getAttibute("+strKey+")["+dictionarySetting.getAttribute(strKey)+"]");

				HashMap<String, String> values = new HashMap<String, String>();
				values.put(strKey, key);
				
				if ( null == values.get(strKey) || values.get(strKey).length() == 0 ) continue;
				
				for ( String n: names) {
					String v = (String) dictionarySetting.getValue(n);
					values.put(n, (null!=v?v:""));
				}

				Task task = new Task();
				task.set(values.get(strKey)
						, values.get(strType), values.get(strOrder), values.get(strName)
						, values.get(strTitle), values.get(strEnable), values.get(strVisible)
						, values.get(strLocCat), values.get(strFunCat), values.get(strUiPanel)
						, values.get(strUiScreen), values.get(strUiPath));
				tasks.add(task);
				
System.out.println("getTasks task.getHeader()["+task.getHeader()+"]");
System.out.println("getTasks task.getType()["+task.getType()+"]");
System.out.println("getTasks task.getUiPanel()["+task.getUiPanel()+"]");
System.out.println("getTasks task.getUiPath()["+task.getUiPath()+"]");
				
			} else {
System.out.println("getTasks setting["+setting+"] is null");
			}
			
		}
		
		return tasks;
	}
}
