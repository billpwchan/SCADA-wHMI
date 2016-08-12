package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;

public class ActionView extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(ActionView.class.getName());
	
	private String logPrefix			= "[ActionView] ";
	
	private SimpleEventBus eventBus 	= null;

	private CtlMgr ctlMgr 				= null;
	
	private String column_alias			= "ptwdciset_alias";
	private String column_status		= "ptwdciset_value";
	private String column_serviceOwner	= "serviceOwnerID";
	
	final String strSet					= "set";
	final String strUnSet				= "unset";
	final String strApply				= "apply";
	
	final String strDci					= "dci";
	final String strDio					= "dio";
	
	private Widget widgetSet			= null;
	private Widget widgetUnSet			= null;
	private Widget widgetApply			= null;
	
//	private String selectedServiceOwner	= null;
//	private String selectedAlias		= null;
//	private String selectedStatus		= null;
	
	private Set<HashMap<String, String>> selectedSet = null;
	
	private void onButton(ClickEvent event) {
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
				if ( null != element ) {
					
					WidgetStatus statusSet		= null;
					WidgetStatus statusUnSet	= null;
					WidgetStatus statusApply	= null;
					
					if ( element.equals(strSet) ) {

						statusSet				= WidgetStatus.Down;
						statusUnSet				= WidgetStatus.Disable;
						statusApply				= WidgetStatus.Up;

					} else if ( element.equals(strUnSet) ) {
						
						statusSet				= WidgetStatus.Disable;
						statusUnSet				= WidgetStatus.Down;
						statusApply				= WidgetStatus.Up;

					} else if ( element.equals(strApply) ) {

						statusSet				= WidgetStatus.Disable;
						statusUnSet				= WidgetStatus.Disable;
						statusApply				= WidgetStatus.Disable;
						
						for ( HashMap<String, String> hashMap : selectedSet ) {
							String selectedAlias = hashMap.get(column_alias);
//							String selectedStatus = hashMap.get(column_status);
							String selectedServiceOwner = hashMap.get(column_serviceOwner);
							
							String scsEnvId = selectedServiceOwner;
							String alias = selectedAlias;
							
							logger.log(Level.SEVERE, logPrefix + "onClickHandler alias BF ["+alias+"]");
							
							alias = selectedAlias.replace(strDci, strDio);
							
							logger.log(Level.SEVERE, logPrefix + "onClickHandler alias AF ["+alias+"]");
							
							WidgetStatus curStatusSet = uiWidgetGeneric.getWidgetStatus(widgetSet);
							
							int value = 0;
							if ( WidgetStatus.Down == curStatusSet ) {
								value = 1;
							}
							
							ctlMgr.sendControl(scsEnvId, new String[]{alias}, value, 1, 1, 1);
						}

					}
					
					if ( null != widgetSet && null != statusSet )		uiWidgetGeneric.setWidgetStatus(widgetSet, statusSet);
					if ( null != widgetUnSet && null != statusUnSet )	uiWidgetGeneric.setWidgetStatus(widgetUnSet, statusUnSet);
					if ( null != widgetApply && null != statusApply )	uiWidgetGeneric.setWidgetStatus(widgetApply, statusApply);
					
				}
			} else {
				logger.log(Level.SEVERE, logPrefix+"onClickHandler onClickHandler button IS NULL");
			}
		}
	}


	private UIWidgetGeneric uiWidgetGeneric = null;
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	@SuppressWarnings("unchecked")
	void onActionReceived(UIEventAction uiEventAction) {
		
		String op	= (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
		String od1	= (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
		String od2	= (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
		String od3	= (String) uiEventAction.getAction(ViewAttribute.OperationString3.toString());
		
		Object obj1 = uiEventAction.getAction(ViewAttribute.OperationObject1.toString());
		
		logger.log(Level.SEVERE, logPrefix+"onActionReceived op["+op+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od1["+od1+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od2["+od2+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od3["+od3+"]");
		
		if ( null != op ) {
			
			WidgetStatus statusSet		= null;
			WidgetStatus statusUnSet	= null;
			WidgetStatus statusApply	= null;
			
			// Filter Action
			if ( op.equals(ViewerViewEvent.FilterAdded) || op.equals(ViewerViewEvent.FilterRemoved) ) {
				
				statusSet				= WidgetStatus.Disable;
				statusUnSet				= WidgetStatus.Disable;
				statusApply				= WidgetStatus.Disable;
				
			} else if ( op.equals(ViewerViewEvent.RowSelected.toString() ) ) {
				// Activate Selection
				
				selectedSet	= (Set<HashMap<String, String>>) obj1;
				
				String selectedStatus1 = null;
				for ( HashMap<String, String> hashMap : selectedSet ) {
//					String selectedAlias = hashMap.get(column_alias);
//					String selectedStatus = hashMap.get(column_status);
//					String selectedServiceOwner = hashMap.get(column_serviceOwner);
					
					selectedStatus1 = hashMap.get(column_status);;
				}

				if ( null != selectedStatus1 ) {
					if ( "0".equals(selectedStatus1) ) {
						statusSet				= WidgetStatus.Up;
						statusUnSet				= WidgetStatus.Disable;
					} else {
						statusSet				= WidgetStatus.Disable;
						statusUnSet				= WidgetStatus.Up;
					}
				}
				
				statusApply				= WidgetStatus.Disable;
			} else {
				logger.log(Level.SEVERE, logPrefix+"onActionReceived ViewerViewEvent type IS UNKNOW");
			}
			
			if ( null != widgetSet && null != statusSet )		uiWidgetGeneric.setWidgetStatus(widgetSet, statusSet);
			if ( null != widgetUnSet && null != statusUnSet )	uiWidgetGeneric.setWidgetStatus(widgetUnSet, statusUnSet);
			if ( null != widgetApply && null != statusApply )	uiWidgetGeneric.setWidgetStatus(widgetApply, statusApply);
		}
	}
	
	@Override
	public void init() {
		
		logger.log(Level.FINE, logPrefix+"init Begin");
		
		ctlMgr = CtlMgr.getInstance("ptw");
		
		eventBus = (SimpleEventBus) parameters.get(ParameterName.SimpleEventBus.toString());

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setXMLFile(ViewWidget.UIPanelPTWAction.toString());
		uiWidgetGeneric.init();
		
		widgetSet			= uiWidgetGeneric.getWidget( strSet );
		widgetUnSet			= uiWidgetGeneric.getWidget( strUnSet );
		widgetApply			= uiWidgetGeneric.getWidget( strApply );
		
		if ( null == widgetSet ) 	{ logger.log(Level.SEVERE, logPrefix+"init widgetSet IS NULL"); }
		if ( null == widgetUnSet )	{ logger.log(Level.SEVERE, logPrefix+"init widgetUnSet IS NULL"); }
		if ( null == widgetApply )	{ logger.log(Level.SEVERE, logPrefix+"init widgetApply IS NULL"); }
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				onButton(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);
		
		uiWidgetGeneric.setWidgetStatus( widgetSet, WidgetStatus.Disable );
		uiWidgetGeneric.setWidgetStatus( widgetUnSet, WidgetStatus.Disable );
		uiWidgetGeneric.setWidgetStatus( widgetApply, WidgetStatus.Disable );
		
		logger.log(Level.FINE, logPrefix + "init End");
	}

}
