package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/******************************************************************************/
/*  FILE  : DAC_TYPEacqstatus.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DAC_TYPEacqstatus extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		StringBuffer fonctionCE = new StringBuffer("SCSDPC_ACQSTATUS(");
	  	int nbrVariable = 0 ;
		Iterator venameIt = ValidatorFunctions.getNodeVectorIterator(walker , "veTable_vename");
		/*
		* Recherche du nombre de variables externe
		*/
	  	while( venameIt.hasNext()) {
			  String next = (String )venameIt.next();
			  if( next.length()> 3 ) {
				  nbrVariable++ ;
			  }
	  	}

	  	for( int index = 0 ; index < nbrVariable ; index++) {
			if( index > 0 ) {
				fonctionCE.append(',');
			}
			fonctionCE.append("[.veTable(");
			fonctionCE.append(index);
			fonctionCE.append(",status)]");
	  	}
	  	fonctionCE.append(')');
	  	if (nbrVariable == 0) {
		  		return "";
	  	}
	  	return fonctionCE.toString();
    }
}
