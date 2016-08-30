package com.thalesgroup.scadagen.bps.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.configuration.AbstractConfiguredEntityType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.sdk.connector.notification.tools.ConfiguredSubscriptionBuilder;
import com.thalesgroup.hv.ws.notification_v1.xsd.FilterType;
import com.thalesgroup.scadagen.bps.conf.actions.ActionsManager;
import com.thalesgroup.scadagen.bps.conf.actions.IAction;
import com.thalesgroup.scadagen.bps.conf.bps.ActionType;
import com.thalesgroup.scadagen.bps.conf.bps.TriggerType;
import com.thalesgroup.scadagen.bps.conf.common.And;
import com.thalesgroup.scadagen.bps.conf.common.Equals;
import com.thalesgroup.scadagen.bps.conf.common.In;
import com.thalesgroup.scadagen.bps.conf.common.Operator;
import com.thalesgroup.scadagen.bps.conf.common.StatusOperator;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;

public class ConfiguredEntityStatusesManager
		extends EntityManagerAbstract<ConfiguredEntityStatusesDataDescriptionAbstract> {

	Set<ConfiguredEntityStatusesDataDescriptionAbstract> desc_ = null;
	
	public ConfiguredEntityStatusesManager(IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector) {
		super(subscriptionConnector, operationConnector);
	}

	public void startSubscription(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
			throws HypervisorException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Ask subscribe to [{}].", entityDataDescriptions.toString());
		}

		if (!entityDataDescriptions.isEmpty()) {
			desc_ = entityDataDescriptions;
			List<FilterType> filters = getFilter(entityDataDescriptions);
			startSubscription(filters);
		}
	}

