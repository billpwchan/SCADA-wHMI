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
		
		// Set compute method for SCADAgen
		boolean useDefaultComputer = false;
		
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
            
            name += walker.walkToParentNode().getCurrentNode().getAttribute("UNIVNAME") ; // equipment name
            // If pointname in xciXXX-NNNNNN format, use SCADAgen computer
            if (ni.indexOf("-") <= 0) {
            	name += "_" + ni.substring(3);
            	result.add(name);
            } else {
            	/*SCADAgen VE name calculation*/
            	if (name.charAt(0) == 'd' ){
            		//if DCI
            		String getNature = walker.goToNode(homePath).getCurrentNode().getAttribute("nature");
            		
            		try{
                		String tempstr = "";
                		@SuppressWarnings("unchecked")
						Iterator<SAXElement> iCombineList = walker.getChildrenNodesOfType("dac_type_VECTOR_combineList");
                		if (iCombineList.hasNext()){
                			SAXElement combineList = iCombineList.next();
                			
                			tempstr = combineList.getAttribute("v0");
                			if ( tempstr != "") {
                				result.add(name + "-" + tempstr);
                				tempstr = combineList.getAttribute("v1");
	                			if ( tempstr != "") result.add(name + "-" + tempstr);
	                			tempstr = combineList.getAttribute("v2");
	                			if ( tempstr != "") result.add(name + "-" + tempstr);
	                			tempstr = combineList.getAttribute("v3");
	                			if ( tempstr != "") result.add(name + "-" + tempstr);
                			} else {
                				useDefaultComputer = true;
                			}
                		} else {
                			useDefaultComputer = true;
                		}
                	} catch (Exception e) {
                		useDefaultComputer = true;
                	}
            		
            		if (true == useDefaultComputer){
	            		if (getNature.charAt(0)=='0'){
	            			//DPC_COMBINE
	            			//If combineList not set for combined points, split the vename into two by its pointname
	                		result.add(name + ni.substring(ni.indexOf("-"),ni.indexOf("-") + 1 + (int)((ni.length()-ni.indexOf("-") - 1) /2)));
		            		result.add(name + "-" + ni.subSequence(ni.indexOf("-") + (int)((ni.length()-ni.indexOf("-") + 1 ) /2), (int)ni.length()));
	            		} else {
	            			//DPC_SELECT
	            			name += ni.substring(ni.indexOf("-"));
	                		result.add(name);
	            		}
            		}
            		
            	} else {
            		//If ACI or SCI
            		name += ni.substring(ni.indexOf("-"));
            		result.add(name);
            	}
            	
            }
		    
		}
		return result.iterator();
    }
}