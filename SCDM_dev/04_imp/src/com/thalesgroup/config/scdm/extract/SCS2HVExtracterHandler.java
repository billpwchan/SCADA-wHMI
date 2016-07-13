package com.thalesgroup.config.scdm.extract;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.thalesgroup.config.scdm.extract.SCS2HVMapSubSystemUtility.SCSSubSystem;
//import com.thalesgroup.config.scdm.extract.hypervisor.SysXmlExtractionHelper;
import com.thalesgroup.config.scdm.extract.hypervisor.HypervisorExtractorHelper;
import com.thalesis.config.storage.Resource;
import com.thalesis.config.utils.Logger;

public class SCS2HVExtracterHandler extends DefaultHandler {

	private String exportDirectory_;

	/** name of the attribute containing the SCADAsoft equipment type */
	public final static String ATT_SCSTYPE = "scstype";
	/** name of the attribute containing the SHV equipment type */
	/*  name coming from melodyadvance: hypervisor.data.stresstest.config.equipment.Door  */
	/*  name of java class: com.thalesgroup.hypervisor.data.stresstest.config.equipment.DoorStatusesType  */
	public final static String ATT_HVQNAME = "hvqname";
	//public final static String HVQNAME_PREFIX = "com.thalesgroup.";
	//public final static String HVQNAME_SUFFIX = "StatusesType";
	
	// system_configuration/instances
	private BufferedWriter instancesFile_ = null;

	private static class SCSMapping {
		SCSMapping(String scs, String hv ) {
			scsid_ = scs;
			hvid_ = hv;
		}

		public String hvid_;
		public String scsid_;
	}

	private Map<String, List<SCSMapping>> instancesMap_ = new HashMap<String, List<SCSMapping>>();

	private Map<Integer, Map<String, List<SCSMapping>>> instancesPerSubsystMap_ = new HashMap<Integer, Map<String, List<SCSMapping>>>();

	private SCS2HVMapSubSystemUtility subSystemUtility_;

	//private Map<String, String> hv2scstypeMap_= new HashMap<String, String>();
	private Map<String, String> scs2hvtypeMap_= new HashMap<String, String>();

