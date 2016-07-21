/**
 * 
 */
package com.thalesgroup.config.scdm.extract.hypervisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.thalesis.config.business.datamodel.AttributeDefinition;
import com.thalesis.config.business.datamodel.CompositionDefinition;
import com.thalesis.config.business.datamodel.DataModel;
import com.thalesis.config.business.datamodel.ObjectDefinition;
import com.thalesis.config.exception.PropertyNotFoundException;
import com.thalesis.config.exchange.PrototypeMap;
import com.thalesis.config.storage.Resource;
import com.thalesis.config.utils.CommonConst;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.PropertyManager;

/**
 * @author T0126039
 * <p>This Extractor is responsible for extracting the Hypervisor configuration in the SysXml format</p>
 */
public class HypervisorExtractor extends DefaultHandler {

	private static final String DESCRIPTION = "This extractor output is the files for Hypervisor configuration concerning instances and mapping of system conf."
		+"\nOnly instances with a hvQName not empty will be concerned.";
	
	class FileMarker {
		public FileMarker(FileOutputStream[] fileOutputStreams, long position) {
			this.position = position;
			this.files = fileOutputStreams;
		}
		public FileOutputStream files[];
		public long position;
		public String textToInsert;
	}
	/**
	 * key is scsId and value is hvId
	 */
	@SuppressWarnings("serial")
	class MappingInstance extends HashMap<String,String> {}
	/**
	 * key is area name and ArrayList of hvId
	 */
	@SuppressWarnings("serial")
	class MappingArea extends HashMap<String,ArrayList<String>> {}
	/**
	 * key is subsystem name and ArrayList of hvId
	 */
	@SuppressWarnings("serial")
	class MappingSubsytem extends HashMap<String,ArrayList<String>> {}
	
	private ArrayList<FileMarker> postTreatment;
	private FileMarker namespaceMarkerInstanceEqpt;
	private HashMap<String,String> usedNamespaceInstanceEqpt;

	// to resolve hvId
	private MappingInstance idMap;
	// memorize map to produce at the end the mapping file for Hv
	private MappingArea areaMap;
	private Map<String, String> reverseAreaMap;
	private MappingSubsytem subsytemMap;
	
	// SubsystemMap
	private Map<Integer, String> subsystemDefintion;
	
	private Stack<String> elementRoleStack;
	private Stack<String> elementScsNameStack;
	private Stack<Integer> elementNumberStack; /// to follow start element and end element
	private Stack<String> areaStack;/// to allocate the equipment to area along the SAX parsing
	private String currentPrototypeRootElementName;
	private String currentPrototypeUID;
	private ArrayList<String> uniqueId;
	
	private ObjectDefinition currentObjectDefinition;
	private FileWriter badIdsFileWriter;
	
	private PrototypeMap protoMap;
	private FileOutputStream instanceEqptfileOutputStream;
	private FileOutputStream instanceAreaFileOutputStream;
	
	private FileOutputStream mappingAreaFileOutputStream;
	private FileOutputStream mappingSubsytemFileOutputStream;
	private FileOutputStream mappingQNameFileOutputStream;
	private XMLStreamWriter instanceEqptStreamWriter;
	private XMLStreamWriter instanceAreaStreamWriter;
	
	private XMLStreamWriter mappingAreaStreamWriter;
	private XMLStreamWriter mappingSubsystemStreamWriter;
	private XMLStreamWriter mappingQNameStreamWriter;
	private XMLStreamWriter[] streamers;
	private FileOutputStream finalInstancefileOutputStream;
	private File instanceEqptFile;
	private String extractDirectory;
	private Workbook odeWorkbook;


	// Model contraint : this extractor needs the CoreModel
	private static final String LOCATED_INSTANCE_CLASS_NAME = "CmLocatedObject";
	private static final String EQPT_CLASS_NAME = "CmEquipment";
	private static final String MAPPING_SCHEMA_LOCATION = "http://www.thalesgroup.com/hv/data-v1/system/configuration hypervisor_system_configuration.xsd";
	private static final String MAPPING_SCHEMA_P = "http://www.thalesgroup.com/hv/data-v1/system/configuration";
	private static final String AREA_ROLE_NAME = "area";
	
	// output files naming
	private static final String SYSTEM_DIRECTORY 		= "systemConfiguration" + File.separator;
	private static final String INSTANCE_DIRECTORY 		= SYSTEM_DIRECTORY + "instances" + File.separator;
	private static final String MAPPING_DIRECTORY 		= SYSTEM_DIRECTORY + "mapping" + File.separator;
	private static final String INSTANCE_EQPT_TEMP_OUTPUT_FILENAME	= "equipments.temp.xml";/// missing insertion
	private static final String INSTANCE_EQPT_OUTPUT_FILENAME	= "equipments.xml";
	private static final String INSTANCE_AREA_OUTPUT_FILENAME	= "areas.xml";
	
	private static final String MAPPING_AREA_OUTPUT_FILENAME	= "entitiesPerAreaAllocation.xml";
	private static final String MAPPING_SUBSYS_OUTPUT_FILENAME	= "entitiesPerSubsystemAllocation.xml";
	private static final String MAPPING_QNAME_OUTPUT_FILENAME = "qnameMapping.xml";
	
