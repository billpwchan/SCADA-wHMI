package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;
import java.util.Vector;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.attributecomputer.VectorAttributeComputer;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/******************************************************************************/
/*  FILE  : ACvetable_status.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class ACvetable_status extends VectorAttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterator compute(TreeWalker walker, ComputeContext context) {
		Vector result = new Vector();
        
		// Move to the real point : not the vector
		walker.walkToParentNode();
		// Get the VE links
		Iterator veIterator = ValidatorFunctions.getNodeLinkIterator(walker,"ve");

		// For each VE, get the name of the VE
		while(veIterator.hasNext())
		{
            veIterator.next();
		result.add("1024");
		}
		
		//si pas de lien: lire la dac
		if (result.isEmpty())
		{
            result.add("1024");
		}
		
		return result.iterator();
    }
}
