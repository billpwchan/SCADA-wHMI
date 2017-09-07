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

public class Int2IntCompare extends SCSStatusComputer implements SGSymbol_i {
	
	private final Logger logger = LoggerFactory.getLogger(Int2IntCompare.class.getName());
	
	protected final String mappingname		= ".valuemapping.";
	
	protected final String fieldname1		= ".fieldname1";
	protected final String fieldname2		= ".fieldname2";
	
	protected String field1					= "eqpType";
	protected String field2					= "symbol1";
	
	// Operator and Value
	private final String STR_OPERATOR					= "operator";
	private final String STR_VALUE						= "value";								
	
	// Equality
	private final String STR_EQUAL_TO 					= "EQUAL_TO";
	
	// Relational
	private final String STR_NOT_EQUAL_TO				= "NOT_EQUAL_TO";
	private final String STR_GREATER					= "GREATER_THAN";
	private final String STR_GREATER_THAN_OR_EQUAL_TO	= "GREATER_THAN_OR_EQUAL_TO";
	private final String STR_LESS_THAN					= "LESS_THAN";
	private final String STR_LESS_THAN_OR_EQUAL_TO		= "LESS_THAN_OR_EQUAL_TO";
	
	// Conditional
	//private final String strAnd					= "AND";
	//private final String strOr					= "OR";
	
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
	
    public Int2IntCompare() {
    	
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
    	
    	// Load Int value
    	int inValue2 = util.loadIntValue(inputStatusByName, field2);
    	logger.debug("compute inValue2[{}]", inValue2);
    	
    	util.setPrefix(inValue1);

    	// Append the prefix if exists
    	String configPrefix = util.getConfigPrefix(mappings, inValue1);
    	logger.debug("compute configPrefix[{}]", configPrefix);
    	
    	boolean outValue = false;
    	
    	int counter = 1;
    	
    	logger.debug("compute configPrefix[{}]", new Object[]{configPrefix});
    	
    	String strOperatorNameEqp = configPrefix+util.getConfigAttribute(STR_OPERATOR+counter);
    	String strValueNameEqp = configPrefix+util.getConfigAttribute(STR_VALUE+counter);
    	
    	// Loading global setting if per-equipment setting not found
    	if ( null != configPrefix && configPrefix.isEmpty() ) {
        	strOperatorNameEqp = util.getConfigAttribute(STR_OPERATOR+counter);
        	strValueNameEqp = util.getConfigAttribute(STR_VALUE+counter);
    	}
    
        logger.debug("compute strOperatorNameEqp[{}] strValueNameEqp[{}]", new Object[]{strOperatorNameEqp, strValueNameEqp});
    	
    	String strOperator = mappings.get(strOperatorNameEqp);
    	String strValue = mappings.get(strValueNameEqp);

    	logger.debug("compute inValue2[{}] strOperator[{}] strValue[{}]", new Object[]{inValue2, strOperator, strValue});
    	
     	outValue = compare(inValue2, strOperator, strValue);

    	// Return value
    	AttributeClientAbstract<?> ret = util.getIntAttribute((outValue?1:0), true, new Date());
    	logger.debug("compute End");
		return ret;
	}

	private boolean compare(int leftValue, String operator, String strRigthValue) {
		boolean ret = false;
    	boolean isValid = false;
    	int rightValue = 0;
    	if ( null != operator ) {
    		try {
    			rightValue = Integer.parseInt(strRigthValue);
    			isValid = true;
    		} catch ( NumberFormatException e ) {
    			logger.warn("compare rightValue[{}] Integer.toString Exception[{}]", rightValue, e.toString());
    		}
    	}
    	if ( isValid ) {
    		ret = compare(leftValue, operator, rightValue);
    	}
    	logger.debug("compare leftValue[{}] operator[{}] strRigthValue[{}] ret[{}]", new Object[]{leftValue, operator, strRigthValue, ret});
    	return ret;
	}
	
	private boolean compare(int leftValue, String operator, int rigthValue) {
		boolean ret = false;
		
		if ( 0 == operator.compareTo(STR_EQUAL_TO) ) {
			ret = (leftValue == rigthValue);
		}
		else if ( 0 == operator.compareTo(STR_NOT_EQUAL_TO) ) {
			ret = (leftValue != rigthValue);
		}
		else if ( 0 == operator.compareTo(STR_GREATER) ) {
			ret = (leftValue > rigthValue);
		}	
		else if ( 0 == operator.compareTo(STR_GREATER_THAN_OR_EQUAL_TO) ) {
			ret = (leftValue >= rigthValue);
		} 
		else if ( 0 == operator.compareTo(STR_LESS_THAN) ) {
			ret = (leftValue < rigthValue);
		} 
		else if ( 0 == operator.compareTo(STR_LESS_THAN_OR_EQUAL_TO) ) {
			ret = (leftValue <= rigthValue);
		}
		
		// Request Java 1.7
//		switch(operator) {
//		case STR_EQUAL_TO: 						ret = (leftValue == rigthValue);
//		break;
//		case STR_NOT_EQUAL_TO: 					ret = (leftValue != rigthValue);
//		break;
//		case STR_GREATER: 						ret = (leftValue > rigthValue);
//		break;
//		case STR_GREATER_THAN_OR_EQUAL_TO: 		ret = (leftValue >= rigthValue);
//		break;
//		case STR_LESS_THAN: 					ret = (leftValue < rigthValue);
//		break;
//		case STR_LESS_THAN_OR_EQUAL_TO: 		ret = (leftValue <= rigthValue);
//		break;
//		}
		logger.debug("compare leftValue[{}] operator[{}] rigthValue[{}] ret[{}]", new Object[]{leftValue, operator, rigthValue, ret});
    	return ret;
	}
}
