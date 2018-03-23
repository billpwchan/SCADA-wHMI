package com.thalesgroup.scadasoft.myba.plugin;

import com.thalesgroup.hv.common.broker.ExecutionMode;
import com.thalesgroup.hv.common.configuration.api.IConfiguration;
import com.thalesgroup.hv.data.tools.helper.IDataHelper;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.subscriptionmanager.core.exception.SubscriptionInitException;
import com.thalesgroup.hv.subscriptionmanager.core.plugin.transien.IConfigTransient;

public class PluginNBTransientGeneric2 extends PluginNBTransientGenericAbstract {

	public static final String entityClassNamePropertyKey = PluginNBTransientGeneric2.class.getName() + PluginNBTransientGenericAbstract.entityClassNameStr;
	public static final String fromExpressionPropertyKey = PluginNBTransientGeneric2.class.getName() + PluginNBTransientGenericAbstract.fromExpressionStr;
	public static final String topicExpressionPropertyKey = PluginNBTransientGeneric2.class.getName() + PluginNBTransientGenericAbstract.topicExpressionStr;

	public PluginNBTransientGeneric2(IConfigTransient configPlugin,
			Class<? extends AbstractEntityStatusesType> entityStatusesTypeClass, String fromExpression,
			String topicExpression) throws SubscriptionInitException {
		super(configPlugin, entityStatusesTypeClass, fromExpression, topicExpression);
		pluginId = PluginNBTransientGeneric2.class.getName();
	}

	public PluginNBTransientGeneric2(IConfiguration configuration, IDataHelper dataHelper,
			Class<? extends AbstractEntityStatusesType> entityStatusesTypeClass, ExecutionMode executionMode,
			String fromExpression, String topicExpression) throws SubscriptionInitException {
		super(configuration, dataHelper, entityStatusesTypeClass, executionMode, fromExpression, topicExpression);
		pluginId = PluginNBTransientGeneric2.class.getName();
	}

}
