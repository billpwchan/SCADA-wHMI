package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.api.IConfiguration;
import com.thalesgroup.hv.data_v1.area.configuration.AreaType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.entity.EntityClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.IFormatter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.notification.NotificationElementType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.preprocessor.EntityPreProcessor;

public class CoccEntityPreProcessor implements EntityPreProcessor {

	private static final Logger logger_ = LoggerFactory.getLogger(CoccEntityPreProcessor.class);
	
	private final IConfiguration configuration_;
	
	private Map<String, IFormatter> formatters_ = new HashMap<String, IFormatter>();
	
	public CoccEntityPreProcessor(Map<String, IFormatter> formatters,
			IConfiguration configuration) {
		configuration_ = configuration;
		formatters_ = formatters;
	}
	
	@Override
	public void preProcess(EntityClient entityClient,
			NotificationElementType type) {
		String station = null;
		
		if (entityClient != null) {
		
			// Area
			Set<AreaType> areas = configuration_.getEntityAreas(entityClient.getId());
			if (areas != null && !areas.isEmpty()) {
				station = areas.iterator().next().getId();
				StringAttribute stationAttribute = new StringAttribute();
				stationAttribute.setAttributeClass(StringAttribute.class.getName());
				stationAttribute.setValid(true);
				stationAttribute.setTimestamp(new Date());
				stationAttribute.setValue(station);
				entityClient.addAttribute("station", translateValue("station", stationAttribute));
			}
			
			// Line
			StringAttribute lineAttribute = new StringAttribute();
			lineAttribute.setAttributeClass(StringAttribute.class.getName());
			lineAttribute.setValid(true);
			lineAttribute.setTimestamp(new Date());
			if (station != null) {
				lineAttribute.setValue("MCS_" + station.substring(0,3));
			} else {
				lineAttribute.setValue("-");
			}
			entityClient.addAttribute("line", translateValue("line", lineAttribute));
			
			// Equipment Name
			StringAttribute eqpClassAttribute = new StringAttribute();
			eqpClassAttribute.setAttributeClass(StringAttribute.class.getName());
			eqpClassAttribute.setValid(true);
			eqpClassAttribute.setTimestamp(new Date());	
			String eqpName = entityClient.getEntityClass().substring(entityClient.getEntityClass().lastIndexOf('.')+1);
			eqpClassAttribute.setValue("equipmentType_" + eqpName.substring(0, eqpName.indexOf("Statuses")));
			entityClient.addAttribute("equipmentName", translateValue("equipmentName", eqpClassAttribute));
			
			// Subsystem
			StringAttribute subsystemAttribute = new StringAttribute();
			subsystemAttribute.setAttributeClass(StringAttribute.class.getName());
			subsystemAttribute.setValid(true);
			subsystemAttribute.setTimestamp(new Date());
			String subsystem = entityClient.getEntityClass().substring(0, entityClient.getEntityClass().lastIndexOf('.'));
			subsystemAttribute.setValue(subsystem.substring(subsystem.lastIndexOf('.')+1).toUpperCase());
			entityClient.addAttribute("subsystem", translateValue("subsystem", subsystemAttribute));
			
			// Label
			StringAttribute labelAttribute = new StringAttribute();
			labelAttribute.setAttributeClass(StringAttribute.class.getName());
			labelAttribute.setValid(true);
			labelAttribute.setTimestamp(new Date());
			String label = "entityName_" + entityClient.getId();
			labelAttribute.setValue(label);
			entityClient.addAttribute("label", translateValue("label", labelAttribute));
		}
	}
	
	private AttributeClientAbstract<?> translateValue(final String attName, final AttributeClientAbstract<?> value) {
		AttributeClientAbstract<?> toReturn = value;
		if (this.formatters_.containsKey(attName))
			try {
				toReturn = formatters_.get(attName).format(value);
			} catch (HypervisorException e) {
				logger_.error("Error while preprocessing the attribute " + attName, e);
			}
		return toReturn;
	}

}
