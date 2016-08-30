package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class ReadConfigXML implements ReadConfigInterface {
	private final String className = "ReadConfigXML";
	private final String logPrefix = "["+className+"]";
	
//	@Override
//	public List<String> getTags(String path) {
//		final String function = "getTags";
//		List<String> configs = new ArrayList<String>();
//		try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
//            factory.setIgnoringComments (true);
//            factory.setIgnoringElementContentWhitespace (true);
//            factory.setValidating (false);
//            DocumentBuilder builder = factory.newDocumentBuilder ();
//            Document document = builder.parse (path);
//		    NodeList nodeList = document.getElementsByTagName("*");
//		    for (int i = 0; i < nodeList.getLength(); i++) {
//		        Node node = nodeList.item(i);
//		        if (node.getNodeType() == Node.ELEMENT_NODE) {
//		        			        	
//		            System.out.println(node.getNodeName());
//		            configs.add(node.getNodeName());
//		        }
//		    }
//		} catch (ParserConfigurationException e) {
//			System.out.println(logPrefix+function+" \nParserConfigurationException:" + e.toString());
//			e.printStackTrace();
//		} catch (SAXException | IOException e) {
//			System.out.println(logPrefix+function+" \nSAXException | IOException e" + e.toString());
//			e.printStackTrace();
//		}
//		return configs;
//	}
	@Override
	public List<Dictionary> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary> getDictionary(String path, String elm) {
		final String function = "getDictionary";
		System.out.println(logPrefix+function+" Begin");
		System.out.println(logPrefix+function+" Reading from the path["+path+"] elm["+elm+"]");
		
		List<Dictionary> dictionarys = new ArrayList<Dictionary>();

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
        				
//System.out.println(logPrefix+function+" getDictionary key[" + key + "] value[" + value + "]");
						config.setAttribute(key, value);
        			}
        			
	        		NodeList nodeList = cfgNode.getChildNodes();
	        		for (int y = 0; y < nodeList.getLength(); y++) {
	        			Node node = nodeList.item(y);
	        			if ( node.getNodeType() == Node.ELEMENT_NODE ) {
	        			
	        				String name = node.getNodeName();
	        				String content = node.getTextContent();

//System.out.println(logPrefix+function+" getDictionary name[" + name + "] content[" + content + "]");						

							config.addValue(name, content);
		                    dictionarys.add(config);
	        			}
	                }       			
        		}

        	}

		} catch (ParserConfigurationException e) {
			System.out.println(logPrefix+function+" getDictionary ParserConfigurationException:" + e.toString());
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			System.out.println(logPrefix+function+" getDictionary SAXException | IOException e" + e.toString());
			e.printStackTrace();
		}
		
		System.out.println(logPrefix+function+" End");
		
		return dictionarys;
	}

}
