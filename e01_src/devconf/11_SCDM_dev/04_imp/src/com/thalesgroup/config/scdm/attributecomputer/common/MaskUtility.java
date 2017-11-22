package com.thalesgroup.config.scdm.attributecomputer.common;

/******************************************************************************/
/*  FILE  : MaskManager.java                                                  */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 15 janv. 07                                               */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/


public class MaskUtility {

	static public int or(String val1, int val2) {
		int valInt1 = Integer.valueOf(val1);
		return or(valInt1, val2);
	}
	
	static public int or(int val1, int val2) {
		return (val1 |= val2);
	}

}
