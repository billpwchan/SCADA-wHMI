package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.utils.LinkUtility;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : EQ_TYPECommunCat.java                                             */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public abstract class EQ_TYPECommunCat extends AttributeComputer {

	private static String ATTR1_ = "OpmCategory";
	/** 
	 * @brief Place here a synthetic description of the method’s role 	  
	 * Recupere l'id du lien pere ou mettre le libelle par defaut
	 * @param		
	 * @return		
	 * @throw	
	 */
	@SuppressWarnings("rawtypes")
	protected static String getCurrentOrParentLinkAttributValue(
		
			TreeWalker walker, ComputeContext context, String attributName, String attributeToComputeName ) {
		String att = null;
		Iterator it = walker.getLinkedNodeIDs( LinkUtility.getAttributeNameFromMCDAttributeName(attributName) );
		
		
		while( it.hasNext() ) {
			Object obj = it.next();
			
			if( obj != null ) {
				att=obj.toString();
			}
		}
		// if the current point has already been calculated
		// take the value not to follow the link
		String resultat = walker.getCurrentNode().getAttribute(attributeToComputeName);
		if (resultat != null && !resultat.equals(NOT_COMPUTED_VALUE)) {
			return resultat;
		}
		// foolow the link or go to parent node
		if( att == null || att.length() == 0 ) {
			TreeWalker parentWalker = walker.walkToParentNode();
			if( parentWalker.getCurrentPath() != null ) {
				// recursivity
				resultat = getCurrentOrParentLinkAttributValue( parentWalker, context, attributName, attributeToComputeName);
			}
		} else {
			resultat = walker.goToNode(att).getCurrentNode().getAttribute(ATTR1_); // walk to linked object
		}
		if (resultat == null) {
			return "0";
		} else {
		return resultat;
	}
	}
}
