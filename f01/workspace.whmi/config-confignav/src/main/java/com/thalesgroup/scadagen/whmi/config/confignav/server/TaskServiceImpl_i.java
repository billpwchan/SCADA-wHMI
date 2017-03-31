package com.thalesgroup.scadagen.whmi.config.confignav.server;

import java.io.File;

public interface TaskServiceImpl_i {
	
	final static String XML_TAG				= "option";
	
	final static String FILE_SEPARATOR		= File.separator;
	final static String SERVLET_ROOT_PATH	= "/";

	final static String MODULE_NAME			= "project.navigation.module";
	final static String MAPPING_NAME		= "project.navigation.module.mapping";
	final static String SETTING_NAME		= "project.navigation.module.setting";

	public enum AttributeValue {
		  OR("OR")
		, AND("AND")
		;
		private final String text;
		private AttributeValue(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }

	}
}
