package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.ExtendBitSet;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.SAXElementIterator;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : SCADASubsystemPreMask.java                                        */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 15 janv. 07                                               */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class SCADASubsystemPreMask extends AttributeComputer {
	
	// Map key
	private static String MAPKEY = "__SUBSYSTEMIDS__";
	
	// used attributes
	private static String ATTR1_ = "link_SCADASubsystem";
	private static String ATTR2_ = "SCADASubsystemPreMask";
	
	// to memorize the current pre-mask, field of 8 bytes
    private ExtendBitSet SCADASubsystemPreMask_ = new ExtendBitSet();
    
	@SuppressWarnings("unchecked")
	public String compute( TreeWalker walker, ComputeContext context ) {
		Map<String, ExtendBitSet> subsytemIds = null;
		if( !context.globalVariables.containsKey(MAPKEY) ) {
			subsytemIds = new Hashtable<String, ExtendBitSet>();
			SAXElementIterator it = null;
			try {
				it = context.query( "//SCADASubsystem" );
                it.setNodeFilter( null );
                SAXElement elt = (SAXElement)it.next();
				while( elt != null ) {
					String value = elt.getAttribute( "SCADASubsystemID" );
					subsytemIds.put( elt.getAttribute("ID"), new ExtendBitSet(Integer.valueOf(value)) );
					elt = (SAXElement)it.next();
				}
				context.globalVariables.put( MAPKEY, subsytemIds );
			} catch( Exception e ) {
				Logger.USER.error( "In SCADASubsystemPreMask.java : Can not fill the map of subsystem ids!" );
			} finally {
				if( it != null ) {
					it.release();
					it = null;
				}
			}
		} else {
			subsytemIds = (Map<String, ExtendBitSet>)context.globalVariables.get( MAPKEY );
		}
		
		// reset the pre-mask, the pre-mask has all it bits set into false
    	SCADASubsystemPreMask_.clear();
		
		// default value to return if :
		//  - no subsystemids
		//  - no subsystem set as link
		//  - no subsystem on parent class
		String retVal = SCADASubsystemPreMask_.toString();
		
		// if no subsystemid, no need to go further
		// return a default pre-mask with all bits set to 0
		if( !subsytemIds.isEmpty() ) {
			/**
			 *  IF a mask is set on the link, we compute all links set with a OR mask
			 *     and return this pre-mask
			 *  ELSE IF we look at the pre-mask of the parent class and if set 
			 *     we return the last one
			 *  ELSE we return a default pre-mask with all bits set to 0  
			 */
			
    	// loop on linked class
			boolean skip = false;
			Iterator<String> it = walker.getLinkedNodeIDs( ATTR1_ );
    	if( it.hasNext() ) {
    		while(it.hasNext()) {
    			String link = (String)it.next();
    			ExtendBitSet extendBitSet = subsytemIds.get(link);
    			if (extendBitSet!=null) {
    				SCADASubsystemPreMask_.or( extendBitSet );
    			} else {
    				return NOT_CALCULABLE;
    			}
    		}
    		
				// return a computed pre-mask
				skip = true;
				retVal = SCADASubsystemPreMask_.toString();
    	} 
    	
			// no subsytems defined, look at the parent,
			// careful, the parent could be the root node
			if( !skip ) {
    	SAXElement parentNode = walker.walkToParentNode().getCurrentNode();
    	if( parentNode != null ) {
					String parentSCADASubsytemPreMask = parentNode.getAttribute( ATTR2_ );
					if( parentSCADASubsytemPreMask != null ) {
    		// return the pre-mask of the parent
						retVal = parentSCADASubsytemPreMask;
    		}
				}
    	}
		}
    	
		return retVal;
	}
}
