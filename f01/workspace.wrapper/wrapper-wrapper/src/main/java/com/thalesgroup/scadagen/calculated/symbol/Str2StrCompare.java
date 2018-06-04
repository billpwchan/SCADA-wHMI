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

public class Str2StrCompare extends SCSStatusComputer implements SGSymbol_i {
	
	private UILogger_i logger					= UILoggerFactory.getInstance().get(this.getClass().getName());
	
	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected String field1					= "eqpType";
	protected String field2					= "symbol1";
	
	// Operator and Value
	private final String STR_VALUE			= "value";
	
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
		
		field1 = mappings.get(fieldname1);
		field2 = mappings.get(fieldname2);
		
		// Field to Subscribe
		logger.debug("field1[{}] field2[{}]", new Object[]{field1, field2});
    	
		m_statusSet.add(field1);
    	m_statusSet.add(field2);
		
	}
	
    public Str2StrCompare() {
    	
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
		
		// Reset log
		util.setPrefix(m_name);
    	
    	// Load eqpType value
    	String inValue1 = util.loadStringValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load String value
    	String inValue2 = util.loadStringValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	util.setPrefix(inValue1);

    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	
    	boolean outValue = false;
    	
    	logger.debug("compute configPrefix[{}]", new Object[]{configPrefix});
    	
    	String strValueNameEqp = configPrefix+util.getConfigAttribute(STR_VALUE);
    	
    	// Loading global setting if per-equipment setting not found
    	if ( null != configPrefix && configPrefix.isEmpty() ) {
        	strValueNameEqp = util.getConfigAttribute(STR_VALUE);
    	}
    
        logger.debug("compute strValueNameEqp[{}]", new Object[]{strValueNameEqp});
    	
    	String strValue = mappings.get(strValueNameEqp);

    	logger.debug("compute inValue2[{}] strValue[{}]", new Object[]{inValue2, strValue});
    	
    	if ( null != inValue2 && null != strValue) {
    		outValue = (0==inValue2.compareTo(strValue));
    	}
     	
    	// Return value
    	AttributeClientAbstract<?> ret = util.getIntAttribute((outValue?1:0), true, new Date());
    	logger.debug("compute End");
		return ret;
	}

}
