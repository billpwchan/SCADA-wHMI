package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EntitySelectionInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.EqptSelectionChangeEvent.EqptSelectionEventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIPanelInspector;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIPanelInspectorEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperInfoCmdTabPanel;

public class UIPanelInspectorWrapper implements UIInspector_i, EqptSelectionEventHandler {
	
	private final String cls = this.getClass().getName();
	private final String className = UIWidgetUtil.getClassSimpleName(cls);
	private UILogger logger = UILoggerFactory.getInstance().getLogger(UIWidgetUtil.getClassName(cls));
	
	private UIPanelInspector uiPanelInspector_ = null;
	private WrapperInfoCmdTabPanel infoCmdTabPanel_ = null;
	private UINameCard uiNameCard_ = null;
	private String hvid_ = null;
	private String hvType_ = null;
	private UIPanelInspectorEvent uiPanelInspectorEvent_ = null;
	private boolean enableInfoPanel_ = true;
	private boolean enableCmdPanel_ = true;
	private EventBus eventBus_ = new MwtEventBus();
	
	private IHv2Scs hv2scs_ = null;
	private Panel containerPanel_ = null;
	
	private static final String DICTIONARY_CACHE_NAME = UIInspector_i.strUIInspector;
	private static final String DICTIONARY_FILE_NAME = "inspectorpanelwrapper.properties";
	private static final String NON_GENERIC_DATAMODEL_PREFIX = "inspectorpanelwrapper.nonGenericDataModelNamespace.";
	private static final String INFO_CMD_TAB_OPTION = "inspectorpanelwrapper.infocmdtab.";
	protected Set<String> nonGenericDataModelNamespaces = new HashSet<String>();
	private final List<HandlerRegistration> listHandlerRegistrations_ = new ArrayList<HandlerRegistration>();

	private String function = null;
	private String location = null;
	public void setFunctionLocation(String function, String location) {
		logger.debug("UIPanelInspectorWrapper setFunctionLocation function[" + function + "]  location[" + location +"]");
		this.function = function;
		this.location = location;
	}
	
	public void setHvInfo(String hvid, String hvType) {
		logger.debug("UIPanelInspectorWrapper setHvInfo hvid=" + hvid + "  hvType=" + hvType);
		hvid_ = hvid;
		hvType_ = hvType;

		readNonGenericDataModelNameSpaces();
		
		readInfoCmdTabOption();
	}
	
