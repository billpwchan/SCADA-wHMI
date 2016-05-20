
package com.thalesgroup.scadasoft.gwebhmi.main.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationActivationEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.ChangePassPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.ChangePassPanelAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.soundserver.SoundServerPanel;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadasoft.gwebhmi.main.client.layout.OverviewScreen;
import com.thalesgroup.scadasoft.gwebhmi.main.client.util.ConfigurationConstantUtil;

/**
 * This class implements the application toolbar.
 */
public class Toolbar extends Composite {

    /**
     * CSS class applied to toolbar's items
     */
    public static final String CSS_TOOLBAR_ITEM = "showcase-toolbar-item";
    public static final String CSS_MENU_HORIZONTAL = "scs-menubar-horizontal";
    public static final String CSS_MENU_VERTICAL = "scs-menubar-vertical";
    /**
     * Main panel wrapping this widget and passed to its {@link Composite}
     * parent.
     */
    private final FlowPanel mainPanel_;

    /**
     * History button
     */
    private Button historyBtn_;

    private OverviewScreen overviewScreen_;

    /**
     * Constructor.
     * 
     * @param overviewScreen
     *            the overview screen instance
     */
    public Toolbar(OverviewScreen overviewScreen) {
        overviewScreen_ = overviewScreen;
        mainPanel_ = new FlowPanel();
        mainPanel_.addStyleName("showcase-toolbar");

        addLogo();
        addSoundController();
        addItemsSeparator();
        // addLogoutButton();
        addListMenu();
        addItemsSeparator();
        addHistoryButton();
        addItemsSeparator();
        initWidget(mainPanel_);
    }

    /**
     * Creates and add the Thalesgroup logo to the toolbar.
     */
    private void addLogo() {
        final Image logo = new Image("img/thalesLogo.png");
        logo.addStyleName("showcase-toolbar-logo");
        logo.addStyleName(CSS_TOOLBAR_ITEM);

        mainPanel_.add(logo);
    }

    /**
     * Creates and add the history button to the toolbar.
     */
    private void addHistoryButton() {
        historyBtn_ = new Button(Dictionary.getWording("toolbar_btn_history"));
        historyBtn_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final NavigationActivationEvent navigationEvent = new NavigationActivationEvent(
                        ConfigurationConstantUtil.SpecialNavPlaces.HISTORY.getValue());

                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
            }
        });
        historyBtn_.addStyleName(CSS_TOOLBAR_ITEM);

        mainPanel_.add(historyBtn_);
    }

    /**
     * Creates and add a separator to the toolbar.
     */
    private void addItemsSeparator() {
        final Label sep = new Label();
        sep.addStyleName("showcase-toolbar-sep");
        mainPanel_.add(sep);
    }

    /**
     * Add a panel for sound control
     */
    private void addSoundController() {
        final SoundServerPanel sndPanel = new SoundServerPanel(AppUtils.EVENT_BUS);
       
        sndPanel.addStyleName(CSS_TOOLBAR_ITEM);
        
        mainPanel_.add(sndPanel);

        addItemsSeparator();
    }
    
    /**
     * Create application menu<br>
     * Contains the logout menu and the list menu
     */
    private void addListMenu() {

        // create logout button
        Command cmdLogout = new Command() {
            @Override
            public void execute() {
                Window.Location.replace("j_spring_security_logout");
            }
        };

        Command cmdChangePass = new Command() {
            @Override
            public void execute() {
/*
                final ChangePassPanelAbstract changePassPanel_ = new ChangePassPanelAbstract() {

                    @Override
					public boolean checkPassword(String password) {
                        if (password.length() > 5)
                            return true;
                        return false;
                    }

					@Override
					public String getRegExpPassword() {
						// TODO Auto-generated method stub
						return null;
					}

                };

                changePassPanel_.setGlassEnabled(true);
                changePassPanel_.show();
                changePassPanel_.setPopupPositionAndShow(new PositionCallback() {
                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = ((Window.getClientWidth() - offsetWidth) / 4) >> 0;
                        int top = ((Window.getClientHeight() - offsetHeight) / 2) >> 0;
                        changePassPanel_.setPopupPosition(left, top);
                    }
                });
                */
                ChangePassPanel cp = new ChangePassPanel();
                cp.show();
            }
        };

        Command cmdDpcEqpList = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addDpcListTab();
            }
        };

        Command cmdOpeNotifList = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addOperationNotifListTab();
            }
        };
        
        Command connectionEqpList = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addConnectionListTab();
            }
        };

        Command cmdAlarmList = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addAlarmListTab();
            }
        };

        Command cmdEventGrid = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addEventListTab();
            }
        };

        Command cmdAlarmMatrix = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addAlarmDiagramTab();
                ;
            }
        };

        Command cmdTSSConf = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addTSSConfTab();
            }
        };
        
        MenuBar menu1 = new MenuBar(true);
        menu1.addItem(Dictionary.getWording("toolBarItemLogout"), cmdLogout);
        menu1.addItem(Dictionary.getWording("toolBarItemChangePwd"), cmdChangePass);
        menu1.addStyleName(CSS_MENU_VERTICAL);

        MenuBar menu2 = new MenuBar(true);
        menu2.addItem(Dictionary.getWording("toolBarItemAlarmList"), cmdAlarmList);
        menu2.addItem(Dictionary.getWording("toolBarItemEvenList"), cmdEventGrid);
        menu2.addItem(Dictionary.getWording("toolBarItemDpcEqp"), cmdDpcEqpList);
        menu2.addItem(Dictionary.getWording("toolBarItemNotifList"), cmdOpeNotifList);
        menu2.addItem(Dictionary.getWording("toolBarItemConnection"), connectionEqpList);
        menu2.addItem(Dictionary.getWording("toolBarItemAlarmDiagram"), cmdAlarmMatrix);
        menu2.addItem(Dictionary.getWording("toolBarItemTSSConf"), cmdTSSConf);

        menu2.addStyleName(CSS_MENU_VERTICAL);

        MenuBar menuLoggin = new MenuBar();
        menuLoggin.addItem(
                Dictionary.getWording("toolBarLoginMenu")
                        + ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId() + "  " + '\u25BE',
                menu1);
        menuLoggin.addStyleName(CSS_MENU_HORIZONTAL);
        mainPanel_.add(menuLoggin);
        addItemsSeparator();
        MenuBar menuList = new MenuBar();
        menuList.addItem(Dictionary.getWording("toolBarList") + '\u25BE', menu2);
        menuList.addStyleName(CSS_MENU_HORIZONTAL);
        mainPanel_.add(menuList);

    }
}
