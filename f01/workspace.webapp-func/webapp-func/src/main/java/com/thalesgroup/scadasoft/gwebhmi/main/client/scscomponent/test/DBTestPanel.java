/**
 * 
 */
package com.thalesgroup.scadasoft.gwebhmi.main.client.scscomponent.test;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.alm.ScsALMComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ChangeEvent;

/**
 * @author T0009042
 *
 */
public class DBTestPanel extends Composite implements IRTDBComponentClient, IClientLifeCycle, RequiresResize {

    private static DBTestPanelUiBinder uiBinder = GWT.create(DBTestPanelUiBinder.class);

    @UiField
    TabLayoutPanel tabPanel;

    @UiField
    Label nbClassLabel;
    @UiField
    Label nbInstLabel;
    @UiField
    Label nbAttLabel;
    @UiField
    ListBox classListBox;

    @UiField
    Label instanceNbLabel;
    @UiField
    Label classNameLabel;
    @UiField
    Label attNbLabel;
    @UiField
    ListBox instanceListBox;

    @UiField
    ListBox childrenAliasesListBox;

    @UiField
    TextBox classIdTB;
    @UiField
    TextBox userClassIdTB;
    @UiField
    TextBox nbInstTB;
    @UiField
    Button refreshB;

    @UiField
    TextBox classNameTB;
    @UiField
    TextBox aliasTB;
    @UiField
    TextBox fullPathTB;
    @UiField
    TextBox nbAttTB;
    @UiField
    TextBox instancesUserClassIdTB;

    @UiField
    TextBox sizeTB;
    @UiField
    TextBox nbClassesTB;
    @UiField
    TextBox nbFormulaTB;
    @UiField
    TextBox nbInstancesTB;
    @UiField
    TextBox scsPathTB;

    @UiField
    ListBox attributeListBox;
    @UiField
    Label attNb;
    @UiField
    Button readButton;

    @UiField
    Button addDateButton;
    @UiField
    ListBox hourValueLB;
    @UiField
    ListBox minuteValueLB;
    @UiField
    ListBox secondValueLB;
    @UiField
    TextBox msecondValueTB;
    @UiField
    DateBox dateBox;

    @UiField
    Button addValueButton;
    @UiField
    RadioButton stringValue;
    @UiField
    RadioButton intValue;
    @UiField
    RadioButton floatValue;
    @UiField
    TextBox typedValueTB;

    @UiField
    TextBox formulaTB;
    @UiField
    TextBox setFormulaTB;
    @UiField
    Button addButton;
    @UiField
    Button getCEButton;
    @UiField
    TextBox CEMode;

    @UiField
    Button upButton;
    @UiField
    Button downButton;
    @UiField
    ListBox exploratorListBox;

    @UiField
    TextBox queryTB;
    @UiField
    TextBox queryAddressTB;
    @UiField
    Button queryButton;
    @UiField
    ListBox queryListBox;

    @UiField
    TextBox setAddressTB;

    @UiField
    InlineLabel messageLabel;

    @UiField
    FlexTable flexTable;

    private ScsALMComponentAccess m_ALMAccess = null;
    private ScsRTDBComponentAccess m_RTDBAccess = null;

    interface DBTestPanelUiBinder extends UiBinder<Widget, DBTestPanel> {
    }

    public DBTestPanel() {

        initWidget(uiBinder.createAndBindUi(this));
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        clearHMI();
        init();
    }

    public DBTestPanel(String firstName) {

        initWidget(uiBinder.createAndBindUi(this));
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        clearHMI();
        init();
    }

