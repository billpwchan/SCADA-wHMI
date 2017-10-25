package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : SAC_TYPEacqvalue.java                                             */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class SAC_TYPEacqvalue extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute (TreeWalker walker, ComputeContext context)
	{
		return "DPC_SIIVLOAD([.veTable(0,value1)],[.veTable(0,value2)])";
	}

}
