package com.thalesis.config.utils;

/******************************************************************************/
/*  FILE  : Chrono.java									                      */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/11                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/** classe permettant de faire des tests de performances */
public class Chrono {
	private long startTime;
	private long endTime;

	/** constructeur avec un start par défaut */
	public Chrono() {
		start();
	}
 
	/** démarrage du chrono */
	public void start() {
		startTime = System.currentTimeMillis();
	}

	/** retourne le delai en milli-secondes entre le start et le end */
	public long end() {
		endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
}