	// Hv naming convention in xml files
	private static final String UNRESOLVED = "#unresolved_area_mapping#";
	private static final String PENDING_EQPT = "#pending_area_mapping#";;
	private static final String HV_MAPPING_ELEMENT = "p:mapping";
	private static final String HV_ENTITY_NAMESPACE_VALUE = "http://www.thalesgroup.com/hv/data-v1/entity/configuration";
	private static final String HV_ENTITY_NAMESPACE_KEY = "xmlns:hv-conf";
	private static final String HV_AREA_NAMESPACE_VALUE = "http://www.thalesgroup.com/hv/data-v1/area/configuration";
	private static final String HV_AREA_NAMESPACE_KEY = "xmlns:area";
	private static final String HV_MAPPING_DEFAULT = "DEFAULT";
	private static final String HV_MAPPING_ROOTNODE = "p:configuration";
	private static final String HV_MAPPING_ENTRY_TYPE = "p:IdToIdEntryType";
	private static final String HV_SCHEMALOCATION_ATTRIBUTE = "xsi:schemaLocation";
	private static final String HV_MAPPING_MAP_TYPE = "p:IdToIdMapType";
	private static final String HV_MAPPING_AREA_TYPE = "area:AreaType";

	
	/**
	 * Extractor For Hypervisor VS9.1. Should be used with a dedicated core model.
	 * Expected classes are CmSubsystem, CmArea...
	 * 
	 * @throws IOException 
	 * @throws XMLStreamException 
	 * 
	 */
	public HypervisorExtractor(String extractDirectory, String root, Resource resource) throws IOException, XMLStreamException {

		this.currentObjectDefinition = null;
		this.extractDirectory = extractDirectory;
		
		// Create output file stream
		File systemConfigurationDirectory = new File(extractDirectory, SYSTEM_DIRECTORY);
		File instanceDirectory = new File(extractDirectory, INSTANCE_DIRECTORY);
		File finalInstanceEqptFileOutput = new File(extractDirectory, INSTANCE_DIRECTORY + INSTANCE_EQPT_OUTPUT_FILENAME);
		this.instanceEqptFile = new File(extractDirectory, INSTANCE_DIRECTORY + INSTANCE_EQPT_TEMP_OUTPUT_FILENAME);
		File instanceAreaFileOutput = new File(extractDirectory, INSTANCE_DIRECTORY + INSTANCE_AREA_OUTPUT_FILENAME);
		File mappingDirectory = new File(extractDirectory, MAPPING_DIRECTORY);
		File mappingAreaFileOutput = new File(extractDirectory, MAPPING_DIRECTORY + MAPPING_AREA_OUTPUT_FILENAME);
		File mappingSubsystemFileOutput = new File(extractDirectory, MAPPING_DIRECTORY + MAPPING_SUBSYS_OUTPUT_FILENAME);
		File mappingQNameFileOutput = new File(extractDirectory, MAPPING_QNAME_OUTPUT_FILENAME);
		File[] files = new File[] {instanceEqptFile,
				instanceAreaFileOutput,
				mappingAreaFileOutput,
				mappingSubsystemFileOutput};
		// Create expected directories
		if (!systemConfigurationDirectory.exists()) {
			systemConfigurationDirectory.mkdir();
		}
		if (!instanceDirectory.exists()) {
			instanceDirectory.mkdir();
		}
		if (!mappingDirectory.exists()) {
			mappingDirectory.mkdir();
		}
		// Create if not existing
		for (File file : files) {
			file.createNewFile();
		}
		this.finalInstancefileOutputStream = new FileOutputStream(finalInstanceEqptFileOutput);
		this.instanceEqptfileOutputStream = new FileOutputStream(instanceEqptFile);
		this.instanceAreaFileOutputStream = new FileOutputStream(instanceAreaFileOutput);
		
		this.mappingAreaFileOutputStream = new FileOutputStream(mappingAreaFileOutput);
		this.mappingSubsytemFileOutputStream = new FileOutputStream(mappingSubsystemFileOutput);
		this.mappingQNameFileOutputStream = new FileOutputStream(mappingQNameFileOutput);
		// Create XmlWriter output stream
		this.instanceEqptStreamWriter = XmlStreamHelper.createXmlOutputStream(this.instanceEqptfileOutputStream, XmlStreamHelper.UTF_8_ENCODING, indentOutputFile());
		this.instanceAreaStreamWriter = XmlStreamHelper.createXmlOutputStream(this.instanceAreaFileOutputStream, XmlStreamHelper.UTF_8_ENCODING, indentOutputFile());
		this.mappingAreaStreamWriter = XmlStreamHelper.createXmlOutputStream(this.mappingAreaFileOutputStream, XmlStreamHelper.UTF_8_ENCODING, indentOutputFile());
		this.mappingSubsystemStreamWriter = XmlStreamHelper.createXmlOutputStream(this.mappingSubsytemFileOutputStream, XmlStreamHelper.UTF_8_ENCODING, indentOutputFile());
		this.mappingQNameStreamWriter = XmlStreamHelper.createXmlOutputStream(this.mappingQNameFileOutputStream, XmlStreamHelper.UTF_8_ENCODING, indentOutputFile());
		this.streamers = new XMLStreamWriter[] {instanceEqptStreamWriter, instanceAreaStreamWriter,
				mappingAreaStreamWriter, mappingSubsystemStreamWriter,mappingQNameStreamWriter};
		
		this.odeWorkbook = createODEWorkbook();
		
		this.elementRoleStack = new Stack<String>();
		this.elementScsNameStack = new Stack<String>();
		this.uniqueId = new ArrayList<String>();
		this.usedNamespaceInstanceEqpt = new HashMap<String,String>();
		this.elementNumberStack = new Stack<Integer>();
		this.areaStack = new Stack<String>();
		this.postTreatment = new ArrayList<HypervisorExtractor.FileMarker>();
		// internal maps to produce mapping files
		this.areaMap = new MappingArea();
		this.reverseAreaMap = new HashMap<String, String>();
		this.subsytemMap = new MappingSubsytem();
		this.idMap = new MappingInstance();
		
		// Initialize elementScsNameStack
		if(root != null && root.contains(":")) {
			String[] split = root.split(":");
			for(String fragment : split) {
				if(fragment.isEmpty())
					continue;
				
				this.elementScsNameStack.push(fragment);
			}
		}
	}
	
	public void setProtoMap(PrototypeMap protoMap) {
		this.protoMap = protoMap;
	}
	
	public void setSubsytemMap(final Map<Integer, String> subsystemDefintion) {
		this.subsystemDefintion = subsystemDefintion;
	}
	
	/**
	 * Check in properties files if exchange.extract.hypervisor.HypervisorExtractor.indent property value
	 * @return <code>true</code> if property is found and set set to "true", <code>false</code> otherwise
	 */
	private boolean indentOutputFile() {
		String indentProperty = "false";
		try {
			indentProperty = PropertyManager.getProperty("exchange.extract.hypervisor.HypervisorExtractor.indent");
		} catch (PropertyNotFoundException e) {
			Logger.EXCHANGE.error("exchange.extract.hypervisor.HypervisorExtractor.indent property not found in properties file", e);
			return false;
		}
		return Boolean.parseBoolean(indentProperty);
	}
	
