package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UICookies;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class SpringLogin {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SpringLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strCssForm = "project-SpringLogin-from";
	private final String strCssPanel = "project-SpringLogin-panel";
	private final String strCssLabel = "project-SpringLogin-label-connect";
	
	private String strMsgConnecting = "Connecting to Web Server......";
	
	private final String strSpringLogin = "SpringLoginLabel";
	
	private FormPanel form = null;
	private Hidden field1 = null, field2 = null;

	public SpringLogin(final String actionUrl, final String name1, final String name2) {
		String function = "SpringLogin";
		logger.begin(className, function);
		
		logger.debug(className, function, "actionUrl[{}] name1[{}] name2[{}]  ", new Object[]{actionUrl, name1, name2});

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
    	
        field1 = new Hidden();
        field1.setName(name1);
        
        field2 = new Hidden();
        field2.setName(name2);
        
        horizontalPanel.add(field1);
        horizontalPanel.add(field2);
        
        if ( null != UICookies.getCookies(strSpringLogin) ) {
        	strMsgConnecting = UICookies.getCookies(strSpringLogin);
        }
        
        Label label = new Label(strMsgConnecting);
        label.addStyleName(strCssLabel);
        
        horizontalPanel.add(label);
        
        // Fix the Chrome "Form submission canceled because the form is not connected"
        RootLayoutPanel.get().add(form);
        
        logger.end(className, function);
	}

	public void login(final String operator, final String password) {
		String function = "login";
		logger.begin(className, function);
		
		logger.debug(className, function, "operator[{}]", operator);
		
		field1.setValue(operator);
		field2.setValue(password);
		form.submit();
		
		logger.end(className, function);
	}
	
}
