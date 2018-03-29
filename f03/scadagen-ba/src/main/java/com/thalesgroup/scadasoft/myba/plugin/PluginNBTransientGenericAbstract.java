package com.thalesgroup.scadasoft.myba.plugin;

import com.thalesgroup.hv.common.broker.ExecutionMode;
import com.thalesgroup.hv.common.configuration.api.IConfiguration;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.subscriptionmanager.core.exception.SubscriptionInitException;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.transien.IConfigTransient;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.transien.PluginNBTransientAbstract;

public abstract class PluginNBTransientGenericAbstract extends PluginNBTransientAbstract {
	
	protected Class<? extends AbstractEntityStatusesType> transientEntityClass = null;
	
	protected String fromExpression = null;
	
	protected String topicExpression = null;
	
	protected String pluginId = null;
	
	public static final String entityClassNameStr = ".entityClassName";
	
	public static final String fromExpressionStr = ".fromExpression";
	
	public static final String topicExpressionStr = ".topicExpression";

	public PluginNBTransientGenericAbstract(IConfigTransient configPlugin, Class<? extends AbstractEntityStatusesType> entityStatusesTypeClass,
			String fromExpression, String topicExpression) throws SubscriptionInitException {
		super(configPlugin);

		this.transientEntityClass = entityStatusesTypeClass;
		this.fromExpression = fromExpression;
		this.topicExpression = topicExpression;
	}

	public PluginNBTransientGenericAbstract(IConfiguration configuration, IDataHelper dataHelper,
			Class<? extends AbstractEntityStatusesType> entityStatusesTypeClass, ExecutionMode executionMode,
			String fromExpression, String topicExpression)
			throws SubscriptionInitException {
		super(configuration, dataHelper, entityStatusesTypeClass, executionMode);
		
		this.transientEntityClass = entityStatusesTypeClass;
		this.fromExpression = fromExpression;
		this.topicExpression = topicExpression;
	}

	@Override
	public Class<? extends AbstractEntityStatusesType> getExpectedEntityTypeClass() {
		return transientEntityClass;
	}

	@Override
	public String getFromExpression() {
		return fromExpression;
	}

	@Override
	public String getTopicExpression() {
		return topicExpression;
	}

	@Override
	public String getPluginIdentifier() {
		return pluginId;
	}

}
