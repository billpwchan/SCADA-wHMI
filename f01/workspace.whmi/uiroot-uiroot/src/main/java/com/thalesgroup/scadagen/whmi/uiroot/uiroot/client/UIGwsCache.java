package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;

public class UIGwsCache {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private final String strCssMainPanel	= "project-UIGwsCache-panel";
	private final String strCssLabel		= "project-UIGwsCache-label-receiving";
	private String strMsgReveiving 	= "Receiving configuration from Thales Web Server.....";
	
	private final String strUIGwsCacheLabel = "UIGwsCacheLabel";
	
	private UINameCard uiNameCard = null;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		final String function = "getMainPanel";
		
		logger.begin(function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
        // Create a panel to hold all of the form widgets.
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName(strCssMainPanel);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		if ( null != UICookies.getCookies(strUIGwsCacheLabel) ) {
			strMsgReveiving = UICookies.getCookies(strUIGwsCacheLabel);
		}

        Label label = new Label(strMsgReveiving);
        label.addStyleName(strCssLabel);
        
        horizontalPanel.add(label);
        
        logger.end(function);
        
		return horizontalPanel;
	}

}
