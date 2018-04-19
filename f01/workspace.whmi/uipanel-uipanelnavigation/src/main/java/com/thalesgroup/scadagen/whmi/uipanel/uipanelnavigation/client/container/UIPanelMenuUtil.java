package com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIPanelMenuUtil {
	
	private final static String cls = UIPanelMenuUtil.class.getName();
	private final static  String className = UIWidgetUtil.getClassSimpleName(cls);
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(UIWidgetUtil.getClassName(cls));
	
	//	Char	Escape String
	//	<	&lt;
	//	>	&gt;
	//	"	&quot;
	//	'	&apos;
	//	&	&amp;	
	public static String backwardConvertXMLTag(final String element) {
		final String function = "backwardConvertXMLTag";
		logger.begin(className, function);
		logger.debug(className, function, "element[{}]", element);
		String ret = element;
		
		ret = ret.replace("&amp;", "&");
		ret = ret.replace("&lt;", "<");
		ret = ret.replace("&gt;", ">");
		ret = ret.replace("&quot;", "\"");
		ret = ret.replace("&apos;", "'");
		
		logger.debug(className, function, "ret[{}]", ret);
		return ret;
	}
}
