
package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;

import com.google.gwt.dom.client.Style;
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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dialog.ChangePassPanelAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccOverviewScreen;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.util.ConfigurationConstantUtil;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget.ClockHeaderWidget;
import com.thalesgroup.scadasoft.gwebhmi.main.client.AppUtils;

/**
 * This class implements the application toolbar.
 */
public class CoccToolbar extends Composite {

    /**
     * CSS class applied to toolbar's items
     */
    public static final String CSS_TOOLBAR_ITEM = "showcase-toolbar-item";
    public static final String CSS_MENU_HORIZONTAL = "scs-menubar-horizontal";
    public static final String CSS_MENU_VERTICAL = "scs-menubar-vertical";
    public static final String CSS_TOOLBAR_CLOCK = "scs-toolbar-clock";
    /**
     * Main panel wrapping this widget and passed to its {@link Composite} parent.
     */
    private final FlowPanel mainPanel_;

    /**
     * History button
     */
    //private Button historyBtn_;
    
    //private Button realtimeSystemBtn_;
    
    //private Button bigScreenBtn_;
    
    //private Button businessAppBtn_;

    //private Button bigDataBtn_;
    
    //private Button cctvBtn_;
    
    //private Button appSwitchBtn_;
    
    private Button helpBtn_;
    
    private Button printBtn_;
    
    private CoccOverviewScreen overviewScreen_;
    
    private ClockHeaderWidget clockWidget_;
    
    /**
     * Constructor.
     * @param overviewScreen the overview screen instance
     */
    public CoccToolbar(CoccOverviewScreen overviewScreen) {
        overviewScreen_ = overviewScreen;
    	mainPanel_ = new FlowPanel();
        mainPanel_.addStyleName("showcase-toolbar");

        addLogo();
        addItemsSeparator();
        //addLogoutButton();
        addListMenu();
        addItemsSeparator();
        //addRealtimeSystemButton();
        //addItemsSeparator();
        //addBigScreenButton();
        //addItemsSeparator();
        //addBusinessAppButton();
        //addItemsSeparator();
        //addBigDataButton();
        //addItemsSeparator();
        //addCctvButton();
        //addItemsSeparator();
        //addAppSwitchButton();
        //addItemsSeparator();
        //addHistoryButton();
//        addItemsSeparator();
        addClockWidget();
        addHelpButton();
        addPrintButton();

        initWidget(mainPanel_);
    }

    /**
     * Creates and add the Thalesgroup logo to the toolbar.
     */
    private void addLogo() {
        final Image logo = new Image("resources/project/img/GZMetroLogo.png");
        logo.addStyleName("showcase-toolbar-logo");
        logo.addStyleName(CSS_TOOLBAR_ITEM);

        mainPanel_.add(logo);
    }

    /**
     * Creates and add the history button to the toolbar.
     */
//    private void addHistoryButton() {
//        historyBtn_ = new Button(Dictionary.getWording("toolbar_btn_history"));
//        historyBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
//                final NavigationActivationEvent navigationEvent =
//                    new NavigationActivationEvent(ConfigurationConstantUtil.SpecialNavPlaces.HISTORY.getValue());
//
//                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//        historyBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(historyBtn_);
//    }
    
//    private void addRealtimeSystemButton() {
//    	realtimeSystemBtn_ = new Button(Dictionary.getWording("toolbar_btn_realtimeSystem"));
//    	realtimeSystemBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.REALTIME_SYSTEM_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//    	realtimeSystemBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(realtimeSystemBtn_);
//    }
    
//    private void addBigScreenButton() {
//    	bigScreenBtn_ = new Button(Dictionary.getWording("toolbar_btn_bigScreen"));
//    	bigScreenBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.BIG_SCREEN_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//    	bigScreenBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(bigScreenBtn_);
//    }
    
//    private void addBusinessAppButton() {
//    	businessAppBtn_ = new Button(Dictionary.getWording("toolbar_btn_businessApp"));
//    	businessAppBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                 final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.BusinessApp_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//        businessAppBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(businessAppBtn_);
//    }
    
//    private void addBigDataButton() {
//    	bigDataBtn_ = new Button(Dictionary.getWording("toolbar_btn_bigData"));
//    	bigDataBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.BIG_DATA_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//    	bigDataBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(bigDataBtn_);
//    }
    
//    private void addCctvButton() {
//    	cctvBtn_ = new Button(Dictionary.getWording("toolbar_btn_cctv"));
//    	cctvBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.CCTV_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//    	cctvBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(cctvBtn_);
//    }
    
//    private void addAppSwitchButton() {
//    	appSwitchBtn_ = new Button(Dictionary.getWording("toolbar_btn_appSwitch"));
//    	appSwitchBtn_.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(final ClickEvent event) {
////                final NavigationActivationEvent navigationEvent =
////                    new NavigationActivationEvent(ConfigurationConstantUtil.APP_SWITCH_ID);
//
////                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
//            }
//        });
//    	appSwitchBtn_.addStyleName(CSS_TOOLBAR_ITEM);
//
//        mainPanel_.add(appSwitchBtn_);
//    }
    
