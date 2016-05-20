package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.TabLayoutPanelWithScrollButtons;

/**
 * A widget that provides a Tabulation panel with closeable tabs.
 */
public class CoccTabLayoutPanel extends ResizeComposite implements HasCloseHandlers<String>, SelectionHandler<Integer> {

    /**
     * CSS class applied to this widget
     */
    public static final String CSS_TAB_CONTAINER = "showcase-tab-container";

    /**
     * CSS class applied when tabs overflow the container
     */
    public static final String CSS_TABS_OVERFLOW = "tabsOverflow";

    /**
     * Prefix of dictionary keys used to retrieved the caption displayed in {@link CoccTabLayoutPanel}
     * tabs
     */
    private static final String KEY_TAB_DICO = "tabLayout_";

    /** Key of Local Config Item for Max Num of Tabs */
    private static final String LOCAL_CONFIG_KEY_MAX_NUM_OF_TABS = "MaxNumOfTabs";

	/** Logger */
	private static final ClientLogger logger_ = ClientLogger.getClientLogger();

    // Maximum Number of Tabs
    private int MAX_NUM_OF_TABS = 10;
    /**
     * Tabs bar height
     */
    protected static final int DEFAULT_BAR_HEIGHT_PX = 40;

    // DOM Element of the Tab Bar
    private Element tabBarElement_;

    // Offset Location of the Tab Bar DOM Element
    private int tabBarElementOffsetX_ = 0;

    // Sum of Widths of all Tabs
    private int tabsWidth_ = 0;

    // Indicates whether the Container is Overflowed by the Tabs
    private boolean isContainerOverflowed_ = false;

    /**
     * Managed GWT {@link com.google.gwt.user.client.ui.TabLayoutPanel}
     */
    //protected final com.google.gwt.user.client.ui.TabLayoutPanel mainPanel_;
    protected final TabLayoutPanelWithScrollButtons mainPanel_;

    /*
    public class TabBarAnimation extends Animation {
    	private Element tabBarElement_ = null;
    	private int offsetDiff_ = 0;
        private final ClientLogger s_logger = ClientLogger.getClientLogger();

    	public TabBarAnimation(Element inElement, int inOffsetDiff) {
    		super();
    		setTabBarElement(inElement);
    		setOffsetDiff(inOffsetDiff);
    	}
    	@Override
    	protected void onUpdate(double progress) {
    		// TODO Auto-generated method stub
    	}

    	protected void onComplete() {
    		super.onComplete();
    	}

    	public void setOffsetDiff(int inOffsetDiff) {
    		offsetDiff_ = inOffsetDiff;
    	}
    	public void setTabBarElement(Element inElement) {
    		tabBarElement_ = inElement;
    	}
    }
    */

    //private final ClientLogger s_logger = ClientLogger.getClientLogger();

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
     * Creates a {@link CoccTabLayoutPanel} panel with the default tab bar height.
     */
    public CoccTabLayoutPanel() {
        this(DEFAULT_BAR_HEIGHT_PX, Unit.PX);
    }

