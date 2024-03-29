package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class UIGwsLogin {
	
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	private FormPanel form = null;
	
	private String actionUrl;
	private String name1, value1;
	private String name2, value2;
	
	public void set(String actionUrl, String name1, String value1, String name2, String value2) {
		this.actionUrl = actionUrl;
		this.name1 = name1;
		this.value1 = value1;
		this.name2 = name2;
		this.value2 = value2;
	}
	
	public void submit() {
		form.submit();
	}
	
	public FormPanel getMainPanel() {
    	
    	// Create a FormPanel and point it at a service.
        form = new FormPanel();
        form.setAction(actionUrl);

        // Removing target attribute prevent 302 HTTP responses to be stored within a hidden iframe
        form.getElement().removeAttribute("target");
        
        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        //form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHeight("100%");
		horizontalPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setWidth("100%");
		verticalPanel.setHeight("100%");
		verticalPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		verticalPanel.add(horizontalPanel);
        
        form.setWidget(verticalPanel);
    	
        Hidden field1 = new Hidden();
        field1.setName(name1);
        field1.setValue(value1);
        
        Hidden field2 = new Hidden();
        field2.setName(name2);
        field2.setValue(value2);
        
        horizontalPanel.add(field1);
        horizontalPanel.add(field2);
        
        horizontalPanel.add(new Label("Connecting to the Web Server..."));
        
        return form;
    	
    }
}
