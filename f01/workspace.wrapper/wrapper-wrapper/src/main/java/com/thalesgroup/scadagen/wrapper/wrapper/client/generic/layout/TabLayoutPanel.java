
package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.layout;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;

/**
 * A widget that provides a Tabulation panel with closeable tabs.
 */
public class TabLayoutPanel extends ResizeComposite implements HasCloseHandlers<String>, SelectionHandler<Integer> {

    /**
     * CSS class applied to this widget
     */
    public static final String CSS_TAB_CONTAINER = "showcase-tab-container";

    /**
     * Prefix of dictionary keys used to retrieved the caption displayed in
     * {@link TabLayoutPanel} tabs
     */
    private static final String KEY_TAB_DICO = "tabLayout_";

    /**
     * Tabs bar height
     */
    protected static final int DEFAULT_BAR_HEIGHT_PX = 40;

    /**
     * Managed GWT {@link com.google.gwt.user.client.ui.TabLayoutPanel}
     */
    protected final com.google.gwt.user.client.ui.TabLayoutPanel mainPanel_;

    /**
     * Scheduled command to scheduled a resize
     */
    private final ScheduledCommand scheduledResize_ = new ScheduledCommand() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void execute() {
            onResize();
        }
    };

    /**
     * Creates a {@link TabLayoutPanel} panel with the default tab bar height.
     */
    public TabLayoutPanel() {
        this(DEFAULT_BAR_HEIGHT_PX, Unit.PX);
    }

    /**
     * Creates a {@link TabLayoutPanel} panel with a specified tab bar height.
     *
     * @param barHeight
     *            the size of the tab bar
     * @param barUnit
     *            the unit in which the tab bar size is specified
     */
    public TabLayoutPanel(final double barHeight, final Unit barUnit) {
        mainPanel_ = new com.google.gwt.user.client.ui.TabLayoutPanel(barHeight, barUnit);
        initWidget(mainPanel_);

        addStyleName(CSS_TAB_CONTAINER);
        addSelectionHandler(this);
    }

    /**
     * Allows to add tab styled according to MWT style guide.
     *
     * @param widget
     *            the widget displayed inside the tab main panel
     * @param navigationId
     *            navigation Id in tab
     * @param closeable
     *            specifies whether the tab can be closed
     */
    public void add(final Widget widget, final String navigationId, final boolean closeable) {
        if (!contains(widget)) {

            final FlowPanel newTab = new FlowPanel();
            newTab.addStyleName("mwt-tabs-toolbar");

            String caption = Dictionary.getWording(KEY_TAB_DICO + navigationId);
            if (caption.startsWith("#Undefined#")) {
                caption = navigationId;
            }
            final Label titleLbl = new Label(caption);
            newTab.add(titleLbl);

            final Image tabIcon;
            if (closeable == true) {
                tabIcon = new Image("Images/tab-close.gif");

                tabIcon.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        TabLayoutPanel.this.remove(widget);
                        CloseEvent.fire(TabLayoutPanel.this, navigationId);
                    }
                });
            } else {
                tabIcon = new Image("Images/no_icon.png");
            }
            tabIcon.setStyleName("mwt-TabPanels-CloseIcon");
            newTab.add(tabIcon);
            mainPanel_.add(widget, newTab);
        }
        selectTab(widget);
    }

    /**
     * Removes a tab widget.
     *
     * @param widget
     *            the widget whose tab is to be removed
     */
    public void remove(final Widget widget) {
        final int widgetIdx = mainPanel_.getWidgetIndex(widget);
        if (widgetIdx >= 0) {
            if (widget instanceof IClientLifeCycle) {
                ((IClientLifeCycle) widget).terminate();
            }
            mainPanel_.remove(widgetIdx);
        }
    }

    /**
     * Selects the specified tab.
     *
     * @param index
     *            the index of the tab to be selected
     */
    public void selectTab(final int index) {
        mainPanel_.selectTab(index);
    }

    /**
     * Select the tab containing a given widget.
     *
     * @param widget
     *            the widget whose tab is to be selected
     */
    public void selectTab(final Widget widget) {
        if (contains(widget)) {
            mainPanel_.selectTab(widget);
        }
    }

    /**
     * Returns <code>true</code> if this {@link TabLayoutPanel} contains a given
     * widget.
     *
     * @param widget
     *            the widget
     * @return <code>true</code> if this {@link TabLayoutPanel} contains the
     *         widget; <code>false</code> otherwise
     */
    public boolean contains(final Widget widget) {
        boolean ret = false;
        if (mainPanel_.getWidgetIndex(widget) >= 0) {
            ret = true;
        }
        return ret;
    }

    /**
     * Gets the index of the currently-selected tab.
     *
     * @return the selected index, or <code>-1</code> if none is selected.
     */
    public int getSelectedIndex() {
        return mainPanel_.getSelectedIndex();
    }

    /**
     * Gets the widget in the tab at the given index.
     *
     * @param index
     *            the index of the tab to be retrieved
     * @return the tab's widget (widget contained in tab, not the Tab itself)
     */
    public Widget getTabWidget(final int index) {
        Widget widget = null;
        if ((index >= 0) && (index < mainPanel_.getWidgetCount())) {
            widget = mainPanel_.getWidget(index);
        }
        return widget;
    }

    /**
     * Adds {@link SelectionHandler} called when a tab is selected.
     *
     * @param handler
     *            selection handler
     * @return the handler registration
     */
    public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
        return mainPanel_.addSelectionHandler(handler);
    }

    /**
     * Adds {@link CloseHandler} called when a tab is closed.
     *
     * @param handler
     *            handler
     * @return the handler registration
     */
    @Override
    public HandlerRegistration addCloseHandler(final CloseHandler<String> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelection(final SelectionEvent<Integer> event) {
        Scheduler.get().scheduleDeferred(scheduledResize_);
    }

}