    /**
     * Creates a {@link CoccTabLayoutPanel} panel with a specified tab bar height.
     *
     * @param barHeight the size of the tab bar
     * @param barUnit the unit in which the tab bar size is specified
     */
    public CoccTabLayoutPanel(final double barHeight, final Unit barUnit) {
        //mainPanel_ = new com.google.gwt.user.client.ui.TabLayoutPanel(barHeight, barUnit);
        mainPanel_ = new TabLayoutPanelWithScrollButtons(barHeight, barUnit);
        initWidget(mainPanel_);

		// Retrieve Configured Max Num of Tabs from Local Config
		int maxNumOfTabs;
		String strMaxNumOfTabs = ConfigProvider.getInstance().getClientData().getProjectConfigurationMap().get(LOCAL_CONFIG_KEY_MAX_NUM_OF_TABS);
		try {
			if(strMaxNumOfTabs == null) {
				// Item not Found in Local Config
				logger_.info("Item \"" + LOCAL_CONFIG_KEY_MAX_NUM_OF_TABS +
						"\" is not defined in local config.");
			} else if((maxNumOfTabs = Integer.parseInt(strMaxNumOfTabs)) <= 0) {
				// Invalid Value
				logger_.info("Invalid Value of \"" + LOCAL_CONFIG_KEY_MAX_NUM_OF_TABS + "\": "
						+ maxNumOfTabs);
			} else {
				// Successfully retrieved property and parse the value, which
				// is greater than 0
				MAX_NUM_OF_TABS = maxNumOfTabs;
			}
		} catch(Exception e) {
			// Error while Parsing the Value
			logger_.error("Failed to Parse Value of \""
					+ LOCAL_CONFIG_KEY_MAX_NUM_OF_TABS + "\": " + e.getMessage());
		}

        // Add Click Event Handler for the Prev Button
        mainPanel_.attachLeftBtnClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollToPreviousTab();
			}
        });

        // Add Click Event Handler for the Next Button
        mainPanel_.attachRightBtnClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollToNextTab();
			}
        });

        // Add Window Resize Handler
        Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				addScrollBtnsIfTabsOverflow();
			}
        });

        // Add CSS Class Name and Selection Handler
        addStyleName(CSS_TAB_CONTAINER);
        addSelectionHandler(this);
    }

    /**
     * Allows to add tab styled according to MWT style guide.
     *
     * @param widget the widget displayed inside the tab main panel
     * @param navigationId navigation Id in tab
     * @param closeable specifies whether the tab can be closed
     */
    public void add(final Widget widget, final String navigationId, final boolean closeable) {
        if (!contains(widget)) {

            final FlowPanel newTab = new FlowPanel();
            newTab.addStyleName("mwt-tabs-toolbar");

            final String caption = Dictionary.getWording(KEY_TAB_DICO + navigationId);
            final Label titleLbl = new Label(caption);
            newTab.add(titleLbl);

            final Image tabIcon;
            if (closeable == true) {
                tabIcon = new Image("Images/tab-close.gif");
                
                tabIcon.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(final ClickEvent event) {
                        CoccTabLayoutPanel.this.remove(widget);
                        CloseEvent.fire(CoccTabLayoutPanel.this, navigationId);
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
     * @param widget the widget whose tab is to be removed
     */
    public void remove(final Widget widget) {
        final int widgetIdx = mainPanel_.getWidgetIndex(widget),
        		selectedIdx = mainPanel_.getSelectedIndex();
        if (widgetIdx >= 0) {
            if (widget instanceof IClientLifeCycle) {
                ((IClientLifeCycle) widget).terminate();
            }
            mainPanel_.remove(widgetIdx);

			if(selectedIdx != widgetIdx) {
				// Add Scroll Buttons if Tabs Overflow the Container
				addScrollBtnsIfTabsOverflow();
			}
        }
    }

    /**
     * Selects the specified tab.
     *
     * @param index the index of the tab to be selected
     */
    public void selectTab(final int index) {
        mainPanel_.selectTab(index);
    }
    
    /**
     * Select the tab containing a given widget.
     *
     * @param widget the widget whose tab is to be selected
     */
    public void selectTab(final Widget widget) {
        if (contains(widget)) {
            mainPanel_.selectTab(widget);
        }
    }

    /**
     * Returns <code>true</code> if this {@link CoccTabLayoutPanel} contains a given widget.
     *
     * @param widget the widget
     * @return <code>true</code> if this {@link CoccTabLayoutPanel} contains the widget;
     *         <code>false</code> otherwise
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
     * @param index the index of the tab to be retrieved
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
     * @param handler selection handler
     * @return the handler registration
     */
    public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
        return mainPanel_.addSelectionHandler(handler);
    }

    /**
     * Adds {@link CloseHandler} called when a tab is closed.
     *
     * @param handler handler
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
        // [COCC-16] Add Scroll Buttons if Tabs Overflow the Container
        addScrollBtnsIfTabsOverflow();

        Scheduler.get().scheduleDeferred(scheduledResize_);
    }

	// 2015-07-23 delo: [COCC-16] Tab Management
	/**
	 * Adds CSS class name to the container if overflowed by its tabs or
	 * removes the class name otherwise
	 */
	public void addScrollBtnsIfTabsOverflow() {
		Widget tmpWidget;	// Temporarily store the Tab widget
		int widgetCount = mainPanel_.getWidgetCount();

		if(widgetCount == 1) {
			// Singular Tab
			displayScrollBtns(false);
		} else {
			// Calculate if Tabs Overflow the Container
			tabsWidth_ = 0;
			for(int iWidget = 0, tmpWidth = 0;
					iWidget < widgetCount && (tmpWidget = mainPanel_.getTabWidget(iWidget)) != null;
					iWidget++) {
				// Offset Width of the Widget plus 2px Margin
				tabsWidth_ += (tmpWidth = tmpWidget.getOffsetWidth()) > 0 ? tmpWidth + 2 : 0;
			}

			// Adjust Class Name to Indicate whether Tabs Overflow the Container
			if(tabsWidth_ > mainPanel_.getOffsetWidth()) {
				// Overflowed, Show Scroll Buttons
				displayScrollBtns(true);

				// Make sure the Entire Selected Tab is Visible
				ensureTabVisible(mainPanel_.getSelectedIndex());
			} else {
				// Not Overflowed, Hide Scroll Buttons
				displayScrollBtns(false);
			}
		}
	}

	/**
	 * Shows or hides the scroll buttons
	 * @param isContainerOverflowed
	 */
	public void displayScrollBtns(boolean isContainerOverflowed) {
		if(isContainerOverflowed) {
			mainPanel_.addStyleName(CSS_TABS_OVERFLOW);
		} else {
			mainPanel_.removeStyleName(CSS_TABS_OVERFLOW);
		}
		initTarBarElementOffset(isContainerOverflowed);
	}

	// 2015-07-23 delo: [COCC-16] Tab Management
	/**
	 * Returns true if the number of tabs has reached limit, or false otherwise
	 * @return whether the number of tabs has reached limit
	 */
	public boolean reachesMaxNumOfTabs() {
		boolean retValue;
		if(mainPanel_.getWidgetCount() >= MAX_NUM_OF_TABS) {
			retValue = true;
		} else {
			retValue = false;
		}
		return retValue;
	}

	/**
	 * Initializes the location of the tab bar
	 * @param inTabsOverflow Indicates whether the tabs overflow their container
	 */
	public void initTarBarElementOffset(boolean inTabsOverflow) {
		Element tabBarElement = getTabBarElement();
		if(tabBarElement != null) {
			if(!inTabsOverflow) {
				// Not Overflowed, Move the Element back to Initial Position
				tabBarElement.getStyle().setLeft(0, Unit.PX);
				isContainerOverflowed_ = false;
			} else if(!isContainerOverflowed_) {
				// Overflowed, Move slightly Right for Scroll Buttons
				tabBarElement.getStyle().setLeft(DEFAULT_BAR_HEIGHT_PX, Unit.PX);
				tabBarElementOffsetX_ = 0;
				isContainerOverflowed_ = true;
			}
		}
	}

	/**
	 * Scrolls to show the next tab
	 */
	public void scrollToNextTab() {
		Element tabBarElement;	// DOM Element of the Tab Bar
		Widget tmpWidget;		// Temporarily store the Tab widget
		if(isContainerOverflowed_ && (tabBarElement = getTabBarElement()) != null) {
			for(int iWidget = 0,
						rightBtnLeft = mainPanel_.getParent().getOffsetWidth() - DEFAULT_BAR_HEIGHT_PX,
						tmpRight = DEFAULT_BAR_HEIGHT_PX - tabBarElementOffsetX_,
						widgetCount = mainPanel_.getWidgetCount();
					iWidget < widgetCount && (tmpWidget = mainPanel_.getTabWidget(iWidget)) != null;
					iWidget++) {
				tmpRight += tmpWidget.getOffsetWidth() + 2;
				if(tmpRight > rightBtnLeft) {
					// Tab and Right Button overlapped, slightly Move the Tab
					// Bar so the entire Tab is Shown
					moveTabBarTo(tabBarElement, tabBarElementOffsetX_ += tmpRight - rightBtnLeft);
					break;
				} else if(tmpRight == rightBtnLeft && iWidget + 1 < widgetCount) {
					// Move to the Next Tab
					if((tmpWidget = mainPanel_.getTabWidget(iWidget + 1)) != null) {
						moveTabBarTo(tabBarElement, tabBarElementOffsetX_ += mainPanel_.getTabWidget(iWidget + 1).getOffsetWidth() + 2);
					}
					break;
				} else if(tmpRight < rightBtnLeft && iWidget + 1 >= widgetCount && tabBarElementOffsetX_ > 0) {
					// Extra Space between Last Tab and Right Button, this
					// usually Happens after Resizing the Window
					if(rightBtnLeft - tmpRight < tabBarElementOffsetX_) {
						moveTabBarTo(tabBarElement, tabBarElementOffsetX_ -= rightBtnLeft - tmpRight);
					} else {
						moveTabBarTo(tabBarElement, tabBarElementOffsetX_ = 0);
					}
					break;
				}
			}
		}
	}

	/**
	 * Scrolls to show the previous tab
	 */
	public void scrollToPreviousTab() {
		Element tabBarElement;	// DOM Element of the Tab Bar Element
		Widget tmpWidget;		// Temporarily store the Tab widget
		if(isContainerOverflowed_ && (tabBarElement = getTabBarElement()) != null && tabBarElementOffsetX_ > 0) {
			for(int iWidget = 0,
						tmpLeft = DEFAULT_BAR_HEIGHT_PX - tabBarElementOffsetX_,
						tmpRight = 0,
						tmpWidth = 0;
					iWidget < mainPanel_.getWidgetCount() && (tmpWidget = mainPanel_.getTabWidget(iWidget)) != null;
					iWidget++) {
				tmpWidth = tmpWidget.getOffsetWidth() + 2;
				tmpRight = tmpLeft + tmpWidth;
				if(tmpRight > DEFAULT_BAR_HEIGHT_PX) {
					moveTabBarTo(tabBarElement, tabBarElementOffsetX_ -= DEFAULT_BAR_HEIGHT_PX - tmpLeft);
					break;
				} else if(tmpRight == DEFAULT_BAR_HEIGHT_PX) {
					moveTabBarTo(tabBarElement, tabBarElementOffsetX_ -= tmpWidth);
					break;
				} else {
					tmpLeft = tmpRight;
				}
			}
		}
	}

	/**
	 * Ensures the specified tab is visible in the tab bar
	 * @param inTabIdx Index of the tab
	 */
	private void ensureTabVisible(int inTabIdx) {
		Element tabBarElement;	// DOM Element of the Tab Bar
		Widget tmpWidget;		// Temporarily Stores that Tab Widget
		int widgetCount;		// Number of Tabs
		//s_logger.debug("TabLayoutPanel ensureTabVisible: " + inTabIdx);
		if(isContainerOverflowed_ &&
				(tabBarElement = getTabBarElement()) != null &&
				inTabIdx >= 0 &&
				inTabIdx < (widgetCount = mainPanel_.getWidgetCount())) {
			for(int iWidget = 0,
						rightBtnLeft = mainPanel_.getParent().getOffsetWidth() - DEFAULT_BAR_HEIGHT_PX,
						tmpLeft = DEFAULT_BAR_HEIGHT_PX - tabBarElementOffsetX_,
						tmpRight = 0;
					iWidget < widgetCount && (tmpWidget = mainPanel_.getTabWidget(iWidget)) != null;
					iWidget++, tmpLeft = tmpRight) {
				tmpRight = tmpLeft + tmpWidget.getOffsetWidth() + 2;
				if(iWidget + 1 == widgetCount && tmpRight < rightBtnLeft) {
					// Space Left between the Last Tab and the Right Button
					// Shift Right to Fill-in the Space
					moveTabBarTo(tabBarElement, tabBarElementOffsetX_ -= rightBtnLeft - tmpRight);
				} else if(iWidget == inTabIdx) {
					if(tmpLeft < DEFAULT_BAR_HEIGHT_PX) {
						// Tab is partially or entirely hidden beyond the left
						// button, shift right to show the whole tab
						moveTabBarTo(tabBarElement, tabBarElementOffsetX_ -= DEFAULT_BAR_HEIGHT_PX - tmpLeft);
						break;
					} else if(tmpRight > rightBtnLeft) {
						// Tab is partially or entirely hidden beyond the right
						// button, shift left to show the whole tab
						moveTabBarTo(tabBarElement, tabBarElementOffsetX_ += tmpRight - rightBtnLeft);
						break;
					}
				}
			}
		}
	}

	/**
	 * Gets and returns the DOM element of the tab bar
	 * @return DOM element of the tab bar
	 */
	private Element getTabBarElement() {
		Element retElement = null;
		if (tabBarElement_ != null) {
			// Element already Found, Return the Element
			retElement = tabBarElement_;
		} else {
			// Element not Found yet, Go through each Child Node
			for(int i = 0; i < mainPanel_.getElement().getChildCount(); i++) {
				Node child = mainPanel_.getElement().getChild(i), grandChild;
				if(child.hasChildNodes() &&
						(grandChild = child.getFirstChild()).getNodeType() == Node.ELEMENT_NODE &&
						((Element) grandChild).getClassName() == "gwt-TabLayoutPanelTabs") {
					// Element Found, Store and Return the Element
					retElement = (Element)grandChild;
					break;
				}
			}

			// Store the Element
			if(retElement != null) {
				tabBarElement_ = retElement;
			}
		}
		return retElement;
	}

	/**
	 * Moves the tab bar left
	 * @param inElement DOM element of the tab bar
	 * @param inLeft How far the tab bar should shift left from its origin (in px)
	 */
	private void moveTabBarTo(Element inElement, int inLeft) {
		if(inElement != null) {
			inElement.getStyle().setLeft(DEFAULT_BAR_HEIGHT_PX - inLeft, Unit.PX);
		}
	}
}
