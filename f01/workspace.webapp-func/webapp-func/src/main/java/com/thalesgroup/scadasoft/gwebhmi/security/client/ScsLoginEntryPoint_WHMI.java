package com.thalesgroup.scadasoft.gwebhmi.security.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadasoft.gwebhmi.security.client.AppEntryPoint_WHMI_i.ProjectName;
import com.thalesgroup.prj_gz_cocc.gwebhmi.security.client.CoccLoginPanel;

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
        
        String mode = ConfigurationType.XMLFile.toString();
        String module = null;
        String folder = "UIConfig";
        String xml = "UILauncher.xml";
        String tag = "header";
        String keyname = "entrypoint";
        
        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
        web.getWebConfig(mode, module, folder, xml, tag, keyname, new WebConfigMgrEvent() {
			@Override
			public void updated(String value) {
				launch(value);
			}
			@Override
			public void failed() {
				launch(null);
			}
        });

    }
    
    private void launch(String key) {
    	
    	if ( null != key ) {
    		if ( ProjectName.C1166B.toString().equals(key) )  {
		    	launch_WHMI();
	 	   } else if ( ProjectName.COCC.toString().equals(key) )  {
		    	launch_COCC();
		    }
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
    	UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

    	CoccLoginPanel loginPanel_ = new CoccLoginPanel();
        loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
    }
    
    private void launch_WHMI () {
		final UIGws uiGws = new UIGws();
		uiGws.setDictionaryFolder("UIWidgetGeneric");
		uiGws.setPropertyFolder("UIInspectorPanel");
		uiGws.setViewXMLFile("UILayoutEntryPointLoginSummary/UILayoutEntryPointLoginSummary.view.xml");
		RootLayoutPanel.get().add(uiGws.getMainPanel());
    }
}
