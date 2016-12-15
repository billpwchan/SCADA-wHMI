package com.thalesgroup.scadagen.wrapper.wrapper.client.opm;

import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class SpringLogin {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SpringLogin.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private FormPanel form = null;
	private Hidden field1 = null, field2 = null;

	public SpringLogin(String actionUrl, String name1, String name2) {
		String function = "SpringLoginForm";
		
		logger.begin(className, function);
		
		logger.info(className, function, "actionUrl[{}] name1[{}] name2[{}]  ", new Object[]{actionUrl, name1, name2});

    	// Create a FormPanel and point it at a service.
        form = new FormPanel();
        form.addStyleName("project-gwt-panel-from");
		form.setWidth("100%");
		form.setHeight("100%");
        form.setAction(actionUrl);

        // Removing target attribute prevent 302 HTTP responses to be stored within a hidden iframe
        form.getElement().removeAttribute("target");
        
        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        //form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("project-gwt-panel-gwslogin");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHeight("100%");

        form.setWidget(horizontalPanel);
    	
        field1 = new Hidden();
        field1.setName(name1);
        
        field2 = new Hidden();
        field2.setName(name2);
        
        horizontalPanel.add(field1);
        horizontalPanel.add(field2);
        
        InlineLabel inlineLabel = new InlineLabel("Connecting to the Web Server.....");
        inlineLabel.addStyleName("project-gwt-inlinelabel-gwslogin-connecting");
        
        horizontalPanel.add(inlineLabel);
        
        logger.end(className, function);
	}

	public void login(String value1, String value2) {
		String function = "login";
		
		logger.begin(className, function);
		
		logger.info(className, function, "value1[{}] value2[{}]", value1, value2);
		
		field1.setValue(value1);
		
		field2.setValue(value2);
		
		form.submit();
		
		logger.end(className, function);
	}
	
}