//	@SuppressWarnings("unchecked")
//	private void notifyFirstInsert(Set<ConfiguredEntityStatusesDataDescriptionAbstract> filteredDescription) {
//		List<NotificationElement> notifList = new ArrayList<NotificationElement>();
//
//		for (ConfiguredEntityStatusesDataDescriptionAbstract desc : filteredDescription) {
//			if (!desc.getPropertiesName().isEmpty()) {
//				if ((desc instanceof ConfiguredEntityStatusesInstancesDataDescription)) {
//					ConfiguredEntityStatusesInstancesDataDescription dataDescription = (ConfiguredEntityStatusesInstancesDataDescription) desc;
//
//					Set<String> entityIds = dataDescription.getEntityIds();
//
//					for (String entityId : entityIds) {
//						GenericEntity gEntity = getEntity(entityId, desc.getPropertiesName());
//
//						notifList.add(getNotifElem(gEntity));
//					}
//				} else if ((desc instanceof ConfiguredEntityStatusesTypeDataDescription)) {
//					ConfiguredEntityStatusesTypeDataDescription dataDescription = (ConfiguredEntityStatusesTypeDataDescription) desc;
//
//					String type = ConfigurationTools.getConfiguredClassName(dataDescription.getEntityType());
//
//					try {
//						Class<? extends AbstractConfiguredEntityType> clazz = (Class<? extends AbstractConfiguredEntityType>) Class.forName(type);
//
//						Map<String, ? extends AbstractConfiguredEntityType> entitiesAsMap = getSubscriptionConnector()
//								.getTools().getSystemConfiguration().getEntitiesAsMap(clazz);
//
//						Set<String> entityIds = entitiesAsMap.keySet();
//						for (String entityId : entityIds) {
//							GenericEntity gEntity = getEntity(entityId, desc.getPropertiesName());
//
//							notifList.add(getNotifElem(gEntity));
//						}
//					} catch (Exception e) {
//						LOGGER.error("Cannot get the list of entities of type [" + type + "].", e);
//					}
//				}
//			}
//		}
//	}
//
//	private static NotificationElement getNotifElem(GenericEntity entity) {
//		return new NotificationElement(NotificationElementType.INSERT, entity);
//	}
//
//	private GenericEntity getEntity(String entityId, Set<String> propertiesName) {
//		AbstractConfiguredEntityType entity = getSubscriptionConnector().getTools().getSystemConfiguration().getEntity(entityId);
//
//		GenericEntity genEntity = new GenericEntity();
//		String entityClass = EntityTool.getEntityClassName(entity);
//		genEntity.setEntityClass(entityClass);
//
//		if (propertiesName.contains("_type_")) {
//			genEntity.addAttribute("_type_", getStringAttribute(entityClass));
//		}
//		try {
//			String baseTypeName = EntityTool.getBaseType(entity).getName();
//			genEntity.setEntityBaseClass(baseTypeName);
//
//			if (propertiesName.contains("_base_type_")) {
//				genEntity.addAttribute("_base_type_", getStringAttribute(baseTypeName));
//			}
//		} catch (ClassNotFoundException e) {
//			LOGGER.error("Error while getting the base type of the entity [" + entityId + "].", e);
//		}
//
//		genEntity.setId(entityId);
//
//		for (String propertyName : propertiesName) {
//
//			if ((!"_type_".equals(propertyName)) && (!"_base_type_".equals(propertyName))) {
//				genEntity.addAttribute(propertyName, getProperty(entityId, propertyName));
//			}
//		}
//
//		return genEntity;
//	}
//
//	private static StringAttributeType getStringAttribute(String attributeValue) {
//		StringAttributeType stringAttribute = new StringAttributeType();
//		stringAttribute.setTimestamp(System.currentTimeMillis());
//		stringAttribute.setValid(true);
//		stringAttribute.setValue(attributeValue);
//		return stringAttribute;
//	}
//
//	private AbstractAttributeType getProperty(String entityId, String propertyName) {
//		AbstractConfiguredEntityType entity = getSubscriptionConnector().getTools().getSystemConfiguration()
//				.getEntity(entityId);
//
//		AbstractAttributeType attribute = null;
//		try {
//			attribute = getSubscriptionConnector().getTools().getDataHelper().getBeanEditor().getValue(entity,
//					propertyName);
//		} catch (BeanManipulationException e) {
//			LOGGER.error("Error while getting the property [" + propertyName + "] of the entity [" + entityId + "].");
//		}
//
//		return attribute;
//	}
	
	public List<FilterType> getFilter(Set<ConfiguredEntityStatusesDataDescriptionAbstract> entityDataDescriptions)
			throws HypervisorConversionException {
		ConfiguredSubscriptionBuilder builder = new ConfiguredSubscriptionBuilder(getSubscriptionConnector().getTools());

		for (ConfiguredEntityStatusesDataDescriptionAbstract entityClientDescription : entityDataDescriptions) {
			if ((entityClientDescription instanceof ConfiguredEntityStatusesInstancesDataDescription)) {
				ConfiguredEntityStatusesInstancesDataDescription description = (ConfiguredEntityStatusesInstancesDataDescription) entityClientDescription;

				if ((!description.getEntityIds().isEmpty()) && (!description.getStatusesName().isEmpty())) {

					if (description.getCriteria().isEmpty()) {
						builder.addSubscribtionToIds(description.getEntityIds(), description.getStatusesName());
					} else {
						builder.addSubscribtionToIdsWithCriteria(description.getEntityIds(),
								description.getStatusesName(), description.getCriteria());
					}

				}
			} else if ((entityClientDescription instanceof ConfiguredEntityStatusesTypeDataDescription)) {
				ConfiguredEntityStatusesTypeDataDescription description = (ConfiguredEntityStatusesTypeDataDescription) entityClientDescription;

				if (description.getCriteria().isEmpty()) {
					builder.addSubscribtionToType(description.getEntityType(), description.getStatusesName());
				} else {
					builder.addSubscribtionToTypeWithCriteria(description.getEntityType(),
							description.getStatusesName(), description.getCriteria());
				}
			}
		}

		return builder.build();
	}
	
	@Override
	protected void onNotificationElement( EntityNotificationElementType notifElem) {
		super.onNotificationElement(notifElem);
		
		if (notifElem.getModificationType() == ElementModificationType.INSERT ||
				notifElem.getModificationType() == ElementModificationType.UPDATE) {
			
			AbstractEntityStatusesType entity = notifElem.getEntity();

			Set<AbstractEntityStatusesType> entities = new HashSet<AbstractEntityStatusesType>();
			entities.add(entity);
	
			if (!triggerList_.isEmpty()) {
				for (TriggerType trigger: triggerList_) {
					if (CompareOperator(entity, trigger.getCriteria())) {
						for (ActionType actionHandler: trigger.getAction()) {
							IAction action = ActionsManager.getInstance().getAction(actionHandler.getActionHandler());
							if (action != null) {
								action.execute(getOperationConnector(), actionHandler.getActionConfig(), entities);
							}
						}
					}
				}
			}
		}
	}

	private boolean CompareOperator(AbstractEntityStatusesType entity, Operator op) {
		if (op instanceof And) {
			return CompareOperator(entity, ((And)op).getFirstOperand()) && CompareOperator(entity, ((And)op).getSecondOperand());
		} else if (op instanceof StatusOperator){
			LOGGER.trace("Compare operator {}", ((StatusOperator) op).getStatus());
			StatusOperator statusOp = (StatusOperator)op;
			String statusName = statusOp.getStatus();
			
			try {
				String entityType = getOperationConnector().getTools().getEquipmentTypeFromId(entity.getId());
				Class<? extends AbstractConfiguredEntityType> clazz = (Class<? extends AbstractConfiguredEntityType>) Class.forName(entityType);
				
				String methodName = "get" + statusName.substring(0,1).toUpperCase() + statusName.substring(1);

				Method m = clazz.getMethod(methodName, new Class[]{});
				m.setAccessible(true);

				LOGGER.trace("Invoke method {} for entity {}", methodName, entity.getId());
				AbstractAttributeType att = (AbstractAttributeType)m.invoke(clazz.cast(entity));
	
				if (att instanceof IntAttributeType) {
					int val = ((IntAttributeType) att).getValue();
					
					if (statusOp instanceof Equals) {
						if (Integer.toString(val).compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare int value {} return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare int value {} return false", statusName);
							return false;
						}
					} else if (statusOp instanceof In) {
						List<String> list = ((In) statusOp).getValue();							
						if (list.contains(Integer.toString(val))) {
							return true;
						} else {
							return false;
						}
					}
				} else if (att instanceof StringAttributeType) {
					if (statusOp instanceof Equals) {
						if (((StringAttributeType)att).getValue().compareTo(((Equals) statusOp).getValue()) == 0) {
							LOGGER.trace("Compare String value {} return true", statusName);
							return true;
						} else {
							LOGGER.trace("Compare String value {} return false", statusName);
							return false;
						}
					} else if (statusOp instanceof In) {
						List<String> list = ((In) statusOp).getValue();
						if (list.contains(((StringAttributeType) att).getValue())) {
							return true;
						} else {
							return false;
						}
					}
				}
			} catch (HypervisorException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (NoSuchMethodException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (SecurityException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Error getting entity type. {}", e);
			} catch (InvocationTargetException e) {
				LOGGER.error("Error getting entity type. {}", e);
			}	
		}
		return false;
	}

}
