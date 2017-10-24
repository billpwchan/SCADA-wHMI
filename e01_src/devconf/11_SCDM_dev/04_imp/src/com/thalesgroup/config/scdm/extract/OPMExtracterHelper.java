package com.thalesgroup.config.scdm.extract;

import org.xml.sax.ContentHandler;

import com.thalesis.config.exception.TechnicalException;
import com.thalesis.config.exchange.extract.ResourceExtracter;
import com.thalesis.config.utils.XMLContentDispatcher;

/******************************************************************************/
/*  FILE  : OPMExtracterHelper.java                                           */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 9 juil. 07                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class OPMExtracterHelper extends ResourceExtracter {
	
	public ContentHandler getExtractHandler(String extractDirectory, String root) 
								throws TechnicalException {
		// dispatcher handler
		XMLContentDispatcher dispatcher = new XMLContentDispatcher();
		
		// handler to generate the definition file
		dispatcher.add(new OPMDefinitionWriterHandler(extractDirectory));
		
		// handler to generate the operator file
		dispatcher.add(new OPMOperatorWriterHandler(extractDirectory));
		
		// handler to generate the operator file
		dispatcher.add(new OPMProfileWriterHandler(extractDirectory));
		
		return dispatcher;
	}

}