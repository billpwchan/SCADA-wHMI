
package com.thalesgroup.scadasoft.gwebhmi.main.client.layout;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.AppClientContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.RpcMonitorEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.InfoDialog;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.tree.presenter.NavTreeInitContext;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppEventsHandler;
import com.thalesgroup.scadasoft.gwebhmi.main.client.panels.Toolbar;
import com.thalesgroup.scadasoft.gwebhmi.main.client.presenter.MobileToolBarPresenter;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.CookieUtil;
import com.thalesgroup.scadasoft.gwebhmi.main.client.view.MobileToolbar;

/**
 * This class implements the application layout.
 */
public class AppPanel extends ResizeComposite {

    /**
     * Showcase toolbar height
     */
    private static final int TOOLBAR_HEIGHT_PX = 42;
    private static final String MOBILE_TOOL_BAR_NAV_ID = "scadasoft_nav_tree";

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private final LayoutPanel mainPanel_;

    /**
     * Showcase main toolbar
     */
    private Toolbar toolbar_;

    /**
     * Overview screen located on the left screen
     */
    private OverviewScreen overviewScreen_;

    /**
     * Overview screen located on the left screen
     */
    private MobileOverviewScreen mobileOverviewScreen_;

    /**
     * Widget containing {@link ActionPanel} and the {@link OverviewScreen} in
     * mono screen mode
     */
    private TabLayoutPanel tabPanelsContainer_;

    /**
     * Screen layout choice made by the operator at login
     */
    private boolean dualScreen_;
    private boolean mobileScreen_;
    /**
     * Mobile toolbar
     */
    private MobileToolbar mobileToolbar_;

    @SuppressWarnings("unused")
    private AppEventsHandler appEventsHandler_;

    @SuppressWarnings("unused")
    private MobileToolBarPresenter toolbarPresenter_;

    /**
     * Constructor.
     */
    public AppPanel() {
        mainPanel_ = new LayoutPanel();
        tabPanelsContainer_ = new TabLayoutPanel();
        initWidget(mainPanel_);

        setScreenMode();
        setupLayout();
        setupAppEventMgt();
    }

    /**
     * Set the screen mode (mono / dual screen) according to operator choice at
     * login time.
     */
    private void setScreenMode() {
        dualScreen_ = CookieUtil.isDualScreenSetFromCookie();// ScsLoginPanel.isDualScreenSetFromCookie();
        mobileScreen_ = CookieUtil.isMobileScreenSetFromCookie();// ScsLoginPanel.isMobileScreenSetFromCookie();
    }

    private void createLargeScreenWidgets() {

        overviewScreen_ = new OverviewScreen();
        toolbar_ = new Toolbar(overviewScreen_);

        createServerConnectionDialogPanel();
        setupLargeScreenToolbar();
    }

    private void createMobileScreenWidgets() {

        mobileOverviewScreen_ = new MobileOverviewScreen();
        mobileToolbar_ = new MobileToolbar(mobileOverviewScreen_);

        // A context
        final NavTreeInitContext context = new NavTreeInitContext();
        context.setNavTreeTypeId(MOBILE_TOOL_BAR_NAV_ID);
        toolbarPresenter_ = new MobileToolBarPresenter(context, mobileToolbar_);

        createServerConnectionDialogPanel();
        setupMobileScreenToolbar();
    }

    /**
     * Creates a dialog panel displayed when connection issues occurs between
     * the HMI server and the web browser.
     */
    @SuppressWarnings("static-method")
    protected void createServerConnectionDialogPanel() {
        // We get the event bus provided by MWT
        final EventBus eventBus = AppClientContext.getInstance().getEventBus();

        // MWT publishes events related to server connection status in MWT bus.
        // The InfoDialog widget is automatically displayed when such events
        // occurs
        eventBus.addHandler(RpcMonitorEvent.TYPE, new InfoDialog(eventBus));
    }

    /**
     * Setup the application layout.
     */
    private void setupLayout() {

        // Screens layout depends on the dual screen choice made by the operator
        // at login time
        if (dualScreen_ == true) {
            setupDualScreenLayout();
        } else if (mobileScreen_ == true) {
            setupMobileScreenLayout();
        } else {
            setupMonoScreenLayout();
        }
    }

    private void setupLargeScreenToolbar() {
        // Toolbar at the top
        mainPanel_.add(toolbar_);
        mainPanel_.setWidgetTopHeight(toolbar_, 0, Unit.PX, TOOLBAR_HEIGHT_PX, Unit.PX);
    }

    private void setupMobileScreenToolbar() {
        // Toolbar at the top
        mainPanel_.add(mobileToolbar_);
        mainPanel_.setWidgetTopHeight(mobileToolbar_, 0, Unit.PX, TOOLBAR_HEIGHT_PX, Unit.PX);
    }

    /**
     * Setup the application layout in order to use 2 physical screens.
     * <p>
     * In dual screen mode
     * <ul>
     * <li>the Overview screen is on the left physical screen</li>
     * <li>the Action screen is on the right physical screen</li>
     * </ul>
     * </p>
     */
    private void setupDualScreenLayout() {
        createLargeScreenWidgets();
        // Overview screen on the left
        mainPanel_.add(overviewScreen_);
        mainPanel_.setWidgetLeftWidth(overviewScreen_, 0, Unit.PCT, 50, Unit.PCT);
        mainPanel_.setWidgetTopBottom(overviewScreen_, TOOLBAR_HEIGHT_PX, Unit.PX, 0, Unit.PX);

        // Action screen on the right
        mainPanel_.add(tabPanelsContainer_);
        mainPanel_.setWidgetRightWidth(tabPanelsContainer_, 0, Unit.PCT, 50, Unit.PCT);
        mainPanel_.setWidgetTopBottom(tabPanelsContainer_, TOOLBAR_HEIGHT_PX, Unit.PX, 0, Unit.PX);
    }

    /**
     * Setup the application layout in order to use 1 physical screens.
     * <p>
     * In mono screen mode, a TabLayoutPanel contains the overview screen and
     * any {@link ActionPanel} of the Action screen.
     * </p>
     */
    private void setupMonoScreenLayout() {
        createLargeScreenWidgets();
        tabPanelsContainer_.add(overviewScreen_, "overviewPanel", false);

        mainPanel_.add(tabPanelsContainer_);
        mainPanel_.setWidgetTopBottom(tabPanelsContainer_, TOOLBAR_HEIGHT_PX, Unit.PX, 0, Unit.PX);
    }

    private void setupMobileScreenLayout() {
        createMobileScreenWidgets();
        mainPanel_.add(mobileOverviewScreen_);
        mainPanel_.setWidgetTopBottom(mobileOverviewScreen_, TOOLBAR_HEIGHT_PX, Unit.PX, 0, Unit.PX);
    }

    /**
     * Setup the management of application events.
     */
    private void setupAppEventMgt() {

        appEventsHandler_ = new AppEventsHandler(tabPanelsContainer_, this);
    }

    public boolean isMobileScreen() {
        return mobileScreen_;
    }

    public MobileOverviewScreen getMobileOverviewScreen() {
        return mobileOverviewScreen_;
    }

}
