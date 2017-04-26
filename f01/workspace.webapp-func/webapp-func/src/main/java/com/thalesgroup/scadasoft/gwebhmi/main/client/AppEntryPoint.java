
package com.thalesgroup.scadasoft.gwebhmi.main.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.base.MwtEntryPointApp;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.shared.DictionaryCacheInterface.ConfigurationType;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppEntryPoint_i.FrameworkName;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppEntryPoint_i.PropertiesName;
import com.thalesgroup.scadasoft.gwebhmi.main.client.factory.COCC;
import com.thalesgroup.scadasoft.gwebhmi.main.client.factory.FAS;
import com.thalesgroup.scadasoft.gwebhmi.main.client.factory.IAppEntryPoint;
import com.thalesgroup.scadasoft.gwebhmi.main.client.factory.WHMI;

/**
 * Hypervisor showcase's entry point : everything starts here !
 */
public class AppEntryPoint extends MwtEntryPointApp {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[AppEntryPoint] ";
	
	private final static String mode = ConfigurationType.XMLFile.toString();
	private final static String module = null;
	private final static String folder = "UIConfig";
    private final static String xml = "UILauncher_AppEntryPoint.xml";
    private final static String tag = "header";

	/**
     * Constructor.
     */
    public AppEntryPoint() {
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
				LOGGER.debug(LOG_PREFIX+"onModuleLoad updated");
				launch(keyValues);
			}
			@Override
			public void failed() {
				LOGGER.debug(LOG_PREFIX+"onModuleLoad failed");
				launch(null);
			}
        });
    }
    
    private void launch(Map<String, String> map) {
    	
    	IAppEntryPoint iAppEntryPoint = null;

    	if ( null != map ) {
    		
    		String framework = map.get(PropertiesName.framework.toString());
    		
    		LOGGER.debug(LOG_PREFIX+"launch framework["+framework+"]");

    		iAppEntryPoint = getEntryPoint(framework);
    	} else {
    		LOGGER.warn(LOG_PREFIX+"launch map IS NULL");
    	}
    	
		if ( null != iAppEntryPoint ) {
			iAppEntryPoint.launch(map);
		} else {
			LOGGER.warn(LOG_PREFIX+"launch iAppEntryPoint IS NULL");
		}
    	
    }
    
    private IAppEntryPoint getEntryPoint(String key) {
    	IAppEntryPoint iAppEntryPoint = null;
    	
    	LOGGER.debug(LOG_PREFIX+"getEntryPoint key["+key+"]");
    	if ( null != key ) {
        	if ( FrameworkName.SCADAgen.toString().equals(key) )  {
        		iAppEntryPoint = new WHMI();
        	} 
        	else if ( FrameworkName.FAS.toString().equals(key) )  {
        		iAppEntryPoint = new FAS();
    		} 
        	else if ( FrameworkName.COCC.toString().equals(key) )  {
        		iAppEntryPoint = new COCC();
    		} 
        	else {
        		iAppEntryPoint = new FAS();
    		}
    	} else {
    		LOGGER.warn(LOG_PREFIX+"getEntryPoint key IS NULL");
    	}
    	
    	return iAppEntryPoint;
    }

}
