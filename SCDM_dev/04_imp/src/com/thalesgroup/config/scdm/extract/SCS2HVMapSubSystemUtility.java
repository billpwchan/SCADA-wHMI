package com.thalesgroup.config.scdm.extract;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import com.thalesis.config.exception.TechnicalException;
import com.thalesis.config.exchange.extract.ExtractKeys;
import com.thalesis.config.storage.Resource;
import com.thalesis.config.storage.query.QueryIterator;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;

/******************************************************************************/
/*  FILE  : SubsytemUtility.java                                              */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 12 oct. 07                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright � THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/**
 * utility for subsystem management.
 * Load in a structure and calculate mask from each instance that inherits CommonSubsystem
 */
public class SCS2HVMapSubSystemUtility {
	
	/** name of the class corresponding to a SCADAsoft subsystem */
	public final static String CLASS_SCS_SUBSYSTEM = "SCADASubsystem";
	/** name of the attribute containing the SCADAsoft logical name */
	public final static String ATT_SCS_ENVNAME = "scsenvname";
	/** name of generated file  */
	public final static String HV2SCS_FILENAME = "scs2hv.xml";

	/** map SubSystem name to ID */
	private Map<Integer, SCSSubSystem> subsystMap_ = new LinkedHashMap<Integer, SCSSubSystem>();

	public static class SCSSubSystem {
		public SCSSubSystem(int id, String name, String scsenv) {
			id_ = id;
			scsenvname_ = scsenv;
			name_ = name;
		}
		
		public int id_;
		public String scsenvname_;
		public String name_;
		public BufferedWriter fileWrite_ = null;
	}
	
	public SCS2HVMapSubSystemUtility(Resource resource) throws Exception {
		QueryIterator it = null;
		// 
		String xpathElementFilter = "//" + CLASS_SCS_SUBSYSTEM;
		
	    it = new QueryIterator(xpathElementFilter, resource);
	    // no filter used with the query iterator
	    it.setNodeFilter(null);
	    buildMap(it);
	}
	
	public void buildMap(QueryIterator it) throws TechnicalException {
	    SAXElement element = null;
	    while((element = it.next()) != null) {
	    	String id   = element.getAttribute(ExtractKeys.ATT_SUBSYSTEM_ID);
	    	String name = element.getAttribute(ExtractKeys.ATT_INSTANCE_NAME);
	    	String envname = element.getAttribute(ATT_SCS_ENVNAME);
	    	try {
	    		Integer i = Integer.parseInt(id);
	    		subsystMap_.put(i, new SCSSubSystem(i, name, envname));
	    	} catch (NumberFormatException e) {
	    		// nothing to do
	    		Logger.EXCHANGE.warn("cannot convert '" + id + "' to number", e);
	    	}
	    }		
	}
	
	public void createSubsytemWriters(String outputPath) {
		// set up a writer for each subsystem
	    for (SCSSubSystem entry : subsystMap_.values()) {
	    	String name = entry.name_;

	    	entry.fileWrite_ = createFileWriter(outputPath + File.separator + name + File.separator + "scs2hv.xml");
		}
	}
	
	private BufferedWriter createFileWriter(String filename) {
		BufferedWriter bwriter = null;

		// create File and folder if necessary
		File fo = new File(filename);
		if (!fo.getParentFile().exists()) {
			Logger.EXCHANGE.info("SCS2HVExtracterHandler create folder " + fo.getParent());
			if (!fo.getParentFile().mkdirs()) {
				Logger.EXCHANGE.error("SCS2HVExtracterHandler cannot create folder " + fo.getParent());
				return null;
			}
		}

		// create writer in utf8
		try {
			FileOutputStream fout = new FileOutputStream(fo);
			OutputStreamWriter outstream = new OutputStreamWriter(fout, Charset.forName("UTF-8"));

			bwriter = new BufferedWriter(outstream);
		} catch (FileNotFoundException ex) {
			Logger.USER.error("SCS2HVExtracterHandler createFileWriter : " + Logger.exStackTraceToString(ex) );
		}

		return bwriter;
	}
	
	public void closeAllFile() {
	    for (SCSSubSystem entry : subsystMap_.values()) {	
	    	if (entry.fileWrite_ != null) {
	    		try {
					entry.fileWrite_.close();
				} catch (IOException e) {
				}
	    	}
		}
	}

	public Collection<SCSSubSystem> getSubsystems() {
		return subsystMap_.values();
	}
	
}
