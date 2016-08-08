package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.logging.Level;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.PTW_i.PTW;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;

public class PTWAction extends UIWidget_i {
	
    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
	
	private EventBus eventBus = null;

	private CtlMgr ctlMgr = null;
	
	public PTWAction (EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	String strSet			= "set";
	String strUnSet			= "unset";
	String strApply			= "apply";
	String strFilterCancel	= "filterreset";
	String strFilterApply	= "filterapplied";
	
	private void onButton(ClickEvent event) {
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
				onButtonClick(element);
			} else {
				logger.log(Level.SEVERE, "onClickHandler onClickHandler button IS NULL");
			}
		}
	}
	
	private void onButtonClick(String element ) {
		if ( null != element ) {
			if ( element.equals(strSet) ) {
//				ctlMgr.sendControl(scsEnvId, new String[]{alias}, value, 1, 1, 1);
			} else if ( element.equals(strUnSet) ) {
				
			} else if ( element.equals(strApply) ) {

			} else if ( element.equals(strFilterCancel) ) {
//				UIEventAction event = new UIEventAction();
//				event.setAction(PTW.Operation.toString(), PTWOperation.Filter.toString());
//				event.setAction(PTW.OperationDetail1.toString(), PTWOperationDetail.NAN.toString());
//				this.eventBus.fireEventFromSource(event, this);
			} else if ( element.equals(strFilterApply) ) {
				
			}
		}
	}

	private String strUIPanelPTWAction = "UIPanelPTWAction.xml";
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	void onActionReceived(UIEventAction uiEventAction) {
		String op	= uiEventAction.getAction(PTW.Operation.toString());
		String od	= uiEventAction.getAction(PTW.OperationDetail1.toString());
		String od2	= uiEventAction.getAction(PTW.OperationDetail2.toString());
		
//		lblOperation.setText(PTW.Operation.toString() + " [" + op + "]");
//		lblOperationDetail.setText(PTW.OperationDetail1.toString() + " [" + od + "]");
//		lblOperationDetail.setText(PTW.OperationDetail2.toString() + " [" + od2 + "]");
//		
//		if ( null != od ) {
//			txtAliasDci.setText(od);
//			String aliasDio = od.replace("dci", "dio");
//			txtAliasDio.setText(aliasDio);
//			txtValue.setText(od2);
//		} else {
//			txtAliasDci.setText("");
//			txtAliasDio.setText("");
//			txtValue.setText("");
//		}
	}
	@Override
	public void init() {
		
		logger.log(Level.FINE, "init Begin");
		
		ctlMgr = ctlMgr.getInstance("ptw");

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setXMLFile(strUIPanelPTWAction);
		uiWidgetGeneric.init();
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				onButton(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});

		this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {

			@Override
			public void onAction(UIEventAction uiEventAction) {
				onActionReceived(uiEventAction);
			}
		});
		
		logger.log(Level.FINE, "init End");
	}

}
