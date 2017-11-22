package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/******************************************************************************/
/*  FILE  : DAC_TYPEacqvalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DAC_TYPEacqvalue extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		// get nature
        int nature = 0;
        SAXElement vector = walker.getCurrentNode();
		// return its length
		String nat = null;
		if (null!=vector) {
			nat = vector.getAttribute("nature");
		}
        if ("1".equals(nat)) {
            nature = 1;
        }
        
        /*
	  	* Comptage du nombre de variables externe
	  	*/
	  	int nbrVariable = 0 ;
		Iterator venameIt = ValidatorFunctions.getNodeVectorIterator(walker , "veTable_vename");

		while( venameIt.hasNext()) {
		 	String next = (String )venameIt.next();
		 	if( next.length()> 3 ) {
		 		nbrVariable++ ;
	 		}
		}
        
        
	  	String formuleCE = "" ;
		if( nbrVariable == 1 ) {
			formuleCE = "[.veTable(0,value)]" ;
		} else if( nbrVariable == 2 ) {
            if (nature == 0) {
			    formuleCE = "DPC_COMBIN2([.veTable(0,value)],[.veTable(1,value)])";
            } else {
                formuleCE = "DPC_SELECT3([.veTable(0,value)],[.veTable(1,value)],0)";
            }
		} else if( nbrVariable == 3 ) {
			if (nature == 0) {
                formuleCE = "DPC_COMBIN3([.veTable(0,value)],[.veTable(1,value)],[.veTable(2,value)])";
            } else {
                formuleCE = "DPC_SELECT3([.veTable(0,value)],[.veTable(1,value)],[.veTable(2,value)])";
            }
        } else if( nbrVariable == 4 && nature == 1) {
            formuleCE = "DPC_SELECT4([.veTable(0,value)],[.veTable(1,value)],[.veTable(2,value)],[.veTable(3,value)])";
		} else {
			formuleCE = NOT_CALCULABLE ;
		}

		return formuleCE;
    }
}
