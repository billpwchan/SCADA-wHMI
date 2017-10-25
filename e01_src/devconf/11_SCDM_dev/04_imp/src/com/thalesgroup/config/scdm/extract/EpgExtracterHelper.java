package com.thalesgroup.config.scdm.extract;

import org.xml.sax.ContentHandler;

import com.thalesis.config.exchange.extract.ResourceExtracter;
import com.thalesis.config.storage.*;

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
public class EpgExtracterHelper extends ResourceExtracter {
	
  public ContentHandler getExtractHandler(String extractDirectory, String root) throws com.thalesis.config.exception.TechnicalException {
	return new EpgWriterHandlerHelper(extractDirectory);
  }

  public void setInstancesFilter(String instancesFilterFileURI) throws com.thalesis.config.exception.InvalidOperationException, com.thalesis.config.exception.TechnicalException {
	super.setInstancesFilter(instancesFilterFileURI);
  }
  public ContentHandler getQueryExtractHandler(Resource resource, String extractDirectory, String root) throws com.thalesis.config.exception.TechnicalException {
	return getExtractHandler(extractDirectory, root);
  }

}