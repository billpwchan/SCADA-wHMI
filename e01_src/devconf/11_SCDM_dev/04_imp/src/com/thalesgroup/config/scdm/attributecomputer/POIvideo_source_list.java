
package com.thalesgroup.config.scdm.attributecomputer;

/******************************************************************************/
/*  FILE  : POIvideo_source_list.java                                         */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES COMMUNICATIONS & SECURITY                               */
/*  CREATION DATE : 27 juin 2012                                              */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © T3S 1996-2012.                                                */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/


import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/**
 * 
 */
public class POIvideo_source_list extends AttributeComputer {
	
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
	final static private String __SEQUENCE_ID_ATT__ = "sequence_id";
	
	/**
	 * 
	 */
	final static private String __VIDEO_SOURCE_LK__ = "link_video_source";
	
	/**
	 * 
	 */
	final static private String __SOURCE_CAMERAS_LK__ = "link_cameras";
	
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	final static private Map<String, String> __MAP__ = Collections.unmodifiableMap(
			new Hashtable<String, String>() {{
				put( "CAMERA"                 , "ScsTvs::switchSingleCamera"       );
				put( "VIDEO_SEQUENCE"         , "ScsTvs::switchOnTheFlySequence"   );
				put( "EXTERNAL_VIDEO_SEQUENCE", "ScsTvs::switchPredefinedSequence" );
			}}
	);


	/**
	 * 
	 */
	@Override
	@SuppressWarnings( "rawtypes" )
	public String compute( TreeWalker walker, ComputeContext context ) {
		String source_list = "";
		
		Iterator itVideoSource = walker.getLinkedNodeIDs( __VIDEO_SOURCE_LK__ );
		while( itVideoSource.hasNext() ) {
			String video_source_path = (String)itVideoSource.next();
			if( video_source_path != null
					&& !video_source_path.trim().equals("") ) {
				walker.jumpToNode( video_source_path );
				SAXElement video_source_node = walker.getCurrentNode();
				if( video_source_node != null ) {
					String node_type = walker.getCurrentNodeType();
					if( node_type != null ) {
						source_list += __MAP__.get( node_type );
						source_list += __SEPARATOR__;
					
						SAXElement node = walker.getCurrentNode();
						if( node_type.equals("CAMERA") ) {
							String camera_id = node.getAttribute( __CAMERA_ID_ATT__ );
							source_list += camera_id;
						} else if( node_type.equals("VIDEO_SEQUENCE") ) {
							Iterator itCameras = walker.getLinkedNodeIDs( __SOURCE_CAMERAS_LK__ );
							while( itCameras.hasNext() ) {
								String camera_path = (String)itCameras.next();
								if( camera_path != null
										&& !camera_path.trim().equals("") ) {
									walker.jumpToNode( camera_path );
									SAXElement camera_node = walker.getCurrentNode();
									String camera_id = camera_node.getAttribute( __CAMERA_ID_ATT__ );
									if( camera_id != null 
											&& !camera_id.trim().equals("") ) {
										source_list += camera_id;
										source_list +=',';
									}
								}								
							}
							if( source_list.charAt(source_list.length()-1) == ',' ) {
								source_list = source_list.substring( 0, source_list.length()-1 );
							}
						} else if( node_type.equals("EXTERNAL_VIDEO_SEQUENCE") ) {
							String sequence_id = node.getAttribute( __SEQUENCE_ID_ATT__ );
							source_list += sequence_id;
						}
					}
				}
			}
			source_list += __SEPARATOR__;
		}
		
		if( source_list.length() > 0 ) {
			if( source_list.charAt(source_list.length()-1) == __SEPARATOR__ ) {
				source_list = source_list.substring( 0, source_list.length()-1 );
			}
		}
		
		return source_list;
	}
	
}
