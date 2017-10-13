package com.thalesgroup.rtt.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
public class ConnectorAddrManager {
	
	private static final String ENDPOINT_NODE_NAME = "hv-sub:notificationProducerEndpoint";
	
	private static final String PARENT_TYPE_CHECK_TAG = "xsi:type";
	
	private String connectorMappingXsiType;
	
	private final Map<String, String> idToAddrMap = new HashMap<String, String>();
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public ConnectorAddrManager(@Value("${connector.mapping.addr}") String xmlFile, @Value("${connector.mapping.xsi_type}") String connectorMappingXsiType ,ResourceLoader loader) {
		InputStream inStream = null;
		this.connectorMappingXsiType = connectorMappingXsiType;
		log.debug("Read connector address from [{}] with xsiType [{}]", xmlFile, connectorMappingXsiType);
		
		try {
			inStream = loader.getClassLoader().getResourceAsStream(xmlFile);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inStream);
			doc.getDocumentElement().normalize();
			NodeList nodelist = doc.getElementsByTagName(ENDPOINT_NODE_NAME);
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
		Element parentElement = (Element) node.getParentNode();
		if (parentElement.getAttribute(PARENT_TYPE_CHECK_TAG).equals(connectorMappingXsiType)) {
			String id = parentElement.getAttribute("id");
			String addr = ((Element) node).getTextContent();
			idToAddrMap.put(id, addr);
			log.trace("Add connector [{}] address [{}] to map", id, addr);
		}
	}
	
	public Set<Entry<String,String>> getAddrMappings() {
		return idToAddrMap.entrySet();
	}
}