    private void init() {

        if (m_RTDBAccess != null) {
            messageLabel.setText("Try to init.");
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    m_RTDBAccess.getClasses("classes", "B001");
                    m_RTDBAccess.getDatabaseInfo("dataInfo", "B001");
                    m_RTDBAccess.getChildren("children", "B001", ":");
                }
            });

            for (int i = 0; i <= 59; i++) {
                secondValueLB.addItem(Integer.toString(i));
            }
            for (int i = 0; i <= 59; i++) {
                minuteValueLB.addItem(Integer.toString(i));
            }
            for (int i = 0; i <= 23; i++) {
                hourValueLB.addItem(Integer.toString(i));
            }

        } else {
            messageLabel.setText("DB access object not defined.");
        }
    }

    private void clearHMI() {

        classListBox.clear();
        queryListBox.clear();
        attributeListBox.clear();
        exploratorListBox.clear();
        instanceListBox.clear();
        childrenAliasesListBox.clear();

        classIdTB.setText("");
        userClassIdTB.setText("");
        nbInstTB.setText("");
        classNameTB.setText("");
        aliasTB.setText("");
        fullPathTB.setText("");
        nbAttTB.setText("");
        instancesUserClassIdTB.setText("");
        msecondValueTB.setText("miliseconds...");
        formulaTB.setText("");
        typedValueTB.setText("");
        setAddressTB.setText("Enter an address (:SITE1:B001:F000:CCTV)");
        CEMode.setText("");

        nbInstLabel.setText("Number of instances in this class:");
        nbAttLabel.setText("Number of attributes of this class:");
        instanceNbLabel.setText("Number of instances: 0");
        classNameLabel.setText("Class name of this instance:");
        attNbLabel.setText("Number of attributes of this instance:");
        attNb.setText("Number of attributes:");
        messageLabel.setText("HMI cleared.");

        dateBox.setValue(null);

        clearFlexTable();

        DOM.setElementAttribute(refreshB.getElement(), "id", "RefreshBId");
        DOM.setElementAttribute(classListBox.getElement(), "id", "RefreshBId");
    }

    @Override
    public void destroy() {
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
    public void setReadResult(String key, String[] value, int errorCode, String errorMessage) {

        if (value != null && value.length > 0) {
            for (String e : value) {
                /*
                 * IF VALUE IS A TABLE --> BUILD THIS TABLE
                 * 
                 * ELSE ADD THE VALUE NEXT TO THE ATTRIBUTE IN THE LISTBOX
                 */
                int isTable = e.indexOf("[[");
                if (isTable != -1) {
                    buildTable(e);
                } else {
                    /*
                     * IF A VALUE IS ALREADY ADDED NEXT TO THE ATTRIBUTE -->
                     * REPLACE THIS VALUE
                     * 
                     * ELSE ADD THE VALUE NEXT TO THE ATTRIBUTE
                     */
                    String attribute;
                    int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
                    if (index >= 0) {
                        attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0,
                                index - 1);
                    } else {
                        attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
                    }
                    String currentValue = attribute + " > " + e;
                    attributeListBox.setItemText(attributeListBox.getSelectedIndex(), currentValue);
                }
            }
        }

        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("SetReadResult OK");
        }

    }

    private void buildTable(String table) {
        flexTable.setVisible(true);

        /*
         * PARSE THE STTRING INTO A TABLE.
         */
        int startTab = table.indexOf("[[");
        int startCol = table.indexOf("[", startTab + 1);
        int endCol = table.indexOf("]", startCol);
        int separatorPosition = table.indexOf(",", startCol);
        int previousSeparatorPosition = startCol + 1;

        int i = 0, j = 0;

        /*
         * WHILE A COLUMN IS STILL PRESENT
         */
        while (startCol != -1) {
            /*
             * WE START WITH i=1 AS THE FIRST ROW IS RESERVED FOR THE COLUMNS
             * NAME
             */
            i = 1;
            /*
             * WHILE WE ARE IN THE SAME COLUMN --> WE WRYTE VALUE IN EACH CASE
             * OF THE COLUMN
             */
            while (startCol <= endCol && separatorPosition <= endCol && separatorPosition >= startCol) {

                flexTable.setText(i, j, table.substring(previousSeparatorPosition, separatorPosition));

                previousSeparatorPosition = separatorPosition + 1;
                separatorPosition = table.indexOf(",", previousSeparatorPosition + 1);
                i++;
            }

            /*
             * WE WRITE THE LAST VALUE OF THE COLUMN
             */
            flexTable.setText(i - 1, j, table.substring(previousSeparatorPosition, endCol));

            /*
             * WE CALCULATE THE NEW POSITION OF THE NEXT COLUMN
             */
            startCol = table.indexOf("[", endCol);
            endCol = table.indexOf("]", startCol);
            previousSeparatorPosition = startCol + 1;
            separatorPosition = table.indexOf(",", startCol);

            j++;
        }
        flexTable.setCellSpacing(5);
        flexTable.setCellPadding(3);
    }

    @Override
    public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
    }

    @Override
    public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
        /*
         * HERE WE FILL IN THE INSTANCE LISTBOX
         */
        instanceListBox.clear();
        instanceNbLabel.setText("Number of instances: " + values.length);

        if (values != null && values.length > 0) {
            for (String e : values) {
                instanceListBox.addItem(e);
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetInstancesByClassId OK");
        }
    }

    @Override
    public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
        clearHMI();

        if (values != null && values.length > 0) {
            java.util.Arrays.sort(values);
            nbClassLabel.setText("Total number of classes: " + values.length);
            for (String e : values) {
                classListBox.addItem(e);
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetClasses OK");
        }
    }

    @Override
    public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
        classNameTB.setText(className);
        classNameLabel.setText("Class name of this instance: " + className);

        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetClassName OK");
        }
    }

    @Override
    public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
        classIdTB.setText(Integer.toString(cid));
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetClassId OK");
        }
    }

    @Override
    public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {

        userClassIdTB.setText("");

        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            if (key.equals("userClassId")) {
                userClassIdTB.setText(Integer.toString(cuid));
                m_RTDBAccess.getInstancesByUserClassId("InstancesByUCId", "B001", cuid, "");
            }
            if (key.equals("classinfo")) {
                instancesUserClassIdTB.setText(Integer.toString(cuid));
            }
            messageLabel.setText("GetUserClassId OK");
        }

    }

    @Override
    public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {

        if (cinfo != null) {

            m_RTDBAccess.getInstancesByClassId("InstancesByCId", "B001", cinfo.m_classId, "");

            classIdTB.setText(Integer.toString(cinfo.m_classId));
            nbInstTB.setText(Integer.toString(cinfo.m_nbInstances));
            nbInstLabel.setText("Number of instances in this class: " + cinfo.m_nbInstances);
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetClassInfo OK");
        }
    }

    @UiHandler("refreshB")
    void onRefreshBClick(ClickEvent event) {

        clearHMI();
        init();

    }

    @SuppressWarnings("deprecation")
    @UiHandler("addValueButton")
    void onaddValueButtonClick(ClickEvent event) {
        /*
         * WRITE A VALUE IN THE DATABASE
         */
        String attribute;
        int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

        /*
         * WE RETRIEVE THE NAME OF THE ATTRIBUTE IN THE LISTBOX
         */
        if (index >= 0) {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
        } else {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
        }

        /*
         * WE CONCATENATE THE POINT ALIAS OF THE ATTRIBUTE WITH THE ATTRIBUTE
         */
        String dbaddresses = setAddressTB.getText() + attribute;
        String value = typedValueTB.getText();

        /*
         * CHECK WHAT TYPE OF VALUE WILL BE TYPED
         */
        if (intValue.isChecked()) {
            int intVal = Integer.parseInt(value);
            m_RTDBAccess.writeIntValueRequest("writeValue", "B001", dbaddresses, intVal);
        } else if (floatValue.isChecked()) {
            float floatVal = Float.parseFloat(value);
            m_RTDBAccess.writeFloatValueRequest("writeValue", "B001", dbaddresses, floatVal);
        } else {
            m_RTDBAccess.writeStringValueRequest("writeValue", "B001", dbaddresses, value);
        }

        /*
         * NOTIFY THE EVENT
         */
        m_ALMAccess.notifyExternalEvent("extEvent", "B001", "almext.dat", 0, "", 1, 0,
                "The value " + value + " has been typed at this point : " + dbaddresses);
    }

    @UiHandler("hourValueLB")
    void onhourValueLBChange(ChangeEvent event) {
        Date date = dateBox.getValue();
        int hours = Integer.parseInt(hourValueLB.getItemText(hourValueLB.getSelectedIndex()));
        date.setHours(hours);
        dateBox.setValue(date);
    }

    @UiHandler("minuteValueLB")
    void onminuteValueLBChange(ChangeEvent event) {
        int minutes = Integer.parseInt(minuteValueLB.getItemText(minuteValueLB.getSelectedIndex()));
        Date date = dateBox.getValue();
        date.setMinutes(minutes);
        dateBox.setValue(date);
    }

    @UiHandler("secondValueLB")
    void onsecondValueLBChange(ChangeEvent event) {
        int seconds = Integer.parseInt(secondValueLB.getItemText(secondValueLB.getSelectedIndex()));
        Date date = dateBox.getValue();
        date.setSeconds(seconds);
        dateBox.setValue(date);
    }

    @UiHandler("addDateButton")
    void onAddDateButtonClick(ClickEvent event) {
        /*
         * THIS METHOD WORKS IN THE SAME WAY THAN THE ADD VALUE METHOD (ON ADD
         * VALUE BUTTON CLICK)
         */
        String attribute;
        int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

        if (index >= 0) {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
        } else {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
        }

        String dbaddresses = setAddressTB.getText() + attribute;

        long myMseconds = 0;
        try {
            myMseconds = Long.parseLong(msecondValueTB.getText());
        } catch (Throwable e) {
            String ev = e.getMessage();
        }

        Date date = dateBox.getValue();
        long msecond = date.getTime() + myMseconds;

        long second = msecond / 1000;
        long usecond = (msecond % 1000) * 1000;

        m_RTDBAccess.writeDateValueRequest("dateValueR", "B001", dbaddresses, second, usecond);
    }

    @UiHandler("tabPanel")
    void onTabSelection(SelectionEvent<Integer> event) {

        classIdTB.setText("");
        userClassIdTB.setText("");
        nbInstTB.setText("");
        classNameTB.setText("");
        aliasTB.setText("");
        fullPathTB.setText("");
        nbAttTB.setText("");
        instancesUserClassIdTB.setText("");

        nbInstLabel.setText("Number of instances in this class:");
        nbAttLabel.setText("Number of attributes of this class:");
        classNameLabel.setText("Class name of this instance:");
        attNbLabel.setText("Number of attributes of this instance:");
        attNb.setText("Number of attributes:");

        attributeListBox.clear();
        dateBox.setValue(null);
    }

    @UiHandler("addButton")
    void onAddButtonClick(ClickEvent event) {
        /*
         * METHOD TO WRITE A FORMULA
         */
        String formula;
        formula = setFormulaTB.getText();

        String attribute;
        int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

        if (index >= 0) {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
        } else {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
        }
        String addresses = setAddressTB.getText() + attribute;

        m_RTDBAccess.SetAttributeFormula("AttrFormula", "B001", addresses, formula);
    }

    @UiHandler("queryButton")
    void onQueryButtonClick(ClickEvent event) {
        String name = queryTB.getText();
        String address = queryAddressTB.getText();
        m_RTDBAccess.queryByName("query", "B001", name, address);
    }

    @UiHandler("getCEButton")
    void onGetCEButtonClick(ClickEvent event) {
        String dbaddressesArray[] = new String[1];

        String attribute;
        int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

        if (index >= 0) {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
        } else {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
        }

        String dbaddresses = setAddressTB.getText() + attribute;

        dbaddressesArray[0] = dbaddresses;
        m_RTDBAccess.getCEOper("CEOper", "B001", dbaddressesArray);
    }

    @UiHandler("readButton")
    void onReadButtonClick(ClickEvent event) {
        /*
         * READ THE VALUE OF AN ATTRIBUTE
         */
        String attribute;
        int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

        if (index >= 0) {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
        } else {
            attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
        }

        String dbaddresses = setAddressTB.getText() + attribute;
        m_RTDBAccess.readValueRequest(dbaddresses, "B001", dbaddresses);
    }

    private void clearFlexTable() {
        // TODO Auto-generated method stub
        flexTable.removeAllRows();
    }

    @UiHandler("upButton")
    void onUpButtonClick(ClickEvent event) {
        /*
         * GOING UP IN THE EXPLORATOR TREE
         */
        int selIndex = exploratorListBox.getSelectedIndex();
        /*
         * IF AN ITEM IS SELECTED --> GET THIS ITEM AND REMOVE THE LAST POINT
         * ALIAS
         */
        if (selIndex >= 0) {
            int index = (setAddressTB.getText()).lastIndexOf(":");
            String same = (setAddressTB.getText()).substring(0, index);

            int index2 = same.lastIndexOf(":");
            String parent = same.substring(0, index2);
            m_RTDBAccess.getChildren("parent", "B001", parent);
            setAddressTB.setText(same);
        }

        /*
         * IF THE LIST IS EMPTY (BECAUSE IT IS THE END OF A BRANCH) --> GET THE
         * LAST ADDRESS SELECTED IN ORDER TO BE IN THE SAME SITUATION THAT
         * PREVIOUSLY. AS IF THE LIST CONTAINS THE LAST ALIAS POINT OF THE
         * BRANCH
         */
        else if (exploratorListBox.getItemCount() == 0) {
            String lastItem = setAddressTB.getText();
            int i = lastItem.lastIndexOf(":");
            String addressLI = lastItem.substring(0, i);
            m_RTDBAccess.getChildren("CLI", "B001", addressLI);
        }
        /*
         * IF NO ITEM IS SELECTED (BUT THE LIST IS NOT EMPTY) -->
         */
        else {
            /*
             * On récupère la fin du premier item et on l'ajoute à l'adresse
             */
            String firstItem = exploratorListBox.getItemText(0);
            int index = firstItem.lastIndexOf(":");
            String endFI = firstItem.substring(index, firstItem.length());
            String addressFI = (setAddressTB.getText()) + endFI;

            /*
             * On récupère 2 fois la première partie de l'addresse (le parent)
             */
            int i = addressFI.lastIndexOf(":");
            String same = addressFI.substring(0, i);

            int index2 = same.lastIndexOf(":");
            String parent = same.substring(0, index2);
            m_RTDBAccess.getChildren("parent", "B001", parent);
            setAddressTB.setText(parent);
        }

        classIdTB.setText("");
        userClassIdTB.setText("");
        nbInstTB.setText("");
        classNameTB.setText("");
        aliasTB.setText("");
        fullPathTB.setText("");
        nbAttTB.setText("");
        instancesUserClassIdTB.setText("");

        nbInstLabel.setText("Number of instances in this class:");
        nbAttLabel.setText("Number of attributes of this class:");
        instanceNbLabel.setText("Number of instances: 0");
        classNameLabel.setText("Class name of this instance:");
        attNbLabel.setText("Number of attributes of this instance:");
        attNb.setText("Number of attributes:");

        attributeListBox.clear();
        dateBox.setValue(null);
    }

    @Override
    public void terminate() {
        try {
            m_RTDBAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
        clearHMI();
    }

    @Override
    public void setPresenter(HypervisorPresenterClientAbstract<?> presenter) {
    }

    @UiHandler("classListBox")
    void onClassListBoxChange(ChangeEvent event) {
        int selIndex = classListBox.getSelectedIndex();
        if (selIndex >= 0) {
            String v = classListBox.getItemText(selIndex);

            childrenAliasesListBox.clear();
            attributeListBox.clear();

            classNameTB.setText("");
            aliasTB.setText("");
            fullPathTB.setText("");
            nbAttTB.setText("");
            attNbLabel.setText("");
            formulaTB.setText("");
            typedValueTB.setText("");
            setAddressTB.setText(":" + v);

            m_RTDBAccess.getClassInfo("classinfo", "B001", v);
            m_RTDBAccess.getAttributes("attributesClasse", "B001", v);
            m_RTDBAccess.getUserClassId("userClassId", "B001", v);
            m_RTDBAccess.getAttributesDescription("AttributesDescription", "B001", v);
            m_RTDBAccess.getInstancesByClassName("InstancesByCN", "B001", v, "");
        }
    }

    @UiHandler("instanceListBox")
    void onInstanceListBoxChange(ChangeEvent event) {
        int selIndex = instanceListBox.getSelectedIndex();
        if (selIndex >= 0) {
            String v = instanceListBox.getItemText(selIndex);
            m_RTDBAccess.getClassId("classID", "B001", v);
            m_RTDBAccess.getUserClassId("classinfo", "B001", v);
            m_RTDBAccess.getClassName("className", "B001", v);
            m_RTDBAccess.getAlias("alias", "B001", v);
            m_RTDBAccess.getFullPath("fullpath", "B001", v);
            m_RTDBAccess.getAttributes("attributesInstance", "B001", v);
            m_RTDBAccess.getChildrenAliases("childrenAliases", "B001", v);
        }
    }

    @UiHandler("attributeListBox")
    void onAttributeListBoxChange(ChangeEvent event) {
        int selIndex = attributeListBox.getSelectedIndex();
        if (selIndex >= 0) {
            String attribute;
            int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");

            if (index >= 0) {
                attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index - 1);
            } else {
                attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
            }

            clearFlexTable();
            String dbaddresses = setAddressTB.getText() + attribute;

            m_RTDBAccess.getAttributeFormulas("attributeFormula", "B001", dbaddresses);
            m_RTDBAccess.getAttributeStructure("attributeFormula", "B001", dbaddresses);
        }
    }

    @UiHandler("exploratorListBox")
    void onExploratorLBChange(ChangeEvent event) {
        int selIndex = exploratorListBox.getSelectedIndex();
        if (selIndex >= 0) {
            String v = exploratorListBox.getItemText(selIndex);

            m_RTDBAccess.getFullPath("fullpath", "B001", v);
            m_RTDBAccess.getClassId("classID", "B001", v);
            m_RTDBAccess.getUserClassId("classinfo", "B001", v);
            m_RTDBAccess.getClassName("className", "B001", v);
            m_RTDBAccess.getAlias("alias", "B001", v);
            m_RTDBAccess.getAttributes("attributes", "B001", v);
        }
    }

    @UiHandler("downButton")
    void onDBC(ClickEvent event) {
        int selIndex = exploratorListBox.getSelectedIndex();
        if (selIndex >= 0) {
            String v = exploratorListBox.getItemText(selIndex);
            m_RTDBAccess.getChildren("child", "B001", v);
        }
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
        exploratorListBox.clear();
        if (instances != null && instances.length > 0) {
            java.util.Arrays.sort(instances);
            for (String e : instances) {
                exploratorListBox.addItem(e);
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetChildren OK");
        }
    }

    @Override
    public void setGetChildrenAliasesResult(String clientKey, String[] values, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        childrenAliasesListBox.clear();
        if (values != null && values.length > 0) {
            java.util.Arrays.sort(values);
            for (String e : values) {
                childrenAliasesListBox.addItem(e);
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetChildrenAliases OK");
        }
    }

    @Override
    public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        fullPathTB.setText(fullPath);
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetFullPath OK");
        }
        setAddressTB.setText(fullPath.substring(1, (fullPath.length() - 1)));
    }

    @Override
    public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        aliasTB.setText(alias);
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("GetAlias ok");
        }
    }

    @Override
    public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        queryTB.setText("");
        queryAddressTB.setText("");

        queryListBox.clear();

        if (instances != null && instances.length > 0) {
            java.util.Arrays.sort(instances);
            for (String e : instances) {
                queryListBox.addItem(e);
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("QueryByName OK");
        }
    }

    @Override
    public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

        /*
         * IF THE RESPONSE COMES FROM ON CLASS LIST BOX CHANGE
         */
        if (clientKey.equals("attributesClasse")) {
            attributeListBox.clear();
            if (attributes != null && attributes.length > 0) {
                java.util.Arrays.sort(attributes);
                nbAttLabel.setText("Number of attributes of this class: " + attributes.length);
                attNb.setText("Number of attributes: " + attributes.length);
                for (String e : attributes) {
                    attributeListBox.addItem(e);
                }
            }
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }
        }

        /*
         * IF THE RESPONSE COMES FROM ON INSTANCE LIST BOX CHANGE
         */
        if (clientKey.equals("attributesInstance")) {
            attributeListBox.clear();
            if (attributes != null && attributes.length > 0) {
                java.util.Arrays.sort(attributes);
                attNbLabel.setText("Number of attributes of this instance: " + attributes.length);
                nbAttTB.setText(Integer.toString(attributes.length));
                attNb.setText("Number of attributes: " + attributes.length);
                for (String e : attributes) {
                    attributeListBox.addItem(e);
                }
            }
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }
        }

        /*
         * IF THE RESPONSE COMES FROM ON EXPLORATOR LIST BOX CHANGE
         */
        if (clientKey.equals("attributes")) {
            attributeListBox.clear();
            if (attributes != null && attributes.length > 0) {
                java.util.Arrays.sort(attributes);
                attNb.setText("Number of attributes: " + attributes.length);
                for (String e : attributes) {
                    attributeListBox.addItem(e);
                }
            }
            if (errorCode != 0) {
                messageLabel.setText(errorMessage);
            } else {
                messageLabel.setText("");
            }
        }
    }

    @Override
    public void setGetAttributesDescriptionResult(String clientKey, ScsClassAttInfo[] attributesInfo, int errorCode,
            String errorMessage) {
        // TODO Auto-generated method stub
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("");
            if (attributesInfo != null && attributesInfo.length > 0) {
                for (ScsClassAttInfo e : attributesInfo) {
                    String attribute = e.m_attributeName;
                    attributeListBox.addItem(attribute);
                }
            }
        }
    }

    @Override
    public void setGetAttributeStructureResult(String clientKey, String attType, int fieldCount, int recordCount,
            int recordSize, String[] fieldNames, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * HERE WE WRITE THE NAME OF EACH COLUMN OF THE ATTRIBUTE (WHICH TYPE IS
         * A TABLE) INTO THE FLEXTABLE BEFORE THE USER READ THE ATTRIBUTE. WE PREPARE THE FLEXTABLE
         */
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("");
            if (fieldNames != null && fieldNames.length > 0) {
                int j = 0;
                for (String e : fieldNames) {
                    flexTable.setText(0, j, e);
                    j++;
                }
                /*
                 * FLEXTABLE WILL BE VISIBLE WHEN THE USER WOULD READ THE ATTRIBUTE
                 */
                flexTable.setVisible(false);
            }
        }
    }

    @Override
    public void setGetUserFormulasNamesResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGetDatabaseInfoResult(String clientKey, long dbsize, int nbClasses, int nbFormula, int nbInstances,
            String scsPath, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        sizeTB.setText(Long.toString(dbsize));
        nbClassesTB.setText(Integer.toString(nbClasses));
        nbFormulaTB.setText(Integer.toString(nbFormula));
        nbInstancesTB.setText(Integer.toString(nbInstances));
        scsPathTB.setText(scsPath);
    }

    @Override
    public void setForceSnapshotResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        /*
         * =================================
         */
    }

    @Override
    public void setGetAttributeFormulasResult(String clientKey, String[] formulas, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        if (formulas != null && formulas.length > 0) {
            for (String e : formulas) {
                boolean bool = e.isEmpty();
                if (bool) {
                    formulaTB.setText("No formula associated");
                } else {
                    formulaTB.setText(e);
                }
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("");
        }
    }

    @Override
    public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        setFormulaTB.setText("");
    }

    @Override
    public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub
        if (ceModes != null && ceModes.length > 0) {
            for (int e : ceModes) {
                CEMode.setText("CE Operation Indicator : " + Integer.toString(e));
            }
        }
        if (errorCode != 0) {
            messageLabel.setText(errorMessage);
        } else {
            messageLabel.setText("");
        }
    }

    @Override
    public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
        // TODO Auto-generated method stub

    }
}
