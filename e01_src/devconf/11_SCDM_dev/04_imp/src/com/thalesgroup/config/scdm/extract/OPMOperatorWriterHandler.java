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
public class OPMOperatorWriterHandler extends DefaultHandler {

	/**
	 * Name of the configuration file
	 */
	final static private String confFileName = "ScsOpmOperators.cfg";
	
	/** 
	 * Writer handlers on file 
	 */
	private ByteArrayWriter opFileWriter_ = null;
	private FileWriter wFile_ = null;

	/** 
	 * 
	 */
	public OPMOperatorWriterHandler(String extractDirectory) {
		try {
			File currentConfFile = 
				new File(extractDirectory, confFileName);
			
			wFile_         = new FileWriter(currentConfFile);
			opFileWriter_            = new ByteArrayWriter();
		}
		catch (IOException ex) {
			Logger.USER.error(
				"OPMOperatorWriterHandler : " + Logger.exStackTraceToString(ex) );
		}
	}
	
	/**
	 * 
	 */
	public void startDocument() throws SAXException {
		WriterUtility.writeHeader(opFileWriter_, confFileName);
	}

	/**
	 *
	 */
	public void startElement(String uri, String localName, 
			String qName, Attributes atts) throws SAXException {
		if("OpmOperator".equals(qName)) {
			opFileWriter_.print("Define Operator ");
			opFileWriter_.printlnStringValue(atts.getValue("OpmOperatorName"));
			
			// begin block
			opFileWriter_.println("{");
			
			opFileWriter_.printts("Profiles = ",1);
			opFileWriter_.printlnStringValue(atts.getValue("OpmOperatorProfiles"));
			
			opFileWriter_.printts("Password = ",1);
			opFileWriter_.printlnStringValue(atts.getValue("OpmOpCryptedPassword"));
			
			String value = atts.getValue("OpmOperatorGroup");
			if(!"".equals(value)) {
				opFileWriter_.printts("Group = ",1);
				opFileWriter_.printlnStringValue(value);
			}
			
			opFileWriter_.printts("Update = ",1);
			opFileWriter_.println(atts.getValue("OmpOperatorUpdate"));
			
			opFileWriter_.printts("Status = ",1);
			opFileWriter_.printlnStringValue(atts.getValue("OpmOperatorStatus"));
			
			opFileWriter_.printts("CurrentFailure = ",1);
			opFileWriter_.println(atts.getValue("OpmOperatorCurrentFailure"));
			
			opFileWriter_.printts("Expiration = ",1);
			opFileWriter_.println(atts.getValue("OmpOperatorExpiration"));
			
			opFileWriter_.printts("MinLength = ",1);
			opFileWriter_.println(atts.getValue("OmpOperatorMinLength"));
			
			opFileWriter_.printts("MaxFailure = ",1);
			opFileWriter_.println(atts.getValue("OmpOperatorMaxFailure"));
			
		} if("OpmOperator_VECTOR_Properties".equals(qName)) {
			String strLength = atts.getValue("maxLength");
			int length = Integer.valueOf(strLength);
			for(int i=0; i<length; i++) {
				String value = atts.getValue("v"+i);
				if(!"".equals(value)) {
					int index = i + 1;
					opFileWriter_.printts("Property" + index +  " = ",1);
					opFileWriter_.printlnStringValue(value);
				}
			}
			
			// end block
			opFileWriter_.println("}");
			opFileWriter_.println();
		}
	}
	
	/**
	 * 
	 */
	public void endDocument() throws SAXException {
		try {
			wFile_.write(opFileWriter_.toString());
			wFile_.close();
		} catch (IOException e) {
			Logger.USER.error(
					"OPMOperatorWriterHandler : " + Logger.exStackTraceToString(e) );
		}
	}

}