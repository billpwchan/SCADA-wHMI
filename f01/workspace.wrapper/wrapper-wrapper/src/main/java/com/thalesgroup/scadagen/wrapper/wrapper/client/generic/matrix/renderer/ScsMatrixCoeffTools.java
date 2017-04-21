package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer;

import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class ScsMatrixCoeffTools {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(ScsMatrixCoeffTools.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static final String EMPTY_COEFFICIENT = "";

	/**
     * Compute Intersection Coefficient Text
     * @param mxIntersectionState Matrix Intersection State
     * @return Intersection Coefficient Text
     */
    public static String getCoefficientText(final MxIntersectionState mxIntersectionState, final String coeffAttributeName) {
        final Map<String, EntityClient> listEntities = mxIntersectionState.getListEntities();
        final EntityClient entity =
            listEntities != null && coeffAttributeName != null && !listEntities.isEmpty() ? listEntities.values().iterator().next() : null;
        return entity != null ? getCoefficientTextEntity(entity, coeffAttributeName) : EMPTY_COEFFICIENT;
    }
    
    private static String getCoefficientTextEntity(final EntityClient entity, final String coeffAttributeName) {
    	final String function = "getCoefficientTextEntity";
    	final String value = getAttributeValue(entity, coeffAttributeName);
    	
    	if (value != null) {
    		return (Translation.getWording("&" + coeffAttributeName + "_" + value));
    	} else {
    		logger.debug(className, function, "Warning: getAttributeValue for coeffAttributeName [{}] return null ", coeffAttributeName);
    	}

    	return EMPTY_COEFFICIENT;
    }
    
    private static String getAttributeValue(final EntityClient entity, final String statusName) {
        final AttributeClientAbstract<Object> attribute = entity != null ? entity.getAttribute(statusName) : null;
        final String attributeValue = attribute != null ? String.valueOf(attribute.getValue()) : null;
        return attributeValue;
    }
    
}
