package com.thalesgroup.scadagen.calculated.common;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.calculated.util.Util;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

public abstract class GDGColumnCheckAccess extends OlsDecoder {
	
	protected UILogger_i logger					= null;
	protected String logPrefix				= null;
	protected String classname				= null;

	
	protected final String modename1		= ".modename1";
	protected final String opmapiname1		= ".opmapiname1";
	protected final String numofactionname	= ".numofaction";
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	protected final String actionname		= ".actionname";
	protected final String actionresultname	= ".actionresultname";
	
	protected String opmapi					= null;
	
	protected String mode1					= null;
	protected int numofaction				= 0;
	protected String field1					= null;
	protected String field2					= null;
	protected String actions[]				= null;
	protected String stractionresults[]		= null;
	protected int actionresults[]			= null;

	protected String fieldNameValue1		= null;
	protected String fieldNameValue2		= null;
	
	private UIOpm_i uiOpm_i 				= null;
	
	protected static Util util = new Util();

	@Override
	public String getComputerId() {
		return classname;
	}
	
	/**
	 * Loading and store the configuration according to the class name as prefix
	 */
	protected void loadCnf() {

		logger = UILoggerFactory.getInstance().get(this.getClass().getName());
		
		classname = this.getClass().getSimpleName();
		
    	logPrefix = classname;
    	
    	logger.debug("[{}] classname[{}] getComputerId[{}]", new Object[]{logPrefix, classname, getComputerId()});
    	
    	mappings = util.loadMapping(m_name);
    	
    	opmapi	= mappings.get(opmapiname1);
		mode1	= mappings.get(modename1);

		field1	= mappings.get(fieldname1);
		field2	= mappings.get(fieldname2);
    	
		String strnumofaction = mappings.get(numofactionname);
		try {
			logger.debug("GDGColumnCheckAccess strnumofaction[{}]", strnumofaction);
			numofaction = Integer.parseInt(strnumofaction);
		} catch(NumberFormatException ex) {
			logger.warn("GDGColumnCheckAccess strnumofaction[{}] numofaction[{}] ex:"+ex.toString(), strnumofaction, numofaction);
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
			
			logger.debug("GDGColumnCheckAccess opmapi[{}]", opmapi);
			logger.debug("GDGColumnCheckAccess mode1[{}]", mode1);
			
			logger.debug("GDGColumnCheckAccess field1[{}]", field1);
			logger.debug("GDGColumnCheckAccess field2[{}]", field2);
			
			logger.debug("GDGColumnCheckAccess numofaction[{}]", numofaction);
			
			for ( int i = 0 ; i < numofaction ; i++ ) {
				logger.debug("GDGColumnCheckAccess actions({})[{}]", i, actions[i]);
				logger.debug("GDGColumnCheckAccess stractionresult1({})[{}]", i, stractionresults[i]);
				logger.debug("GDGColumnCheckAccess actionresults({})[{}]", i, actionresults[i]);
			}

		}
		
		m_name = classname;
		
		logger.debug("[{}] m_name[{}] classname[{}]", new Object[]{logPrefix, m_name, classname});

	}
	
    @Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
        AttributeClientAbstract<?> dataobj = super.compute(operatorOpmInfo, entityId, inputStatusByName, inputPropertiesByName);
        
        StringAttribute jsdata = (dataobj instanceof StringAttribute ? (StringAttribute) dataobj : null);
        
        String inValue1 = null;
        String inValue2 = null;
		
        if (jsdata != null) {
            String data = jsdata.getValue();
            // decode json
            ObjectNode root = null;
			try {
				root = (ObjectNode) s_json_mapper.readTree(data);

				String listname = getListName(entityId);
				logger.debug("[{}] entityId[{}] listname[{}]", new Object[]{logPrefix, entityId, listname});

				{
					String fieldname1key = getFieldKey(listname, fieldname1);
					logger.debug("[{}] listname[{}] fieldname1[{}] fieldname1key[{}]", new Object[]{logPrefix, listname, fieldname1, fieldname1key});
					
					fieldNameValue1 = mappings.get(fieldname1key);
					logger.debug("[{}] fieldname1key[{}] fieldNameValue1[{}]", new Object[]{logPrefix, fieldname1key, fieldNameValue1});
				
					inValue1 = root.get(fieldNameValue1).asText();
				}

				{
					String fieldname2key = getFieldKey(listname, fieldname2);
					logger.debug("[{}] listname[{}] fieldname2[{}] fieldname2key[{}]", new Object[]{logPrefix, listname, fieldname2, fieldname2key});

					fieldNameValue2 = mappings.get(fieldname2key);
					logger.debug("[{}] fieldname2key[{}] fieldNameValue2[{}]", new Object[]{logPrefix, fieldname2key, fieldNameValue2});
				
					inValue2 = root.get(fieldNameValue2).asText();
				}
				
				logger.debug("[{}] inValue1[{}] inValue2[{}]", new Object[]{logPrefix, inValue1, inValue2});
				
//				dataValue = Translation.getDBMessage(dataValue);
				
			} catch (JsonProcessingException e) {
				logger.warn("JsonProcessingException[{}]", e.toString());
			} catch (IOException e) {
				logger.warn("IOException[{}]", e.toString());
			}
        }

    	int outValue = 0;
    	
    	// OPM
    	uiOpm_i = OpmMgr.getInstance(opmapi);
    	logger.debug("opmapi is [{}]", opmapi);
    	logger.debug("uiOpm_i is [{}]", uiOpm_i);
    	boolean rights[] = new boolean[numofaction];
    	
    	for ( int i = 0 ; i < numofaction ; i++ ) {
        	if ( null != actions[i] ) {
        		rights[i] = uiOpm_i.checkAccess(operatorOpmInfo, inValue1, inValue2, actions[i], mode1);
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
