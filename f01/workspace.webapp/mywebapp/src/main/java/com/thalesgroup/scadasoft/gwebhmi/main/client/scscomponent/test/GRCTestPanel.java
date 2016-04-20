package com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.ProgressBar;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.IGRCComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.ctl.ScsGRCComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class GRCTestPanel extends Composite
        implements IGRCComponentClient, IClientLifeCycle, RequiresResize, IRTDBComponentClient {

    private static GRCTestPanelUiBinder uiBinder = GWT.create(GRCTestPanelUiBinder.class);

    private ScsRTDBComponentAccess m_RTDBAccess = null;
    private ScsGRCComponentAccess m_GRCAccess = null;

    @UiTemplate("GRCTestPanel.ui.xml")
    interface GRCTestPanelUiBinder extends UiBinder<Widget, GRCTestPanel> {
    }

    private ProgressBar progressBar;
    private static int numberOfSteps;

    public GRCTestPanel() {

        initWidget(uiBinder.createAndBindUi(this));

        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        m_GRCAccess = new ScsGRCComponentAccess(this);

        progressBar = new ProgressBar();
        progressBar.addStyleName("{style.mwt-ProgressBar-bar}");

        progressBar.getElement().getStyle().setBorderColor("#000000");
        progressBar.getElement().getStyle().setBorderWidth(1, Unit.PX);
        progressBar.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);

        clearHMI();
        init();
    }

    private void init() {

        if (m_GRCAccess != null) {
            messageLabel.setText("Try to init.");
            envNameTB.setText("B001");

            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    m_GRCAccess.getGrcList("grcList", envNameTB.getText());
                }
            });
        } else {
            messageLabel.setText("DB access object and GRC access object are not defined");
        }

        /*
         * Create progressBar widget
         */
        progressBar.setSize("230px", "35px");
        progressBar.setMinProgress(0);
        progressBarHP.add(progressBar);
    }

    @SuppressWarnings("deprecation")
	private void clearHMI() {

        grcLB.clear();
        grcLB.setEnabled(true);
        envNameTB.setText("B001");

        nameTB.setText("No selection");
        labelTB.setText("No selection");
        lastExecTimeTB.setText("No selection");
        inhibFlagTB.setText("No selection");
        initCondTB.setText("No selection");
        currentStatusTB.setText("");

        autoCB.setChecked(true);

        progressBar.setProgress(0);

        toggleButton.setDown(false);

        firstStepTB.setText("No selection");
        firstStepCB.setChecked(false);
        onlyStepTB.setText("No selection");
        onlyStepCB.setChecked(false);
        skipStepTB.setText("No selection");
        skipStepCB.setChecked(false);

        firstStepCB.setEnabled(true);
        skipStepCB.setEnabled(true);

        GRCTB.setText("");
        envTB.setText("");
        stateTB.setText("");
        stepTB.setText("");
        stepStatusTB.setText("");

        grcLabel.setText("Selected GRC : no GRC selected");

    }

    @UiField
    TextBox envNameTB;
    @UiField
    ListBox grcLB;

    @UiField
    InlineLabel grcLabel;
    @UiField
    TextBox nameTB;
    @UiField
    TextBox labelTB;
    @UiField
    TextBox lastExecTimeTB;
    @UiField
    TextBox inhibFlagTB;
    @UiField
    TextBox initCondTB;
    @UiField
    TextBox stateTB;

    @UiField
    CheckBox autoCB;
    @UiField
    CheckBox stopOnFailedCB;
    @UiField
    CheckBox stopOnFirstBrcCB;

    @UiField
    HorizontalPanel progressBarHP;

    @UiField
    Button launchButton;
    @UiField
    Button prepareButton;
    @UiField
    ToggleButton toggleButton;
    @UiField
    Button abortPreparationButton;
    @UiField
    Button abortButton;

    @UiField
    TextBox firstStepTB;
    @UiField
    CheckBox firstStepCB;
    @UiField
    TextBox onlyStepTB;
    @UiField
    CheckBox onlyStepCB;
    @UiField
    TextBox skipStepTB;
    @UiField
    CheckBox skipStepCB;

    @UiField
    TextBox GRCTB;
    @UiField
    TextBox envTB;
    @UiField
    TextBox currentStatusTB;
    @UiField
    TextBox stepTB;
    @UiField
    TextBox stepStatusTB;

    @UiField
    InlineLabel messageLabel;
    @UiField
    Button refreshButton;

    public GRCTestPanel(String firstName) {

        initWidget(uiBinder.createAndBindUi(this));

        progressBar = new ProgressBar();

        progressBar.getElement().getStyle().setBorderColor("#000000");
        progressBar.getElement().getStyle().setBorderWidth(1, Unit.PX);
        progressBar.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);

        // progBarButtons.add(toggleButton);
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        m_GRCAccess = new ScsGRCComponentAccess(this);
        clearHMI();
        init();
    }

    @Override
    public void setPresenter(HypervisorPresenterClientAbstract<?> presenter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        m_GRCAccess = null;
        m_RTDBAccess = null;
    }

    @Override
    public void onResize() {
        // TODO Auto-generated method stub
        getWidget().setHeight(this.getParent().getOffsetHeight() + "px");
        getWidget().setWidth(this.getParent().getOffsetWidth() + "px");
    }

    @Override
    public void terminate() {
        // TODO Auto-generated method stub
        try {
            m_GRCAccess.terminate();
            m_RTDBAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
        clearHMI();
    }

    @UiHandler("grcLB")
    void ongrcLBChange(ChangeEvent event) {

        int index = grcLB.getSelectedIndex();
        if (index >= 0) {
            String grcAddress = grcLB.getItemText(index);
            /*
             * At each change, read the following fields
             */
            grcLabel.setText("Selected GRC : " + grcAddress);

            String nameGrcAddress = "<alias>" + grcAddress + ".name";
            m_RTDBAccess.readValueRequest("name", envNameTB.getText(), nameGrcAddress);

            String labelGrcAddress = "<alias>" + grcAddress + ".label";
            m_RTDBAccess.readValueRequest("label", envNameTB.getText(), labelGrcAddress);

            String lexectimeGrcAddress = "<alias>" + grcAddress + ".lexectime";
            m_RTDBAccess.readValueRequest("lexectime", envNameTB.getText(), lexectimeGrcAddress);

            String inhibflagGrcAddress = "<alias>" + grcAddress + ".inhibflag";
            m_RTDBAccess.readValueRequest("inhibflag", envNameTB.getText(), inhibflagGrcAddress);

            String initcondGrcAddress = "<alias>" + grcAddress + ".initcond";
            m_RTDBAccess.readValueRequest("initcond", envNameTB.getText(), initcondGrcAddress);

            String curstatusGrcAddress = "<alias>" + grcAddress + ".curstatus";
            m_RTDBAccess.readValueRequest("curstatus", envNameTB.getText(), curstatusGrcAddress);

            String curstepGrcAddress = "<alias>" + grcAddress + ".curstep";
            m_RTDBAccess.readValueRequest("curstep", envNameTB.getText(), curstepGrcAddress);

            m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

            /*
             * Read the number of commands
             */
            String brcTableGrcAddress = "<alias>" + grcAddress + ".brctable(0:$, label)";
            m_RTDBAccess.readValueRequest("brctable", envNameTB.getText(), brcTableGrcAddress);

            currentStatusTB.setText("");
            GRCTB.setText("");
            envTB.setText("");
            stateTB.setText("");
            stepTB.setText("");
            stepStatusTB.setText("");
        }
    }

    @UiHandler("refreshButton")
    void onrefreshButtonClick(ClickEvent e) {
        clearHMI();
        init();
    }

    @UiHandler("toggleButton")
    void ontoggleButtonClick(ClickEvent e) {
        int index = grcLB.getSelectedIndex();
        if (index >= 0) {
            String grcAddress = grcLB.getItemText(index);
            boolean resultValue = toggleButton.getValue();
            if (resultValue) {
                m_GRCAccess.resumeGrc("resume", envNameTB.getText(), "<alias>" + grcAddress);
            } else {
                m_GRCAccess.suspendGrc("suspend", envNameTB.getText(), "<alias>" + grcAddress);
            }
        } else {
            Window.alert("Please select an instance");
        }
    }

    @UiHandler("stateTB")
    void onstateTBValueChange(KeyUpEvent key) {

    }

    @UiHandler("abortButton")
    void onabortButtonClickEvent(ClickEvent event) {
        int index = grcLB.getSelectedIndex();
        if (index >= 0) {
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.abortGrc("AbortGrc", envNameTB.getText(), "<alias>" + grcAddress);
        }
    }

    @UiHandler("prepareButton")
    void onprepareButtonClickEvent(ClickEvent event) {
        int index = grcLB.getSelectedIndex();
        if (index >= 0) {
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.prepareGrc("prepareGrc", envNameTB.getText(), "<alias>" + grcAddress);
        }
    }

    @UiHandler("abortPreparationButton")
    void onabortPrepareButtonClickEvent(ClickEvent event) {
        int index = grcLB.getSelectedIndex();
        if (index >= 0) {
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.abortGrcPreparation("abortGrcPrep", envNameTB.getText(), "<alias>" + grcAddress);
        }
    }

    @SuppressWarnings("deprecation")
	@UiHandler("onlyStepCB")
    void ononlyStepCBClickEvent(ClickEvent event) {
        if (onlyStepCB.isChecked()) {
            firstStepCB.setChecked(false);
            firstStepCB.setEnabled(false);
            skipStepCB.setChecked(false);
            skipStepCB.setEnabled(false);
        } else {
            firstStepCB.setEnabled(true);
            skipStepCB.setEnabled(true);
        }
    }

    @SuppressWarnings("deprecation")
	@UiHandler("launchButton")
    void onlaunchButtonClickEvent(ClickEvent event) {

        stepStatusTB.setText("");
        
        int firstStep = 1, grcStepsToSkip = 0; // default value

        int index = grcLB.getSelectedIndex();

        if (index >= 0) {

            String grcAddress = grcLB.getItemText(index);

            short grcExecMode = 1; // Auto (default mode)
            if (autoCB.isChecked()) {
                grcExecMode = 1;
            } else if (stopOnFailedCB.isChecked()) {
                grcExecMode = 2;
            } else {
                grcExecMode = 3;
            }

            if (firstStepCB.isChecked()) {
                try {
                    firstStep = Integer.parseInt(firstStepTB.getText());
                } catch (Throwable e) {
                    Window.alert("Error : " + e.getMessage());
                }
            }
            if (onlyStepCB.isChecked()) {
                try {
                    firstStep = Integer.parseInt(onlyStepTB.getText());
                    grcStepsToSkip = numberOfSteps - firstStep;
                } catch (Throwable e) {
                    Window.alert("Error : " + e.getMessage());
                }
            }
            if (skipStepCB.isChecked()) {
                try {
                    grcStepsToSkip = Integer.parseInt(skipStepTB.getText());
                } catch (Throwable e) {
                    Window.alert("Error : " + e.getMessage());
                }
            }

            /*
             * setLaunchGrcResult will be called each time the state changes. So
             * we can update the progress bar from it (setLaunchGrcResult
             * method).
             */
            if ((grcStepsToSkip + firstStep) > numberOfSteps) {
                Window.alert("Error, there are " + numberOfSteps + " commands. Please change your options.");
            } else {
                progressBar.setMaxProgress(numberOfSteps - grcStepsToSkip);
                m_GRCAccess.launchGrc("LaunchGrc", envNameTB.getText(), "<alias>" + grcAddress, grcExecMode, firstStep,
                        grcStepsToSkip);
            }
        } else {
            Window.alert("Please select an instance");
        }
    }

    @Override
    public void setGetGrcListResult(String clientKey, String[] grcList, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        if (clientKey.equals("grcList")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }
            if (grcList != null && grcList.length > 0) {
                java.util.Arrays.sort(grcList);
                for (String e : grcList) {
                    grcLB.addItem(e);
                }
            }
        }
    }

    @Override
    public void setGetGrcStateResult(String clientKey, int state, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (clientKey.equals("grcState")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }
            switch (state) {
            case 1:
                stateTB.setText("READY");
                break;
            case 2:
                stateTB.setText("RUNNING");
                break;
            case 3:
                stateTB.setText("INHIBITED");
                break;
            case 4:
                stateTB.setText("WAIT FOR INIT CONDITION");
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void setPrepareGrcResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAbortGrcPreparationResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLaunchGrcResult(String clientKey, String name, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        GRCTB.setText(name);
        int index = grcLB.getSelectedIndex();
        String grcAddress = grcLB.getItemText(index);
        m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

        /*
         * Launch response
         */
        if (clientKey.equals("LaunchGrc")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }
        }
    }

    @Override
    public void setGrcStatusResult(String clientKey, int grcStatus, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        int index = grcLB.getSelectedIndex();
        String grcAddress = grcLB.getItemText(index);
        m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

        /*
         * Launch response
         */
        if (clientKey.equals("LaunchGrc")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }

            /*
             * Recover the status
             */
            currentStatusTB.setText("" + grcStatus);
            switch (grcStatus) {
            case 1:
                currentStatusTB.setText("TERMINATED");
                toggleButton.setDown(false);
                break;
            case 2:
                currentStatusTB.setText("WAIT FOR RUN");
                toggleButton.setDown(true);
                break;
            case 3:
                currentStatusTB.setText("INITIALIZING");
                toggleButton.setDown(true);
                break;
            case 4:
                currentStatusTB.setText("RUNNING");
                toggleButton.setDown(true);
                break;
            case 5:
                currentStatusTB.setText("WAITING");
                toggleButton.setDown(true);
                break;
            case 6:
                currentStatusTB.setText("STOPPED");
                toggleButton.setDown(false);
                break;
            case 7:
                currentStatusTB.setText("ABORTED");
                toggleButton.setDown(false);
                break;
            case 8:
                currentStatusTB.setText("SUSPENDED");
                toggleButton.setDown(false);
                break;
            case 9:
                currentStatusTB.setText("RESUMING");
                toggleButton.setDown(true);
                break;
            default:
                toggleButton.setDown(true);
                break;
            }
        }
    }

    @Override
    public void setStepStatusResult(String clientKey, int stepStatus, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        int index = grcLB.getSelectedIndex();
        String grcAddress = grcLB.getItemText(index);
        m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

        /*
         * Launch response
         */
        if (clientKey.equals("LaunchGrc")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }
            switch (stepStatus) {
            case 1:
                stepStatusTB.setText("NOT EXECUTED");
                break;
            case 2:
                stepStatusTB.setText("COMPLETED");
                break;
            case 3:
                stepStatusTB.setText("FAILED");
                break;
            case 4:
                stepStatusTB.setText("SKIPPED");
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void setStepResult(String clientKey, int step, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        
        int index = grcLB.getSelectedIndex();
        String grcAddress = grcLB.getItemText(index);
        m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

        /*
         * Launch response
         */
        if (clientKey.equals("LaunchGrc")) {
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            }

            /*
             * Recover the current step
             */
            stepTB.setText("" + step);
            progressBar.setProgress(step);
        }

    }

    @Override
    public void setAbortGrcResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        if (errorCode != 0) {

        } else {
            int index = grcLB.getSelectedIndex();
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);

            m_RTDBAccess.readValueRequest("curstatus", envNameTB.getText(), "<alias>" + grcAddress + ".curstatus");
        }
    }

    @Override
    public void setSuspendGrcResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        if (errorCode != 0) {

        } else {
            int index = grcLB.getSelectedIndex();
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);
        }
    }

    @Override
    public void setResumeGrcResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * Recover the state
         */
        if (errorCode != 0) {

        } else {
            int index = grcLB.getSelectedIndex();
            String grcAddress = grcLB.getItemText(index);
            m_GRCAccess.getGrcState("grcState", envNameTB.getText(), "<alias>" + grcAddress);
        }
    }

    @Override
    public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        if (key.equals("brctable")) {
            if (value != null && value.length > 0) {
                for (String s : value) {
                    int index = 0;
                    String test = s.replaceAll("\"\",", "");
                    numberOfSteps = -1;
                    do {
                        index = test.indexOf(",");
                        test = test.substring(index + 1);
                        numberOfSteps++;
                    } while (index != -1);
                    progressBar.setMaxProgress(numberOfSteps);
                }
            }
        }

        if (key.equals("name")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    nameTB.setText(e);
                }
            }
        }

        if (key.equals("label")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    labelTB.setText(e);
                }
            }
        }

        if (key.equals("lexectime")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    lastExecTimeTB.setText(e);
                }
            }
        }

        if (key.equals("inhibflag")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    inhibFlagTB.setText(e);
                }
            }
        }

        if (key.equals("initcond")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    initCondTB.setText(e);
                }
            }
        }

        if (key.equals("curstatus")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    currentStatusTB.setText(e);
                    switch (Integer.parseInt(e)) {
                    case 1:
                        currentStatusTB.setText("TERMINATED");
                        toggleButton.setDown(false);
                        break;
                    case 2:
                        currentStatusTB.setText("WAIT FOR RUN");
                        toggleButton.setDown(true);
                        break;
                    case 3:
                        currentStatusTB.setText("INITIALIZING");
                        toggleButton.setDown(true);
                        break;
                    case 4:
                        currentStatusTB.setText("RUNNING");
                        toggleButton.setDown(true);
                        break;
                    case 5:
                        currentStatusTB.setText("WAITING");
                        toggleButton.setDown(true);
                        break;
                    case 6:
                        currentStatusTB.setText("STOPPED");
                        toggleButton.setDown(false);
                        break;
                    case 7:
                        currentStatusTB.setText("ABORTED");
                        toggleButton.setDown(false);
                        break;
                    case 8:
                        currentStatusTB.setText("SUSPENDED");
                        toggleButton.setDown(false);
                        break;
                    case 9:
                        currentStatusTB.setText("RESUMING");
                        toggleButton.setDown(true);
                        break;
                    default:
                        toggleButton.setDown(true);
                        break;
                    }
                }
            }
        }

        if (key.equals("curstep")) {
            if (value != null && value.length > 0) {
                for (String e : value) {
                    stepTB.setText(e);
                    progressBar.setProgress(Integer.parseInt(e));
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

    }

    @Override
    public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

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
