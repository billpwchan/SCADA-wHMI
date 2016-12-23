package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EntitySelectionInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.InfoCommandTabPanel;

public class WrapperInfoCmdTabPanel {

	private InfoCommandTabPanel infoCmdPanel_ = null;
	private ICloseEvent closeEvent_ = null;
	private TextBox txtMsg_ = null;
	
	public interface ICloseEvent {
		void onClose();
	}

    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
    
    /** Wrapper Panel */
    private final FlowPanel rootPanel_ = new FlowPanel();
    
	public WrapperInfoCmdTabPanel(EventBus eventBus) {
		rootPanel_.addStyleName("project-gwt-panel-wrapperinfocmdtab");
		
		infoCmdPanel_ = new InfoCommandTabPanel(eventBus);
		infoCmdPanel_.addStyleName("project-gwt-panel-wrapperinfocmdtab-top");
		if (infoCmdPanel_ != null) {
			rootPanel_.add(infoCmdPanel_);
		}
		
		Button btnClose = new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				s_logger.debug("WrapperInfoCmdTabPanel onClick close");
				close();
			}
	    });
		
		txtMsg_ = new TextBox();
		if (txtMsg_ != null) {
			txtMsg_.setReadOnly(true);
			txtMsg_.addStyleName("project-gwt-textbox-inspector-bottom-message");
		}
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		if (bottomBar != null) {
			bottomBar.addStyleName("project-gwt-panel-wrapperinfocmdtab-bottom");
			bottomBar.add(txtMsg_);
			bottomBar.add(btnClose);
		}
		
		rootPanel_.add(bottomBar);
		
	}
	
	protected void close() {
		if (closeEvent_ != null) {
			s_logger.debug("WrapperInfoCmdTabPanel onClose");
			closeEvent_.onClose();
		}
	}

	public void setSelection(String hvid, String hvEqpType) {
		 s_logger.debug("WrapperInfoCmdTabPanel.setSelection " + hvid + " " + hvEqpType);
		
		final Map<String, EntitySelectionInfo> selectionInfoMap = new HashMap<String, EntitySelectionInfo>();

        final EntitySelectionInfo selecInfo = new EntitySelectionInfo(hvid,
        		hvEqpType);

        if (hvid != null && selecInfo != null) {
        	selectionInfoMap.put(hvid, selecInfo);
        }
       
        infoCmdPanel_.onSelectionChange(new EqptSelectionChangeEvent(
                selectionInfoMap));
	}
	
	public Panel getMainPanel() {
		return rootPanel_;
	}
	
	public void setCloseEvent(ICloseEvent closeEvent) {
		closeEvent_ = closeEvent;
	}
	
	public void setEnableInfoPanel(boolean enable) {
		infoCmdPanel_.setEnableInfoPanel(enable);
	}
	
	public void setEnableCmdPanel(boolean enable) {
		infoCmdPanel_.setEnableCmdPanel(enable);
	}

	public void terminate() {
		infoCmdPanel_.terminate();
	}

}
