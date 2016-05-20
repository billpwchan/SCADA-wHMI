package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationSelectionEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.presenter.NavTreeInitContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.presenter.NavTreePresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.selection.NavTreeSingleSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.view.NavigationTreeView;

public class CoccNavigationTreePanel extends ResizeComposite implements IWidgetController {

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private final CoccCaptionPanel mainPanel_;
    
    /**
     * Client presenter of this navigation tree
     */
    private final NavTreePresenterClient presenter_;
    
    /**
     * Bus used to publish and listen to events
     */
    private final EventBus eventBus_;
    
    /**
     * Widget view
     */
    private final NavigationTreeView treeView_;

    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    private final NavTreeSingleSelectionManager selectionManager_;

	public CoccNavigationTreePanel(String configId, EventBus eventBus) {
        eventBus_ = eventBus;
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();
        mainPanel_ = new CoccCaptionPanel(Dictionary.getWording("navTreePanel_caption"));

        // A context
        final NavTreeInitContext context = new NavTreeInitContext();
        context.setNavTreeTypeId(configId);

        // A view
        treeView_ = new NavigationTreeView();

        // A presenter
        presenter_ = new NavTreePresenterClient(treeView_, context, eventBus_);

        mainPanel_.add(presenter_.getView().asWidget());

        selectionManager_ = new NavTreeSingleSelectionManager(treeView_, presenter_);
        presenter_.setSelectionManager(selectionManager_);

        // Register in order to react to navigation selection events
        if (eventBus_ != null) {
            handlerRegistrations_.add(eventBus_.addHandler(NavigationSelectionEvent.TYPE, presenter_));
        }
        
        initWidget(mainPanel_);	
	}
	
	public void setCaption(String caption) {
		mainPanel_.setCaption(caption);
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
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
        return Dictionary.getWording("layout_title_sop_navigation_tree");
    }

}
