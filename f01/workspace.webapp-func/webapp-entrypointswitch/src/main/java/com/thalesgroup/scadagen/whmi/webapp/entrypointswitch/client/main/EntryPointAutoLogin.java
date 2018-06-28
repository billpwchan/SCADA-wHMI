package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.admin.client.model.benchmark.BenchmarkClientManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.admin.client.model.benchmark.UnitTestClientMwt;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.AppClientContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.Constants;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.RpcMonitorEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.monitor.RpcMonitorEvent.RpcMonitorEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.security.SecurityExceptionEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.tools.debug.MwtDebugTools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.client.rpc.RpcMonitor.RpcMonitorState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.CookieUtils;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.InfoDialog;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dnd.DndUtils;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.AppContextReadyEvent.AppContextReadyEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.ConfigReadyEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.ConfigReadyEvent.ConfigReadyEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.LogoutRequestEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.LogoutRequestEvent.LogoutRequestEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.ZoneSelectionProcessEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.entry.event.ZoneSelectionProcessEvent.ZoneSelectionProcessEventHandler;

/**
 * MWT Entry Point (Application Phase)
 */
public class EntryPointAutoLogin implements EntryPoint, ConfigReadyEventHandler, AppContextReadyEventHandler,
            ZoneSelectionProcessEventHandler, LogoutRequestEventHandler, RpcMonitorEventHandler {

    /** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();

    private EventBus eventBus_;

    /**
     * Constructor
     */
    public EntryPointAutoLogin() {
        super();
    }

    /**
     * Entry point.
     */
    @Override
    public void onModuleLoad() {
        CookieUtils.registerCookiePrefix(getCookiePrefix());
        onModuleLoadAfter();
    }

    /**
     * Get Cookie Name prefix for all MWT Cookies
     * @return Cookie Name prefix for all MWT Cookies
     */
    protected String getCookiePrefix() {
        return CookieUtils.cookiePfxApp_;
    }
    
    protected void onModuleLoadAfter() {
        /*
         * Install an UncaughtExceptionHandler which will produce <code>FATAL</code> log messages
         */
        Log.setUncaughtExceptionHandler();

        // Create Event Bus
        createEventBus();

        // Add Event Bus handlers
        addEventBusHandlers(getEventBus());

        // Load Tests
        final String unitTestId = UnitTestClientMwt.getInstance().getTestId();
        BenchmarkClientManager.getInstance().setTestActive(unitTestId != null && unitTestId.length() > 0);

        // Bridge Method
        MwtDebugTools.createBridgeMethod();

        /* Disables the browser's default context menu on all the application.
        Well, almost, SVG has to be processed on its own */
        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

        /* Disable Navigation Keys */
        UITools.disableKeyboardNavigationKeys();

        // Add Drag-and-Drop Handlers for RootLayoutPanel
        DndUtils.addRootLayoutDndHandlers();

        // Create Configuration Provider
        createConfigProvider(getEventBus());
    }

    protected void createEventBus() {
        // Create Event Bus
        setEventBus(new MwtEventBus());
    }

    protected void addEventBusHandlers(final EventBus eventBus) {
        // Add handler registration

        if (eventBus != null) {
            eventBus.addHandler(ConfigReadyEvent.TYPE, this);
            eventBus.addHandler(AppContextReadyEvent.TYPE, this);
            eventBus.addHandler(ZoneSelectionProcessEvent.TYPE, this);
            eventBus.addHandler(LogoutRequestEvent.TYPE, this);
            eventBus.addHandler(RpcMonitorEvent.TYPE, this);
        }
    }

    protected void createInfoDialog(final EventBus eventBus) {

        // Create InfoDialog
        final InfoDialog infoDialog = new InfoDialog(eventBus);

        if (eventBus != null) {
            eventBus.addHandler(RpcMonitorEvent.TYPE, infoDialog);
            eventBus.addHandler(SecurityExceptionEvent.TYPE, infoDialog);
        }
    }

    protected EventBus getEventBus() {
        return eventBus_;
    }

    protected void setEventBus(final EventBus eventBus) {
        eventBus_ = eventBus;
    }

    protected void createConfigProvider(final EventBus eventBus) {

        // Create {@link ConfigProvider}
        ConfigProvider.getInstance(eventBus, null);

        // Request Configuration
        ConfigProvider.getInstance().requestConfigToServer();
    }

    protected void createAppClientContext(final EventBus eventBus) {

        // Create {@link AppClientContext}
        AppClientContext.getInstance(eventBus, null);

        // Start update service
        AppClientContext.getInstance().startUpdateRequestLoop();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigReadyEvent(final ConfigReadyEvent event) {
        LOGGER.debug("MwtEntryPointApp - onConfigReadyEvent - " + event);

        createAppClientContext(getEventBus());
        createInfoDialog(getEventBus());

        onConfigReadyEventAfter(event);
    }


    /**
     * Called once the config is ready
     * @param event config read event
     */
    protected void onConfigReadyEventAfter(final ConfigReadyEvent event) {
        // Nothing to do - Can be overridden
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAppContextReadyEvent(final AppContextReadyEvent event) {
        LOGGER.debug("MwtEntryPointApp - onAppContextReadyEvent - " + event);

        checkWidgetTermination();

        onContextReadyEventAfter(event);
    }

    /**
     * Called once the context is ready
     * @param event context read event
     */
    protected void onContextReadyEventAfter(final AppContextReadyEvent event) {
        // Nothing to do - Can be overridden
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onZoneSelectionProcessEvent(final ZoneSelectionProcessEvent event) {
        LOGGER.debug("MwtEntryPointApp - onZoneSelectionProcessEvent - " + event);

        checkWidgetTermination();

        onZoneSelectionProcessEventAfter(event);
    }

    /**
     * Called once the zone selection is ready
     * @param event zone selection read event
     */
    protected void onZoneSelectionProcessEventAfter(final ZoneSelectionProcessEvent event) {
        // Nothing to do - Can be overridden
    }


    /**
     * check the termination of a widget
     */
    protected void checkWidgetTermination() {
        // Nothing to do - Can be overridden
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLogoutRequestEvent(final LogoutRequestEvent event) {
        // Set {@link AppClientContext} as Logout-Pending
        AppClientContext.getInstance().getRpcMonitor().setLogOutPending();

        // Redirect to Default Spring-Security Logout URL
        Window.Location.replace(Constants.SPRING_SEC_LOGOUT_URL);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRpcMonitorEvent(final RpcMonitorEvent event) {
        // Auto-login : Go back to Login page if [Disconnected and Server present again]
        if (RpcMonitorState.DISCONNECTED_SERVER_PRESENT.equals(event.getRpcMonitorState())) {
        	//if (CookieUtils.getAutoLoginCookie()) {
            //    History.back();
            //}
        }
    }
}
