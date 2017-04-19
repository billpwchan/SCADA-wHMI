package com.thalesgroup.scadagen.calculated.gdg.alarmevent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

public class GDGMessageCheckAccess extends SCSStatusComputer {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	protected String classname					= this.getClass().getSimpleName();
	protected String propertiesname				= null;
	
	protected final String fieldname1			= ".fieldname1";
	protected final String fieldname2			= ".fieldname2";
	protected final String actionname1			= ".actionname1";
	protected final String actionname2			= ".actionname2";
	protected final String actionname3			= ".actionname3";
	protected final String actionname4			= ".actionname4";
	protected final String actionresultname1	= ".actionresultname1";
	protected final String actionresultname2	= ".actionresultname2";
	protected final String actionresultname3	= ".actionresultname3";
	protected final String actionresultname4	= ".actionresultname4";
	protected final String modename1			= ".modename1";
	protected final String opmapiname1			= ".opmapiname1";
	
	protected String field1					= "function";
	protected String field2					= "location";
	protected String action1				= null;
	protected String action2				= null;
	protected String action3				= null;
	protected String action4				= null;
	protected String stractionresult1		= null;
	protected String stractionresult2		= null;
	protected String stractionresult3		= null;
	protected String stractionresult4		= null;	
	protected int actionresult1				= -1;
	protected int actionresult2				= -1;
	protected int actionresult3				= -1;
	protected int actionresult4				= -1;	
	protected String mode1					= "1";
	protected String opmapi					= null;
	
	private UIOpm_i uiOpm_i 				= null;
	
	protected static Map<String, String> mappings	= new HashMap<String, String>();

	@Override
	public String getComputerId() {
		return classname;
	}
	
    public GDGMessageCheckAccess() {
    	
    	String classnames [] = getComputerId().split(Pattern.quote("."));
    	propertiesname = classnames[classnames.length-1];
    	
    	logger.debug(propertiesname+" getComputerId[{}] propertiesname[{}]", getComputerId(), propertiesname);
    	
    	IConfigLoader configLoader		= ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties	= configLoader.getProjectConfigurationMap();
		if (properties != null) {
			
			// Load all setting with class prefix into buffer
			for ( String key : properties.keySet() ) {
				if ( key.startsWith(propertiesname) ) {
					mappings.put(key, properties.get(key));
				}
			}
		}
		
		field1	= mappings.get(propertiesname+fieldname1);
		field2	= mappings.get(propertiesname+fieldname2);
		action1	= mappings.get(propertiesname+actionname1);
		action2	= mappings.get(propertiesname+actionname2);
		action3	= mappings.get(propertiesname+actionname3);
		action4	= mappings.get(propertiesname+actionname4);
		stractionresult1	= mappings.get(propertiesname+actionresultname1);
		stractionresult2	= mappings.get(propertiesname+actionresultname2);
		stractionresult3	= mappings.get(propertiesname+actionresultname3);
		stractionresult4	= mappings.get(propertiesname+actionresultname4);		
		mode1	= mappings.get(propertiesname+modename1);
		opmapi	= mappings.get(propertiesname+opmapiname1);
		
		if ( null != stractionresult1 ) actionresult1	= Integer.parseInt(stractionresult1);
		if ( null != stractionresult2 ) actionresult2	= Integer.parseInt(stractionresult2);	
		if ( null != stractionresult3 ) actionresult3	= Integer.parseInt(stractionresult3);
		if ( null != stractionresult4 ) actionresult4	= Integer.parseInt(stractionresult4);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug(propertiesname+" field1[{}]", field1);
			logger.debug(propertiesname+" field2[{}]", field2);
			logger.debug(propertiesname+" action1[{}]", action1);
			logger.debug(propertiesname+" action2[{}]", action2);
			logger.debug(propertiesname+" action3[{}]", action3);
			logger.debug(propertiesname+" action4[{}]", action4);
			
			logger.debug(propertiesname+" actionresult1[{}]", stractionresult1);
			logger.debug(propertiesname+" actionresult2[{}]", stractionresult2);
			logger.debug(propertiesname+" actionresult3[{}]", stractionresult3);
			logger.debug(propertiesname+" actionresult4[{}]", stractionresult4);			
			
			logger.debug(propertiesname+" actionresult1[{}]", actionresult1);
			logger.debug(propertiesname+" actionresult2[{}]", actionresult2);
			logger.debug(propertiesname+" actionresult3[{}]", actionresult3);
			logger.debug(propertiesname+" actionresult4[{}]", actionresult4);
			
			logger.debug(propertiesname+" mode1[{}]", mode1);
			logger.debug(propertiesname+" opmapi[{}]", opmapi);
		}
		
