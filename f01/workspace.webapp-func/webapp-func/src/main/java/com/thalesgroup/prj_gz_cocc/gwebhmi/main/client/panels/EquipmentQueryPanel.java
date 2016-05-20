package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.ActionPanel;

public class EquipmentQueryPanel extends ResizeComposite implements
		IClientLifeCycle {
	
	/** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

	/**
     * Bus used for events exchanged between widgets of this {@link EquipmentQueryPanel}
     */
	//private final EventBus equipmentQueryPanelEventBus_;
	
	private boolean isTerminated_ = false;

	/**
     * Identifier of the image contained within this {@link ActionPanel}
     */
    protected final String imageId_;
    
    protected final EventBus appEventBus_;

	/**
     * Panel used to horizontally split the filter panel and the equipment data grid/info panel
     */
    private SplitLayoutPanel outerSplitPanel_;

    /**
     * Panel used to vertically split the equipment data grid and the info panel
     */
    private SplitLayoutPanel innerSplitPanel_;
    
    private EquipmentFilterPanel eqpFilterPanel_;

    private EquipmentListPanel eqpListPanel_;

    /**
     * A widget displaying equipment info panel and command panel
     */
    private CoccInfoCommandTabPanel infoCommandTabPanel_;

    /**
     * Constructor.
     *
     * @param imageId identifier of the image contained within this {@link ActionPanel}
     * @param eventBus application bus into which {@link ActionPanel}'s events are forwarded
     */
    public EquipmentQueryPanel(final String imageId, final EventBus eventBus) {
     
    	imageId_ = imageId;
    	appEventBus_ = eventBus;

    	//equipmentQueryPanelEventBus_ = new MwtEventBus();
        s_logger.debug("EquipmentQueryPanel");

        createLayout();

        addEquipmentFilterPanel();
        
        addInfoCommandPanel();

        addEquipmentListPanel();

        //setupEventHandlers();

        initWidget(outerSplitPanel_);
    }
    
    public boolean isTerminated() {
        return isTerminated_;
    }

    private void addEquipmentFilterPanel() {
		eqpFilterPanel_ = new EquipmentFilterPanel(appEventBus_);
    	outerSplitPanel_.add(eqpFilterPanel_);
	}

    private void addInfoCommandPanel() {
		infoCommandTabPanel_ = new CoccInfoCommandTabPanel(appEventBus_);
        innerSplitPanel_.addEast(infoCommandTabPanel_, 480);
	}

    private void addEquipmentListPanel() {
    	eqpListPanel_ = new EquipmentListPanel(appEventBus_);
		innerSplitPanel_.add(eqpListPanel_);
	}

	private void createLayout() {
    	outerSplitPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        outerSplitPanel_.addStyleName("showcase-action-panel");
        innerSplitPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);

        outerSplitPanel_.addSouth(innerSplitPanel_, 720);
	}

	@Override
	public void terminate() {

	}

}
