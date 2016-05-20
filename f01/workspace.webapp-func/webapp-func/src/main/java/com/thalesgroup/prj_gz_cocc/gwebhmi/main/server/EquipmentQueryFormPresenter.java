package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.ConfigurationTools;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.entity.configuration.AbstractConfiguredEntityType;
import com.thalesgroup.hv.data_v1.equipment.configuration.AbstractEquipmentType;
import com.thalesgroup.hv.data_v1.notification.ElementModificationType;
import com.thalesgroup.hv.data_v1.notification.EntityNotificationElementType;
import com.thalesgroup.hv.data_v1.notification.list.NotificationList;
import com.thalesgroup.hv.mwt.conf.genericdatagrid.GenericDataGridConfiguration;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.action.PresenterOperatorAction;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterInitContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.PresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.OperatorActionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.GenericDataGridPresenter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.conf.ConfLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.FormatterFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.datagrid.formatter.IFormatter;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.PresenterParams;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.server.mvp.presenter.data.ConfiguredEntityStatusesManager;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.action.EqptSearchRequest;

public class EquipmentQueryFormPresenter extends GenericDataGridPresenter {

	/**
     * Log error on server side
     */
    private static final Logger s_logger = LoggerFactory.getLogger(EquipmentQueryFormPresenter.class.getName());
    
    private final List<EntityNotificationElementType> knownEntries_ = new ArrayList<EntityNotificationElementType>();

    protected CoccEntityPreProcessor entityPreProcessor_;
    
    protected ConfiguredEntityStatusesManager entityManager_;
    
