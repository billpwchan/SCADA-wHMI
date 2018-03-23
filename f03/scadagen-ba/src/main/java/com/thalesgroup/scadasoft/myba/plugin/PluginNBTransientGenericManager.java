package com.thalesgroup.scadasoft.myba.plugin;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.transien.ConfigurationTransient;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.transien.IConfigTransient;

public class PluginNBTransientGenericManager {
	
	private static final Logger s_logger = LoggerFactory.getLogger(PluginNBTransientGenericManager.class);

	public static void load(Connector connector, Properties prop) {
		loadGenericPlugin1(connector, prop);
		loadGenericPlugin2(connector, prop);
		loadGenericPlugin3(connector, prop);
		loadGenericPlugin4(connector, prop);
		loadGenericPlugin5(connector, prop);
		loadGenericPlugin6(connector, prop);
		loadGenericPlugin7(connector, prop);
		loadGenericPlugin8(connector, prop);
		loadGenericPlugin9(connector, prop);
		loadGenericPlugin10(connector, prop);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin1(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin1");
		
		String entityClassNamePropKey = PluginNBTransientGeneric1.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric1.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric1.topicExpressionPropertyKey;
		
		s_logger.trace("PluginNBTransientGeneric1 entityClassNamePropKey = [{}]", entityClassNamePropKey);
		s_logger.trace("PluginNBTransientGeneric1 fromExpressionPropKey = [{}]", fromExpressionPropKey);
		s_logger.trace("PluginNBTransientGeneric1 topicExpressionPropKey = [{}]", topicExpressionPropKey);

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);

				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric1(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric1 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin2(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin2");
		
		String entityClassNamePropKey = PluginNBTransientGeneric2.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric2.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric2.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);

				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric2(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric2 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin3(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin3");
		
		String entityClassNamePropKey = PluginNBTransientGeneric3.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric3.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric3.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric3(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric3 not loaded");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin4(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin4");
		
		String entityClassNamePropKey = PluginNBTransientGeneric4.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric4.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric4.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric4(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric4 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin5(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin5");
		
		String entityClassNamePropKey = PluginNBTransientGeneric5.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric5.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric5.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric5(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric5 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin6(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin6");
		
		String entityClassNamePropKey = PluginNBTransientGeneric6.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric6.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric6.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric6(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric6 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin7(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin7");
		
		String entityClassNamePropKey = PluginNBTransientGeneric7.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric7.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric7.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric7(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric7 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin8(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin8");
		
		String entityClassNamePropKey = PluginNBTransientGeneric8.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric8.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric8.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric8(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric8 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin9(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin9");
		
		String entityClassNamePropKey = PluginNBTransientGeneric9.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric9.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric9.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric9(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric9 not loaded");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadGenericPlugin10(Connector connector, Properties prop) {
		s_logger.trace("loadGenericPlugin10");
		
		String entityClassNamePropKey = PluginNBTransientGeneric10.entityClassNamePropertyKey;
		String fromExpressionPropKey = PluginNBTransientGeneric10.fromExpressionPropertyKey;
		String topicExpressionPropKey = PluginNBTransientGeneric10.topicExpressionPropertyKey;

		String entityClassName = prop.getProperty(entityClassNamePropKey);
		String fromExpression = prop.getProperty(fromExpressionPropKey);
		String topicExpression = prop.getProperty(topicExpressionPropKey);
		
		if (entityClassName != null && fromExpression != null && topicExpression != null) {
			s_logger.debug("load plugin for entity class [{}] with topic expression [{}]", entityClassName, topicExpression);

			try {
				Class transientClass = Class.forName(entityClassName);

				final IConfigTransient configPreset = new ConfigurationTransient(
						connector.getSystemConfiguration(),
						connector.getDataHelper(),
						transientClass);
			
				connector.addEntityPlugin(transientClass, new PluginNBTransientGeneric10(configPreset, transientClass, fromExpression, topicExpression));
			} catch (Exception e) {
				s_logger.error("Unable to register Notification Broker Plugin: ", e);
			}
		} else {
			s_logger.trace("PluginNBTransientGeneric10 not loaded");
		}
	}

}
