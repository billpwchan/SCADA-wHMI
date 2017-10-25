package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.exception.TechnicalException;

/******************************************************************************/
/*  FILE  : SCADASOFTdbvname.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class SCADASOFTdbvname extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		String dbvname = "NONAME";
		try {
			dbvname = context.getConfigurationName() +
			          "_" +
			          context.getConfigurationRevisionNumber();
		} catch (TechnicalException e) {
			e.printStackTrace();
		}
		
		return dbvname;
    }
}
