package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;
import java.util.Vector;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.attributecomputer.VectorAttributeComputer;

/******************************************************************************/
/*  FILE  : ACvetable_value.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class ACvetable_value extends VectorAttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public Iterator compute(TreeWalker walker, ComputeContext context) {
		return new Vector().iterator();
    }
}