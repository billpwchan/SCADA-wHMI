package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;
import java.util.Vector;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.attributecomputer.VectorAttributeComputer;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/******************************************************************************/
/*  FILE  : ACvetable_vename.java                                             */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class ACvetable_vename extends VectorAttributeComputer {

    @SuppressWarnings("rawtypes")
	public Iterator compute (TreeWalker walker, ComputeContext context)
	{
//    	 move the AC node (we are on the vector)
		walker.walkToParentNode();
		
		String homePath = walker.getCurrentPath();
		
		// vector for the result
		Vector<String> result = new Vector<String>();
		
		// Get the VE links
		Iterator veIterator = ValidatorFunctions.getNodeLinkIterator(walker,"ve");

		// For each VE, get the name of the VE
		while(veIterator.hasNext())
		{
			String vePath = (String) veIterator.next();
			// goto the VE
			walker.goToNode(vePath);
			// Get the VE
			SAXElement ve = walker.getCurrentNode();
			if (null!=ve) {
				result.add(ve.getAttribute("name"));
			}
			// back to the start point (in order to get the right path if the next link is a relative path)
			walker.goToNode(homePath);
		}
		
		//si pas de lien: lire la dac
		if (result.isEmpty())
		{
			//get the current instance name (AAC or DAC or SAC)
			String name = walker.getCurrentNode().getAttribute("nom_instance");
			// Replace the AC by EI
			name = name.toLowerCase();
			name = name.charAt(0) + "ei";
            String ni = walker.walkToParentNode().getCurrentNode().getAttribute("nom_instance"); // xci name
            name += walker.walkToParentNode().getCurrentNode().getAttribute("UNIVNAME") + "_"; // equipment name
            name += ni.substring(3);
                  
		    result.add(name);
		}
		return result.iterator();
    }
}