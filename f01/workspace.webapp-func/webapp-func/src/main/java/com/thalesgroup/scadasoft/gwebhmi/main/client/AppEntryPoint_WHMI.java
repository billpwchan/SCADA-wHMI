
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppEntryPoint_WHMI_i.FrameworkName;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppEntryPoint_WHMI_i.PropertiesName;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.AppPanel;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint_WHMI extends MwtEntryPointApp {
	
	final String mode = ConfigurationType.XMLFile.toString();
	final String module = null;
    final String folder = "UIConfig";
    final String xml = "UILauncher_AppEntryPoint_WHMI.xml";
    final String tag = "header";

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

        List<String> keys = new LinkedList<String>();
        for ( String properties : PropertiesName.toStrings() ) {
        	keys.add(properties);
        }
        
        UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
        web.getWebConfig( mode,  module, folder, xml, tag, keys, new WebConfigMgrEvent() {
			@Override
			public void updated(Map<String, String> keyValues) {
				launch(keyValues);
			}
			@Override
			public void failed() {
				launch(null);
			}
        });
    	
    }
    
    private void launch(Map<String, String> map) {

    	if ( null != map ) {
    		
    		String framework = map.get(PropertiesName.framework.toString());
    		
    		if ( null != framework ) {
            	if ( FrameworkName.SCADAgen.toString().equals(framework) )  {
            		launch_WHMI(map);
            	} 
            	else 
            	if ( FrameworkName.COCC.toString().equals(framework) )  {
            		launch_COCC(map);
        		} 
            	else {
        			launch_scstraining(map);
        		}
    		}
    	}
    }
    
    private void launch_scstraining (Map<String, String> map) {
        final AppPanel appPanel = new AppPanel();
        RootLayoutPanel.get().add(appPanel);
    }
    
    private void launch_COCC (Map<String, String> map) {
    	final CoccAppPanel appPanel = new CoccAppPanel();
        RootLayoutPanel.get().add(appPanel);
    }
    
    private void launch_WHMI (Map<String, String> map) {
    	
    	if ( null != map ) {

    		String dictionary	= map.get(PropertiesName.dictionary.toString());
    		String property 	= map.get(PropertiesName.property.toString());
    		String uiCtrl		= map.get(PropertiesName.uiCtrl.toString());
    		String uiView		= map.get(PropertiesName.uiView.toString());
    		String uiOpts		= map.get(PropertiesName.uiOpts.toString());
    		String element 		= map.get(PropertiesName.element.toString());
    		
    		final UIGws uiGws = new UIGws();
    		uiGws.setDictionaryFolder(dictionary);
    		uiGws.setPropertyFolder(property);
    		uiGws.setUICtrl(uiCtrl);
    		uiGws.setViewXMLFile(uiView);
    		uiGws.setOptsXMLFile(uiOpts);
    		uiGws.setElement(element);
    		uiGws.init();
    		RootLayoutPanel.get().add(uiGws.getMainPanel());
    		
    	}

    }
}
