package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : AAL_TYPEvaluealarmvector.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class AAL_TYPEvaluealarmvector extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		boolean hashdv = false;
		boolean hasAfo = false;
		walker.walkToParentNode();	//aci
		if (walker.countChildrenNodesOfType("afo_type")==1){
			hasAfo = true;
		}
		walker.walkToParentNode();	//EQPT
		// get hdvflag
		if ( null != walker.getCurrentNode().getAttribute("hdvFlag") 
				&& !"1".equals(walker.getCurrentNode().getAttribute("hdvFlag"))
				&& !"".equals(walker.getCurrentNode().getAttribute("hdvFlag"))) {
			//if there are attribute hdvFlag at Equipment level and hdvFlag is not empty or default value
			hashdv = true;
		}
		
		String almce = "SCS_ALARM_AI(";
		if (hashdv){
			almce += "IF([^:^.hdvFlag],[^.value],{^.value})";
		} else {
			almce += "[^.value]";
		}
		
		almce +=",[.valueLimits],";
        almce += "{.valueAutomaton},{.ackAutomaton},[^.valueDate],{^.valueDate},";
        // Behaviour
        //FORCE (1): raise even if the analogue value has not changed 
        //CHANGE (2): raise only if the analogue value changes,
        //IGNORE (3): do not raise
        // get nature
        SAXElement vector = walker.getCurrentNode();
		// return its length
		String behaviour = null;
        behaviour = vector.getAttribute("AIAlmCEBehaviour");
        if (null != behaviour) {
            almce += behaviour; // behaviour
        } else {
            almce += "1"; // behaviour
        }
        
        //alarm inhibition
        if (hasAfo == true){
        	almce += ",IF(NOT(BITAND([^:afo.forcedStatus],12)),[^.validity],0))";
        } else {
        	almce += ",[^.validity])";
        }
        return almce;
    }
}
