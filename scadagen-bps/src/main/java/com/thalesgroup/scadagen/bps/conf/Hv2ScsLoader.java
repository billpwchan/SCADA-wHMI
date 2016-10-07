package com.thalesgroup.scadagen.bps.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Hv2ScsLoader {
	
	/** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Hv2ScsLoader.class);

	private String hv2scsFile_;
	
	private Map<String, String> hv2scsInstancesMap_ = new HashMap<String, String>();
	
	private Map<String, List<String>> scs2hvInstancesMap_ = new HashMap<String, List<String>>();
	
	private Map<String, String> scsEqpOfHVInstancesMap_ = new HashMap<String, String>();
	
    /**
     * Reads the content of a configuration node.
     * 
     * @param conf
     *            A configuration node.
     * @throws ScsConfigurationException.
     */
    static final private String CONFIGURATION_NODE_NAME = "scsbamapping";
    static final private String CLASS_NODE_NAME = "classdesc";
    static final private String CDESCID_ATTRIBUTE = "cid";
    static final private String HVID_ATTRIBUTE = "hv";
    static final private String SCSID_ATTRIBUTE = "scs";
    static final private String INST_NODE_NAME = "instance";

	
	public Hv2ScsLoader(String hv2scsFile) {
		hv2scsFile_ = hv2scsFile;
	}
	
	public void loadConfiguration() {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(hv2scsFile_);
	    if (stream != null) {
	        try {
	            LOGGER.info("Loading configuration from {}", hv2scsFile_);
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            try {
	                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	                Document doc = dBuilder.parse(stream);
	                doc.getDocumentElement().normalize();
	                NodeList nodelist = doc.getElementsByTagName(CONFIGURATION_NODE_NAME);
	                if (nodelist != null && nodelist.getLength() > 0) {
	                    Node conf = nodelist.item(0);
	                    readConfigurationNode(conf);
	                }
	            } catch (ParserConfigurationException e) {
	                LOGGER.error("FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
	            } catch (SAXException e) {
	                LOGGER.error("FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
	            } catch (IOException e) {
	                LOGGER.error("FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
	            } catch (Exception e) {
	                LOGGER.error("FATAL ERROR while loading HV2SCS mapping.  : {} ", e);
	            }
	        } finally {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                LOGGER.warn("FATAL ERROR while closing HV2SCS file mapping.  : {} ", e);
	            }
	        }
	    } else {
	        LOGGER.error("Cannot find configuration file {}", hv2scsFile_);
	    }
	}
	
	private void readConfigurationNode(Node conf) throws Exception {
        // get all equipment type
        for (Node child = conf.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (CLASS_NODE_NAME.equals(child.getNodeName()) && child.hasAttributes()) {
                readClassNode(child);
            } else if (INST_NODE_NAME.equals(child.getNodeName()) && child.hasAttributes()) {
                readInstanceNode(child, null);
            }
        }
    }
	
	/*
     * Read classdesc node <classdesc cid="Building"> .... </classdesc>
     * 
     */
    private void readClassNode(Node conf) throws Exception {
        Node cdesc_idnode = conf.getAttributes().getNamedItem(CDESCID_ATTRIBUTE);

        // for backward compatibility look for hv
        if (cdesc_idnode == null) {
            cdesc_idnode = conf.getAttributes().getNamedItem(HVID_ATTRIBUTE);
        }

        if (cdesc_idnode != null) {
            String cdescID = cdesc_idnode.getNodeValue();

            for (Node child = conf.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.hasAttributes()) {
                    Node hvidnode = child.getAttributes().getNamedItem(HVID_ATTRIBUTE);
                    Node scsidnode = child.getAttributes().getNamedItem(SCSID_ATTRIBUTE);

                    if (scsidnode != null && hvidnode != null && INST_NODE_NAME.equals(child.getNodeName())) {
                        // <instance hv='B001' scs=':SITE1:B001'/>
                        readInstanceNode(child, cdescID);
                    }
                }
            }
        }
    }
	
	private void readInstanceNode(Node conf, String cdescId) {
        Node hvidnode = conf.getAttributes().getNamedItem(HVID_ATTRIBUTE);
        Node scsidnode = conf.getAttributes().getNamedItem(SCSID_ATTRIBUTE);

        if (hvidnode != null && scsidnode != null) {
            String hvname = hvidnode.getNodeValue();
            String pv = hv2scsInstancesMap_.get(hvname);
            if (pv != null) {
                LOGGER.warn("HV2SCS instances map: overwriting hvid {} with {} previous value was {}",
                        new Object[] { hvname, scsidnode.getNodeValue(), pv });
            }
            hv2scsInstancesMap_.put(hvname, scsidnode.getNodeValue());
            List<String> hvList = scs2hvInstancesMap_.get(scsidnode.getNodeValue());
            if (hvList == null) {
            	hvList = new ArrayList<String>();         	
            }
            hvList.add(hvname);
            scs2hvInstancesMap_.put(scsidnode.getNodeValue(), hvList);
            if (cdescId != null) {
                scsEqpOfHVInstancesMap_.put(hvname, cdescId);
            }
        }
    }
	
	public Map<String, String> getScsEqpOfHVInstancesMap() {
		return scsEqpOfHVInstancesMap_;
	}
	
	public Map<String, String> getHv2ScsInstancesMap() {
		return hv2scsInstancesMap_;
	}
	
	public Map<String, List<String>> getScs2HvInstancesMap() {
		return scs2hvInstancesMap_;
	}
}
