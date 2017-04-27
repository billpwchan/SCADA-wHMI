package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.calculated.util.Util;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

public class GDGMessageCheckAccess extends SCSStatusComputer {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected final String modename1			= ".modename1";
	protected final String opmapiname1			= ".opmapiname1";
	
	protected final String numofactionname		= ".numofaction";
	protected final String fieldname1			= ".fieldname1";
	protected final String fieldname2			= ".fieldname2";
	
	protected final String actionname			= ".actionname";
	protected final String actionresultname		= ".actionresultname";

	protected String opmapi					= null;
	protected String mode1					= "1";
	
	protected int numofaction				= 0;
	protected String field1					= "function";
	protected String field2					= "location";
	
	protected String actions[]				= null;
	protected String stractionresults[]		= null;
	protected int actionresults[]			= null;

	private UIOpm_i uiOpm_i 				= null;
	
	protected static Map<String, String> mappings	= new HashMap<String, String>();
	
	protected static Util util = new Util();

	@Override
	public String getComputerId() {
		return m_name;
	}
	
	public void loadCnf() {
		mappings = util.loadMapping(m_name);

		opmapi	= mappings.get(opmapiname1);
		mode1	= mappings.get(modename1);

		field1	= mappings.get(fieldname1);
		field2	= mappings.get(fieldname2);

		String strnumofaction = mappings.get(numofactionname);
		try {
			logger.debug("GDGMessageCheckAccess strnumofaction[{}]", strnumofaction);
			numofaction = Integer.parseInt(strnumofaction);
		} catch(NumberFormatException ex) {
			logger.warn("GDGMessageCheckAccess strnumofaction[{}] numofaction[{}] ex:"+ex.toString(), strnumofaction, numofaction);
		}		
		
		actions = new String[numofaction];
		stractionresults = new String[numofaction];
		actionresults = new int[numofaction];
		for ( int i = 0 ; i < numofaction ; i++ ) {
			actions[i] = mappings.get(actionname+i);
			stractionresults[i]	= mappings.get(actionresultname+i);
			if ( null != stractionresults[i] ) actionresults[i]	= Integer.parseInt(stractionresults[i]);
		}

		if ( logger.isDebugEnabled() ) {
			
			logger.debug("GDGMessageCheckAccess opmapi[{}]", opmapi);
			logger.debug("GDGMessageCheckAccess mode1[{}]", mode1);
			
			logger.debug("GDGMessageCheckAccess field1[{}]", field1);
			logger.debug("GDGMessageCheckAccess field2[{}]", field2);
			
			logger.debug("GDGMessageCheckAccess numofaction[{}]", numofaction);
			
			for ( int i = 0 ; i < numofaction ; i++ ) {
				logger.debug("GDGMessageCheckAccess actions({})[{}]", i, actions[i]);
				logger.debug("GDGMessageCheckAccess stractionresult1({})[{}]", i, stractionresults[i]);
				logger.debug("GDGMessageCheckAccess actionresults({})[{}]", i, actionresults[i]);
			}

		}
		
		m_statusSet.add(field1);
    	m_statusSet.add(field2);
	}
	
    public GDGMessageCheckAccess() {
    	
    	m_name = this.getClass().getSimpleName();
    	
    	logger.debug("getComputerId[{}]", getComputerId());

        loadCnf();
    	
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)
    {
		logger.debug("compute Begin");
		if ( logger.isDebugEnabled() ) {
			logger.debug("compute field1[{}] field2[{}]", field1, field2);
			logger.debug("compute mode1[{}]", mode1);
			for ( int i = 0 ; i < numofaction ; i++ ) {
				logger.debug("compute actions({})[{}] actionresults({})[{}]", new Object[]{i, actions[i], i, stractionresults[i]});
			}
		}
		
		// Reset log
		util.setPrefix(m_name);

    	// Load value 1
    	int inValue1 = util.loadIntValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load value 2
    	String inValue2 = util.loadStringValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	int outValue = 0;
    	
    	// OPM
    	uiOpm_i = OpmMgr.getInstance(opmapi);
    	
    	String strInValue1 = Integer.toString(inValue1);
    	
    	boolean rights[] = new boolean[numofaction];
    	
    	for ( int i = 0 ; i < numofaction ; i++ ) {
        	if ( null != actions[i] ) {
        		rights[i] = uiOpm_i.checkAccess(operatorOpmInfo, strInValue1, inValue2, actions[i], mode1);
    	    	if ( rights[i] ) outValue = actionresults[i];
    	    	logger.debug("compute rights({})[{}] outValue[{}] actionresults[{}]", new Object[]{i, rights[i], outValue, actionresults[i]});
        	}
    	}
    	
    	// Return value
    	AttributeClientAbstract<?> ret = util.getIntAttribute(outValue, true, new Date());
    	logger.debug("compute End");
		return ret;
	}
}
