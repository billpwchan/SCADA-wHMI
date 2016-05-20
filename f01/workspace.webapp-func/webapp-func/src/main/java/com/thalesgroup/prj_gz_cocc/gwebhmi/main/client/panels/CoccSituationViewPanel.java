package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.AlarmSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.handler.IPresenterStateHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.presenter.SituationViewPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.tools.BaseClassTools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.ISituationView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.selection.ISelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.selection.SingleSelectionModel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.SymbolWidget;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.CoccSVPresenterClient;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.view.CoccSituationView;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget.CustomWidgetFactory;

public class CoccSituationViewPanel extends ResizeComposite implements IClientLifeCycle
{
	/**
     * The configuration id
     */
    private final String configId_;

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private final SimpleLayoutPanel mainPanel_;

    /**
     * Presenter managing the view
     */
    //private final SituationViewPresenterClient presenter_;
    private final CoccSVPresenterClient presenter_;

    /**
     * Bus used to publish and listen to equipment events
     */
    private final EventBus eventBus_;

    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    /** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();

	/** Key of Local Config Item for CCTV Control Panel URL Path */
	private static final String LOCAL_CONFIG_KEY_CCTV_CONTROL_PANEL_URL = "CctvControlPanelPath";

    private final ISelectionModel selectionModelEqpt_;

    private final ISelectionModel selectionModelAlarm_;

    private boolean m_isTerminated = false;

	/** Click Handler */
	private ClickHandler clickHandler_;

	/** URL Path to CCTV Control Panel */
	private String cctvControlPanelURL_ = "custpro://localhost/realtime.htm";

	public CoccSituationViewPanel(String configurationId, EventBus eventBus)
	{
		this(configurationId, eventBus, null);
	}

    public CoccSituationViewPanel( String configurationId, EventBus eventBus, IPresenterStateHandler presenterStateHandler)
    {
    	configId_ = configurationId;
        eventBus_ = eventBus;
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();

        mainPanel_ = new SimpleLayoutPanel();
        initWidget(mainPanel_);

        // A view
        //final SituationView svView = new SituationView();
        final CoccSituationView svView = new CoccSituationView(new CustomWidgetFactory(this), eventBus_);

        // A presenter
        //presenter_ = new SituationViewPresenterClient(configId_, svView, eventBus_, presenterStateHandler);
        presenter_ = new CoccSVPresenterClient(configId_, svView, eventBus_, presenterStateHandler);
        mainPanel_.add(presenter_.getView().asWidget());

        selectionModelEqpt_ = new SingleSelectionModel(BaseClassTools.EQUIPMENT_CLASS_NAME);
        presenter_.setEquipmentSelectionModel(selectionModelEqpt_);
        selectionModelAlarm_ = new SingleSelectionModel(BaseClassTools.ALARM_CLASS_NAME);
        presenter_.setAlarmSelectionModel(selectionModelAlarm_);

        // Register in order to react to equipment selection events
        if (eventBus_ != null) {
            handlerRegistrations_.add(eventBus_.addHandler(EqptSelectionChangeEvent.TYPE, presenter_));
            handlerRegistrations_.add(eventBus_.addHandler(AlarmSelectionChangeEvent.TYPE, presenter_));
        }

		// Retrieve CCTV Control Panel URL from Local Config
		String controlPanelPath = ConfigProvider.getInstance().getClientData().getProjectConfigurationMap().get(LOCAL_CONFIG_KEY_CCTV_CONTROL_PANEL_URL);
		if(controlPanelPath != null) {
			cctvControlPanelURL_ = controlPanelPath;
		} else {
			logger_.info("Item \"" + LOCAL_CONFIG_KEY_CCTV_CONTROL_PANEL_URL + "\" is not defined in local config.");
		}
    }

	public ISituationView getView() {
		return presenter_.getView();
	}

    public SituationViewPresenterClient getPresenter() {
        return presenter_;
    }

	/**
	 * Returns the click handler for CCTV symbols
	 * @return Handler for the click event
	 */
	public ClickHandler getClickHandler() {
		if(clickHandler_ == null) {
			clickHandler_ = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ISituationView view = getView();
					String cameraID = null;

					if(view instanceof CoccSituationView) {
						final SymbolWidget widget = (SymbolWidget)event.getSource();
						cameraID = ((CoccSituationView)view).getCameraID(widget.getEntityId());
					}
					/*
					SymbolWidget widget = (SymbolWidget)event.getSource();
					logger_.debug(widget.getEntityId());
					*/
					Window.Location.assign(cctvControlPanelURL_ +
							(cameraID != null && !cameraID.equals("") ? "?cameraID=" + cameraID : ""));
				}
			};
		}
		return clickHandler_;
	}

    public boolean isTerminated() {
        return m_isTerminated ;
    }
    
    @Override
    public void terminate() {

        if (mainPanel_ != null) {
            mainPanel_.clear();
        }
        
        for (final HandlerRegistration registration : handlerRegistrations_) {
            registration.removeHandler();
        }
        handlerRegistrations_.clear();

        try {
            presenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
            logger_.error("An error occurred when the situation view presenter ["
                    + presenter_.getConfigurationId() + "] try to terminate.", e);
        }
        
        if (presenter_ != null) {
            presenter_.destroy();
        }
        if (selectionModelEqpt_ != null) {
            selectionModelEqpt_.destroy();
        }
        if (selectionModelAlarm_ != null) {
            selectionModelAlarm_.destroy();
        }
        
    	m_isTerminated = true;
    }
}
