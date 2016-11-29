package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public class ReadConfigXML implements ReadConfigInterface {
	
	private Logger logger					= LoggerFactory.getLogger(ReadConfigXML.class.getName());
	
	@Override
	public List<Dictionary> getDictionary(String path) {
		return getDictionary( path, null);
	}
	@Override
	public List<Dictionary> getDictionary(String path, String elm) {
		logger.debug("Begin");
		logger.debug("Reading from the path[{}] elm[{}]", path, elm);
		
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
        				
//logger.debug("getDictionary key[" + key + "] value[" + value + "]");
						config.setAttribute(key, value);
        			}
        			
	        		NodeList nodeList = cfgNode.getChildNodes();
	        		for (int y = 0; y < nodeList.getLength(); y++) {
	        			Node node = nodeList.item(y);
	        			if ( node.getNodeType() == Node.ELEMENT_NODE ) {
	        			
	        				String name = node.getNodeName();
	        				String content = node.getTextContent();

//logger.debug("getDictionary name[" + name + "] content[" + content + "]");						

							config.addValue(name, content);
		                    dictionarys.add(config);
	        			}
	                }       			
        		}

        	}

		} catch (ParserConfigurationException e) {
			logger.debug("getDictionary ParserConfigurationException:" + e.toString());
			e.printStackTrace();
		} catch (SAXException | IOException e) {
			logger.debug("getDictionary SAXException | IOException e" + e.toString());
			e.printStackTrace();
		}
		
		logger.debug("End");
		
		return dictionarys;
	}

}