		m_statusSet.add(field1);
    	m_statusSet.add(field2);
    	
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
		if ( logger.isDebugEnabled() ) {
			logger.debug("compute Begin");
			logger.debug("compute field1[{}]", field1);
			logger.debug("compute field2[{}]", field2);
			logger.debug("compute action1[{}]", action1);
			logger.debug("compute action2[{}]", action2);
			logger.debug("compute action3[{}]", action3);
			logger.debug("compute action4[{}]", action4);
			logger.debug("compute actionresult1[{}]", stractionresult1);
			logger.debug("compute actionresult2[{}]", stractionresult2);
			logger.debug("compute actionresult3[{}]", stractionresult3);
			logger.debug("compute actionresult4[{}]", stractionresult4);			
			logger.debug("compute mode1[{}]", mode1);
		}

    	// Load value 1
		int inValue1 = -1;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field1);
	    	if (obj1 != null && obj1 instanceof IntAttribute) {
	    		inValue1 = ((IntAttribute) obj1).getValue();
	    	} else {
	    		logger.warn("compute field1[{}] IS INVALID", field1);
	    	}
		}
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load value 2
    	String inValue2 = null;
    	{
	    	AttributeClientAbstract<?> obj1 = inputStatusByName.get(field2);
	    	if (obj1 != null && obj1 instanceof StringAttribute) {
	    		inValue2 = ((StringAttribute) obj1).getValue();
	    	} else {
	    		logger.warn("compute field2[{}] IS INVALID", field2);
	    	}
		}
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	int outValue1 = 0;
    	
    	// OPM
    	uiOpm_i = OpmMgr.getInstance(opmapi);
    	
    	String strInValue1 = Integer.toString(inValue1);
    	
    	logger.debug("compute inValue1[{}] strInValue2[{}] action2[{}] mode2[{}]", new Object[]{strInValue1, inValue2, action1, mode1});
    	
    	
    	if ( null != action1 ) {
	    	boolean right1 = uiOpm_i.checkAccess(operatorOpmInfo, strInValue1, inValue2, action1, mode1);
	    	
	    	logger.debug("compute right1[{}]", right1);
	    	
	    	if ( right1 ) outValue1 = actionresult1;
    	}
    	
    	logger.debug("compute inValue1[{}] strInValue2[{}] action2[{}] mode2[{}]", new Object[]{strInValue1, inValue2, action2, mode1});
    	if ( null != action2 ) {
        	
        	boolean right2 = uiOpm_i.checkAccess(operatorOpmInfo, strInValue1, inValue2, action2, mode1);
        	
        	logger.debug("compute right2[{}]", right2);
        	
        	if ( right2 ) outValue1 = actionresult2;
    	}
    	
    	logger.debug("compute inValue1[{}] strInValue2[{}] action3[{}] mode2[{}]", new Object[]{strInValue1, inValue2, action3, mode1});
    	if ( null != action3 ) {
        	
        	boolean right3 = uiOpm_i.checkAccess(operatorOpmInfo, strInValue1, inValue2, action3, mode1);
        	
        	logger.debug("compute right3[{}]", right3);
        	
        	if ( right3 ) outValue1 = actionresult3;
    	}
    	
    	logger.debug("compute inValue1[{}] strInValue2[{}] action4[{}] mode2[{}]", new Object[]{inValue1, inValue2, action4, mode1});
    	if ( null != action4 ) {
        	
        	boolean right4 = uiOpm_i.checkAccess(operatorOpmInfo, strInValue1, inValue2, action4, mode1);
        	
        	logger.debug("compute right4[{}]", right4);
        	
        	if ( right4 ) outValue1 = actionresult4;
    	}
    	
    	// Return value
    	IntAttribute ret = new IntAttribute();
        ret.setValue(outValue1);
        ret.setValid(true);
        ret.setTimestamp(new Date());

        if ( logger.isDebugEnabled() ) {
	        logger.debug("compute outValue1[{}], ret.getValue()[{}]", outValue1, ret.getValue());
	        logger.debug("compute End");
        }
		
		return ret;

	}
}
