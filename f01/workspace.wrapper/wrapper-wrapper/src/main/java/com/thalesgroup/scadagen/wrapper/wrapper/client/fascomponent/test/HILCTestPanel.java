package com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.client.ConfigProvider;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.view.HypervisorView;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc.FasHILCComponentAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.fascomponent.hilc.IHILCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

public class HILCTestPanel extends Composite
	implements IHILCComponentClient, IWidgetController, RequiresResize, IRTDBComponentClient {
	
	private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
	    
	private static final String LOG_PREFIX = "[HILCTestPanel] ";

	private static HILCTestPanelUiBinder uiBinder = GWT.create(HILCTestPanelUiBinder.class);

	private FasHILCComponentAccess m_HILCAccess = null;
	
	private String scsEnvId = null;
	
	private String operatorName = null;
	
	private String workstationName = null;
	
	private int cmdType = 0;
	
	private int cmdValue = 0;
	
	private int cmdValueDiv = 0;
	
	private String eqpAlias = null;
	
	private String eqpType = null;
	
	private String cmdName = null;


	public HILCTestPanel() {
	        LOGGER.debug(LOG_PREFIX + "HILCTestPanel(): Constructor ");
	        initWidget(uiBinder.createAndBindUi(this));
	        m_HILCAccess = new FasHILCComponentAccess(this);

	        clearHMI();
	        init();
	}
	 
	/*
     * Bind the ui.xml file.
     */
    @UiTemplate("HILCTestPanel.ui.xml")
    interface HILCTestPanelUiBinder extends UiBinder<Widget, HILCTestPanel> {
    }
        
    @UiField
    TextBox envNameTB;
    @UiField
    TextBox workstationNameTB;
    @UiField
    TextBox cmdTypeTB;
    @UiField
    TextBox cmdValueTB;
    @UiField
    TextBox eqpAliasTB;
    @UiField
    TextBox eqpTypeTB;
    @UiField
    TextBox cmdNameTB;
    
    @UiField
    Button selectButton;
    @UiField
    Button executeButton;

    @UiField
    InlineLabel messageLabel;
    
	@Override
	public void setPresenter(HypervisorPresenterClientAbstract<? extends HypervisorView> presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		m_HILCAccess = null;
	}

	@Override
	public void terminate() {
		try {
			m_HILCAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
        clearHMI();
	}
	
	private void init() {
		operatorName = ConfigProvider.getInstance().getOperatorOpmInfo().getOperator().getId();
		workstationName = "TestWorkstation";

	}

	private void clearHMI() {
		messageLabel.setText("");
	}
	
	private void checkInput() {
    	// Check scsEnvId
		scsEnvId = envNameTB.getText();
    	if (scsEnvId == null || scsEnvId.isEmpty()) {
    		messageLabel.setText("scsEnvId is invalid");
    		return;
    	}
    	
    	// Check cmdType
    	String cType = cmdTypeTB.getText();
    	if (cType == null || cType.isEmpty()) {
    		messageLabel.setText("cmdType is invalid. 2 for DIO / 3 for AIO");
    		return;
    	} else if (cType.equals("2")) {
    		cmdType = 2;
    	} else if (cType.equals("3")) {
    		cmdType = 3;
    	} else {
    		messageLabel.setText("cmdType is invalid");
    		return;
    	}
    	
    	// Check cmdValue and set cmdValueDiv
    	String cValue = cmdValueTB.getText();
    	if (cValue == null || cValue.isEmpty()) {
    		messageLabel.setText("cmdValue is invalid.");
    		return;
    	} else if (cmdType == 2) {	// DIO
    		cmdValue = Integer.parseInt(cValue);
    		cmdValueDiv = 1;
    	} else if (cmdType == 3) {	// AIO
    		int len = cValue.length();
    		int idx = cValue.indexOf('.');
    		cmdValueDiv = 1;
    		if (idx >= 0) {
    			int power = (len - 1) - idx;
    			float fval = Float.parseFloat(cValue);
    			for (int i=0; i<power; i++) {
    				fval = fval * 10;
    				cmdValueDiv = cmdValueDiv * 10;
    			}
    			cmdValue = (int)fval;
    		} else {
    			cmdValue = Integer.parseInt(cValue);    			
    		}
    	} else {
    		messageLabel.setText("cmdValue is invalid");
    		return;
    	}
    	
    	// Check eqpAlias
    	eqpAlias = eqpAliasTB.getText();
    	if (eqpAlias == null || eqpAlias.isEmpty()) {
    		messageLabel.setText("eqpAlias is invalid.");
    		return;
    	}
    	
    	// Check eqpType
    	eqpType = eqpTypeTB.getText();
    	if (eqpType == null || eqpType.isEmpty()) {
    		messageLabel.setText("eqpType is invalid.");
    		return;
    	}
    	
    	// Check cmdName
    	cmdName = cmdNameTB.getText();
    	if (cmdName == null || cmdName.isEmpty()) {
    		messageLabel.setText("cmdName is invalid.");
    		return;
    	}

    	LOGGER.debug("scsEnvId = " + scsEnvId);
    	LOGGER.debug("operatorName = " + operatorName);
    	LOGGER.debug("workstationName = " + workstationName);
    	LOGGER.debug("cmdType = " + cmdType);
    	LOGGER.debug("cmdValue = " + cmdValue);
    	LOGGER.debug("cmdValueDiv = " + cmdValueDiv);
    	LOGGER.debug("eqpAlias = " + eqpAlias);
    	LOGGER.debug("eqpType = " + eqpType);
    	LOGGER.debug("cmdName = " + cmdName);
	}
	
	/**
     * <b>User send a HILC preparation request by clicking on the selOpenButton button.</b>
     * 
     * <p>
     * <ul>
     * <li></li>
     * </ul>
     * </p>
     * 
     * @param e
     *            ClickEvent
     */
    @UiHandler("selectButton")
    void onselectButtonClick(ClickEvent e) {
    	LOGGER.debug("onselectButtonClick");
    	
    	String key = "preparationRequest";

    	checkInput();
    	
    	messageLabel.setText("");
    	
        m_HILCAccess.hilcPreparationRequest(key, scsEnvId, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
    }
    
    /**
     * <b>User send a HILC confirm request by clicking on the exeOpenButton button.</b>
     * 
     * <p>
     * <ul>
     * <li></li>
     * </ul>
     * </p>
     * 
     * @param e
     *            ClickEvent
     */
    @UiHandler("executeButton")
    void onexecuteButtonClick(ClickEvent e) {
    	LOGGER.debug("onexecuteButtonClick");
    	
    	String key = "confirmRequest";

    	checkInput();
    	
    	messageLabel.setText("");

        m_HILCAccess.hilcConfirmRequest(key, scsEnvId, operatorName, workstationName, cmdType, cmdValue, cmdValueDiv, eqpAlias, eqpType, cmdName);
    }
	
	@Override
	public void onResize() {
		getWidget().setHeight(this.getParent().getOffsetHeight() + "px");
        getWidget().setWidth(this.getParent().getOffsetWidth() + "px");
	}

	@Override
	public Widget getLayoutView() {
		return this;
	}

	@Override
	public SafeUri getIconUri() {
		return null;
	}

	@Override
	public String getWidgetTitle() {
		return "HILCTest";
	}

	@Override
	public void setHILCPreparationResult(String key, int errorCode, String errorMessage) {
		if (errorCode != 0) {
			messageLabel.setText("HILCPreparation request failed.  " + errorMessage);
		} else {
			messageLabel.setText("HILCPreparation request sent successfully");
		}
	}

	@Override
	public void setHILCConfirmResult(String key, int errorCode, String errorMessage) {
		if (errorCode != 0) {
			messageLabel.setText("HILCConfirm request failed.  " + errorMessage);
		} else {
			messageLabel.setText("HILCConfirm request sent successfully");
		}
	}

	@Override
	public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {

	}

	@Override
	public void setWriteValueRequestResult(String key, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetClassInfoResult(String key, DbmClassInfo cinfo, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetInstancesByClassNameResult(String key, String[] instances, int errorCode,
			String errorMessage) {
		messageLabel.setText("Receive callback from GetInstancesByClassId method");

        if (key.equals("DioInstances")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }
            if (instances != null && instances.length > 0) {
                java.util.Arrays.sort(instances);

            }
        }
	}

	@Override
	public void setGetInstancesByUserClassIdResult(String key, String[] instances, int errorCode,
			String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetChildrenResult(String key, String[] instances, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetFullPathResult(String key, String fullPath, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetAliasResult(String key, String alias, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQueryByNameResult(String key, String[] instances, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetAttributesResult(String key, String[] attributes, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetAttributesDescriptionResult(String key, ScsClassAttInfo[] attributesInfo, int errorCode,
			String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetAttributeStructureResult(String key, String attType, int fieldCount, int recordCount,
			int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetUserFormulasNamesResult(String key, String[] formulas, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetDatabaseInfoResult(String key, long dbsize, int nbClasses, int nbFormula, int nbInstances,
			String scsPath, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setForceSnapshotResult(String key, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetAttributeFormulasResult(String key, String[] formulas, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSetAttributeFormulaResult(String key, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetCEOperResult(String key, int[] ceModes, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setControlCEResult(String key, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGetChildrenAliasesResult(String key, String[] values, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

}
