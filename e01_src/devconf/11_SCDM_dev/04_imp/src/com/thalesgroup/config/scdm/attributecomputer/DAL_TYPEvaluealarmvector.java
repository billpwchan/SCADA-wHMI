package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : DAL_TYPEvaluealarmvector.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DAL_TYPEvaluealarmvector extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		
		boolean hasHdv = false;
		boolean hasDAC = false;
		//boolean setHidden = false;
		boolean setInhibit = false;
		
        // Behaviour
        //FORCE (1): raise even if the analogue value has not changed 
        //CHANGE (2): raise only if the analogue value changes,
        //IGNORE (3): do not raise
        // get nature
        SAXElement vector = walker.getCurrentNode();
		// return its length
		String behaviour = null;
        behaviour = vector.getAttribute("DIAlmCEBehaviour");
        if (null == behaviour || "".equals(behaviour)) {
            behaviour = "1"; // behaviour
        }
        
        /*if ( null != vector.getAttribute("hidden")
        		&& !"".equals(vector.getAttribute("hidden"))) {
        	setHidden = true;
        }*/
        if ( null != vector.getAttribute("inhibit")
        		&& !"".equals(vector.getAttribute("inhibit"))) {
        	setInhibit = true;
        }
        
        walker.walkToParentNode().walkToParentNode();	//EQPT
		if ( null != walker.getCurrentNode().getAttribute("hdvFlag") 
				&& !"1".equals(walker.getCurrentNode().getAttribute("hdvFlag"))
				&& !"".equals(walker.getCurrentNode().getAttribute("hdvFlag"))){
			hasHdv = true;
		}
		walker.goToStartNode();
		
		walker.walkToNode("^:dac");
        SAXElement dac_node = walker.getCurrentNode();
        if (null != dac_node) {
        	hasDAC = true;
        }
        
        String almce = "SCS_ALARM_DI(";
        //value
        if (hasHdv) {
        	almce += "IF([^:^.hdvFlag],[^.value],{^.value}),";
        } else {
        	almce += "[^.value],";
        }
        almce += "0"; // always 0
        almce += ",{.valueAutomaton},{.ackAutomaton}";
        //valueDate
        if (hasDAC){
        	almce += ",{^:dac.equipmentDate},[^.valueDate],";
        } else {
        	almce += ",[^.valueDate],{^.valueDate},";
        }
        almce += behaviour;
        //alarm inhibitation
        walker.walkToNode("^:dfo");
	    SAXElement dfo_node = walker.getCurrentNode();
	    if (dfo_node != null) {
	    	//if (setHidden || setInhibit) {
	    	if (setInhibit) {
		    	almce += ",IF((NOT(BITAND([^:dfo.forcedStatus],12))";
				/*if (setHidden ){
					almce += " AND ([.hidden]!=1)";
	        	}*/
				if	(setInhibit){
					almce += " AND ([.inhibit]!=1)";
				}
				almce += "),[^.validity],0))";
		    } else {
	        	almce += ",IF(NOT( BITAND( [^:dfo.forcedStatus], 12)) && [^.validity],1,0))";
		    } 
	    } else {
	      almce += ", [^.validity])";
	    }
	    
		return almce;
    }
}