    private void addHelpButton() {
    	helpBtn_ = new Button(Dictionary.getWording("toolbar_btn_help"));
    	helpBtn_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
//                 final NavigationActivationEvent navigationEvent =
//                    new NavigationActivationEvent(ConfigurationConstantUtil.Help_ID);

//                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
            }
        });
    	helpBtn_.addStyleName(CSS_TOOLBAR_ITEM);
    	helpBtn_.getElement().getStyle().setFloat(Style.Float.RIGHT);

        mainPanel_.add(helpBtn_);
    }
    
    private void addPrintButton() {
    	printBtn_ = new Button(Dictionary.getWording("toolbar_btn_print"));
    	printBtn_.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
//                 final NavigationActivationEvent navigationEvent =
//                    new NavigationActivationEvent(ConfigurationConstantUtil.Print_ID);

//                AppUtils.EVENT_BUS.fireEventFromSource(navigationEvent, event.getSource());
            }
        });
    	printBtn_.addStyleName(CSS_TOOLBAR_ITEM);
    	printBtn_.getElement().getStyle().setFloat(Style.Float.RIGHT);

        mainPanel_.add(printBtn_);
    }
    
    private void addClockWidget() {
    	clockWidget_ = new ClockHeaderWidget();
    	
    	clockWidget_.addStyleName(CSS_TOOLBAR_ITEM);
    	clockWidget_.addStyleName(CSS_TOOLBAR_CLOCK);
    	clockWidget_.getElement().getStyle().setFloat(Style.Float.RIGHT);
    	
    	mainPanel_.add(clockWidget_);
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
     * Create application menu<br>
     * Contains the logout menu and the list menu
     */
    private void addListMenu(){

        //create logout button
        Command cmdLogout = new Command() {
            @Override
            public void execute() {
            	Window.Location.replace("j_spring_security_logout");
            }
        };

        Command cmdChangePass = new Command() {
            @Override
            public void execute(){
                
                final ChangePassPanelAbstract changePassPanel_ = new ChangePassPanelAbstract() {

                    @Override
                    public boolean checkPassword(String password) {
                        if(password.length() > 5)
                            return true;
                        return false;
                    }
                    
                    @Override
                    public String getRegExpPassword() {
                    	
                    	return ".+";
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
            }
        };

//        Command cmdDpcEqpList = new Command() {
//            @Override
//            public void execute() {
//                overviewScreen_.addDpcListTab();
//            }
//        };

        Command connectionMatrix = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addConnectionListTab();
            }
        };
        
//        Command cmdAlarmList = new Command() {
//            @Override
//            public void execute() {
//                overviewScreen_.addAlarmListTab();
//            }
//        };

        Command cmdEventGrid = new Command() {
            @Override
            public void execute() {
                //overviewScreen_.addEventListTab();
            	final NavigationActivationEvent navigationEvent =
                        new NavigationActivationEvent(ConfigurationConstantUtil.EVENT_LIST_ID);

                    AppUtils.EVENT_BUS.fireEvent(navigationEvent);
            }
        };

        Command cmdAlarmMatrix = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addAlarmDiagramTab();
            }
        };
        
