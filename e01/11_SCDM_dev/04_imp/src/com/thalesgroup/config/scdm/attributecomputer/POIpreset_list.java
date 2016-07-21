
package com.thalesgroup.config.scdm.attributecomputer;

/******************************************************************************/
/*  FILE  : POIpreset_list.java                                               */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES COMMUNICATIONS & SECURITY                               */
/*  CREATION DATE : 29 juin 2012                                              */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © T3S 1996-2012.                                                */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/


import java.util.Iterator;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/**
 * 
 */
public class POIpreset_list extends AttributeComputer {
	
	/**
	 * 
	 */
	final static char __SEPARATOR__ = ';';
	
	/**
	 * 
	 */
	final static private String __CAMERA_ID_ATT__ = "camera_id";
	
	/**
	 * 
	 */
	final static private String __PRESET_ID_ATT__ = "preset_id";
	
	/**
	 * 
	 */
	final static private String __CAMERA_LK__ = "link_camera";
	
	/**
	 * 
	 */
	final static private String __ORIENTATIONS_LK__ = "link_orientations";

	/**
	 * 
	 */
	@Override
	@SuppressWarnings( "rawtypes" )
	public String compute( TreeWalker walker, ComputeContext context ) {
		String preset_list = "";
		
		Iterator itOrientations = walker.getLinkedNodeIDs( __ORIENTATIONS_LK__ );
		while( itOrientations.hasNext() ) {
			String orientation_path = (String)itOrientations.next();
			if( orientation_path != null
					&& !orientation_path.trim().equals("") ) {
				walker.jumpToNode( orientation_path );
				SAXElement orientation_node = walker.getCurrentNode();
				if( orientation_node != null ) {
					String preset_id = orientation_node.getAttribute( __PRESET_ID_ATT__ );
					Iterator itCamera = walker.getLinkedNodeIDs( __CAMERA_LK__ );
					while( itCamera.hasNext() ) {
						String camera_path = (String)itCamera.next();
						if( camera_path != null
								&& !camera_path.trim().equals("") ) {
							walker.jumpToNode( camera_path );
							SAXElement camera_node = walker.getCurrentNode();
							String camera_id = camera_node.getAttribute( __CAMERA_ID_ATT__ );
							preset_list += camera_id;
							preset_list += __SEPARATOR__;
						}
					}
					preset_list += preset_id;
					preset_list += __SEPARATOR__;
				}
			}
		}
		
		if( preset_list.length() > 0 ) {
			if( preset_list.charAt(preset_list.length()-1) == __SEPARATOR__ ) {
				preset_list = preset_list.substring( 0, preset_list.length()-1 );
			}
		}
		
		return preset_list;
	}
	
}
