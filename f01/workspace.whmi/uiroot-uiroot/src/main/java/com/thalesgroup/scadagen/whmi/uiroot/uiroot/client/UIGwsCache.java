package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIGwsCache {
	
	private static Logger logger = Logger.getLogger(UIGwsCache.class.getName());
	
	private UINameCard uiNameCard = null;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
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
        
        logger.log(Level.FINE, "getMainPanel End");
        
		return horizontalPanel;
	}

}
