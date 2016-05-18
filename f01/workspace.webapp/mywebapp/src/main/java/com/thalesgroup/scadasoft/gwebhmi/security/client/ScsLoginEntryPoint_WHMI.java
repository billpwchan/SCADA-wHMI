package com.thalesgroup.scadasoft.gwebhmi.security.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsLogin;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;

/**
 * Login application for MAESTRO demo.
 */
public class ScsLoginEntryPoint_WHMI implements EntryPoint {

    /**
     * Login application entry point
     */
    @Override
    public void onModuleLoad() {

        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());
        
        String module = null;
        String folder = "UIConfig";
        String xml = "UILauncher.xml";
        String tag = "header";
        String key = "entrypoint";
        
        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
//        web.getWebConfig( key, new WebConfigMgrEvent() {
//			
//			@Override
//			public void updated(String value) {
//				
//				launch(value);
//			}
//
//			@Override
//			public void failed() {
//				
//        		launch("");
//			}
//		});
        
        web.getWebConfig( module, folder, xml, tag, key, new WebConfigMgrEvent() {
			
			@Override
			public void updated(String value) {
				
				launch(value);
			}

			@Override
			public void failed() {
				
				launch("");
			}
        });

    }
    
    private void launch(String key) {
        
	    if ( 0 == key.compareTo("C1166B") )  {
	    	launch_WHMI();
	    } else if ( 0 == key.compareTo("COCC") )  {
	    	launch_COCC();
	    } else {
	    	launch_scstraining();
	    }
    }
    
    private void launch_scstraining () {
    	ScsLoginPanel loginPanel_ = new ScsLoginPanel();
        loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
    }
    
    private void launch_COCC () {
    	
    }
    
    private void launch_WHMI () {
		String SPRING_SEC_PROCESSING_URL = "j_spring_security_check";
		
		String user_name = "j_username";
		String pass_name = "j_password";
		
		String user = "chief";
		String pass = "thales";

		UIGwsLogin uiGwsLogin = new UIGwsLogin();
		uiGwsLogin.set(SPRING_SEC_PROCESSING_URL, user_name, user, pass_name, pass);
		RootLayoutPanel.get().add(uiGwsLogin.getMainPanel());
		uiGwsLogin.submit();
    }
}
