package com.thalesgroup.scadasoft.stresstest.calculated.widget;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.scadasoft.stresstest.calculated.SCSStatusComputer;

public class SectionState extends SCSStatusComputer {

	public static int C_ENABLED_NOTTRIPPED = 0;
	public static int C_ENABLED_TRIPPED = 1;
	public static int C_DISABLED_NOTTRIPPED = 2;
	public static int C_DISABLED_TRIPPED = 3;

	public static int C_DEFAULT = C_ENABLED_NOTTRIPPED;

	static final Logger logger = LoggerFactory.getLogger("SectionState");
	
	public SectionState() {

		m_statusSet.add("tripState");
		m_statusSet.add("tripEnable");

		m_name = "com.thalesgroup.scadasoft.stresstest.calculated.widget.SectionState";
	}

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
			String entityId,
			Map<String, AttributeClientAbstract<?>> inputStatusByName,
			Map<String, Object> inputPropertiesByName) 
	{

		// return valid
		IntAttribute ret = new IntAttribute();
		ret.setValue(C_DEFAULT);
		ret.setValid(true);
		ret.setTimestamp(new Date());

		// default input value
		int state = 0;
		int enable = 0;

		// get real input value
		AttributeClientAbstract<?> obj = inputStatusByName.get("tripState");
		if (obj != null && obj instanceof IntAttribute) {
			state = ((IntAttribute)obj).getValue();
		}

		obj = inputStatusByName.get("tripEnable");
		if (obj != null && obj instanceof IntAttribute) {
			enable = ((IntAttribute)obj).getValue();
		}      

		// do calculation
		int computedValue = C_DEFAULT;
		if (enable == 1) {
			if (state == 1) {
				computedValue = C_ENABLED_TRIPPED;
			} else {
				computedValue = C_ENABLED_NOTTRIPPED;
			}
		} else {
			if (state == 1) {
				computedValue = C_DISABLED_TRIPPED;
			} else {
				computedValue = C_DISABLED_NOTTRIPPED;
			}
		}

		ret.setValue(computedValue);
		logger.debug("Exit calculator: {}", ret.getValue());
		return ret;
	}
}