	protected void readNonGenericDataModelNameSpaces() {
		logger.debug("UIPanelInspectorWrapper readNonGenericDataModelNameSpaces");
		
		// Read number of non-generic data model namespace
		String valueKey = NON_GENERIC_DATAMODEL_PREFIX + "size";
		
		int nameCount = ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, 0);
		logger.debug("UIPanelInspectorWrapper " + valueKey + "=" + Integer.toString(nameCount) );
		if (nameCount > 0) {
			for (int i=0; i<nameCount; i++) {
				valueKey = NON_GENERIC_DATAMODEL_PREFIX + Integer.toString(i);
				
				// Read non-generic data model namespaces
				String dataModelNamespace = ReadProp.readString(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, "");
				logger.trace("UIPanelInspectorWrapper " + valueKey + "=" + dataModelNamespace );
				if (!dataModelNamespace.isEmpty()) {
					if (!nonGenericDataModelNamespaces.contains(dataModelNamespace)) {
						nonGenericDataModelNamespaces.add(dataModelNamespace);
					}
				}
			}
		}
	}
	
	protected void readInfoCmdTabOption() {
		logger.debug("readNonGenericDataModelNameSpaces");
		
		String valueKey = INFO_CMD_TAB_OPTION + "enableInfoPanel";
		enableInfoPanel_ = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, true);
		logger.trace("UIPanelInspectorWrapper " + valueKey + "=" + Boolean.toString(enableInfoPanel_) );
		
		valueKey = INFO_CMD_TAB_OPTION + "enableCmdPanel";
		enableCmdPanel_ = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, true);
		logger.trace("UIPanelInspectorWrapper " + valueKey + "=" + Boolean.toString(enableCmdPanel_) );
	}

	@Override
	public void init() {
		logger.debug("UIPanelInspectorWrapper init");
		if (!isGenericDataModel(hvType_)) {
			logger.debug("create WrapperInfoCmdTabPanel");
			infoCmdTabPanel_ = new WrapperInfoCmdTabPanel(eventBus_);
			if (uiPanelInspectorEvent_ != null) {
				uiPanelInspectorEvent_.setTitle(hvid_);
				infoCmdTabPanel_.setCloseEvent(new WrapperInfoCmdTabPanel.ICloseEvent() {				
					@Override
					public void onClose() {
						logger.debug("WrapperInfoCmdTabPanel onClose");
						uiPanelInspectorEvent_.onClose();
					}
				});
			}
			
			infoCmdTabPanel_.setEnableInfoPanel(enableInfoPanel_);
			infoCmdTabPanel_.setEnableCmdPanel(enableCmdPanel_);
			
		} else {
			logger.debug("create UIPanelInspector");
			uiPanelInspector_ = new UIPanelInspector();
			if (uiPanelInspector_ != null) {
				if (uiNameCard_ == null) {
					EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
					ResettableEventBus RESETABLE_EVENT_BUS = new ResettableEventBus(EVENT_BUS);	
					
					uiNameCard_ = new UINameCard(0, "", RESETABLE_EVENT_BUS);
				}
				uiPanelInspector_.setUINameCard(uiNameCard_);
				uiPanelInspector_.setUIInspectorEvent(uiPanelInspectorEvent_);
				uiPanelInspector_.setFunction(this.function);
				uiPanelInspector_.setLocation(this.location);
				uiPanelInspector_.init();
			}
		}
	}
	
	private boolean isGenericDataModel(String hvType) {
		for (String namespace: nonGenericDataModelNamespaces) {
			if (hvType.startsWith(namespace)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setParent(String scsEnvId, String parent) {
		logger.debug("UIPanelInspectorWrapper setParent");
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.setParent(scsEnvId, parent);
		}
	}

	@Override
	public Panel getMainPanel() {
		Panel panel = null;
		if (uiPanelInspector_ != null) {
			panel = uiPanelInspector_.getMainPanel();
		} else if (infoCmdTabPanel_ != null) {
			panel = infoCmdTabPanel_.getMainPanel();
		}
		return panel;
	}

	@Override
	public void connect() {
		if (uiPanelInspector_ != null) {
			logger.debug("connect UIPanelInspector");
			uiPanelInspector_.connect();
		} else if (infoCmdTabPanel_ != null) {
			logger.debug("connect WrapperInfoCmdTabPanel");
			infoCmdTabPanel_.setSelection(hvid_, hvType_);
		}
	}

	@Override
	public void disconnect() {
		if (uiPanelInspector_ != null) {
			logger.debug("disconnect UIPanelInspector");
			uiPanelInspector_.disconnect();
		} else if (infoCmdTabPanel_ != null) {
			logger.debug("disconnect WrapperInfoCmdTabPanel");
			infoCmdTabPanel_.setSelection(null, null);
		}
	}

	@Override
	public void close() {
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.close();
		}
	}

	@Override
	public void setElement(String element) {
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.setElement(element);
		}
	}
	

	@Override
	public String getElementName() { return uiPanelInspector_.getElementName(); }



	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		logger.debug("UIPanelInspectorWrapper setUINameCard");
		uiNameCard_ = new UINameCard(uiNameCard);
	}
	
	@Override
	public UINameCard getUINameCard() { return uiNameCard_; }

	@Override
	public void setCtrlHandler(String ctrlHandler) {
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.setCtrlHandler(ctrlHandler);
		}
	}

	@Override
	public void setViewXMLFile(String viewXMLFile) {
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.setViewXMLFile(viewXMLFile);
		}
	}
	
	@Override
	public String getViewXMLFile() { return uiPanelInspector_.getViewXMLFile(); }

	@Override
	public void setOptsXMLFile(String optsXMLFile) {
		if (uiPanelInspector_ != null) {
			uiPanelInspector_.setOptsXMLFile(optsXMLFile);
		}
	}
	
	@Override
	public String getOptsXMLFile() { return uiPanelInspector_.getOptsXMLFile(); }

	public void setUIInspectorEvent(UIPanelInspectorEvent uiPanelInspectorEvent) {
		uiPanelInspectorEvent_ = uiPanelInspectorEvent;
	}
	
	// Methods for interfacing with HV
	// 
	public void terminate() {
		for (final HandlerRegistration handlerRegistration : listHandlerRegistrations_) {
            handlerRegistration.removeHandler();
        }
        listHandlerRegistrations_.clear();
        
		if (infoCmdTabPanel_  != null) {
			infoCmdTabPanel_.terminate();
		}
	}
	
	public void setHvEventBus(EventBus eventBus) {
		logger.debug("UIPanelInspectorWrapper setHvEventBus");
		eventBus_ = eventBus;
		
		final HandlerRegistration busRegistration = eventBus_.addHandler(EqptSelectionChangeEvent.TYPE, this);
        listHandlerRegistrations_.add(busRegistration);
	}

	@Override
	public void onSelectionChange(EqptSelectionChangeEvent event) {
		logger.debug("UIPanelInspectorWrapper onSelectionChange");
		
		// Remove previous selection
		disconnect();
    	containerPanel_.clear();
		
		final Map<String, EntitySelectionInfo> entityIds = event.getSelectionInfo();
        if (entityIds != null && entityIds.size() > 0) {
        	String hvid = entityIds.keySet().iterator().next();
        	String hvType = entityIds.get(hvid).getEntityClassName();
        	
			if (hv2scs_ != null && hvid != null) {
				String dbaddress = hv2scs_.convert(hvid);
				setParent(hvid, dbaddress);
				setHvInfo(hvid, hvType);
			} else {
				logger.error("UIPanelInspectorWrapper onSelectionChange hv2scs=null or hvid=null");
				return;
			}
        	
			// Create and init inspectorpanel/infocmdtabpanel
        	init();

        	containerPanel_.add(getMainPanel());

        	connect();
        }
	}
	
	public interface IHv2Scs {
		String convert(String hvid);
	}
	
	public void setHv2Scs(IHv2Scs hv2scs) {
		logger.debug("UIPanelInspectorWrapper setHv2Scs");
		hv2scs_ = hv2scs;
	}
	
	public void setContainerPanel(Panel panel) {
		containerPanel_ = panel;
	}
}
