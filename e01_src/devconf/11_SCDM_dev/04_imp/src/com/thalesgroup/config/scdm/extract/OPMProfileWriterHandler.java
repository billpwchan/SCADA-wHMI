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
public class OPMProfileWriterHandler extends DefaultHandler {

	/**
	 * Name of the configuration file
	 */
	final static private String confFileName = "ScsOpmProfiles.cfg";
	
	/** 
	 * Writer handlers on file 
	 */
	private ByteArrayWriter profFileWriter_ = null;
	private FileWriter wFile_ = null;

	/** 
	 * 
	 */
	public OPMProfileWriterHandler(String extractDirectory) {
		try {
			File currentConfFile = 
				new File(extractDirectory, confFileName);
			
			wFile_           = new FileWriter(currentConfFile);
			profFileWriter_            = new ByteArrayWriter();
		}
		catch (IOException ex) {
			Logger.USER.error(
				"OPMProfileWriterHandler : " + Logger.exStackTraceToString(ex) );
		}
	}
	
	/**
	 * 
	 */
	public void startDocument() throws SAXException {
		WriterUtility.writeHeader(profFileWriter_, confFileName);
	}

	/**
	 *
	 */
	public void startElement(String uri, String localName, 
			String qName, Attributes atts) throws SAXException {
		if("OpmProfile".equals(qName)) {
			profFileWriter_.print("Define Profile ");
			profFileWriter_.printlnStringValue(atts.getValue("OpmProfileName"));
			
			// begin block
			profFileWriter_.println("{");
		} if("OpmProfileAccessLine".equals(qName)) {
			profFileWriter_.printts(atts.getValue("OpmProfAccessFunction"),1);
			profFileWriter_.print(" ");
			
			profFileWriter_.print(atts.getValue("OpmProfAccessLocation"));
			profFileWriter_.print(" = ");
			
			for(int i=1; i<=4; i++) {
				String value = atts.getValue("OpmProfAccessMask" + i);
				try {
					Integer.valueOf(value);
					profFileWriter_.print(value);
				} catch (Exception e){
					profFileWriter_.printStringValue(value);
				}
				profFileWriter_.print(" ");
			}
			
			profFileWriter_.println();
		}
	}
	
	/**
	 * 
	 */
	public void endElement(String uri, String localName, 
			String qName) throws SAXException {
		if("OpmProfile".equals(qName)) {
			// end block
			profFileWriter_.println("}");
			profFileWriter_.println();
		}
	}
	
	/**
	 * 
	 */
	public void endDocument() throws SAXException {
		try {
			wFile_.write(profFileWriter_.toString());
			wFile_.close();
		} catch (IOException e) {
			Logger.USER.error(
					"OPMProfileWriterHandler : " + Logger.exStackTraceToString(e) );
		}	}

}