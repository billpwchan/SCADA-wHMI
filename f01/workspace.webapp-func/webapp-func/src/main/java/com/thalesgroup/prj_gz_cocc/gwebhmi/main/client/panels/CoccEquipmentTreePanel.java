package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.EquipmentTreePanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.presenter.ObjectTreeExplicitContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.renderer.ObjectTreeRendererFromDictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.sample.EqptTreeMenuPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.sample.ObjectTreeView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.sample.TreeContextMenu;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.selection.ObjectTreeMultipleSelectionManager;

public class CoccEquipmentTreePanel extends ResizeComposite implements IWidgetController{
	/**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private final CoccCaptionPanel mainPanel_;

    /**
     * Client presenter of this navigation tree
     */
    private final EqptTreeMenuPresenterClient treePresenter_;

    /**
     * Bus used to publish and listen to equipment events
     */
    private final EventBus eventBus_;
    
    private final ObjectTreeView entityTreeView_;

    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    /** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();

    private final ObjectTreeMultipleSelectionManager selectionManager_;

    /**
     * Builds a {@link EquipmentTreePanel} widget displaying a static entities tree configuration.
     *
     * @param objectTreeId the id of the object tree configuration
     * @param eventBus eventBus bus used to listen to and publish {@link EqptSelectionChangeEvent} events
     */
    public CoccEquipmentTreePanel(final String objectTreeId, final EventBus eventBus) {
        eventBus_ = eventBus;
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();

        mainPanel_ = new CoccCaptionPanel(Dictionary.getWording("entityTreePanel_caption"));
        initWidget(mainPanel_);

        // A context
        final ObjectTreeExplicitContext treeContext = new ObjectTreeExplicitContext();
        treeContext.setNavTreeTypeId(objectTreeId);

        // A view
        final ObjectTreeRendererFromDictionary objectTreeRenderer = new ObjectTreeRendererFromDictionary();
        entityTreeView_ = new ObjectTreeView(objectTreeRenderer);

        // A presenter
        treePresenter_ = new EqptTreeMenuPresenterClient(entityTreeView_, treeContext, eventBus_);

        mainPanel_.add(treePresenter_.getView().asWidget());

        selectionManager_ = new ObjectTreeMultipleSelectionManager(entityTreeView_, treePresenter_);
        treePresenter_.setTreeItemSelectionManager(selectionManager_);

        // Menu
        final TreeContextMenu menu = new TreeContextMenu(selectionManager_);
        treePresenter_.setContextMenu(menu);

        // Register in order to react to equipment selection events
        if (eventBus_ != null) {
            handlerRegistrations_.add(eventBus_.addHandler(EqptSelectionChangeEvent.TYPE, treePresenter_));
        }
    }

    /**
     * {@inheritDoc}
     */
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
            treePresenter_.terminate();
        } catch (final IllegalStatePresenterException e) {
            logger_.error("An error occurred while tying to terminate the equimpent tree presenter.", e);
        }
        
        if (selectionManager_ != null) {
            selectionManager_.destroy();
        }
        if (treePresenter_ != null) {
            treePresenter_.destroy();
        }
    }

    @Override
    public Widget getLayoutView() {
        return this;
    }

    @Override
    public SafeUri getIconUri() {
        return null;
    }

    @Override
    public String getWidgetTitle() {
        return Dictionary.getWording("layout_title_equipment_tree");
    }
}
