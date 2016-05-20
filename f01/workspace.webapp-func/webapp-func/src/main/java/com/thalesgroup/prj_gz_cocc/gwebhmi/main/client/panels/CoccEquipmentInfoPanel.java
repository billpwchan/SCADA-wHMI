package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import java.util.Set;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.infopanel.presenter.InfoPanelClientConstants;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.infopanel.presenter.InfoPanelContextEntityType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.infopanel.presenter.InfoPanelPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.infopanel.renderer.IInfoPanelRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.infopanel.view.InfoPanelView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;

/**
 * A widget displaying the attributes of Hypervisor equipment entities.
 */
public class CoccEquipmentInfoPanel extends ResizeComposite implements IClientLifeCycle {

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
     */
    private final SimpleLayoutPanel mainPanel_;

    /**
     * Client presenter of this navigation tree
     */
    private InfoPanelPresenterClient infoPanelPresenter_;

    /** Logger */
    private static final ClientLogger logger_ = ClientLogger.getClientLogger();

    /**
     * Builds a navigation tree from a configuration identifier.
     *
     * @param equipmentIds set of equipments identifiers
     * @param eqptTypeId full qualified name of the type of equipments entities
     */
    public CoccEquipmentInfoPanel(final Set<String> equipmentIds, final String eqptTypeId) {
        mainPanel_ = new SimpleLayoutPanel();
        initWidget(mainPanel_);

        if (equipmentIds != null && equipmentIds.size() > 0) {

            // A context
            final InfoPanelContextEntityType context = new InfoPanelContextEntityType();
            context.setPluginId(InfoPanelClientConstants.pluginEqpt);
            context.setListEntityIds(equipmentIds);
            context.setEntityTypeId(eqptTypeId);

            // A view
            final InfoPanelView view = new InfoPanelView() {
            	@Override
            	protected IInfoPanelRenderer getRenderer() {
            		return new IInfoPanelRenderer() {
						
						@Override
						public HTML generateHeaderRightLabel(Set<String> listEntityIds) {
							HTML widgetHtml;
					        
					        if (listEntityIds.size() > 1) {
					            widgetHtml = new HTML("(" + listEntityIds.size() + ")");
					        }
					        else if (listEntityIds.size() == 1) {
					            final String eqptName = listEntityIds.iterator().next();
					            widgetHtml = new HTML(Dictionary.getEntityId(eqptName));

					        } else {
					            // nbEntities selected <= 0 , nothing to display here
					            widgetHtml = new HTML("");
					        }
					        
					        return widgetHtml;
						}
					};
            	}
            };

            // A presenter
            infoPanelPresenter_ = new InfoPanelPresenterClient(view, context);

            mainPanel_.add(infoPanelPresenter_.getView().asWidget());

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        if (infoPanelPresenter_ != null) {
            try {
                infoPanelPresenter_.terminate();
            } catch (final IllegalStatePresenterException e) {
                logger_.error("Error when trying to terminate the info panel presenter.", e);
            }
        }
    }
}
