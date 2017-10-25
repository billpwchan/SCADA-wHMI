package com.thalesgroup.config.scdm.extract;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.thalesis.config.exception.AlreadyExistException;
import com.thalesis.config.exception.BadParameterException;
import com.thalesis.config.utils.ErrorManager;

public class ArchiveConfigFileHandler implements ContentHandler {

	private HashSet<ArchiveName> archives_ = null;
	private ArchiveName currentArchive_ = null;
	private String outputPath_ = null;
	
	public static final String	LISTENER_TYPE		= "type";
	public static final String	LISTENER_VALUE		= "value";
	public static final String	LISTENER_QUALITY	= "quality";
	public static final String	LISTENER_TIMESTAMP	= "timestamp";
	
	
	public ArchiveConfigFileHandler() {
		super();
		this.archives_ = new HashSet<ArchiveName>();
	}
	
	public ArchiveConfigFileHandler(String outputPath) {
		super();
	    this.archives_ = new HashSet<ArchiveName>();
	    this.outputPath_ = new String(outputPath);		
	}
	
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
			
		if (localName.equals("Archive")) {
		      try {		    	    
		        this.currentArchive_ = new ArchiveName(this.outputPath_, atts.getValue("name"));
		      } catch (BadParameterException e) {
		        throw new SAXException(e);
		      } catch (IOException e) {
		        throw new SAXException(e);
		      }
		} else if (localName.equals("ChangeBased")) {
	      //this.currentArchive_.setType("Change Based", qName);
	    } else if (localName.equals("Database")) {
	      //this.currentArchive_.setType("Database", qName);
	    } else if (localName.equals("Points")) {
	    	
	    	try {	    			    		
				currentArchive_.addListener(atts.getValue(LISTENER_TYPE),
						atts.getValue(LISTENER_VALUE),
						atts.getValue(LISTENER_QUALITY),
						atts.getValue(LISTENER_TIMESTAMP));

	        } catch (AlreadyExistException e) {
	          throw new SAXException(e);
	        }
	       
	    } else if (localName.equals("Archives")) {
	        return;
	    } else {
	    	throw new SAXException(ErrorManager.getMessage("exchange.extract.invalidTag", localName));  
	    }
	    
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if ((localName.equals("Archive")) && 
	     (!(this.archives_.add(this.currentArchive_)))) {
			throw new SAXException(ErrorManager.getMessage("exchange.extract.nomArchiveDejaExistant", this.currentArchive_.getName()));
		}
	    	    
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { 
		// TODO Auto-generated method stub		
	}

	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub		
	}

	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub		
	}

	public ArchiveName getArchive(String name)
	{
	   Iterator<ArchiveName> it = this.archives_.iterator();
	   while (it.hasNext()) {
		   ArchiveName ar = (ArchiveName)it.next();
	     if (ar.getName().equals(name))
	       return ar;
	   }
	   return null;
	}	
	
	public void writeHeaders() throws IOException {		
		Iterator<ArchiveName> it = this.archives_.iterator();
	    while (it.hasNext()) {
	      ArchiveName ar = (ArchiveName)it.next();
		  ar.writeHeader();
		}		
	}

	public void writeBottoms() throws IOException {
		Iterator<ArchiveName> it = this.archives_.iterator();
		while (it.hasNext()) {
		  ArchiveName ar = (ArchiveName)it.next();
		  ar.writeBottom();
		}
	}	
	
}
