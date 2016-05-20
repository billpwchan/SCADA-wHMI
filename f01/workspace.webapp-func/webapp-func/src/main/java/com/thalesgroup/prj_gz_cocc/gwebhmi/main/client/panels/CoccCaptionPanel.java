package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;

public class CoccCaptionPanel extends DockLayoutPanel {

	private final HTML caption_;
	
	/**
     * Constructor.
     * @param caption the caption
     */
    public CoccCaptionPanel(final String caption) {
        super(Unit.PX);
        
        caption_ = new HTML(caption);
        caption_.setStyleName("cocc-captionPanel-header");
        
        this.setStyleName("cocc-captionPanel-container");
        
        final int widgetSize = 30;
        this.addNorth(caption_, widgetSize);
        
    }
    
    /**
     * Constructor.
     */
    public CoccCaptionPanel() {
        this("");
    }
    
    /**
     * Set the caption of the panel.
     * @param caption the caption
     */
    public void setCaption(final String caption) {
        caption_.setText(caption);
    }

}
