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
        String almce = "SCS_ALARM_DI([^.value],";
        almce += "0"; // always 0
        almce += ",{.valueAutomaton},{.ackAutomaton},[^.valueDate],{^.valueDate},";
        // Behaviour
        //FORCE (1): raise even if the analogue value has not changed 
        //CHANGE (2): raise only if the analogue value changes,
        //IGNORE (3): do not raise
        // get nature
        SAXElement vector = walker.getCurrentNode();
		// return its length
		String behaviour = null;
        behaviour = vector.getAttribute("DIAlmCEBehaviour");
        if (null != behaviour) {
            almce += behaviour; // behaviour
        } else {
            almce += "1"; // behaviour
        }
    
    walker.walkToNode("^:dfo");
    SAXElement node = walker.getCurrentNode();
    if (node != null) {
        almce += ",IF(NOT( BITAND( [^:dfo.forcedStatus], 12)) && [^.validity],1,0))";
    } else {
      almce += ", [^.validity])";
    }
    
		return almce;
    }
}