	/**
	 * Constructeur
	 * @param exportDirectory
	 * @param resource 
	 * @param root 
	 */
	public SCS2HVExtracterHandler(String exportDirectory, String root, Resource resource) {
		super();
		exportDirectory_ = exportDirectory;
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

	public void startDocument() throws SAXException
	{
		instancesMap_.clear();

		if (subSystemUtility_ == null) {
			// create writers
			instancesFile_ = createFileWriter(exportDirectory_ + "/scs2hv.xml");
			// write headers
			writeInstHeader(instancesFile_, "SRV1", "SCADA1");
		} else {
			
			/*
			Generate default configuration for all subsystems
			*/
			// create writers
			instancesFile_ = createFileWriter(exportDirectory_ + "/scs2hv.xml");
			// write headers
			writeInstHeader(instancesFile_, "FULL_SCADA", "HVSYSCONF");
			/*
			End 
			*/
			
			subSystemUtility_.createSubsytemWriters(exportDirectory_);

			for (SCSSubSystem entry : subSystemUtility_.getSubsystems()) {
				writeInstHeader(entry.fileWrite_, entry.scsenvname_, entry.name_);
			}
		}
	}

	public void endDocument() throws SAXException
	{
		if (subSystemUtility_ == null) {
			// write instances
			writeInstances(instancesFile_, instancesMap_);
			// write footer
			writeInstFooter(instancesFile_);

			// close file
			try {
				if (instancesFile_ != null) {
					instancesFile_.close();
					instancesFile_ = null;
				}
			} catch (IOException ex) {
				Logger.USER.error("SCS2HVExtracterHandler endDocument : " + Logger.exStackTraceToString(ex) );
			}
		} else {
			
			/*
			Generate default configuration for all subsystems
			*/	
			// write instances
			writeInstances(instancesFile_, instancesMap_);
			// write footer
			writeInstFooter(instancesFile_);

			// close file
			try {
				if (instancesFile_ != null) {
					instancesFile_.close();
					instancesFile_ = null;
				}
			} catch (IOException ex) {
				Logger.USER.error("SCS2HVExtracterHandler endDocument : " + Logger.exStackTraceToString(ex) );
			}
			/*
			end
			*/
			
			for (SCSSubSystem entry : subSystemUtility_.getSubsystems()) {
				
				Map<String, List<SCSMapping>> instMap = instancesPerSubsystMap_.get(entry.id_);
				writeInstances(entry.fileWrite_, instMap);
				writeInstFooter(entry.fileWrite_);
			}
			subSystemUtility_.closeAllFile();
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		String id = atts.getValue("ID");
		
		int idx = atts.getIndex("scsid");	/* for Hypervisor CDV */
		int idxhv = atts.getIndex("hvid");  /* for SCADA CDV */
		
		if (id != null && idx != -1 && idxhv != -1 ) {
		
			String hvid = atts.getValue("hvid"); /* for Hypervisor CDV */
			if (hvid.trim() == "") {
				hvid = atts.getValue("nom_instance");
			}

			// check if userid exists and is not empty
			String userid = atts.getValue(idx);
			if (userid.length() > 0) {
				String hvTypeName = "";
				int tindex = atts.getIndex(ATT_HVQNAME);
				if (tindex != -1) {
					//hvTypeName = HVQNAME_PREFIX + atts.getValue(tindex) + HVQNAME_SUFFIX;
					hvTypeName = atts.getValue(tindex);
				}
				
				//replace "<" and ">"
				if (userid.contains("<")) {
					userid = userid.replace("<", "&lt;");
				}
				if (userid.contains(">")) {
					userid = userid.replace(">", "&gt;");
				}
				
				// get SCADAsoft type to update map
				String scsTypeName = "";
				tindex = atts.getIndex(ATT_SCSTYPE);
				if (tindex != -1) {
					scsTypeName = atts.getValue(tindex);
				}
				
				// create bind name
				String bindTypeName = scsTypeName + "_" + hvTypeName.substring(hvTypeName.lastIndexOf('.') + 1);

				if (hvTypeName.length() > 0 && scsTypeName.length() > 0) {
					//String curscstype = hv2scstypeMap_.get(hvTypeName);
					String curscstype = scs2hvtypeMap_.get(bindTypeName);
					if (curscstype != null && !curscstype.equals(bindTypeName)) {						
						Logger.USER.warn("SCS2HVExtracterHandler: replace type mapping for " + bindTypeName + " from " + curscstype + " to " + hvTypeName);
					}
					scs2hvtypeMap_.put(bindTypeName,hvTypeName);
				}

				
				if (subSystemUtility_ != null) {
					// we have subsystem check mask
					String subsystemMask = atts.getValue(HypervisorExtractorHelper.SCS_SUBSYSTEM_ID_ATTR_NAME);

					if (subsystemMask != null) {
						int maskInt = 0;
						if (!"".equals(subsystemMask)) {
							maskInt = Integer.parseInt(subsystemMask,2); // parse base 2 (0010 gives 2)
						}
						if (maskInt > 0 ) {
							for (SCSSubSystem subSys : subSystemUtility_.getSubsystems()) {
								int ssIdvalue = subSys.id_;
								// check if the mask contains the subsystem id
								int ssIdsubMask = 1 << ssIdvalue; // bit offset
								if ((ssIdsubMask & maskInt) != 0 ) { 									
									addMapping(ssIdvalue, bindTypeName, new SCSMapping(userid, hvid)); /* for Hypervior CDV*/
								}
							}
						}
					}
			
					/*
					Generate default configuration for all subsystems
					*/					
					if (instancesMap_.get(bindTypeName) == null) {
						instancesMap_.put(bindTypeName, new ArrayList<SCSMapping>());
					}					
					instancesMap_.get(bindTypeName).add(new SCSMapping(userid, hvid));  /* for Hypervior CDV*/
					/*
					end
					*/
					
				} else {
					// subsystem are not used
					if (instancesMap_.get(bindTypeName) == null) {
						instancesMap_.put(bindTypeName, new ArrayList<SCSMapping>());
					}					
					instancesMap_.get(bindTypeName).add(new SCSMapping(userid, hvid));  /* for Hypervior CDV*/
				}
			}
		}

	}

	private void addMapping(int ssIdvalue, String typeName,	SCSMapping scsMapping)
	{
		Map<String, List<SCSMapping>> instMap = instancesPerSubsystMap_.get(ssIdvalue);
		if (instMap == null) {
			instMap = new HashMap<String, List<SCSMapping>>();
			instancesPerSubsystMap_.put(ssIdvalue, instMap);
		}
		
		if (instMap.get(typeName) == null) {
			instMap.put(typeName, new ArrayList<SCSMapping>());
		}
		instMap.get(typeName).add(scsMapping);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{

	}

	private void writeInstHeader(BufferedWriter writer, String scsenv, String creatorId) {
		if (writer == null) {
			return;
		}

		try {
			writer.write("<?xml version='1.0' encoding='UTF-8'?>");
			writer.newLine();
			writer.write("<scsbamapping scsenv='" + scsenv + "' hvcreatorid='" + creatorId + "'>");
			writer.newLine();
			writer.newLine();

		} catch (IOException ex) {
			Logger.USER.error("SCS2HVExtracterHandler : cannot write Instances Header" + Logger.exStackTraceToString(ex) );
		}
	}

	private void writeInstances(BufferedWriter writer, Map<String, List<SCSMapping>> instances) {
		if (writer == null || instances == null) {
			return;
		}

		try {
			// write instance
			
			List<String> l = new ArrayList<String>(instances.keySet());
            Collections.sort(l);	//sort by scs, i.e. eqGX_; ORG: sort by hvqname, i.e. prj_gz_cocc.data.mcs.configuration.equipment.afc.Gate
            
			for(String entry : l) {
				
				if (entry.length() > 0) {
					//e.g. entry = "eqGX_"
					String hv = scs2hvtypeMap_.get(entry);
					hv = hv.substring(hv.lastIndexOf('.') + 1);
					String bindtype = entry;
					String scstype = bindtype.replace('_' + hv ,"");
					
					writer.write("  <classdesc hv='" + bindtype + "' scs='" + scstype + "'>");					
					
					writer.newLine();
				}

				for (SCSMapping item : instances.get(entry)) {
					writer.write("    <instance hv='" + item.hvid_ + "' scs='" + item.scsid_ + "'/>");
					writer.newLine();
				}

				if (entry.length() > 0) {
					writer.write("  </classdesc>");
					writer.newLine();
				}
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeInstFooter(BufferedWriter writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.newLine();
			writer.write("</scsbamapping>");
			writer.newLine();
			writer.newLine();
		} catch (IOException ex) {
			Logger.USER.error("SCS2HVExtracterHandler : cannot write Instances Footer" + Logger.exStackTraceToString(ex) );
		}
	}

	public void setSubsytemMap(SCS2HVMapSubSystemUtility subSystemUtility) {
		subSystemUtility_ = subSystemUtility;
	}

}
