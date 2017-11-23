package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : EQ_TYPEprevalarmsynthesis.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class EQ_TYPEprevalarmsynthesis extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return "SCS_AlarmSynth([.alarmSynthesis])"  ;
    }
}
