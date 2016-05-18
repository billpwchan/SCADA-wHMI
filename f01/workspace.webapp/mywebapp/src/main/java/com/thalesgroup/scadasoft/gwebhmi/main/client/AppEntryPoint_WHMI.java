
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.AppPanel;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint_WHMI extends MwtEntryPointApp {

	/**
     * Constructor.
     */
    public AppEntryPoint_WHMI() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {

        String module = null;
        String folder = "UIConfig";
        String xml = "UILauncher.xml";
        String tag = "header";
        String key = "entrypoint";
        
        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
//        web.getWebConfig( key, new WebConfigMgrEvent() {
//			@Override
//			public void updated(String value) {
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
        final AppPanel appPanel = new AppPanel();
        RootLayoutPanel.get().add(appPanel);
    }
    
    private void launch_COCC () {

    }
    
    private void launch_WHMI () {
		final UIGws uiGws = new UIGws();
		RootLayoutPanel.get().add(uiGws.getMainPanel());
    }
}
