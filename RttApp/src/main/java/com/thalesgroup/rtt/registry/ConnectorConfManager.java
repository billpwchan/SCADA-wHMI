package com.thalesgroup.rtt.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@Component
public class ConnectorConfManager {
	
	private static final String VALUE_NODE_NAME = "p:value";
	
	private final Map<String, String> idToEnvMap = new HashMap<String, String>();
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public ConnectorConfManager(@Value("${connector.mapping.conf}") String xmlFile, ResourceLoader loader) {
		InputStream inStream = null;
		try {
			inStream = loader.getClassLoader().getResourceAsStream(xmlFile);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inStream);
			doc.getDocumentElement().normalize();
			NodeList nodelist = doc.getElementsByTagName(VALUE_NODE_NAME);
			for (int temp = 0; temp < nodelist.getLength(); temp++) {
				Node nNode = nodelist.item(temp);
			    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			    	addToMap(nNode);
			    }
			}
			inStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ParserConfigurationException e) {
			log.error(e.getMessage());
		} catch (SAXException e) {
			log.error(e.getMessage());
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		
	}
	
	private void addToMap(Node node) {
		String env = ((Element) node.getParentNode()).getAttribute("key");
		String hvId = ((Element) node).getTextContent();
		idToEnvMap.put(hvId, env);
	}
	
	public String getSubsystem(String hvId) {
		return idToEnvMap.get(hvId);
	}

}