	@Override
	public void startDocument() throws SAXException {
		Logger.EXCHANGE.info("Start Hypervisor SysXml Extraction");
		
		try {
			for (XMLStreamWriter stream : this.streamers) {
				stream.writeStartDocument(XmlStreamHelper.UTF_8_ENCODING, "1.0");
				stream.writeComment(" Generated with MetaConfigurator ");
			}
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while starting document in hypervisor output stream", e);
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		try {
			writeAreaMapping();
			writeSubsystemMapping();
			for (XMLStreamWriter stream : this.streamers) {
				stream.writeEndDocument();
			}
			// insert all that have not been written in first loop
			writeMarkers();
			updateODEArea(this.odeWorkbook, this.reverseAreaMap);
			createODEPhysInfo(this.odeWorkbook, this.subsytemMap.keySet());
			saveODEWorkbook(this.odeWorkbook, this.extractDirectory + File.separator + "ode.xlsx");
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while ending document in hypervisor output stream", e);
		} catch (IOException e) {
			Logger.EXCHANGE.error("Error while writing namespace or reference id.", e);
		} catch (Exception e) {
			Logger.EXCHANGE.error("Unknown error", e);
		}
		finally {
			try {
				for (XMLStreamWriter stream : this.streamers) {
					stream.flush();
					stream.close();
				}
				this.instanceEqptfileOutputStream.close();
				if (badIdsFileWriter!=null) {
					Logger.EXCHANGE.error("There are duplicated eqpt id in hypervisor generated files. Use attribute hvid on instance to distinguish them.");
					this.badIdsFileWriter.close();
				}
				// delete temp file
				if (this.instanceEqptFile.exists()) {
					instanceEqptfileOutputStream.close();
					this.instanceEqptFile.delete();
				}
				Logger.EXCHANGE.info("End of Hypervisor SysXml Extraction");
				
			} catch (XMLStreamException e) {
				Logger.EXCHANGE.error("Error while closing Hypervisor extraction xml output stream ", e);
			} catch (IOException e) {
				Logger.EXCHANGE.error("Error while closing Hypervisor extraction file output stream", e);
			}
		}
	}
	
	private boolean isStartRootElement(String qName, Attributes attributes) {
		if(HypervisorExtractorHelper.SCS_ROOT_ALPHANUM_ELEMENT.equals(qName)) {
			try {
				// write the root node : hv-conf:entitiesConfiugration for instances or p:configuration for mapping files
				for (XMLStreamWriter stream : this.streamers) {
					if (stream == this.mappingAreaStreamWriter || stream == this.mappingSubsystemStreamWriter) {
						stream.writeStartElement(HV_MAPPING_ROOTNODE);
					} else {
						stream.writeStartElement(HypervisorExtractorHelper.INSTANCE_ROOT_ELEMENT_NAME);
					}
					// Add XML NameSpace attribute
					stream.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				}
				addSchemaLocationAttribute();
				// mark where to put the namespaces used for equipments, other files have not namespace to be discovered during parsing
				try {
					long position = instanceEqptfileOutputStream.getChannel().position();
					namespaceMarkerInstanceEqpt = new FileMarker(new FileOutputStream[] {instanceEqptfileOutputStream},position);
					this.postTreatment.add(namespaceMarkerInstanceEqpt);
				} catch (IOException e) {
					// if cannot get the file do not write namespace
				}
			} catch (XMLStreamException e) {
				Logger.EXCHANGE.error("Error while writing root element: " + qName, e);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Add the schema location as attribute to the current element
	 */
	private void addSchemaLocationAttribute() {
		try {
			this.instanceEqptStreamWriter.writeAttribute(HV_ENTITY_NAMESPACE_KEY, HV_ENTITY_NAMESPACE_VALUE);
			this.instanceAreaStreamWriter.writeAttribute(HV_ENTITY_NAMESPACE_KEY, HV_ENTITY_NAMESPACE_VALUE);
			this.instanceAreaStreamWriter.writeAttribute(HV_AREA_NAMESPACE_KEY, HV_AREA_NAMESPACE_VALUE);
			this.mappingAreaStreamWriter.writeAttribute("xmlns:p", MAPPING_SCHEMA_P);
			this.mappingAreaStreamWriter.writeAttribute(HV_SCHEMALOCATION_ATTRIBUTE, MAPPING_SCHEMA_LOCATION);
			this.mappingSubsystemStreamWriter.writeAttribute("xmlns:p", MAPPING_SCHEMA_P);
			this.mappingSubsystemStreamWriter.writeAttribute(HV_SCHEMALOCATION_ATTRIBUTE, MAPPING_SCHEMA_LOCATION);
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while writing schema location", e);
		}
	}
	
	/**
	 * Set the current {@link ObjectDefinition} corresponding to className
	 * @param className The Class Name
	 */
	private void setCurrentObjectDefinition(String className) {
		this.currentObjectDefinition = DataModel.getInstance().getObjectDefinition(className);
		if(this.currentObjectDefinition == null) {
			Logger.EXCHANGE.error("No object definition found for class " + className);
			return;
		}
	}
	
	private int startCount=0;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(ignoreElement(qName))
			return;
		this.elementScsNameStack.push(qName);
		startCount++;
		// specific process for root element
		if (isStartRootElement(qName, attributes))
			return;
		// retrieve the definition in DAtaModel for further checks 
		setCurrentObjectDefinition(qName);
		
		boolean isAProto = (attributes.getValue(HypervisorExtractorHelper.SCS_PROTOTYPE_ATTR_NAME)!= null);
		if( isAProto ) {
			// the children of certain type will be changed into attributes and others will be ignored
			startProtoElement(uri, localName, qName, attributes);
		}
		// initialize area
		List<String> superclasses = null;
		String qNameOfLink = null;
		int beginIndex = qName.indexOf(HypervisorExtractorHelper.SCS_INSTANCE_LINK_TOKEN);
		if (beginIndex>0) {
			qNameOfLink = qName.substring(0,beginIndex);
			ObjectDefinition objectDefinition = DataModel.getInstance().getObjectDefinition(qNameOfLink);
			superclasses = objectDefinition.getSuperClasses();
		} else {
			ObjectDefinition objectDefinition = DataModel.getInstance().getObjectDefinition(qName);
			superclasses = objectDefinition.getSuperClasses();
		}
		
		//---------- AREA mapping management
		// it is not possible to just memorize the current area because it is defined on a node in it sub element myClass_link_area
		// As the parsing is alphabetical ordered, it is possible to parse an instance contained in a node before having resolved it attached area
		String currentArea = null;

		// memorize the current area if set (there can be only one) and replace parent
		// so that at end element all instance that were parsed before the link_area, will be added to the corresponding area
		if (superclasses != null &&	beginIndex>0 &&
				superclasses.contains(LOCATED_INSTANCE_CLASS_NAME)	&&
				qName.endsWith(AREA_ROLE_NAME)) {
			String area = attributes.getValue(HypervisorExtractorHelper.SCS_AREA_ID_LINK_ATTR_NAME);
			if (area != null && !area.isEmpty()) {
				beginIndex = area.lastIndexOf(CommonConst.PATH_SEPARATOR);
				if (beginIndex>0) {
					currentArea = area.substring(beginIndex+1);
					// replace parent node area allocation
					this.areaStack.pop();
					this.areaStack.push(currentArea);
				}
			}
		}
		this.areaStack.push(currentArea);
	}
	
	private void startProtoElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// First simple implementation that does not take custom parameters into account
		if (Arrays.asList(HypervisorExtractorHelper.HV_CLASSES).contains(qName)) {
			// filter
			return;
		}
		// Only treat as equipment the instances of prototype that inherits
		ObjectDefinition objectDefinition = DataModel.getInstance().getObjectDefinition(qName);
		List<String> superclasses = objectDefinition.getSuperClasses();

		boolean inheritsFromCmEquipment = false;
		if (superclasses != null &&	(superclasses.contains(EQPT_CLASS_NAME) || superclasses.contains("CmEquipmentGroup")) ) {
			inheritsFromCmEquipment = true;
			int idx = attributes.getIndex("hvid");
			if (idx != -1) {
				String hvid = attributes.getValue("hvid");
				if (hvid.length() == 0) {
					inheritsFromCmEquipment = false;
				}
			} else {
				inheritsFromCmEquipment = false;
			}
		}
		
		// Only the root of the proto is written or its references
		String prototype_ins = attributes.getValue(HypervisorExtractorHelper.SCS_PROTOTYPE_INS_ATTR_NAME);
		boolean isProtoRoot = "1".equals(prototype_ins);
		String protoUID = attributes.getValue(HypervisorExtractorHelper.SCS_PROTOTYPE_ATTR_NAME);

		// write new instance create
		if( isProtoRoot && inheritsFromCmEquipment) {
			this.currentPrototypeRootElementName = qName;
			this.currentPrototypeUID = protoUID;
			// Instance
			try {
				writeScsConfInstance(uri, localName, HypervisorExtractorHelper.INSTANCE_ELEMENT_NAME, attributes);
				this.instanceEqptStreamWriter.writeEndElement();
				elementNumberStack.push(startCount);
			} catch (XMLStreamException e) {
				Logger.EXCHANGE.error("Error while writing instance: " + qName, e);
			}
			return;
		}
		// add reference and properties
		if (currentPrototypeUID==null || (!isProtoRoot && protoUID!=null && protoUID.equals(this.currentPrototypeUID)
				&& ! (  qName.startsWith(this.currentPrototypeRootElementName)
						&& HypervisorExtractorHelper.SCS_INSTANCE_LINK_TOKEN.contains(qName)) )) {
			// filter every thing but the root node of the proto and its direct references
			return;
		}
		// TODO : properties, ref
		// if HvStatus ...
				// direct reference has to be output 
		//		try {
		//			addReferences(uri, localName, qName, attributes);
		//		} catch (XMLStreamException e) {
		//			Logger.EXCHANGE.error("Error while adding references in hypervisor output stream", e);
		//		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	
		if(ignoreElement(qName))
			return;
		
		if (this.elementNumberStack.size()>0) {
			Integer pop = this.elementNumberStack.peek();
			// write the equipment
			if (pop.intValue() == startCount) {
				System.out.println("starting element " + qName + " at level " + startCount);
				try {
					// JCM this.instanceEqptStreamWriter.writeEndElement();
					this.mappingQNameStreamWriter.writeEndElement();
				} catch (XMLStreamException e) {
					Logger.EXCHANGE.error("Error while writing end element [" + qName + "], ", e);
				}
				elementNumberStack.pop();
			}
		}
		// resolve pending unresolved equipments or if can not be done at this level transfer it to the previous level
		// all of this is done because the information of area affectation is done
		if (this.areaStack.size()>0) {
			String currentArea = this.areaStack.pop();
			String upperUnresolved = HypervisorExtractor.UNRESOLVED + Integer.toString(startCount+1);
			String currentUnresolved = HypervisorExtractor.UNRESOLVED + Integer.toString(startCount);
			ArrayList<String> upperEqptList = this.areaMap.get(upperUnresolved);
			ArrayList<String> curEqptList = this.areaMap.get(currentUnresolved);
			ArrayList<String> pendingEqptList = this.areaMap.get(HypervisorExtractor.PENDING_EQPT);

			// first handle the pending list to assign equipment directly assigned to  an area
			if (pendingEqptList!=null && !pendingEqptList.isEmpty()) {
				if (currentArea!=null) {
					ArrayList<String> currentList = this.areaMap.get(currentArea);
					if (currentList!=null) {
						currentList.addAll(pendingEqptList);
					} else {
						this.areaMap.put(currentArea, pendingEqptList);
					}
				} else {
					// the pending list should be moved in unresolved list to be processed at the end of an element that as an area
					if (curEqptList!=null) {
						curEqptList.addAll(pendingEqptList);
					} else {
						this.areaMap.put(currentUnresolved, pendingEqptList);
					}
				}
				this.areaMap.remove(HypervisorExtractor.PENDING_EQPT);
			}
			// then handle the unresolved list
			if (upperEqptList!=null && !upperEqptList.isEmpty()) {
				if (currentArea!=null) {
					ArrayList<String> currentList = this.areaMap.get(currentArea);
					if (currentList!=null) {
						currentList.addAll(upperEqptList);
					} else {
						this.areaMap.put(currentArea, upperEqptList);
					}
					this.areaMap.remove(upperUnresolved);
				}else {
					// if not yet resolved see if it should be down set to the lower level
					if (startCount>0) {
						ArrayList<String> currentList = this.areaMap.get(currentUnresolved);
						if (currentList!=null) {
							currentList.addAll(upperEqptList);
						} else {
							this.areaMap.put(currentUnresolved, upperEqptList);
						}
					} else {
						// there must be unassigned eqpt
						this.areaMap.put("???", upperEqptList);
					}
					this.areaMap.remove(upperUnresolved);
				}
			}
		}
		startCount--;
	}
	/*
	 * Writes equipments.xml and prepare the mappings
	 */
	private void writeScsConfInstance(String uri, String localName, String qName, Attributes attributes) throws XMLStreamException {
		String hvQName = attributes.getValue(HypervisorExtractorHelper.HV_QUALIFIED_CLASS_NAME);
		String hvID = attributes.getValue(HypervisorExtractorHelper.HV_ID_ATTR_NAME);
		//String hvType = attributes.getValue(HypervisorExtractorHelper.HV_TYPE_ID);
		String hvx = attributes.getValue(HypervisorExtractorHelper.HV_X);
		String hvy = attributes.getValue(HypervisorExtractorHelper.HV_Y);
		String scsId = attributes.getValue(HypervisorExtractorHelper.SCS_ID_ATTR_NAME);
		String protoUID = attributes.getValue(HypervisorExtractorHelper.SCS_PROTOTYPE_ATTR_NAME);
		String subsystemMask = attributes.getValue(HypervisorExtractorHelper.SCS_SUBSYSTEM_ID_ATTR_NAME);
		String protoName = null;
		if (protoMap!=null) {
			protoName = protoMap.getUIDMap().get(protoUID);
		}
		
		this.instanceEqptStreamWriter.writeStartElement(qName);
		// mapping between scsID and hvID
		this.mappingQNameStreamWriter.writeStartElement("entry");
		// prepare mapping entity per area
		String pendingArea = HypervisorExtractor.PENDING_EQPT; // can only be set on endElement
		ArrayList<String> eqptIdInarea = this.areaMap.get(pendingArea);
		if (eqptIdInarea == null) {
			eqptIdInarea = new ArrayList<String>();
			this.areaMap.put(pendingArea, eqptIdInarea); // the unresolved mapping will be evaluate at endElement
		}
		eqptIdInarea.add(scsId);
		// for subsystems
		String subSysName = "";
		if( subsystemMask != null && this.subsystemDefintion!=null) {
			int maskInt = 0;
			if (!"".equals(subsystemMask)) {
				maskInt = Integer.parseInt(subsystemMask,2); // parse base 2 (0010 gives 2)
			}
			if (maskInt > 0 ) {
				for (Entry<Integer, String> subSys : this.subsystemDefintion.entrySet()) {
					int ssIdvalue = subSys.getKey();
					// check if the mask contains the subsystem id
					int ssIdsubMask = 1 << ssIdvalue; // bit offset
					if ((ssIdsubMask & maskInt) != 0 ) {
					    subSysName = subSys.getValue();
						ArrayList<String> set = this.subsytemMap.get(subSysName);
						if (set==null) {
							set = new ArrayList<String>();
							this.subsytemMap.put(subSysName, set);
						}
						set.add(scsId);
					}
				}
			}
		}
		// memorize namespace used
		String hvclass = "";
		String xmlQName = "";
		if (hvQName != null && !hvQName.isEmpty()) {
			int endIndex = hvQName.lastIndexOf(".");
			String packageQName = null;
			String eqptName = null;
			if (endIndex>0) {
				packageQName = hvQName.substring(0, endIndex);
				eqptName = hvQName.substring(endIndex+1);
			} else {
				packageQName = hvQName;
				eqptName = hvQName;
			}
			xmlQName = usedNamespaceInstanceEqpt.get(packageQName);
			if (xmlQName==null) {
				xmlQName = resolveXmlQName(hvQName);
				this.usedNamespaceInstanceEqpt.put(packageQName, xmlQName);
			}
			// derive type from hvQName
			// Type is append to the qName to be compliant to melody generated XSD namming convention
			hvclass = eqptName;
			String type = xmlQName + ":" + eqptName + "Type";
			this.instanceEqptStreamWriter.writeAttribute(HypervisorExtractorHelper.SYSXML_TYPE_ATTR_NAME, type);
		} else if (protoName!=null){
			int lastIndexOf = protoName.lastIndexOf(CommonConst.PATH_SEPARATOR);
			if (lastIndexOf>0) {
				protoName = protoName.substring(lastIndexOf+1);
			}
			String packageQName = "???";
			xmlQName = usedNamespaceInstanceEqpt.get(packageQName);
			if (xmlQName==null) {
				xmlQName = "???";
				this.usedNamespaceInstanceEqpt.put(packageQName, xmlQName);
			}
			String type = xmlQName + ":" + protoName + "Type";
			this.usedNamespaceInstanceEqpt.put(packageQName, xmlQName);
			this.instanceEqptStreamWriter.writeAttribute(HypervisorExtractorHelper.SYSXML_TYPE_ATTR_NAME, type);
			Logger.EXCHANGE.error("Error while writing end element [" + qName + "] " + scsId + " : Missing attribute " + HypervisorExtractorHelper.HV_QUALIFIED_CLASS_NAME);
		}
		
		String ID=null;
		if(hvID != null && hvID.length()>0) {
			ID = hvID;
		}
		else {
			String converScsIdToSysXmlId = converScsIdToSysXmlId(scsId);
			ID = converScsIdToSysXmlId;
			Logger.EXCHANGE.info("Error while writing end element [" + qName + "] " + scsId + " : Missing attribute " + HypervisorExtractorHelper.HV_ID_ATTR_NAME + ", using " + ID + ".");
		}
		if (!this.uniqueId.contains(ID)) {
			this.uniqueId.add(ID);
		} else {
			// duplicated ID
			try {
				if (badIdsFileWriter==null) {
					// file for bad ids (duplicated)
					File badIdFileOutput = new File(this.extractDirectory, HypervisorExtractorHelper.BAD_IDS_OUTPUT_FILENAME);
					badIdFileOutput.createNewFile();
					badIdsFileWriter = new FileWriter(badIdFileOutput);
					badIdsFileWriter.write("Already used ID : ");
				}
				badIdsFileWriter.write(ID + " in " + scsId +"\n");
			} catch (IOException e) {
				// do nothing if cannot write problems
			}
		}
		this.idMap.put(scsId, ID);
		this.instanceEqptStreamWriter.writeAttribute(HypervisorExtractorHelper.INSTANCE_ID_ATTRIBUTE_NAME, ID);
		if (hvx != null && hvy != null && hvx.length()>0 && hvy.length() > 0) {
			this.instanceEqptStreamWriter.writeStartElement(HypervisorExtractorHelper.LOCATION_ELEMENT_NAME);
			this.instanceEqptStreamWriter.writeAttribute("x", hvx);
			this.instanceEqptStreamWriter.writeAttribute("y", hvy);
			this.instanceEqptStreamWriter.writeEndElement();
		}
		
		//SCADAgen-194 : TYUEN, generate custom properties which enabled as "supervisedAttribute"
		ObjectDefinition objectDefinition = DataModel.getInstance().getObjectDefinition(hvclass);
		if (objectDefinition == null) {
			Logger.EXCHANGE.info("Trying to retrieve attribute to be exported in DataModel but class " + qName + " not found.");			
		} else {
			Collection attributeDefinitions = objectDefinition.getAttributeDefinitions();
			
			for (Iterator i$ = attributeDefinitions.iterator(); i$.hasNext(); ) { 
				AttributeDefinition attributeDefinition = (AttributeDefinition)i$.next();				
				if (attributeDefinition.isMwtSupervisedAttribute()) {
					
					String name = attributeDefinition.getName();					
					int idx = attributes.getIndex(name);
					String custAttValue = attributes.getValue(name);
					
					if (idx != -1 && custAttValue != null && custAttValue.length() > 0  ) {
						Logger.EXCHANGE.info("Namespace: " + xmlQName + "; Attribute name: " + name + ", value: " + custAttValue );
						this.instanceEqptStreamWriter.writeStartElement(xmlQName + ":" + name);
						this.instanceEqptStreamWriter.writeCharacters(custAttValue);
						this.instanceEqptStreamWriter.writeEndElement();
					}
				}
			}
		}
		
		// update ODE excel file
		String label = attributes.getValue("label");
		String shortname = attributes.getValue("shortname");
		this.addODEEntry(this.odeWorkbook, hvID, hvclass, hvx, hvy, "PendingAreaMapping", subSysName, label, shortname);					
	}
	
	/**
	 * Create list of link, memorize
	 * @param attributes
	 * @return
	 */
	private List<String> getReferencedInstancesRefPaths(Attributes attributes) {
		List<String> refPaths = new ArrayList<String>();
		for(int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
			String attrQName = attributes.getQName(attrIndex);
			String attrValue = attributes.getValue(attrIndex);
			if(attrQName.startsWith(HypervisorExtractorHelper.SCS_INSTANCE_LINK_ATTRIBUTE_PREFIX)) {
				if(attrValue != null && !attrValue.trim().isEmpty()) {
					String id = attributes.getValue(HypervisorExtractorHelper.SCS_ID_ATTR_NAME);
					String refPath = resolveRefPath(id, attrValue);
					if(refPath != null)
						refPaths.add(refPath);
					else
						Logger.EXCHANGE.warn("Could not resolve reference to: " + attrValue);
				}
			}
		}
		return refPaths;
	}
	@SuppressWarnings("unused")
	private void addReferences(String uri, String localName, String qName, Attributes attributes) throws XMLStreamException {
		String refClass = qName.substring(qName.indexOf(HypervisorExtractorHelper.SCS_INSTANCE_LINK_TOKEN) + HypervisorExtractorHelper.SCS_INSTANCE_LINK_TOKEN.length());
		for(String referencedInstance : getReferencedInstancesRefPaths(attributes)) {
			this.instanceEqptStreamWriter.writeStartElement(HypervisorExtractorHelper.SYSXML_REFERENCE_PREFIX + refClass);
			this.instanceEqptStreamWriter.writeStartElement(HypervisorExtractorHelper.SYSXML_REFERENCED_ID_ELEMENT);
			this.instanceEqptStreamWriter.writeCharacters(referencedInstance);
			this.instanceEqptStreamWriter.writeEndElement();
			this.instanceEqptStreamWriter.writeEndElement();
		}
	}
	
	private String resolveRefPath(String id, String refId) {
		String scsPath = refId;
		if(refId.contains("^")) {
			scsPath = resolveRelativePath(refId, id);
		}
		return converScsIdToSysXmlId(scsPath);
	}
	
	private String resolveRelativePath(String scsRelativePath, String currentPath) {
		String scsPath = "";
		String[] split = scsRelativePath.split(":");
		for(String fragment : split) {
			if(fragment.equals("^")) {
				int lastIndexOfSeparator = currentPath.lastIndexOf(":");
				if(lastIndexOfSeparator == -1)
					break;
				
				currentPath = currentPath.substring(0, lastIndexOfSeparator);
			}
		}
		currentPath = currentPath.substring(0, currentPath.lastIndexOf(":"));
		scsPath = currentPath.concat(scsRelativePath.substring(scsRelativePath.lastIndexOf("^:")+1, scsRelativePath.length()));
		return scsPath;
	}
	
	
	private String converScsIdToSysXmlId(String scsId) {
		if(scsId == null || scsId.length()<5) // if root_alphanum ignore
			return null;
		// take the last part that is the nom_instance attribute
		//TODO : use a map to store pairs of technical ID (ie ELEMENT_ID) and business id (nom_instance) or ???
		int lastIndexOf = scsId.lastIndexOf(":")+1;
		if (lastIndexOf>scsId.length()) {
			return scsId;
		}
		return scsId.substring(lastIndexOf); // only keep the last part of ID
		
	}

	/**
	 * <p>Check if an element <b>qName</b> has to be ignored</p>
	 * @param qName The element to check
	 * @return <code>true</code> if the element qName has to be ignored, <code>false</code> otherwise
	 */
	private boolean ignoreElement(String qName) {
		if(qName.equalsIgnoreCase(HypervisorExtractorHelper.SCS_ROOT_ELEMENT))
			return true;
		
		return false;
	}
	
	public String getCurrentScsPath() {
		StringBuilder pathBuilder = new StringBuilder("");
		for(int index=0; index < this.elementScsNameStack.size(); index++) {
			String string = this.elementScsNameStack.get(index);
			pathBuilder.append(":" + string);
		}
		return pathBuilder.toString();
	}
	
	public String getCurrentSysXmlPath() {
		StringBuilder pathBuilder = new StringBuilder("");
		for(int index=0; index < this.elementRoleStack.size(); index++) {
			String string = this.elementRoleStack.get(index);
			pathBuilder.append("." + string);
		}
		String path = pathBuilder.substring(1); // Remove first "."
		return path;
	}
	// substitutes the marker in the destination file 
	private void writeMarkers() throws IOException {
		String baseNamespace = "http://www.thalesgroup.com/";
		String baseXmlNamespace = "xmlns:";
		if (namespaceMarkerInstanceEqpt != null) { // defensive code, should never be null
			this.namespaceMarkerInstanceEqpt.textToInsert = "";
			for (Entry<String, String> hvqnameAndxmlQName : this.usedNamespaceInstanceEqpt.entrySet()) {
				// transform qname used in namespace
				String hvqname = hvqnameAndxmlQName.getKey();
				String xmlNamespace = baseXmlNamespace + hvqnameAndxmlQName.getValue();
				String namespace = baseNamespace + hvqname.replace(".","/");
				// format something like xmlns:scadasoft="http://www.thalesgroup.com/scadasoft/sample/data/equipment/configuration" from
				// hvqname scadasoft.sample.data.equipment
				this.namespaceMarkerInstanceEqpt.textToInsert += " " + xmlNamespace + "=\""+ namespace + "/configuration\"";
			}
		}
		// it is not possible to insert in a file, so write a new one
		FileInputStream tempFileInput = new FileInputStream(this.instanceEqptFile);
		long curOffset = 0;
		long nextOffsetOffset = 0;
		final long BuffferSize = 2048; // should be no longer than an int
		long sizeToRead = 0;
		byte[] b = new byte[(int) BuffferSize];
		for (FileMarker marker : this.postTreatment) {
			nextOffsetOffset = (int) marker.position;
			sizeToRead = Math.min(nextOffsetOffset-(int)curOffset, BuffferSize);
			while (sizeToRead>0) {
				int readSize = tempFileInput.read(b , 0, (int)sizeToRead);
				finalInstancefileOutputStream.write(b, 0, readSize);
				curOffset = tempFileInput.getChannel().position();
				sizeToRead = Math.min(nextOffsetOffset-(int)curOffset, BuffferSize);
			}
			finalInstancefileOutputStream.write(marker.textToInsert.getBytes(),0, marker.textToInsert.length());
		}
		// write the end of file
		nextOffsetOffset = tempFileInput.getChannel().size();
		sizeToRead = Math.min(nextOffsetOffset-(int)curOffset, BuffferSize);
		while (sizeToRead>0) {
			int readSize = tempFileInput.read(b , 0, (int)sizeToRead);
			finalInstancefileOutputStream.write(b, 0, readSize);
			curOffset = tempFileInput.getChannel().position();
			sizeToRead = Math.min(nextOffsetOffset-(int)curOffset, BuffferSize);
		}
		tempFileInput.close();
		finalInstancefileOutputStream.close();
	}
	
	private static int identicalNamespaceCount = 2;
	private String resolveXmlQName(String hvQName) {
		// transform qname used in namespace
		String xmlNamespace = null;
		int endIndex = hvQName.indexOf(".");
		// format something like xmlns:scadasoft="http://www.thalesgroup.com/scadasoft/sample/data/equipment/configuration" from
		// hvqname scadasoft.sample.data.equipment.configuration
		if (endIndex>0) {
			xmlNamespace = hvQName.substring(0, endIndex);
		} else {
			xmlNamespace = hvQName;
		}
		// check that it does not already exists
		if (this.usedNamespaceInstanceEqpt.containsValue(xmlNamespace)) {
			xmlNamespace += identicalNamespaceCount++;
		}
		return xmlNamespace;
	}
	private void writeSubsystemMapping() {
		try {
			this.mappingSubsystemStreamWriter.writeStartElement(HV_MAPPING_ELEMENT);
			this.mappingSubsystemStreamWriter.writeAttribute("runtimeMode", "OPERATIONAL");
			this.mappingSubsystemStreamWriter.writeAttribute("xsi:type", HV_MAPPING_MAP_TYPE);
				
			for (Entry<String, ArrayList<String>> entry : this.subsytemMap.entrySet()) {
				String subsystemName = entry.getKey();
				
				// mapping
				try {
					this.mappingSubsystemStreamWriter.writeStartElement("p:entry");
					this.mappingSubsystemStreamWriter.writeAttribute("key", subsystemName);
					this.mappingSubsystemStreamWriter.writeAttribute("role", HV_MAPPING_DEFAULT);
					this.mappingSubsystemStreamWriter.writeAttribute("xsi:type", HV_MAPPING_ENTRY_TYPE);

					// add a mapping on the service itself for service operation
					try {
						this.mappingSubsystemStreamWriter.writeStartElement("p:value");
						this.mappingSubsystemStreamWriter.writeCharacters(subsystemName);
						this.mappingSubsystemStreamWriter.writeEndElement();
					} catch (XMLStreamException e) {
						Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
					}
					// add entities mapping
					ArrayList<String> values = entry.getValue();
					for (String value : values) {
						try {
							this.mappingSubsystemStreamWriter.writeStartElement("p:value");
							String hvId = idMap.get(value);
							if(hvId!=null) {
								value = hvId;
							}
							this.mappingSubsystemStreamWriter.writeCharacters(value);
							this.mappingSubsystemStreamWriter.writeEndElement();
						} catch (XMLStreamException e) {
							Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
						}
					}
					this.mappingSubsystemStreamWriter.writeEndElement();
				} catch (XMLStreamException e) {
					Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
				}
			}
			this.mappingSubsystemStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
			return;
		}
	}

	
	private void writeAreaMapping() {
		try {
			this.mappingAreaStreamWriter.writeStartElement(HV_MAPPING_ELEMENT);
			this.mappingAreaStreamWriter.writeAttribute("runtimeMode", "OPERATIONAL");
			this.mappingAreaStreamWriter.writeAttribute("xsi:type", HV_MAPPING_MAP_TYPE);
			for (Entry<String, ArrayList<String>> entry : this.areaMap.entrySet()) {
				try {
					String areaName = entry.getKey();
					//instance
					this.instanceAreaStreamWriter.writeStartElement("hv-conf:entity");
					this.instanceAreaStreamWriter.writeAttribute("xsi:type", HV_MAPPING_AREA_TYPE);
					this.instanceAreaStreamWriter.writeAttribute("id", areaName);
					this.instanceAreaStreamWriter.writeEndElement();
					// mapping
					this.mappingAreaStreamWriter.writeStartElement("p:entry");
					if (entry.getKey()!=null) {
						this.mappingAreaStreamWriter.writeAttribute("key", areaName);
						this.mappingAreaStreamWriter.writeAttribute("role", HV_MAPPING_DEFAULT);
						this.mappingAreaStreamWriter.writeAttribute("xsi:type", HV_MAPPING_ENTRY_TYPE);
					}
					ArrayList<String> values = entry.getValue();
					for (String value : values) {
						try {
							// build reverse map to update excel file for ODE
							
							
							this.mappingAreaStreamWriter.writeStartElement("p:value");
							String hvId = idMap.get(value);
							if(hvId!=null) {
								value = hvId;
							}
							this.reverseAreaMap .put(hvId, areaName);
							this.mappingAreaStreamWriter.writeCharacters(value);
							this.mappingAreaStreamWriter.writeEndElement();
						} catch (XMLStreamException e) {
							Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
						}
					}
					this.mappingAreaStreamWriter.writeEndElement();
				} catch (XMLStreamException e) {
					Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
				}
			}
			this.mappingAreaStreamWriter.writeEndElement();
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while writing subsytems mapping", e);
			return;
		}
	}

	public static String getDescription() {
		return DESCRIPTION;
	}
	
	private Workbook createODEWorkbook() {
		// create a new workbook
		Workbook wb = new XSSFWorkbook();
		
		return wb;
	}
	
	private void addODEEntry(Workbook wb, String hvid, String hvclass, String x, String y, String area, String subsystem, String label, String shortname) {
		Sheet s = getODEEntitySheet(wb, hvclass);
		
		Row r = s.createRow(s.getLastRowNum() + 1);
		Cell c = r.createCell(c_EqpClass_ID_POS);
		c.setCellValue(hvid); // c.setCellValue("& id");
		c = r.createCell(c_EqpClass_NAME_POS);
		c.setCellValue(shortname); // c.setCellValue("name");
		c = r.createCell(c_EqpClass_LABEL_POS);
		c.setCellValue(shortname); // c.setCellValue("label");
		c = r.createCell(c_EqpClass_LONGLABEL_POS);
		c.setCellValue(label); // c.setCellValue("longLabel");
		
		// add dummy name in id to have a pseudo key
		c = r.createCell(c_EqpClass_COORD_ID_POS);
		c.setCellValue("dummy_id");
		c = r.createCell(c_EqpClass_X_POS);
		c.setCellValue(x); // c.setCellValue("location.x");
		c = r.createCell(c_EqpClass_Y_POS);
		c.setCellValue(y); // c.setCellValue("location.y");
		
		c = r.createCell(c_EqpClass_CLUSTER_ID_POS);
		c.setCellValue(subsystem); // c.setCellValue("In Cluster");
		
		c = r.createCell(c_EqpClass_AREA_ID_POS);
		c.setCellValue(area); // c.setCellValue("In PhysicalArea");

		addODEClustermapping(wb, hvid, subsystem);
	}
	
	private void addODEClustermapping(Workbook wb, String hvid, String subsystem) {
		Sheet s = getODEEntitySheet(wb, "SCADA_Cluster");
		
		Row r = s.createRow(s.getLastRowNum() + 1);
		Cell c = r.createCell(c_Cluster_ID_POS);
		c.setCellValue(subsystem); // c.setCellValue("& id");
		
		c = r.createCell(c_Cluster_HISTO_POS);
		c.setCellValue(true); // c.setCellValue("historizationEnabled");
		
		c = r.createCell(c_Cluster_ENTITY_ID_POS);
		c.setCellValue(hvid); // c.setCellValue("_supervisedObject AbstractConfiguredEntity.& id");
	}
	
	// define excel format
	private static final String[] c_EqpClassColumn = {"& id", "name", "label", "longLabel", "location Coordinates.& id",
		"location Coordinates.x", "location Coordinates.y", "location GeographicalCoordinates.& id",
		"location GeographicalCoordinates.x", "location GeographicalCoordinates.y", "In Cluster", "In PhysicalArea"};
	private static final int c_EqpClass_ID_POS = 0;
	private static final int c_EqpClass_NAME_POS = 1;
	private static final int c_EqpClass_LABEL_POS = 2;
	private static final int c_EqpClass_LONGLABEL_POS = 3;
	private static final int c_EqpClass_COORD_ID_POS = 4;
	private static final int c_EqpClass_X_POS = 5;
	private static final int c_EqpClass_Y_POS = 6;
	private static final int c_EqpClass_CLUSTER_ID_POS = 10;
	private static final int c_EqpClass_AREA_ID_POS = 11;
	
	private static final String[] c_PhysAreaColumn = {"& id", "name", "label", "longLabel", "configuredEntity AbstractConfiguredEntity.& id"};
	private static final int c_PhysArea_ID_POS = 0;
	private static final int c_PhysArea_ENTITY_ID_POS = 4;

	private static final String[] c_SCADAClusterColumn = {"networkId", "& id", "notificationURL", "operationURL", "historizationEnabled", "notificationPort",
		"operationPort", "monitoringPort", "Service Service.& id", "_supervisedObject AbstractConfiguredEntity.& id"};
	private static final int c_Cluster_ID_POS = 1;
	private static final int c_Cluster_NETWORKID_POS = 0;
	private static final int c_Cluster_HISTO_POS = 4;
	private static final int c_Cluster_ENTITY_ID_POS = 9;
	private static final int c_Cluster_SERVICE_ID_POS = 8;
	
	private static final String[] c_PhysSystemColumn = {"& id", "isMonitoringEnabled", "Workstation Workstation.& id", "_supervisedObject AbstractConfiguredEntity.& id", "Node Node.& networkId",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.& id", "connectionMonitoringServiceSettings ConnectionMonitoringSettings.logHeartBeatSent",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.logHeartBeatReceived", "connectionMonitoringServiceSettings ConnectionMonitoringSettings.logCacheOnStateChange",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.timerPeriodSendBeat", "connectionMonitoringServiceSettings ConnectionMonitoringSettings.timerPeriodCheckForActiveNodes",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.serverSocketTimeout", "connectionMonitoringServiceSettings ConnectionMonitoringSettings.serverSocketBufferSize",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.messageSeparator", "connectionMonitoringServiceSettings ConnectionMonitoringSettings.staleTimeRatio",
		"connectionMonitoringServiceSettings ConnectionMonitoringSettings.staleTimeDelay", "Service Cluster Service Cluster.& id" };
	private static final int c_PhysSystem_ID_POS = 0;
	private static final int c_PhysSystem_MONITORING_POS = 1;
	private static final int c_PhysSystem_NODE_ID_POS = 4;
	private static final int c_PhysSystem_CLUSTER_ID_POS = 16;

	private static final String[] c_SCADAColumn = {"& id", "name", "label", "longLabel", "weight", "notificationURL", "operationURL", "cacheFileLocation",
		"schedulerFileLocation", "internalSubscriptionDuration", "numberOfSubscriptionWorkers", "notificationMode", "operationMode", "servicePath",
		"historianBuffer HistorianBuffer.& id", "historianBuffer HistorianBuffer.enabled", "historianBuffer HistorianBuffer.maxKeepTimeH",
		"historianBuffer HistorianBuffer.maxKeepNumber", "historianBuffer HistorianBuffer.resendPeriod", "historianBuffer HistorianBuffer.path"};
	private static final int c_Service_ID_POS = 0;
	
	private static final String[] c_NodeColumn = {"& networkId", "name",	"Service Service.& id"};
	private static final int c_Node_ID_POS = 0;
	private static final int c_Node_SERVICE_ID_POS = 2;
	
	private static final Map<String, String[]> c_ODESheetHeaderMap;
    static
    {
    	c_ODESheetHeaderMap = new HashMap<String, String[]>();
    	c_ODESheetHeaderMap.put("PhysicalArea", c_PhysAreaColumn);
    	c_ODESheetHeaderMap.put("SCADA_Cluster", c_SCADAClusterColumn);
    	c_ODESheetHeaderMap.put("SCADA Physical System", c_PhysSystemColumn);
    	c_ODESheetHeaderMap.put("SCADA", c_SCADAColumn);
    	c_ODESheetHeaderMap.put("Node", c_NodeColumn);
    }
    
	private Sheet getODEEntitySheet(Workbook wb, String entityName) {
		Sheet s = null;
		// create a new sheet
		int is = wb.getSheetIndex(entityName);
		
		if (is == -1) {
			s = wb.createSheet(entityName);
			// get column definition
			String[] columns = c_ODESheetHeaderMap.get(entityName);
			if (columns == null) {
				columns = c_EqpClassColumn;
			}
			// add header row
			Row r = s.createRow(0);
			int i = 0;
			for(String cname : columns) {
				Cell c = r.createCell(i);
				c.setCellValue(cname);
				i = i + 1;
			}
			
		} else {
			s = wb.getSheetAt(is);
		}
		
		
		return s;
	}
	
	private void createODEPhysInfo(Workbook wb, Collection<String> subSysList) {
		Row r;
		Cell c;
		
		// create Physical System entries
		Sheet phys = getODEEntitySheet(wb, "SCADA Physical System");
		for(String ssName : subSysList) {
		    r = phys.createRow(phys.getLastRowNum() + 1);
		    c = r.createCell(c_PhysSystem_ID_POS);
			c.setCellValue("PhysSystem"); // c.setCellValue("& id");
			
		    c = r.createCell(c_PhysSystem_MONITORING_POS);
			c.setCellValue(true); // c.setCellValue("isMonitoringEnabled");
			
			c = r.createCell(c_PhysSystem_NODE_ID_POS);
			c.setCellValue("host1"); // c.setCellValue("Node Node.& networkId");
			
		    c = r.createCell(c_PhysSystem_CLUSTER_ID_POS);
			c.setCellValue(ssName); // c.setCellValue("Service Cluster Service Cluster.& id");
		}
		// create SCADA Service entries
		Sheet scada = getODEEntitySheet(wb, "SCADA");
		for(String ssName : subSysList) {
			r = scada.createRow(scada.getLastRowNum() + 1);
			c = r.createCell(c_Service_ID_POS);
			c.setCellValue(ssName + "N1"); // c.setCellValue("& id");
			
			// add dummy service with ss id for oeration on service
			r = scada.createRow(scada.getLastRowNum() + 1);
			c = r.createCell(c_Service_ID_POS);
			c.setCellValue(ssName); // c.setCellValue("& id");
		}
		// add mapping in Service cluster
		Sheet cluster = getODEEntitySheet(wb, "SCADA_Cluster");
		for(String ssName : subSysList) {
			r = cluster.createRow(cluster.getLastRowNum() + 1);
			
			c = r.createCell(c_Cluster_NETWORKID_POS);
			c.setCellValue(ssName); // c.setCellValue("networkId");
			
			c = r.createCell(c_Cluster_ID_POS);
			c.setCellValue(ssName); // c.setCellValue("& id");
			
			c = r.createCell(c_Cluster_HISTO_POS);
			c.setCellValue(true); // c.setCellValue("historizationEnabled");
			
		    c = r.createCell(c_Cluster_SERVICE_ID_POS);
			c.setCellValue(ssName + "N1"); // c.setCellValue("Service Service.& id");
			// add mapping on service with ss id for oeration on service
			c = r.createCell(c_Cluster_ENTITY_ID_POS);
			c.setCellValue(ssName); // c.setCellValue("_supervisedObject AbstractConfiguredEntity.& id");
		}
		// add mapping in Node
		// create one host (mandatory for ODE generation)
		Sheet node = getODEEntitySheet(wb, "Node");
		for(String ssName : subSysList) {
			r = node.createRow(node.getLastRowNum() + 1);
			
			c = r.createCell(c_Node_ID_POS);
			c.setCellValue("host1"); // c.setCellValue("& networkId");
			
			c = r.createCell(c_Node_SERVICE_ID_POS);
			c.setCellValue(ssName + "N1"); // c.setCellValue("Service Service.& id");
		}
	}
	
	private void updateODEArea(Workbook wb, Map<String, String> reverseAreaMap) {
		Sheet areas = getODEEntitySheet(wb, "PhysicalArea");
		
		Iterator<Sheet> sitr = wb.sheetIterator();
		while(sitr.hasNext()) {
			Sheet s = sitr.next();
			Iterator<Row> rowIterator = s.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getCell(c_EqpClass_ID_POS) != null) {
					String hvid = row.getCell(c_EqpClass_ID_POS).getStringCellValue();
					String area = reverseAreaMap.get(hvid);
					if (area != null) {
						row.getCell(c_EqpClass_AREA_ID_POS).setCellValue(area);
						// add info in PhysicalArea
						Row r = areas.createRow(areas.getLastRowNum() + 1);
						Cell c = r.createCell(c_PhysArea_ID_POS);
						c.setCellValue(area); // c.setCellValue("& id"); of PhysicalArea
						c = r.createCell(c_PhysArea_ENTITY_ID_POS);
						c.setCellValue(hvid); // c.setCellValue("configuredEntity.& id");of PhysicalArea
					}
				}
			}
		}

	}

	private void saveODEWorkbook(Workbook wb, String fname) {
		// create a new file
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fname);
		} catch (FileNotFoundException e) {
			Logger.EXCHANGE.error("Cannot create ODE xls file:" + fname, e);
			return;
		}
		// write the workbook to the output stream
		// close our file (don't blow out our file handles
		try {
			wb.write(out);
		} catch (IOException e) {
			Logger.EXCHANGE.error("Cannot write ODE xls file:" + fname, e);
		}
		try {
			out.close();
		} catch (IOException e) {
			Logger.EXCHANGE.error("Cannot close ODE xls file:" + fname, e);
		}
	}
	
}
