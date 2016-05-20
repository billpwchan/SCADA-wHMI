package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.prj_gz_cocc.mode_desc_mapping.Mapping;
import com.thalesgroup.prj_gz_cocc.mode_desc_mapping.ModeDescription;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class ModeNoToDesc implements StatusComputer {
	private static final Logger s_logger = LoggerFactory.getLogger(ModeNoToDesc.class.getName());
	//private static final ClientLogger s_logger = ClientLogger.getClientLogger();

	// Name of the Statuses to Use
	private static final String CURRENTMODE = "currentmode";
	
	// Name of the Properties to Use
	private final Set<String> properties_ = Collections.emptySet();
	
	// Mapping from Mode Number to Description
	private final Map<String, String> mapping_ = new HashMap<String, String>();

	// Default Mode Description
	final static String __Default__ = "0";

	public ModeNoToDesc() throws HypervisorException {
		this("mode_desc_mapping.xml");
	}

	public ModeNoToDesc(final String inFilename) throws HypervisorException {
		/*
		try {
		*/
			s_logger.debug("ModeNoToDesc Constructor - inFilename: " + inFilename);
			final MarshallersPool pool = new MarshallersPool(Mapping.class.getPackage().getName(), "schemas/mode_desc_mapping.xsd");
			Mapping mapping = pool.unmarshal(inFilename, Mapping.class);
	
			// Generate Mapping from File
			for (ModeDescription modeDesc : mapping.getModeDescription()) {
				mapping_.put(modeDesc.getId(), modeDesc.getDesc());
			}
		/*
		} catch(Exception e) {
			s_logger.debug(e.getMessage());
		}
		*/
	}

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
			String entityId, Map<String, AttributeClientAbstract<?>> inputStatusesByName,
			Map<String, Object> inputPropertiesByName) {
		s_logger.debug("ModeNoToDesc compute - inputStatusesByName: " + inputStatusesByName +
					"\nentityId: " + entityId);
		// Attribute to Return
		final StringAttribute retAttr = new StringAttribute();
		retAttr.setAttributeClass(StringAttribute.class.getName());

		// Mode ID
		IntAttribute modeIDAttr = (IntAttribute) inputStatusesByName.get(CURRENTMODE);
		//StringAttribute nomInstanceAttr = (StringAttribute) inputStatusesByName.get(INSTANCE_NAME);

		if(modeIDAttr != null) {
			// Mode Desc
			int idxStart, idxEnd;
			String modeDesc, modeID = modeIDAttr.getValue().toString(), modeIdPrefix;
			
			if((idxStart = entityId.indexOf("--")) != -1 &&
					(idxEnd = entityId.indexOf("--", (idxStart += 2))) != -1) {
				modeIdPrefix = entityId.substring(idxStart, idxEnd) + entityId.substring(0, 10) + "-";
			} else {
				modeIdPrefix = "";
			}

			if((modeDesc = mapping_.get(modeIdPrefix + modeID)) != null) {
				// Mapping Found, Set Value
				s_logger.debug("ModeNoToDesc compute - mapping_(" + modeIdPrefix + modeID + "): " + modeDesc);
				retAttr.setValue(modeDesc);
				//retAttr.setValid(true);
			} else {
				// Mapping not Found, Use the ID directly
				s_logger.debug("ModeNoToDesc compute - mapping_(" + modeIdPrefix + modeID + ") not Found, Use \"" + modeID + "\"");
				retAttr.setValue(modeID);
			}
			
			// Set Validity and Timestamp
			retAttr.setValid(modeIDAttr.isValid());
			retAttr.setTimestamp(modeIDAttr.getTimestamp());
		} else {
			// Failed to Retrieve Status
			s_logger.debug("ModeNoToDesc compute - Failed to Retrieve Mode ID");
			retAttr.setTimestamp(new Date());
			retAttr.setValid(true);
			retAttr.setValue(__Default__); // 0
		}
		
		s_logger.debug("ModeNoToDesc compute - retAttr: " + retAttr);
		return retAttr;
	}

	@Override
	public String getComputerId() {
		return ModeNoToDesc.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return properties_;
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(CURRENTMODE);
		return result;
	}
}
