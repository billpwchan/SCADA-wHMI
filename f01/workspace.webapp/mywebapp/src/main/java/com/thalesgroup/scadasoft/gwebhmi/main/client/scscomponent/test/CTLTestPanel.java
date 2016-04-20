package com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ICTLComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsCTLComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

/**
 * @author S005570
 *
 */

public class CTLTestPanel extends Composite
        implements ICTLComponentClient, IRTDBComponentClient, IClientLifeCycle, RequiresResize {

    private static CTLTestPanelUiBinder uiBinder = GWT.create(CTLTestPanelUiBinder.class);

    private ScsCTLComponentAccess m_CTLAccess = null;
    private ScsRTDBComponentAccess m_RTDBAccess = null;

    /*
     * Bind the ui.xml file.
     */
    @UiTemplate("CTLTestPanel.ui.xml")
    interface CTLTestPanelUiBinder extends UiBinder<Widget, CTLTestPanel> {
    }

    public CTLTestPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        m_CTLAccess = new ScsCTLComponentAccess(this);
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        clearHMI();
        init();
    }

    public CTLTestPanel(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        m_CTLAccess = new ScsCTLComponentAccess(this);
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        clearHMI();
        init();
    }

    public static final int DIGITAL_ID = 26;
    public static final int ANALOG_ID = 10;
    public static final int STRUCTURED_ID = 28;
    // public static final int DIGITAL_ID = 105;
    // public static final int ANALOG_ID = 108;
    // public static final int STRUCTURED_ID = 106;

    @UiField
    HorizontalPanel thp;
    @UiField
    TabLayoutPanel tabPanel;
    @UiField
    ListBox digitalListBox;
    @UiField
    ListBox analogListBox;
    @UiField
    ListBox structuredListBox;

    @UiField
    HorizontalPanel bhp;
    @UiField
    CaptionPanel instanceCP;
    @UiField
    TextBox envNameTB;
    @UiField
    TextBox veCoordTB;
    @UiField
    TextBox labelTB;
    @UiField
    TextBox statusTB;
    @UiField
    TextBox execStatusTB;
    @UiField
    ListBox commandValueLB;
    @UiField
    TextBox commandValueTB;

    @UiField
    CheckBox bypassInitCondCB;
    @UiField
    CheckBox bypassRetCondCB;
    @UiField
    CheckBox sendAnywayCB;

    @UiField
    Button sendButton;
    @UiField
    InlineLabel stateLabel;
    @UiField
    Button diversTestButton;
    @UiField
    Button refreshButton;

    @UiField
    InlineLabel messageLabel;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        m_CTLAccess = null;
        m_RTDBAccess = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResize() {
        getWidget().setHeight(this.getParent().getOffsetHeight() + "px");
        getWidget().setWidth(this.getParent().getOffsetWidth() + "px");
    }

    @Override
    public void terminate() {
        try {
            m_CTLAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
        clearHMI();
    }

    private void init() {
        if (m_RTDBAccess != null) {
            messageLabel.setText("Try to init.");
            envNameTB.setText("B001");

            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    m_RTDBAccess.getInstancesByClassId("DioInstances", envNameTB.getText(), DIGITAL_ID, "");
                    m_RTDBAccess.getInstancesByClassId("AioInstances", envNameTB.getText(), ANALOG_ID, "");
                    m_RTDBAccess.getInstancesByClassId("SioInstances", envNameTB.getText(), STRUCTURED_ID, "");

                    tabPanel.setAnimationDuration(400);

                    stateLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
                }
            });
        } else {
            messageLabel.setText("DB access object not defined.");
        }
    }

    @SuppressWarnings("deprecation")
    private void clearHMI() {

        digitalListBox.clear();
        analogListBox.clear();
        structuredListBox.clear();

        instanceCP.setCaptionHTML("Instance selected : no instance selected");

        envNameTB.setText("B001");
        veCoordTB.setText("No selection");
        labelTB.setText("No selection");
        statusTB.setText("No selection");
        execStatusTB.setText("No selection");

        commandValueLB.clear();
        commandValueTB.setText("");

        bypassInitCondCB.setChecked(false);
        bypassRetCondCB.setChecked(false);
        sendAnywayCB.setChecked(false);

        messageLabel.setText("HMI cleared.");

        DOM.setElementAttribute(refreshButton.getElement(), "id", "RefreshBId");
    }

    @SuppressWarnings("deprecation")
	@UiHandler("sendButton")
    void onsendButtonClick(ClickEvent e) {
        String commandValue = commandValueTB.getText();
        
        int bypassInitCond = 0, bypassRetCond = 0, sendAnyway = 0;

        if (commandValue != null && !(commandValue.equals(""))) {
            if (analogListBox.isVisible()) {
                String analogValueAddress[] = new String[1];
                analogValueAddress[0] = analogListBox.getItemText(analogListBox.getSelectedIndex());
                if (analogListBox.getSelectedIndex() != -1) {
                    float floatCommandValue = Float.parseFloat(commandValue);
                    
                    if (bypassInitCondCB.isChecked()){
                        bypassInitCond = 1;
                    }
                    if (bypassRetCondCB.isChecked()) {
                        bypassRetCond = 1;
                    } 
                    if (sendAnywayCB.isChecked()) {
                        sendAnyway = 1;
                    }
                    
                    m_CTLAccess.sendFloatCommand("sendFloatCommand", envNameTB.getText(), analogValueAddress,
                            floatCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
                }

            } else if (structuredListBox.isVisible()) {
                String structuredValueAddress[] = new String[1];
                structuredValueAddress[0] = structuredListBox.getItemText(structuredListBox.getSelectedIndex());
                if (structuredListBox.getSelectedIndex() != -1) {
                    
                    if (bypassInitCondCB.isChecked()){
                        bypassInitCond = 1;
                    }
                    if (bypassRetCondCB.isChecked()) {
                        bypassRetCond = 1;
                    } 
                    if (sendAnywayCB.isChecked()) {
                        sendAnyway = 1;
                    }
                    m_CTLAccess.sendStringCommand("sendStringCommand", envNameTB.getText(), structuredValueAddress,
                            commandValue, bypassInitCond, bypassRetCond, sendAnyway);
                }
            } else {
                String digitalValueAddress[] = new String[1];
                int intCommandValue = commandValueLB.getSelectedIndex() + 1;
                digitalValueAddress[0] = digitalListBox.getItemText(digitalListBox.getSelectedIndex());
                if (digitalListBox.getSelectedIndex() != -1) {
                    
                    if (bypassInitCondCB.isChecked()){
                        bypassInitCond = 1;
                    }
                    if (bypassRetCondCB.isChecked()) {
                        bypassRetCond = 1;
                    } 
                    if (sendAnywayCB.isChecked()) {
                        sendAnyway = 1;
                    }
                    m_CTLAccess.sendIntCommand("sendIntCommand", envNameTB.getText(), digitalValueAddress,
                            intCommandValue, bypassInitCond, bypassRetCond, sendAnyway);
                }
            }
        } else {
            Window.alert("Please select an instance and type a value");
        }

    }

    @UiHandler("diversTestButton")
    void ondiversTestButtonClick(ClickEvent e) {
    }

    @UiHandler("refreshButton")
    void onrefreshButtonClick(ClickEvent e) {
        clearHMI();
        init();
    }

    @UiHandler("tabPanel")
    void onTabSelection(SelectionEvent<Integer> event) {
        commandValueLB.clear();
        switch (event.getSelectedItem()) {
        case 0:
            ondigitalListBoxChange(event);
            break;
        case 1:
            onanalogListBoxChange(event);
            break;
        case 2:
            onstructureListBoxChange(event);
            break;
        default:
            break;
        }
    }

    private void onstructureListBoxChange(SelectionEvent<Integer> event) {
        // TODO Auto-generated method stub
        int index = structuredListBox.getSelectedIndex();
        if (index >= 0) {
            String structuredAddress = structuredListBox.getItemText(index);
            String labelAddress = structuredAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = structuredAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = structuredAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            String veCoordAddress = structuredAddress + ".veCoord";
            m_RTDBAccess.readValueRequest("StructVeCoord", envNameTB.getText(), veCoordAddress);

            instanceCP.setCaptionHTML("Instance selected : " + structuredAddress);
            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    private void onanalogListBoxChange(SelectionEvent<Integer> event) {
        // TODO Auto-generated method stub
        int index = analogListBox.getSelectedIndex();
        if (index >= 0) {
            String analogAddress = analogListBox.getItemText(index);

            String valueLimitsAddress = analogAddress + ".valueLimits";
            m_RTDBAccess.readValueRequest("valueLimits", envNameTB.getText(), valueLimitsAddress);

            String labelAddress = analogAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = analogAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = analogAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            String veCoordAddress = analogAddress + ".veCoord";
            m_RTDBAccess.readValueRequest("AnaVeCoord", envNameTB.getText(), veCoordAddress);

            instanceCP.setCaptionHTML("Instance selected : " + analogAddress);

            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    private void ondigitalListBoxChange(SelectionEvent<Integer> event) {
        // TODO Auto-generated method stub
        int index = digitalListBox.getSelectedIndex();
        if (index >= 0) {
            String digitalAddress = digitalListBox.getItemText(index);

            String valueTableAddress = digitalAddress + ".valueTable";
            m_RTDBAccess.readValueRequest("valueTable", envNameTB.getText(), valueTableAddress);

            String labelAddress = digitalAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = digitalAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = digitalAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            instanceCP.setCaptionHTML("Instance selected : " + digitalAddress);
            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    @UiHandler("digitalListBox")
    void ondigitalListBoxChange(ChangeEvent event) {

        int index = digitalListBox.getSelectedIndex();
        if (index >= 0) {
            String digitalAddress = digitalListBox.getItemText(index);

            String valueTableAddress = digitalAddress + ".valueTable";
            m_RTDBAccess.readValueRequest("valueTable", envNameTB.getText(), valueTableAddress);

            String labelAddress = digitalAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = digitalAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = digitalAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            instanceCP.setCaptionHTML("Instance selected : " + digitalAddress);
            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    @UiHandler("analogListBox")
    void onanalogListBoxChange(ChangeEvent event) {

        int index = analogListBox.getSelectedIndex();
        if (index >= 0) {
            String analogAddress = analogListBox.getItemText(index);

            String valueLimitsAddress = analogAddress + ".valueLimits";
            m_RTDBAccess.readValueRequest("valueLimits", envNameTB.getText(), valueLimitsAddress);

            String labelAddress = analogAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = analogAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = analogAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            String veCoordAddress = analogAddress + ".veCoord";
            m_RTDBAccess.readValueRequest("AnaVeCoord", envNameTB.getText(), veCoordAddress);

            instanceCP.setCaptionHTML("Instance selected : " + analogAddress);

            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    @UiHandler("structuredListBox")
    void onstructureListBoxChange(ChangeEvent event) {

        int index = structuredListBox.getSelectedIndex();
        if (index >= 0) {
            String structuredAddress = structuredListBox.getItemText(index);
            String labelAddress = structuredAddress + ".label";
            m_RTDBAccess.readValueRequest("labelValue", envNameTB.getText(), labelAddress);

            String statusAddress = structuredAddress + ".status";
            m_RTDBAccess.readValueRequest("statusValue", envNameTB.getText(), statusAddress);

            String execStatusAddress = structuredAddress + ".execStatus";
            m_RTDBAccess.readValueRequest("execStatusValue", envNameTB.getText(), execStatusAddress);

            String veCoordAddress = structuredAddress + ".veCoord";
            m_RTDBAccess.readValueRequest("StructVeCoord", envNameTB.getText(), veCoordAddress);

            instanceCP.setCaptionHTML("Instance selected : " + structuredAddress);
            commandValueLB.clear();
            commandValueTB.setText("");

            veCoordTB.setText("");
        } else {
            instanceCP.setCaptionHTML("Instance selected : no instance selected");

            envNameTB.setText("B001");
            veCoordTB.setText("No selection");
            labelTB.setText("No selection");
            statusTB.setText("No selection");
            execStatusTB.setText("No selection");
        }

        stateLabel.setText("RESULT");

        stateLabel.getElement().getStyle().clearColor();
    }

    @UiHandler("commandValueLB")
    void oncommandValueLBChange(ChangeEvent event) {

        int index = commandValueLB.getSelectedIndex();
        if (index >= 0) {
            String digitalAddress = digitalListBox.getItemText(digitalListBox.getSelectedIndex());
            m_RTDBAccess.getChildren(String.valueOf(index), envNameTB.getText(), digitalAddress);

            int intCommandValue = commandValueLB.getSelectedIndex() + 1;
            String commandValue = commandValueLB.getItemText(commandValueLB.getSelectedIndex()) + " (" + intCommandValue
                    + ")";
            commandValueTB.setText(commandValue);

            stateLabel.setText("RESULT");

            stateLabel.getElement().getStyle().clearColor();
        }
    }

    @Override
    public void setPresenter(HypervisorPresenterClientAbstract<?> presenter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSendIntCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (errorCode != 0) {
            stateLabel.setText("COMMAND FAILED");
            stateLabel.getElement().getStyle().setColor("#ff0000");
        } else {
            stateLabel.setText(message);
            stateLabel.getElement().getStyle().setColor("#006622");
        }

    }

    @Override
    public void setSendFloatCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (errorCode != 0) {
            stateLabel.setText("COMMAND FAILED");
            stateLabel.getElement().getStyle().setColor("#ff0000");
        } else {
            stateLabel.setText(message);
            stateLabel.getElement().getStyle().setColor("#006622");
        }
    }

    @Override
    public void setSendStringCommandResult(String clientKey, String message, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        if (errorCode != 0) {
            stateLabel.setText("COMMAND FAILED");
            stateLabel.getElement().getStyle().setColor("#ff0000");
        } else {
            stateLabel.setText(message);
            stateLabel.getElement().getStyle().setColor("#006622");
        }
    }

    @Override
    public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (key.equals("valueLimits")) {
            commandValueLB.clear();
            if (value != null && value.length > 0) {
                commandValueTB.setText("[MIN_VALUE, MAX_VALUE] : " + value[0]);
            }
        }

        if (key.equals("valueTable")) {
            commandValueLB.clear();
            if (value != null && value.length > 0) {
                int endLB = 0;
                for (String e : value) {

                    int start = e.indexOf("],[");
                    int endCommand;

                    do {
                        endCommand = e.indexOf(",", start + 3);
                        endLB = e.indexOf("\"\"", start);
                        String commandValue = e.substring(start + 4, endCommand - 1);
                        commandValueLB.addItem(commandValue);
                        start = endCommand - 2;
                    } while (endCommand < endLB);
                }
            }
        }

        if (key.equals("labelValue")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    labelTB.setText(e);
                }
            }
        }

        if (key.equals("statusValue")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    statusTB.setText(e);
                }
            }
        }

        if (key.equals("execStatusValue")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    execStatusTB.setText(e);
                }
            }
        }

        if (key.equals("veCoord")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    veCoordTB.setText(e);
                }
            }
        }

        if (key.equals("AnaVeCoord")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    veCoordTB.setText(e);
                }
            }
        }

        if (key.equals("StructVeCoord")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    veCoordTB.setText(e);
                }
            }
        }
    }

    @Override
    public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (key.equals("DioInstances")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }
            if (values != null && values.length > 0) {
                java.util.Arrays.sort(values);
                for (String e : values) {
                    digitalListBox.addItem(e);
                }
            }
        }

        if (key.equals("AioInstances")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }

            if (values != null && values.length > 0) {
                java.util.Arrays.sort(values);
                for (String e : values) {
                    analogListBox.addItem(e);
                }
            }
        }

        if (key.equals("SioInstances")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }

            if (values != null && values.length > 0) {
                java.util.Arrays.sort(values);
                for (String e : values) {
                    structuredListBox.addItem(e);
                }
            }
        }
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
    public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetInstancesByClassNameResult(String clientKey, String[] instances, int errorCode,
            String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetInstancesByUserClassIdResult(String clientKey, String[] instances, int errorCode,
            String errorMessage) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("");
        }

        int index = Integer.parseInt(clientKey);
        if (instances != null && instances.length > 0) {
            String childAddress = instances[index];
            String veCoord = childAddress + ".veCoord";
            m_RTDBAccess.readValueRequest("veCoord", envNameTB.getText(), veCoord);
        }
    }

    @Override
    public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText(fullPath);
        }
    }

    @Override
    public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode,
            String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount, int recordCount,
            int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula, int nbInstances,
            String scsPath, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        
    }

}
