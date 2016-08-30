package com.thalesgroup.scadagen.bps.data;

import com.thalesgroup.hv.data_v1.entity.AbstractConfiguredEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.AbstractTransientEntityType;
import com.thalesgroup.hv.data_v1.entity.configuration.AbstractConfiguredEntityType;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.configured.ConfigurationConfigurable;

public final class EntityTool {
	public static String getConfiguredEntityStatusesName(
			Class<? extends AbstractConfiguredEntityType> configuredEntityName) {
		return ConfigurationConfigurable.getPublicClassName(configuredEntityName);
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends AbstractEntityStatusesType> getBaseType(
			Class<? extends AbstractEntityStatusesType> type) {
		Class<? extends AbstractEntityStatusesType> toReturn = type;

		if ((!AbstractConfiguredEntityStatusesType.class.equals(toReturn))
				|| (!AbstractTransientEntityType.class.equals(toReturn))) {

			while ((!toReturn.getSuperclass().equals(AbstractConfiguredEntityStatusesType.class))
					&& (!toReturn.getSuperclass().equals(AbstractTransientEntityType.class))) {
				toReturn = (Class<? extends AbstractEntityStatusesType>) toReturn.getSuperclass();
			}
		}
		return toReturn;
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends AbstractEntityStatusesType> getBaseType(
			AbstractConfiguredEntityType configuredEntityType) throws ClassNotFoundException {
		String configuredEntityStatusesName = getConfiguredEntityStatusesName(configuredEntityType.getClass());

		Class<AbstractEntityStatusesType> loadClass = (Class<AbstractEntityStatusesType>) AbstractEntityStatusesType.class
				.getClassLoader().loadClass(configuredEntityStatusesName);

		return getBaseType(loadClass);
	}

	public static String getEntityClassName(AbstractConfiguredEntityType abstractEntity) {
		return getConfiguredEntityStatusesName(abstractEntity.getClass());
	}

}
