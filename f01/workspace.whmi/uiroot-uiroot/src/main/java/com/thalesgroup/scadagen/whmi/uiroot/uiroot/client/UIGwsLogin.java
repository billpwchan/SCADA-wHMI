package com.thalesgroup.scadagen.whmi.uiroot.uiroot.client;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;

/**
 * @author syau
 * Auto submit page
 */
public class UIGwsLogin {
	
	private final String strCssForm		= "project-UIGwsLogin-from";
	private final String strCssPanel	= "project-UIGwsLogin-panel";
	private final String strCssLabel	= "project-UIGwsLogin-label-connect";
	
	private String strMsgConnecting = "Connecting to Thales Web Server......";
	
	private final String strUIGwsLoginLabel = "UIGwsLoginLabel";
	
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
        form.addStyleName(strCssForm);
        form.setAction(actionUrl);

        // Removing target attribute prevent 302 HTTP responses to be stored within a hidden iframe
        form.getElement().removeAttribute("target");
        
        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        //form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName(strCssPanel);

        form.setWidget(horizontalPanel);
    	
        Hidden field1 = new Hidden();
        field1.setName(name1);
        field1.setValue(value1);
        
        Hidden field2 = new Hidden();
        field2.setName(name2);
        field2.setValue(value2);
        
        horizontalPanel.add(field1);
        horizontalPanel.add(field2);
        
        if ( null != UICookies.getCookies(strUIGwsLoginLabel) ) {
        	strMsgConnecting = UICookies.getCookies(strUIGwsLoginLabel);
        }
        
        Label label = new Label(strMsgConnecting);
        label.addStyleName(strCssLabel);
        
        horizontalPanel.add(label);

        return form;
    	
    }
}
