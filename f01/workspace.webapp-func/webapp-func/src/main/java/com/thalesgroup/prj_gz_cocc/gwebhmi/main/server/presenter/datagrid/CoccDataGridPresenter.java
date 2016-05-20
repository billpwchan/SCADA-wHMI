package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.presenter.datagrid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.mwt.conf.genericdatagrid.GenericDataGridConfiguration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterInitContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.GenericDataGridPresenter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.conf.ConfLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.FormatterFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.IFormatter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.TransientEntityManager;

public class CoccDataGridPresenter extends GenericDataGridPresenter
{
    /**
     * Log error on server side
     */
    private static final Logger s_logger = LoggerFactory.getLogger(CoccDataGridPresenter.class.getName());

    private CoccGDGEntityPreProcessor entityPreProcessor_;

    public CoccDataGridPresenter( PresenterParams params )
    {
        super( params );
    }
/*
    @Override
    public void onCountChange( CounterValueChangeEvent event )
    {
        super.onCountChange( event );
        
        NotificationEvent notifyEvent = new NotificationEvent();
        super.onNotificationProcessed( notifyEvent );
    }
*/    
    @Override
    public StateTransitionReturn onInitialize(IPresenterContext context)
            throws PresenterException {
        if (context instanceof IPresenterInitContext) {
            try {
            	GenericDataGridConfiguration grid = ConfLoader.getInstance().getConfiguration(((IPresenterInitContext) context).getConfigurationId());
                Map<String, Set<IFormatter>> formatters = FormatterFactory.getFormatter(grid.getColumn(), getDictionary());
                Map<String, IFormatter> _formatters = new HashMap<String, IFormatter>();
    			
    			for (Map.Entry<String, Set<IFormatter>> entry : formatters.entrySet()) {
    				
    				Set<IFormatter> formatterSet = entry.getValue();
    				
    				if (formatterSet.size() == 0)
    					continue;
    				
    				_formatters.put(entry.getKey(), ((IFormatter)formatterSet.toArray()[0]));
    			}
                entityPreProcessor_ = new CoccGDGEntityPreProcessor(_formatters);
                
            } catch (HypervisorException e) {
                s_logger.error("Can't initialize custom preprocessor", e);
            }
        }
        return super.onInitialize(context);
    }

    
   @Override
    public TransientEntityManager createTransientEntitiesManager() {
	   TransientEntityManager manager = super.createTransientEntitiesManager();
   		if (manager != null) {
   			manager.addPreProcessor(entityPreProcessor_);
   		}
   		return manager;
    } 
}
