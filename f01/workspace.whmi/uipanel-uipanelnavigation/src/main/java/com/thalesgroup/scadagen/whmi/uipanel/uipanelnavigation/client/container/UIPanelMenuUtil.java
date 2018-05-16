package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIPanelMenuUtil {

	private final static UILogger_i logger = UILoggerFactory.getInstance().getUILogger(UIPanelMenuUtil.class.getName());
	
	//	Char	Escape String
	//	<	&lt;
	//	>	&gt;
	//	"	&quot;
	//	'	&apos;
	//	&	&amp;	
	public static String backwardConvertXMLTag(final String element) {
		final String f = "backwardConvertXMLTag";
		logger.begin(f);
		logger.debug(f, "element[{}]", element);
		String ret = element;
		
		ret = ret.replace("&amp;", "&");
		ret = ret.replace("&lt;", "<");
		ret = ret.replace("&gt;", ">");
		ret = ret.replace("&quot;", "\"");
		ret = ret.replace("&apos;", "'");
		
		logger.debug(f, "ret[{}]", ret);
		return ret;
	}
}
