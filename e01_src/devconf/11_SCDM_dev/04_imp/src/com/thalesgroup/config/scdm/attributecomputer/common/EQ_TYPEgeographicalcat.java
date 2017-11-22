package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.TreeWalker;

import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : EQ_TYPEiscontrolable.java                                         */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public abstract class EQ_TYPEgeographicalcat extends EQ_TYPECommunCat {
	
	private static String ATTRIBUT_LINK_NAME = "geo_cat";
	private static String ATTRIBUT_NAME = "geographicalCat";

	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {		
		return getCurrentOrParentLinkAttributValue( walker,context,ATTRIBUT_LINK_NAME, getAttributeName() );
    }
	
	protected static String getAttributeToComputeValue() {
		return ATTRIBUT_NAME;
	}
}
