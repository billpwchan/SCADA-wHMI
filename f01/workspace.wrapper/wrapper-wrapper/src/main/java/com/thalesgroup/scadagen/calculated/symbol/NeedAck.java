package com.thalesgroup.scadagen.calculated.symbol;

import java.util.Date;
import java.util.Map;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.calculated.common.SGSymbol_i;
import com.thalesgroup.scadagen.calculated.util.Util;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpmSCADAgen;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

public class NeedAck extends SCSStatusComputer implements SGSymbol_i {
	
	private UILogger_i logger					= UILoggerFactory.getInstance().get(this.getClass().getName());
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	protected final String fieldname3		= ".fieldname3";
	protected final String fieldname4		= ".fieldname4";
	
	protected final String fieldname5		= ".fieldname5";
	
	protected final String fieldname6		= ".fieldname6";
	protected final String fieldname7		= ".fieldname7";
	
	protected final String fieldname8		= ".fieldname8";
	protected final String fieldname9		= ".fieldname9";
	protected final String fieldname10		= ".fieldname10";
	protected final String fieldname11		= ".fieldname11";
	
	protected String field1					= "eqpType";
	protected String field2					= "needack";
	
	protected String field3					= "func";
	protected String field4					= "geoc";
	
	protected String field5					= "UIOpmSCADAgen";
	
	protected String field6					= "A";
	protected String field7					= "1";
	
	protected String field8					= null;//"function";
	protected String field9					= null;//"location";
	protected String field10				= null;//"action";
	protected String field11				= null;//"mode";
	
	protected static Map<String, String> mappings	= null;
	
	protected static Util util = new Util();

	@Override
	public String getComputerId() {
		return m_name;
	}
	
	@Override
	public void loadCnf() {
		
		// Loading properties
    	mappings = util.loadMapping(m_name);
		
		field1	= mappings.get(fieldname1);
		field2	= mappings.get(fieldname2);
		field3	= mappings.get(fieldname3);
		field4	= mappings.get(fieldname4);
		
		field5	= mappings.get(fieldname5);
		
		field6	= mappings.get(fieldname6);
		field7	= mappings.get(fieldname7);
		
		field8	= mappings.get(fieldname8);
		field9	= mappings.get(fieldname9);
		field10 = mappings.get(fieldname10);
		field11 = mappings.get(fieldname11);
		
		
		// Field to Subscribe
		logger.debug("field1[{}] field2[{}] field3[{}] field4[{}]", new Object[]{field1, field2, field3, field4});
    	
		m_statusSet.add(field1);
		m_statusSet.add(field2);
		m_statusSet.add(field3);
		m_statusSet.add(field4);
		
	}
	
    public NeedAck() {
    	
    	m_name = this.getClass().getSimpleName();
    	
    	logger.debug("getComputerId[{}]", getComputerId());
    	
    	loadCnf();
        
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)
    {
		logger.debug("compute Begin");
		logger.debug("compute field1[{}] field2[{}]", field1, field2);
		
		// Reset Log
		util.setPrefix(m_name);
		
    	// Load eqpType value
    	String inValue1 = util.loadStringValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load inValue value
    	int inValue2 = util.loadIntValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	util.setPrefix(inValue1);
    	
    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	
    	int outValue1 = 0;
    	
    	if ( inValue2 > 0 ) {
        		
        	outValue1 = inValue2;
        	
        	String opmKey = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname5);
        	
        	logger.debug("compute opmKey[{}]", opmKey);
	    	
        	// Field Name: function, location, action, mode
        	String opmName1 = null, opmName2 = null, opmName3 = null, opmName4 = null;
        	// Field Value: function, location, action, mode
        	String opmValue1 = null, opmValue2 = null, opmValue3 = null, opmValue4 = null;
        	
        	int iOpmValue1 = util.loadIntValue(inputStatusByName, field3);
	    	opmValue1 = Integer.toString(iOpmValue1);
	    	
	    	int iOpmValue2 = util.loadIntValue(inputStatusByName, field4);
	    	opmValue2 = Integer.toString(iOpmValue2);
	    	
	    	opmValue3 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname6);
	    	if ( null == opmValue3 ) opmValue3 = field6;
	    	opmValue4 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname7);
	    	if ( null == opmValue4 ) opmValue4 = field7;
	    	
	    	logger.debug("compute opmValue1[{}] opmValue2[{}] opmValue3[{}] opmValue4[{}]", new Object[]{opmValue1, opmValue2, opmValue3, opmValue4});
	    	
	    	opmName1 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname8);
	    	if ( null == opmName1 ) opmName1 = field8;
	    	if ( null != opmName1 ) {
		    	opmName2 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname9);
		    	if ( null == opmName2 ) opmName2 = field9;
		    	opmName3 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname10);
		    	if ( null == opmName3 ) opmName3 = field10;
		    	opmName4 = util.getConfigPrefixMappingValue(mappings, configPrefix, fieldname11);
		    	if ( null == opmName4 ) opmName4 = field11;
	    	};
	    	logger.debug("compute opmName1[{}] opmName2[{}] opmName3[{}] opmName4[{}]", new Object[]{opmName1, opmName2, opmName3, opmName4});

        	// OPM
        	// Return value equals to number of point should be ack (Matched to .alarmSynthesis(3) / needack )
        	// Equals to 0 : no alarm need ack
        	// Positive value : number of alarm need ack in the same equipment with right
        	// Negative value : number of alarm need ack in the same equipment without right
        	
        	boolean result = false;

        	UIOpm_i uiOpm_i = UIOpmSCADAgen.getInstance();
        	
        	if ( null != uiOpm_i ) {
            	if ( null == opmName1 || null == opmName2 || null == opmName3 || null == opmName4 ) {
            		result = uiOpm_i.checkAccess(operatorOpmInfo, opmValue1, opmValue2, opmValue3, opmValue4);
            	} else {
            		result = uiOpm_i.checkAccess(operatorOpmInfo, opmName1, opmValue1, opmName2, opmValue2, opmName3, opmValue3, opmName4, opmValue4);
            	}
        	} else {
        		 logger.debug("compute uiOpm_i IS NULL");
        	}
        	
        	if ( ! result ) {
        		outValue1 = inValue2 * -1; 
        	}
    	}
//    	else 
//    	{
//    		outValue1 = inValue2;
//    	}
    	
    	// Return value
    	AttributeClientAbstract<?> ret = util.getIntAttribute(outValue1, true, new Date());
    	logger.debug("compute End");
		return ret;
	}
}
