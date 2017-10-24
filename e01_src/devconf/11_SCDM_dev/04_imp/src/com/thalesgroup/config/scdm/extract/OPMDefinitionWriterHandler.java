package com.thalesgroup.config.scdm.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.thalesis.config.utils.Logger;


/******************************************************************************/
/*  FILE  : OPMDefinitionWriterHandler.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 9 juil. 07                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/**
 * <p>Titre : </p>
 * <p>Description : Writer for definition file of OPM </p>
 */
public class OPMDefinitionWriterHandler extends DefaultHandler {

	/**
	 * Name of the configuration file
	 */
	final static private String confFileName = "ScsOpmDefinitions.cfg";
	
	/** 
	 * Writer handlers on file 
	 */
	private ByteArrayWriter funcFileWriter_ = null;
	private ByteArrayWriter locFileWriter_  = null;
	private ByteArrayWriter actFileWriter_  = null;

	private FileWriter wFile_ = null;
	/** 
	 * 
	 */
	public OPMDefinitionWriterHandler(String extractDirectory) {
		try {
			File currentConfFile = 
				new File(extractDirectory, confFileName);
			
			wFile_ = new FileWriter(currentConfFile);
			
			funcFileWriter_ = new ByteArrayWriter();
			locFileWriter_  = new ByteArrayWriter();
			actFileWriter_  = new ByteArrayWriter();
		}
		catch (IOException ex) {
			Logger.USER.error(
				"OPMDefinitionWriterHandler : " + Logger.exStackTraceToString(ex) );
		}
	}
	
	/**
	 * 
	 */
	public void startDocument() throws SAXException {
		// write header
		WriterUtility.writeHeader(locFileWriter_, confFileName);
		
		// Main block for locations open
		locFileWriter_.println("Define Locations");
		locFileWriter_.println("{");
		
		funcFileWriter_.println("Define Functions");
		funcFileWriter_.println("{");
		
		actFileWriter_.println("Define Actions");
		actFileWriter_.println("{");
	}

	/**
	 *
	 */
	public void startElement(String uri, String localName, 
			String qName, Attributes atts) throws SAXException {
		if("OpmLocation".equals(qName)
				&& !"0".equals(atts.getValue("OpmCategory"))) {
			locFileWriter_.printts("Define Location ",1);
			locFileWriter_.printlnStringValue(atts.getValue("OpmCategoryName"));
			
			// begin subblock
			locFileWriter_.printtsln("{", 1);

			writeCommon(locFileWriter_, atts);
			
			// end subblock
			locFileWriter_.printtsln("}",1);
		} else if("OpmFunction".equals(qName)
				&& !"0".equals(atts.getValue("OpmCategory"))) {
			funcFileWriter_.printts("Define Function ",1);
			funcFileWriter_.printlnStringValue(atts.getValue("OpmCategoryName"));
			
			// begin subblock
			funcFileWriter_.printtsln("{", 1);

			writeCommon(funcFileWriter_, atts);
			
			// additonnal
			funcFileWriter_.printts("Family = ",2);
			funcFileWriter_.printlnStringValue(atts.getValue("OpmFunctionFamily"));
			
			// end subblock
			funcFileWriter_.printtsln("}",1);
		} else if("OpmAction".equals(qName)
				&& !"0".equals(atts.getValue("OpmCategory"))) {
			actFileWriter_.printts("Define Action ",1);
			actFileWriter_.printlnStringValue(atts.getValue("OpmCategoryName"));
			
			// begin subblock
			actFileWriter_.printtsln("{", 1);

			writeCommon(actFileWriter_, atts);
			
			// additonnal
			actFileWriter_.printts("Abbrev = ",2);
			actFileWriter_.printlnStringValue(atts.getValue("OpmActionAbbrev"));
			
			// end subblock
			actFileWriter_.printtsln("}",1);
		}
	}
	
	/**
	 * use to factorize common attributes
	 */
	private void writeCommon(ByteArrayWriter writer, Attributes atts) {
		writer.printtsln("Category = " + atts.getValue("OpmCategory"),2);
		
		writer.printts("Description = ",2);
		writer.printlnStringValue(atts.getValue("OpmDescription"));
		
		String value = atts.getValue("OpmFlags");
		if(!"".equals(value)) {
			writer.printts("Flags = ",2);
			writer.printlnStringValue(value);
		}
	}
	
	/**
	 * 
	 */
	public void endDocument() throws SAXException {
		// Main block for locations close
		locFileWriter_.println("}");
		locFileWriter_.println();
		
		funcFileWriter_.println("}");
		funcFileWriter_.println();
		
		actFileWriter_.println("}");
		actFileWriter_.println();
		
		try {
			wFile_.write(locFileWriter_.toString());
			wFile_.write(funcFileWriter_.toString());
			wFile_.write(actFileWriter_.toString());
			wFile_.close();
		} catch (IOException e) {
			Logger.USER.error(
					"OPMDefinitionWriterHandler : " + Logger.exStackTraceToString(e) );
		}
		
	}

}