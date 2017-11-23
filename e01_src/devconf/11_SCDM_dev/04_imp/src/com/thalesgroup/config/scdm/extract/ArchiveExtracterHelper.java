package com.thalesgroup.config.scdm.extract;

import org.xml.sax.ContentHandler;

import com.thalesis.config.exchange.extract.ResourceExtracter;
import com.thalesis.config.storage.*;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.exception.TechnicalException;
import com.thalesis.config.exception.InvalidOperationException;

/******************************************************************************/
/*  FILE  : EpgExtracterHelper.java */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/01/26                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/**
 * <p>Description : Classe pour la construction du handler specifique a
 *  l'extraction des donnees pour l'EPG </p>
 */
public class ArchiveExtracterHelper extends ResourceExtracter {
	
  public ArchiveExtracterHelper() {
	  super();
  }
   
  public ContentHandler getExtractHandler(String extractDirectory, String root) throws TechnicalException {	  
	  
	  Logger.EXCHANGE.debug("ArchiveExtracterHelper : CALLBACK getExtractHandler");
	  ArchiveWriterHandler handler = null;
	  
	  try {
		  handler = new ArchiveWriterHandler(extractDirectory, getResource()); 
	  } catch (Exception ex) {
	      Logger.EXCHANGE.error("ArchiveExtracterHelper - getExtractHandler : " + Logger.exStackTraceToString(ex));
	      throw new TechnicalException(ex);		  
	  }
	  
	  return handler;
  
  }

  public void setInstancesFilter(String instancesFilterFileURI) throws InvalidOperationException, TechnicalException {
	  super.setInstancesFilter(instancesFilterFileURI);
  }
  
  public ContentHandler getQueryExtractHandler(Resource resource, String extractDirectory, String root) throws TechnicalException {	  	  
	  Logger.EXCHANGE.debug("ArchiveExtracterHelper : CALLBACK getQueryExtractHandler");
	  
	  return getExtractHandler(extractDirectory, root);  	  	  
  }

}