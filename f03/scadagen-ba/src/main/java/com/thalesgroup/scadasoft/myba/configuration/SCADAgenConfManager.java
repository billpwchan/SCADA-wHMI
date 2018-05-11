package com.thalesgroup.scadasoft.myba.configuration;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SCADAgenConfManager {
	private static final Logger s_logger = LoggerFactory.getLogger(SCADAgenConfManager.class);
	private static final String LOG_PREFIX = "[SCADAgenConfManager] ";
	private static volatile SCADAgenConfManager s_instance;
	private static final String CONFIGURATION_NODE_NAME = "scsbamapping";
	private static final String ALMLISTFILTER_ATTRIBUTE = "scsalmlistfilter";

	private String m_scsalmlistfilter = "";

	public static SCADAgenConfManager instance() {
		if (s_instance == null) {
			synchronized (com.thalesgroup.scadasoft.services.proxy.ScsConnectorProxy.class) {
				if (s_instance == null) {
					s_instance = new SCADAgenConfManager();
				}
			}
		}
		return s_instance;
	}
	
	public String getScsAlmListFilter() {
		return m_scsalmlistfilter;
	}
	
	private void resetConf() {
		m_scsalmlistfilter = "";
	}
	
	public void loadConfiguration() {
		resetConf();
		
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("scs2hv_model.xml");
		if (stream != null) {
			try {
				s_logger.info(LOG_PREFIX + "Loading configuration from scs2hv_model.xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				
				try {
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(stream);
					doc.getDocumentElement().normalize();
					NodeList nodelist = doc.getElementsByTagName(CONFIGURATION_NODE_NAME);
					if ((nodelist != null) && (nodelist.getLength() > 0)) {
						Node conf = nodelist.item(0);
						readConfigurationNode(conf);
					}
				} catch (ParserConfigurationException e) {
					s_logger.error(LOG_PREFIX + "FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
				} catch (SAXException e) {
					s_logger.error(LOG_PREFIX + "FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
				} catch (IOException e) {
					s_logger.error(LOG_PREFIX + "FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
				} catch (Exception e) {
					s_logger.error(LOG_PREFIX + "FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
				}
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					s_logger.warn(LOG_PREFIX + "FATAL ERROR while closing HV2SCS file mapping.  : {} ", e);
				}
			}
		}
	}
	
	private void readConfigurationNode(Node conf) throws Exception {
		if (conf.hasAttributes()) {
			Node node = conf.getAttributes().getNamedItem(ALMLISTFILTER_ATTRIBUTE);
			if (node != null) {
				m_scsalmlistfilter = node.getNodeValue();
				s_logger.debug(LOG_PREFIX + "scsalmlistfilter = " + m_scsalmlistfilter);
			} else {
				s_logger.debug(LOG_PREFIX + "scsalmlistfilter not found in configuration. scsalmlistfilter is set to empty string");
			}
		}
	}
}
