package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIGwsCache {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIGwsCache.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UINameCard uiNameCard = null;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
        // Create a panel to hold all of the form widgets.
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("project-gwt-panel-gwscache");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        InlineLabel inlineLabel = new InlineLabel("Receiving cache from Web Server.....");
        inlineLabel.addStyleName("project-gwt-inlinelabel-gwscache-receivingcache");
        
        horizontalPanel.add(inlineLabel);
        
        logger.end(className, function);
        
		return horizontalPanel;
	}

}
