package com.thalesgroup.scadagen.calculated.symbol;

import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.calculated.common.SGSymbol_i;
import com.thalesgroup.scadagen.calculated.util.Util;

public class IntMapping extends SCSStatusComputer implements SGSymbol_i {
	
	private final Logger logger = LoggerFactory.getLogger(IntMapping.class.getName());

	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected String field1					= "eqpType";
	protected String field2					= "symbol1";
	
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
	
    public IntMapping() {
    	
    	m_name = this.getClass().getSimpleName();
    	
    	logger.debug("getComputerId[{}]", getComputerId());

    	loadCnf();
    	
    }

	@Override
    public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName)

    {
		logger.debug("compute field1[{}] field2[{}]", field1, field2);
    	
    	// Load eqpType value
    	String inValue1 = util.loadStringValue(inputStatusByName, field1);
    	logger.debug("compute inValue1[{}]", inValue1);
    	
    	// Load Int value
    	int inValue2 = util.loadIntValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	util.setPrefix(inValue1);

    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	
    	// Find mapping
    	String outValue1 = null;
    	{
    		String keyToMap = null;
    		try {
    			keyToMap = configPrefix+mappingname+Integer.toString(inValue2);
    		} catch ( Exception e ) {
    			logger.warn("compute Integer.toString Exception[{}]", e.toString());
    		}
    		logger.debug("compute keyToMap[{}]", keyToMap);
	    	outValue1 = mappings.get(keyToMap);
    	}
    	logger.debug("compute outValue1[{}]", outValue1);	
    	
    	// Return value
    	logger.debug("compute outValue1[{}]", outValue1);
    	AttributeClientAbstract<?> ret = util.getStringAttribute(outValue1, true, new Date());

        logger.debug("compute ret.getValue()[{}]", ret.getValue());
		
		return ret;

	}
    

}