//        Command cmdGis = new Command() {
//            @Override
//            public void execute() {
//                overviewScreen_.addGisMapTab();
//            }
//        };
        
//        Command cmdPower = new Command() {
//            @Override
//            public void execute() {
//                overviewScreen_.addPowerTab();
//            }
//        };
        
        Command cmdPower110KV = new Command() {
            @Override
            public void execute() {
                //overviewScreen_.addPower110KVTab();
                final NavigationActivationEvent navigationEvent =
                                new NavigationActivationEvent(ConfigurationConstantUtil.COCC_POW_110KV);

                            AppUtils.EVENT_BUS.fireEvent(navigationEvent);
            }
        };
        
        Command cmdWaterPumpSummary = new Command() {
            @Override
            public void execute() {
                overviewScreen_.addWaterPumpSummaryTab();
            }
        };
        
        Command cmdEqpQuery = new Command() {
        	public void execute() {
        		final NavigationActivationEvent navigationEvent =
                        new NavigationActivationEvent(ConfigurationConstantUtil.EQUIPMENT_QUERY_PANEL_ID);

                    AppUtils.EVENT_BUS.fireEvent(navigationEvent);
        	}
        };
        
//        Command cmdEqpMatrix = new Command() {
//        	public void execute() {
//        		overviewScreen_.addEqpMatrixTab(ConfigurationConstantUtil.EQUIPMENT_MATRIX_ID);
//        	}
//        };
        
       
        MenuBar menu1 = new MenuBar(true); 
        menu1.addItem(Dictionary.getWording("toolBarItemLogout"),cmdLogout);
        menu1.addItem(Dictionary.getWording("toolBarItemChangePwd"),cmdChangePass);
        menu1.addStyleName(CSS_MENU_VERTICAL);

        MenuBar menu2 = new MenuBar(true); 
//        menu2.addItem(Dictionary.getWording("toolBarItemAlarmList"),cmdAlarmList); 
//        menu2.addItem(Dictionary.getWording("toolBarItemDpcEqp"),cmdDpcEqpList);
        menu2.addItem(Dictionary.getWording("toolBarItemConnection"),connectionMatrix);
        menu2.addItem(Dictionary.getWording("toolBarItemAlarmDiagram"),cmdAlarmMatrix);
//        menu2.addItem(Dictionary.getWording("toolBarItemGIS"),cmdGis);
        menu2.addItem(Dictionary.getWording("toolBarItemPower110KV"),cmdPower110KV);
        menu2.addItem(Dictionary.getWording("toolBarItemWaterPumpSummary"),cmdWaterPumpSummary);
        menu2.addItem(Dictionary.getWording("toolBarItemEqpQuery"),cmdEqpQuery);
        menu2.addItem(Dictionary.getWording("toolBarItemEvenList"),cmdEventGrid); 
        //menu2.addItem(Dictionary.getWording("toolBarItemEqpMatrix"),cmdEqpMatrix);

        menu2.addStyleName(CSS_MENU_VERTICAL);
      
        
        MenuBar menuLoggin = new MenuBar();
        menuLoggin.addItem(Dictionary.getWording("toolBarLoginMenu")+ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId()+"  "+'\u25BE',menu1);   
        menuLoggin.addStyleName(CSS_MENU_HORIZONTAL);
        mainPanel_.add(menuLoggin);
        addItemsSeparator();
        MenuBar menuList = new MenuBar();
        menuList.addItem(Dictionary.getWording("toolBarList")+'\u25BE',menu2);
        menuList.addStyleName(CSS_MENU_HORIZONTAL);
        mainPanel_.add(menuList);
    
    }
}
