
package com.thalesgroup.scadasoft.gwebhmi.main.client.layout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.EquipmentTreePanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.SituationViewPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.InfoCommandTabPanel;

/**
 * This class implements an action panel which allows to visualize a detailed
 * situation view and send commands.
 */
public class ActionPanel extends ResizeComposite implements IClientLifeCycle {

    /**
     * Bus used for events exchanged between widgets of this {@link ActionPanel}
     */
    private final EventBus actionPanelEventBus_;

    /**
     * Identifier of the image contained within this {@link ActionPanel}
     */
    protected final String imageId_;

    /**
     * Panel used to vertically split the situation view and the object tree /
     * info command panels
     */
    private SplitLayoutPanel outerSplitPanel_;

    /**
     * Panel used to horizontally split the object tree and the info command
     * panels
     */
    private SplitLayoutPanel innerSplitPanel_;

    /**
     * Suffix used for building the object tree configuration ids (<view id>>
     * <OBJECT_TREE_ID_SFX>)
     */
    private static final String OBJECT_TREE_ID_SFX = "_object_tree";

    /**
     * A widget displaying a situation view
     */
    private SituationViewPanel situationViewPanel_;

    /**
     * A widget displaying equipment info panel and command panel
     */
    private InfoCommandTabPanel infoCommandTabPanel_;

    /**
     * A widget displaying a tree of equipments
     */
    private EquipmentTreePanel entityTreePanel_;

    /**
     * Application bus into which {@link ActionPanel}'s events are forwarded
     */
    private final EventBus appEventBus_;

    /**
     * Used to remove event handler
     */
    private final List<HandlerRegistration> handlerRegistrations_;

    /**
     * Constructor.
     *
     * @param imageId
     *            identifier of the image contained within this
     *            {@link ActionPanel}
     * @param eventBus
     *            application bus into which {@link ActionPanel}'s events are
     *            forwarded
     */
    public ActionPanel(final String imageId, final EventBus eventBus) {

        imageId_ = imageId;
        appEventBus_ = eventBus;
        actionPanelEventBus_ = new MwtEventBus();
        handlerRegistrations_ = new ArrayList<HandlerRegistration>();

        createLayout();
        addSituationViewPanel();
        addInfoCommandPanel();
        addObjectTreePanel();
        setupEventHandlers();

        initWidget(outerSplitPanel_);
    }

    public EventBus getActionPanelEventBus() {
        return actionPanelEventBus_;
    }

    /** CHECKSTYLE:OFF MagicNumberCheck HMI is magic */
    private void createLayout() {
        outerSplitPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);
        outerSplitPanel_.addStyleName("showcase-action-panel");
        innerSplitPanel_ = new SplitLayoutPanel(AppUtils.SPLIT_DRAGGER_SIZE_PX);

        outerSplitPanel_.addEast(innerSplitPanel_, 320);
    }

    /**
     * Create and add the situation view panel.
     */
    private void addSituationViewPanel() {
        situationViewPanel_ = new SituationViewPanel(imageId_, appEventBus_);

        outerSplitPanel_.add(situationViewPanel_);
    }

    /**
     * Create and add the info command panel.
     */
    private void addInfoCommandPanel() {

        infoCommandTabPanel_ = new InfoCommandTabPanel(appEventBus_);
        innerSplitPanel_.addSouth(infoCommandTabPanel_, 300);
    }

    /**
     * Create and add the object tree panel.
     */
    private void addObjectTreePanel() {

        String treeId = imageId_ + OBJECT_TREE_ID_SFX;
        entityTreePanel_ = new EquipmentTreePanel(treeId, appEventBus_);
        innerSplitPanel_.add(entityTreePanel_);
    }

    /** CHECKSTYLE:ON MagicNumberCheck HMI is magic */

    /**
     * Return the identifier of the image contained within this
     * {@link ActionPanel}.
     *
     * @return the identifier of the image contained within this
     *         {@link ActionPanel}
     */
    public String getImageId() {
        return imageId_;
    }

    /**
     * Register to both action panel bus and application bus in order to forward
     * events.
     */
    private void setupEventHandlers() {
        // Publish alarm selection events to the application bus
        /*
         * handlerRegistrations_.add(actionPanelEventBus_.addHandler(
         * AlarmSelectionChangeEvent.TYPE, new AlarmSelectionEventHandler() {
         * 
         * @Override public void onSelectionChange(final
         * AlarmSelectionChangeEvent event) { if
         * (!event.containsEventBus(appEventBus_)) {
         * appEventBus_.fireEventFromSource(event, this); } } }));
         * 
         * // Publish alarm selection events to the application bus
         * handlerRegistrations_.add(appEventBus_.addHandler(
         * AlarmSelectionChangeEvent.TYPE, new AlarmSelectionEventHandler() {
         * 
         * @Override public void onSelectionChange(final
         * AlarmSelectionChangeEvent event) { if
         * (!event.containsEventBus(actionPanelEventBus_)) {
         * actionPanelEventBus_.fireEventFromSource(event, this); } } }));
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {

        for (final HandlerRegistration handlerRegistration : handlerRegistrations_) {
            handlerRegistration.removeHandler();
        }
        handlerRegistrations_.clear();

        situationViewPanel_.terminate();
        entityTreePanel_.terminate();
        infoCommandTabPanel_.terminate();
    }

}