    /**
     * {@inheritDoc}
     */
    public EquipmentQueryFormPresenter(PresenterParams params) {
        super(params);
    }
    
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
    			
    			
    			entityPreProcessor_ = new CoccEntityPreProcessor(_formatters, getTools().getSystemConfiguration());
    			
    		} catch (HypervisorException e) {
    			s_logger.error("Can't initialize custom preprocessor", e);
    		}
    	}
    	return super.onInitialize(context);
    }
    
    @Override
    public ConfiguredEntityStatusesManager createCfgEntitiesManager() {
    	entityManager_ = super.createCfgEntitiesManager();
    	if (entityManager_ != null) {
    		entityManager_.addPreProcessor(entityPreProcessor_);
    	}
    	return entityManager_;
    }
   
    @Override
    public IOperatorActionReturn manageAction(
    		final PresenterOperatorAction presenterOperatorAction) {
    	if (presenterOperatorAction instanceof EqptSearchRequest) {
    		s_logger.debug("manageAction: " + presenterOperatorAction.toString());
    		
    		final Thread thread = new Thread() {
            	public void run() {
            		try {
    					Thread.sleep(200);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
            		fillContent((EqptSearchRequest) presenterOperatorAction);
            	};
            };
            thread.start();
            OperatorActionReturn actionReturn = new OperatorActionReturn();
        	return actionReturn;
    	}
    	return super.manageAction(presenterOperatorAction);
    }

    protected void fillContent(EqptSearchRequest request) {
    	
    	if (!knownEntries_.isEmpty()) {
    		final NotificationList notificationList = new NotificationList();
    		for ( EntityNotificationElementType notif  : knownEntries_) {
    			notif.setModificationType(ElementModificationType.DELETE);
			}
    		notificationList.getEntityNotificationElement().addAll(knownEntries_);
    		knownEntries_.clear();
    		getEntitiesManager().onNotificationList(notificationList);
    	}
    	Collection<AbstractConfiguredEntityType> eqpts;
    	Set<AbstractConfiguredEntityType> filteredEqpts;
    	
    	// Process line and station filter
		if (!request.getQuery().getStationId().isEmpty()) {
			eqpts = getTools().getSystemConfiguration().getEntitiesInArea(request.getQuery().getStationId());
		} else if (!request.getQuery().getLineId().isEmpty()) {
			eqpts = getTools().getSystemConfiguration().getEntitiesFromService(request.getQuery().getLineId());
		} else {
			eqpts = new HashSet<AbstractConfiguredEntityType> (getTools().getSystemConfiguration().getEntitiesAsMap(AbstractEquipmentType.class).values());
		}
		
		if (eqpts == null) {
			s_logger.warn("EquipmentQueryFormPresenter fillContent equipment list is null");
			return;
		}
		
		filteredEqpts = new HashSet<AbstractConfiguredEntityType> ();
		
		// Process subsystem,eqpt name,eqpt label filter
		for (AbstractConfiguredEntityType eqpt : eqpts) {
			String eqptClass;
			try {
				eqptClass = getTools().getEquipmentTypeFromId(eqpt.getId());
				//s_logger.debug(" - eqptId=" + eqpt.getId() + " equipmentType " + eqptClass);
			} catch (HypervisorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			if (!request.getQuery().getSubsystemId().isEmpty()) {
				
				String subsystem = eqptClass.substring(0, eqptClass.lastIndexOf('.'));
				subsystem = subsystem.substring(0, subsystem.lastIndexOf('.'));
				subsystem = subsystem.substring(subsystem.lastIndexOf('.')+1);
				
				if (subsystem.compareToIgnoreCase(request.getQuery().getSubsystemId()) != 0 ) {
					continue; // No match -> not add to filteredEqpts
				}
			}
			
			if (!request.getQuery().getEqptName().isEmpty()) {
				
				String eqptName = eqptClass.substring(eqptClass.lastIndexOf('.')+1);
				eqptName = eqptName.substring(0, eqptName.indexOf("Type"));
				if (eqptName.compareToIgnoreCase(request.getQuery().getEqptName()) != 0) {
					continue; // No match -> not add to filteredEqpts
				}
			}

			if (!request.getQuery().getEqptLabel().isEmpty()) {
				String eqptLabel = getDictionary().getWording("entityName_" + eqpt.getId());
				if (eqptLabel.indexOf(request.getQuery().getEqptLabel()) < 0) {
					continue; // No match -> not add to filteredEqpts
				}
			}
			
			filteredEqpts.add(eqpt);
		}

        NotificationList notificationList = new NotificationList();
        for(AbstractConfiguredEntityType eqpt : filteredEqpts) {
            
            //convert configuration to notification
            final EntityNotificationElementType notif = new EntityNotificationElementType();
            notif.setModificationType(ElementModificationType.INSERT);
            try {
                //we have to get the notification class to be able to use the default mechanism
                final AbstractEntityStatusesType entity = getTools().getDataHelper().getBeanEditor().createBean(
                        ConfigurationTools.getNotificationClassName(eqpt.getClass()));
                entity.setId(eqpt.getId());
                notif.setEntity(entity);
                notificationList.getEntityNotificationElement().add(notif);
                knownEntries_.add(notif);
            } catch (HypervisorConversionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        getEntitiesManager().onNotificationList(notificationList);
		
	}

	/**
     * Here we will load the needed data to present them to the user.
     * We don't call super method to avoid subscription mechanism which is not needed
     */
    @Override
    public StateTransitionReturn onActivate() throws PresenterException {
        //remove the subscribe behavior
        //get the data form the configuration
//    	// Here we are using a decoupling thread to avoid synchronization issue 
//    	//with the paging feature
//        final Thread thread = new Thread() {
//        	public void run() {
//        		try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//        		fillContent();
//        	};
//        };
//        thread.start();
        return new StateTransitionReturn();
    }

//	private void fillContent() {
//		Collection<AbstractConfiguredEntityType> eqpts;
//		if (!context_.getEqptQuery().getStationId().isEmpty()) {
//			eqpts = getTools().getSystemConfiguration().getEntitiesInArea(context_.getEqptQuery().getStationId());
//		} else {
//			eqpts = new HashSet<AbstractConfiguredEntityType> (getTools().getSystemConfiguration().getEntitiesAsMap(AbstractEquipmentType.class).values());
//		}
//		
//        NotificationList notificationList = new NotificationList();
//        for(AbstractConfiguredEntityType eqpt : eqpts) {
//            
//            //convert configuration to notification
//            final EntityNotificationElementType notif = new EntityNotificationElementType();
//            notif.setModificationType(ElementModificationType.INSERT);
//            try {
//                //we have to get the notification class to be able to use the default mechanism
//                final AbstractEntityStatusesType entity = getTools().getDataHelper().getBeanEditor().createBean(
//                        ConfigurationTools.getNotificationClassName(eqpt.getClass()));
//                entity.setId(eqpt.getId());
//                notif.setEntity(entity);
//                notificationList.getEntityNotificationElement().add(notif);
//            } catch (HypervisorConversionException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        getEntitiesManager().onNotificationList(notificationList);
//	}
    
    @Override
    public StateTransitionReturn onPassivate() throws PresenterException {
        //remove the unsubscribe behavior
        return new StateTransitionReturn();
    }
    
    public ConfiguredEntityStatusesManager getEntitiesManager() {
    	return entityManager_;
    }

}
