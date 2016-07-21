package com.thalesgroup.config.scdm.attributecomputer;


import com.thalesgroup.config.scdm.attributecomputer.common.OpmWinCrypt;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : OPMOPERATORopmopcryptedpassword.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class OPMOPERATORopmopcryptedpassword extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		SAXElement elt = walker.getCurrentNode();
		String passwordToCrypt = elt.getAttribute("OpmOperatorPassword");
		String cryptedPassword = OpmWinCrypt.crypt(passwordToCrypt);
		return cryptedPassword;
    }
}
