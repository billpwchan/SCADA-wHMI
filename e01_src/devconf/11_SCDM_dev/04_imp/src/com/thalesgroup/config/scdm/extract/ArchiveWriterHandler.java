package com.thalesgroup.config.scdm.extract;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import com.thalesis.config.storage.Resource;
import com.thalesis.config.utils.ErrorManager;
import com.thalesis.config.utils.LinkUtility;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.PropertyManager;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.SAXUtility;
import com.thalesis.config.utils.VectorUtility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.thalesis.config.exception.NoSuchObjectException;
import com.thalesis.config.exception.ParsingException;
import com.thalesis.config.exception.PropertyNotFoundException;
import com.thalesis.config.exception.TechnicalException;


public class ArchiveWriterHandler extends DefaultHandler {

private String destinationDirectory_ = null;
private String configurationFilePath_ = null;
private Resource resource_ = null;
private ArchiveConfigFileHandler archiveConfFileHandler_ = null;
private static String listenedAttribute_ = null;
private static String pointNameAttributeName_ = null;
private static final String CONF_FILE_DEFAULT_NAME = "extract_history.xml";
private static final String ARCHIVE_NAME_SEPARATOR = ";";
private static String CONF_FILE_DEFAULT_PATH;	
	
	public ArchiveWriterHandler () {
		 super();
		 this.configurationFilePath_ = CONF_FILE_DEFAULT_PATH + File.separator + CONF_FILE_DEFAULT_NAME; 
	}
	
	public ArchiveWriterHandler(String destinationDirectory, Resource resource) throws TechnicalException {    
		super();

		Logger.EXCHANGE.debug("Construction: ArchiveConfigHandler.");
		
	    this.destinationDirectory_ = destinationDirectory;
	    this.resource_  = resource;
	    
	    File destinationDirectoryFile = new File(this.destinationDirectory_);
	    if (!(destinationDirectoryFile.exists())) {
	      destinationDirectoryFile.mkdirs();
	    }
	    
	    String confFilePath = null;
	    String confFileName = null;
	    try
	    {
	      confFilePath = PropertyManager.getProperty("exchange.extract.history.config_file.path");
	    }
	    catch (PropertyNotFoundException e)
	    {
	      confFilePath = CONF_FILE_DEFAULT_PATH;
	    }
	    try
	    {
	      confFileName = PropertyManager.getProperty("exchange.extract.history.config_file.name");
	    }
	    catch (PropertyNotFoundException e)
	    {
	      confFileName = CONF_FILE_DEFAULT_NAME;
	    }
	    this.configurationFilePath_ = confFilePath + File.separator + confFileName;
	    
	    computeConfigurationFile(new File(this.configurationFilePath_));	    
	    
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
		    throws SAXException
	{
	    if ((!(VectorUtility.isElementAVector(qName))) && (!(LinkUtility.isElementALink(qName))))
	    {
	    	
	    	String currentID = atts.getValue("ID");
	    	AttributesImpl attImpl = new AttributesImpl(atts);
	    	String pointlabel = attImpl.getValue("label");
	    	String pointNom = attImpl.getValue("nom_instance");
	    	
	    	String geographical_cat=null;
	    	String eqptype = null;
	    	String shortlabel = null;
	    	String label = null;
	    	
	    	String parentid = currentID.substring(0, currentID.lastIndexOf(":"));
	    	
			SAXElement saxelement;
			try {
				saxelement = this.resource_.getElement(parentid, true);
				
				eqptype = saxelement.getAttribute("type");
				shortlabel = saxelement.getAttribute("shortLabel");
				label = saxelement.getAttribute("label");
				geographical_cat= saxelement.getAttribute("geographicalCat");
				
			} catch (TechnicalException e) {								
				Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			} catch (NoSuchObjectException e) {				
				Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			}
			
	    	String listenedAttributeValue = atts.getValue(listenedAttribute_);
			if ((null != listenedAttributeValue) && (!("".equals(listenedAttributeValue))))
			{
			    //StringTokenizer tok = new StringTokenizer(listenedAttributeValue, ";");//ARCHIVE_NAME_SEPARATOR
			    StringTokenizer tok = new StringTokenizer(listenedAttributeValue, ARCHIVE_NAME_SEPARATOR);
			    while (tok.hasMoreTokens()) {
			        String archiveName = tok.nextToken();
			        
			        ArchiveName archive = this.archiveConfFileHandler_.getArchive(archiveName); 
			        if (null != archive)
			        {
			           String[] listener = archive.findListener(qName);
			           
			           if (null != listener)
			           {
			             String pointName = atts.getValue(pointNameAttributeName_);
			             if ((null == pointName) || ("".equals(pointName))) {
			                //pointName = cleanedCurrentID + listener[1];
			             }
			             
			              try {
			                archive.writePoint( currentID.substring(4) , shortlabel, pointlabel, eqptype, label, pointNom, geographical_cat);
			              }
			              catch (IOException e)
			              {
			                Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			                throw new SAXException(e);
			              }
			            }
			            else {
			              Logger.EXCHANGE.warn("ArchiveWriterHandler : listener " + qName + " non trouvé !!!");
			            }
			          }
			          else {
			            Logger.EXCHANGE.warn("ArchiveWriterHandler : archive " + archiveName + " non connue !!!");
			          }
			        
			    }
			}
	    }
	}	
	
	
	public void startDocument() throws SAXException {
		
		try {
		      this.archiveConfFileHandler_.writeHeaders();
		} catch (IOException e) {
			  Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			  throw new SAXException(e);
		}		
	}
	
	public void endDocument() throws SAXException {
		
		try {			
			this.archiveConfFileHandler_.writeBottoms();
		} catch (IOException e) {
			  Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			  throw new SAXException(e);
		}		
    }
			  
	private void computeConfigurationFile(File confFile) throws TechnicalException {
			    
		  if ((null != confFile) && (confFile.exists()))  {
			      
			  this.archiveConfFileHandler_ = new ArchiveConfigFileHandler(this.destinationDirectory_);
			  
			  try			  
			  {
			     SAXUtility.parse(confFile, this.archiveConfFileHandler_);
			  }catch (ParsingException e) {
			      Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			      throw new TechnicalException(e);
			  }catch (TechnicalException e) {
			      Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
			      throw new TechnicalException(e);
			  }
			  
		  } else {		  
			  Logger.EXCHANGE.error("ArchiveWriterHandler error : " + ErrorManager.getMessage("exchange.extract.fileNotFound", this.configurationFilePath_));
			  throw new TechnicalException(ErrorManager.getMessage("exchange.extract.fileNotFound", this.configurationFilePath_));
		  }
	  }

	  static {
	    try {
			CONF_FILE_DEFAULT_PATH = PropertyManager.getProperty("directory.resource.history");
			listenedAttribute_ = PropertyManager.getProperty("exchange.extract.histoAttrName");
			//pointNameAttributeName_ = PropertyManager.getProperty("exchange.extract.pointNameAttrName");			
		} catch (PropertyNotFoundException e) {
			Logger.EXCHANGE.error("ArchiveWriterHandler error : " + e.getMessage());
		}
	 }	
	  
}
