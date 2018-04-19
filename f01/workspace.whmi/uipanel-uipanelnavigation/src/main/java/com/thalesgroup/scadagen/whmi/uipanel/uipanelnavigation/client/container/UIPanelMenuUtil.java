package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class UIPanelMenuUtil {

	private final static String className = UIPanelMenuUtil.class.getSimpleName();
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(UIPanelMenuUtil.class.getName());
	
	//	Char	Escape String
	//	<	&lt;
	//	>	&gt;
	//	"	&quot;
	//	'	&apos;
	//	&	&amp;	
	public static String backwardConvertXMLTag(final String element) {
		final String f = "backwardConvertXMLTag";
		logger.begin(className, f);
		logger.debug(className, f, "element[{}]", element);
		String ret = element;
		
		ret = ret.replace("&amp;", "&");
		ret = ret.replace("&lt;", "<");
		ret = ret.replace("&gt;", ">");
		ret = ret.replace("&quot;", "\"");
		ret = ret.replace("&apos;", "'");
		
		logger.debug(className, f, "ret[{}]", ret);
		return ret;
	}
}
