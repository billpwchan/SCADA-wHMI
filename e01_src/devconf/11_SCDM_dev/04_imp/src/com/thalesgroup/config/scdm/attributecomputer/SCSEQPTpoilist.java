package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : BASICEQUIPMENTpoi.java                                            */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES COMMUNICATIONS & SECURITY                               */
/*  CREATION DATE : 26 juin 2012                                              */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © T3S 1996-2012.                                                */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

/**
 * 
 */
public class SCSEQPTpoilist extends AttributeComputer {
	
	/**
	 * 
	 */
	final static private String __POI_LK__ = "link_poi";
	
	/**
	 * 
	 */
	final static private String __UNIVNAME_ATT__ = "UNIVNAME";

	/**
	 * 
	 */
	@Override
	@SuppressWarnings( "rawtypes" )
	public String compute( TreeWalker walker, ComputeContext context ) {
		String poi = "";
		Iterator it = walker.getLinkedNodeIDs( __POI_LK__ );
		while( it.hasNext() ) {
			String poi_path = (String)it.next();
			if( poi_path != null 
					&& !poi_path.trim().equals("") ) {
				walker.jumpToNode( poi_path );
				SAXElement poi_node = walker.getCurrentNode();
				if( poi_node != null ) {
					poi += "<alias>";
					poi += poi_node.getAttribute( __UNIVNAME_ATT__ );
				}
			}
		}
		
		return poi;
	}
	
}
