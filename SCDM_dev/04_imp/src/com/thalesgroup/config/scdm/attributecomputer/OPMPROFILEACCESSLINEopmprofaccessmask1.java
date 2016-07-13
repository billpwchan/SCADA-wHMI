package com.thalesgroup.config.scdm.attributecomputer;


import com.thalesgroup.config.scdm.attributecomputer.common.OPMPROFILEACCESSLINECommonMask;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : OPMPROFILEACCESSLINEopmprofaccessmask1.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class OPMPROFILEACCESSLINEopmprofaccessmask1 extends AttributeComputer {
	/** 
	 * name of the mandatory link to compute
	 */
	final static private String link = "link_OmpActionMode1";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return OPMPROFILEACCESSLINECommonMask.compute(walker, context, link);
	}
}
