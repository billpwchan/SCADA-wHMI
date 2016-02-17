/**
 * 
 */
package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.IRTDBComponentClient;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess;
import com.thalesgroup.scadasoft.gwebhmi.ui.client.scscomponent.dbm.ScsRTDBComponentAccess.ScsClassAttInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;

/**
 * @author T0009042
 *
 */
/**
 * @author syau
 *
 */
public class WrapperScsRTDBAccess extends Composite implements IRTDBComponentClient , IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(WrapperScsRTDBAccess.class.getName());
	
//    private static DBTestPanelUiBinder uiBinder = GWT.create(DBTestPanelUiBinder.class);
//    @UiField HorizontalPanel hp;
//    @UiField CaptionPanel classInfo;
//    @UiField ListBox classListBox;
//    @UiField TextBox classIdTB;
//    @UiField TextBox userClassIdTB;
//    @UiField TextBox nbInstTB;
//    @UiField Button refreshB;
//    
//    @UiField CaptionPanel instanceInfo;
//    @UiField TextBox aliasTB;
//    @UiField TextBox fullPathTB;
//    @UiField TextBox nbAttTB;
//    @UiField TextBox classNameTB;
//    @UiField ListBox instanceListBox;
//    
//    @UiField CaptionPanel info;
//    @UiField InlineLabel messageLabel;
//    @UiField Button upButton;
//    @UiField ListBox childrenListBox;
//    
//    @UiField CaptionPanel dataBaseInfo;
//    @UiField TextBox sizeTB;
//    @UiField TextBox nbClassesTB;
//    @UiField TextBox nbFormulaTB;
//    @UiField TextBox nbInstancesTB;
//    @UiField TextBox scsPathTB;
//    
//    @UiField TextBox queryTB;
//    @UiField TextBox queryAddressTB;
//    @UiField Button queryButton;
//    @UiField ListBox queryListBox;
//    
//    @UiField ListBox attributeListBox;
//    @UiField Button readButton;
//    @UiField Button addValueButton;
//    @UiField RadioButton stringValue;
//    @UiField RadioButton intValue;
//    @UiField RadioButton floatValue;    
//    @UiField TextBox typedValueTB;
//    @UiField TextBox formulaTB;
//    @UiField TextBox setFormulaTB;
//    @UiField Button addButton;
//    
//    @UiField Button getCEButton;
//    @UiField TextBox CEMode;
//    @UiField TextBox setAddressTB;
//    @UiField HTMLPanel mainPanel;
   

    private ScsRTDBComponentAccess m_RTDBAccess = null;
    
    private WrapperScsRTDBAccessEvent wrapperScsRTDBAccessEvent = null;

//    interface DBTestPanelUiBinder extends UiBinder<Widget, DBTestPanel> {
//    }

    public WrapperScsRTDBAccess(WrapperScsRTDBAccessEvent wrapperScsRTDBAccessEvent) {
    	
    	logger.log(Level.FINE, "WrapperScsRTDBAccess Begin");
    	
//        initWidget(uiBinder.createAndBindUi(this));
//        clearHMI() ;
		this.wrapperScsRTDBAccessEvent = wrapperScsRTDBAccessEvent;
        m_RTDBAccess = new ScsRTDBComponentAccess(this);
        init();
        
        logger.log(Level.FINE, "WrapperScsRTDBAccess End");
    }

//    public WrapperScsRTDBAccess(String firstName) {
////        initWidget(uiBinder.createAndBindUi(this));
////        clearHMI() ;
//        m_RTDBAccess = new ScsRTDBComponentAccess(this);
//        init();
//    }

    private void init() {
    	
    	logger.log(Level.FINE, "init Begin");
    	
        if (m_RTDBAccess != null) {
        	
//            messageLabel.setText("Try to init.");
            m_RTDBAccess.getClasses("classes", "B001");
            m_RTDBAccess.getDatabaseInfo("dataInfo", "B001");
            
        } else {
        	
//            messageLabel.setText("DB access object not defined.");
        }
        
        logger.log(Level.FINE, "init End");
    }

//    private void clearHMI() {
//        classInfo.setCaptionHTML(" Info");;
//        classListBox.clear();
//        classIdTB.setText("");
//        userClassIdTB.setText("");
//        nbInstTB.setText("");
//        
//        classNameTB.setText("");
//        aliasTB.setText("");
//        fullPathTB.setText("");
//        nbAttTB.setText("");
//        
//        instanceListBox.clear();
//        
//        childrenListBox.clear();
//        
//        queryListBox.clear();
//        
//        attributeListBox.clear();
//        formulaTB.setText("");
//        typedValueTB.setText("");
//                
//        messageLabel.setText("HMI cleared.");
//        
//        setAddressTB.setText("Enter an address (:SITE1:B001:F000:CCTV)");
//        CEMode.setText("");
//        
//        DOM.setElementAttribute(refreshB.getElement(), "id", "RefreshBId");
//        DOM.setElementAttribute(refreshB.getElement(), "id", "RefreshBId");
//    }

    @Override
    public void destroy() {
    	
    	logger.log(Level.FINE, "destroy Begin");
    	
        m_RTDBAccess = null;
        
        logger.log(Level.FINE, "destroy End");
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void onResize() {
//        getWidget().setHeight(this.getParent().getOffsetHeight() + "px");
//        getWidget().setWidth(this.getParent().getOffsetWidth() + "px");
//    }

    @Override
    public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
    	
    	logger.log(Level.FINE, "setReadResult Begin");
    	
		if (values != null && values.length > 0) {
			if ( ! withQuery ) {
				getReadResult(key, values, errorCode, errorMessage);
			} else {
				this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
			}
			for(String value : values) {
				logger.log(Level.FINE, "setReadResult key["+key+"] value["+value+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");

			}
		} else {
			logger.log(Level.FINE, "setReadResult key["+key+"] values[ IS NULL ] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");

		}
		
		logger.log(Level.FINE, "setReadResult Begin");
    	
    	
//    	getReadResult(key, values, errorCode, errorMessage);
//        if (values != null && values.length > 0) {
//            for(String value : values) {
//Window.alert("WrapperScsRTDBAccess setReadResult: key["+key+"] value["+value+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
//				
//            	String attribute;
//            	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//            	
//            	if (index >= 0)
//            	{
//            		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//            	}
//            	else
//            	{
//            		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//            	}
//            	
//            	String currentValue = attribute + " > " + e;
//                attributeListBox.setItemText(attributeListBox.getSelectedIndex(), currentValue);;
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
    }
    

	@Override
	public void setWriteValueRequestResult(String clientKey, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void setGetInstancesByClassIdResult(String key, String[] values, int errorCode, String errorMessage) {
//        instanceListBox.clear();
//        if (values != null && values.length > 0) {
//            for(String e : values) {
//                instanceListBox.addItem(e);
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
    }

    @Override
    public void setGetClassesResult(String key, String[] values, int errorCode, String errorMessage) {
//        clearHMI();
//        if (values != null && values.length > 0) {
//        	java.util.Arrays.sort(values);
//            for(String e : values) {
//                classListBox.addItem(e);
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
    }

    @Override
    public void setGetClassNameResult(String key, String className, int errorCode, String errorMessage) {
//    	classNameTB.setText(className);
//    	if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
//    	m_RTDBAccess.getInstancesByClassName("InstancesByCN", "B001", className, "");
    }

    @Override
    public void setGetClassIdResult(String key, int cid, int errorCode, String errorMessage) {
//    	classIdTB.setText(Integer.toString(cid));
//    	if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
    }

    @Override
    public void setGetUserClassIdResult(String key, int cuid, int errorCode, String errorMessage) {
//        userClassIdTB.setText(Integer.toString(cuid));
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
//        m_RTDBAccess.getInstancesByUserClassId("InstancesByUCId", "B001", cuid, "");
    }

    @Override
    public void setGetClassInfoResult(String clientKey, DbmClassInfo cinfo, int errorCode, String errorMessage) {
//        if (cinfo != null) {
//            classIdTB.setText(Integer.toString(cinfo.m_classId));
//            m_RTDBAccess.getInstancesByClassId("InstancesByCId", "B001", cinfo.m_classId, "");
//            userClassIdTB.setText("");
//            nbInstTB.setText(Integer.toString(cinfo.m_nbInstances));
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
    }
    
//    @UiHandler("refreshB")
    void onRefreshBClick(ClickEvent event) {
//        clearHMI();
        init();
    }
    
//    @UiHandler("addValueButton")
    void onaddValueButtonClick(ClickEvent event)
    {
//    	String attribute;
//    	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//    	
//    	if (index >= 0)
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//    	}
//    	else
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//    	}
//    	
//    	String dbaddresses = setAddressTB.getText() + attribute;
//    	String value = typedValueTB.getText();
//    	
//    	
//    	if (intValue.isChecked())
//    	{
//    	    int intVal = Integer.parseInt(value);
//    	    m_RTDBAccess.writeIntValueRequest("writeValue", "B001", dbaddresses, intVal);
//    	}
//    	else if (floatValue.isChecked())
//    	{
//    	    float floatVal = Float.parseFloat(value);
//    	    m_RTDBAccess.writeFloatValueRequest("writeValue", "B001", dbaddresses, floatVal);
//    	}
//    	else
//    	{
//    	    m_RTDBAccess.writeStringValueRequest("writeValue", "B001", dbaddresses, value);
//    	}
    }
    
//    @UiHandler("addButton")
    void onAddButtonClick(ClickEvent event) {
//    	String formula;
//    	formula = setFormulaTB.getText();
//    	
//    	String attribute;
//    	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//    	
//    	if (index >= 0)
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//    	}
//    	else
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//    	}
//    	String addresses = setAddressTB.getText() + attribute;
//    	
//        m_RTDBAccess.SetAttributeFormula("AttrFormula", "B001", addresses, formula);
    }
    
//    @UiHandler("queryButton")
    void onQueryButtonClick(ClickEvent event) {
//    	String name = queryTB.getText();
//    	String address = queryAddressTB.getText();
//    	m_RTDBAccess.queryByName("query", "B001", name, address);
    }
    
//    @UiHandler("getCEButton")
    void onGetCEButtonClick(ClickEvent event) {
//    	String dbaddressesArray[] = new String[1];
//    	
//    	String attribute;
//    	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//    	
//    	if (index >= 0)
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//    	}
//    	else
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//    	}
//    	
//    	String dbaddresses = setAddressTB.getText() + attribute;
//    	
//    	dbaddressesArray[0] = dbaddresses;
//    	m_RTDBAccess.getCEOper("CEOper", "B001", dbaddressesArray);
    }
    
//    @UiHandler("readButton")
    void onReadButtonClick(ClickEvent event) {
//    	String attribute;
//    	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//    	
//    	if (index >= 0)
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//    	}
//    	else
//    	{
//    		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//    	}
//    	String dbaddresses = setAddressTB.getText() + attribute;
//Window.alert("DBTestPanel.onReadButtonClick: key["+"ReadValue"+"] scsEnvId["+"B001"+"] dbaddresses["+dbaddresses+"]");
//    	m_RTDBAccess.readValueRequest("ReadValue", "B001", dbaddresses);
    }
    class ReadRequest {
    	String key, scsEnvId, dbaddresses;
    	public ReadRequest(String key, String scsEnvId, String dbaddresses) {
    		this.key = key;
    		this.scsEnvId = scsEnvId;
    		this.dbaddresses = dbaddresses;
    	}
    }
    private HashMap<String, ReadRequest> keysQuery		= new HashMap<String, ReadRequest>();
    private HashMap<String, ReadRequest> keysWaiting	= new HashMap<String, ReadRequest>();
    private void addReadRequest(String key, String scsEnvId, String dbaddresses) {
    	
    	logger.log(Level.FINE, "addReadRequest Begin");
    	
    	ReadRequest readRequest = new ReadRequest(key, scsEnvId, dbaddresses);
    	
    	logger.log(Level.FINE, "addReadRequest readRequest.key["+readRequest.key+"] readRequest.scsEnvId["+readRequest.scsEnvId+"] readRequest.dbaddresses["+readRequest.dbaddresses+"]");

    	if ( keysQuery.get(readRequest.key) == null ) {
    		keysQuery.put(readRequest.key, readRequest);
    	}
    	setReadRequest();
    	
    	logger.log(Level.FINE, "addReadRequest End");
    }
    private void setReadRequest() {
    	
    	logger.log(Level.FINE, "setReadRequest Begin");
    	
    	if ( keysWaiting.size() == 0 ) {
    		if ( keysQuery.size() > 0 ) {
    			try {
    				Map.Entry<String, ReadRequest> entry = keysWaiting.entrySet().iterator().next();
    				//String key = entry.getKey();
    				ReadRequest readRequest = entry.getValue();
    			
    				keysQuery.remove(readRequest.key);
    				
    				logger.log(Level.FINE, "setReadRequest readRequest.key["+readRequest.key+"] readRequest.scsEnvId["+readRequest.scsEnvId+"] readRequest.dbaddresses["+readRequest.dbaddresses+"]");

					m_RTDBAccess.readValueRequest(readRequest.key, readRequest.scsEnvId, readRequest.dbaddresses);
					
    			} catch (NoSuchElementException e)  {
    				Window.alert(e.toString());
    			}
    		}
    	}
    	
    	logger.log(Level.FINE, "setReadRequest End");
    	
    }
    private void getReadResult(String key, String[] values, int errorCode, String errorMessage) {
    	
    	logger.log(Level.FINE, "getReadResult Begin");
    	
    	this.wrapperScsRTDBAccessEvent.setReadResult(key, values, errorCode, errorMessage);
    	
    	//ReadRequest readRequest = keysWaiting.remove(key);
    	
    	logger.log(Level.FINE, "getReadResult key["+key+"] values[0]["+values[0]+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
    	
    	setReadRequest();
    	
    	logger.log(Level.FINE, "getReadResult End");
    }
    
    private boolean withQuery = false;
    /**
     * @param key
     * 				Key - "ReadValue"
     * @param scsEnvId
     * 				ScsEnvName - "B001"
     * @param dbaddresses
     * 				DB Address - ":SITE1:B001:F001:ACCESS:DO001.UNIVNAME"
     */
    public void readValueRequest(String key, String scsEnvId, String dbaddresses) {
    	
    	logger.log(Level.FINE, "readValueRequest Begin");
    	
    	logger.log(Level.FINE, "readValueRequest key["+key+"] scsEnvId["+scsEnvId+"] dbaddresses["+dbaddresses+"]");
    	
    	if ( withQuery ) {
    		addReadRequest(key, scsEnvId, dbaddresses);
    	} else {
    		m_RTDBAccess.readValueRequest(key, scsEnvId, dbaddresses);
    	}
    	
    	logger.log(Level.FINE, "readValueRequest End");
    }
    
//    @UiHandler("upButton")
    void onUpButtonClick(ClickEvent event) {
//    	int selIndex = childrenListBox.getSelectedIndex();
//    	/*
//    	 * Si un item est s�lectionn�
//    	 */
//        if (selIndex >=0) {
//            int index = (setAddressTB.getText()).lastIndexOf(":");
//            String same = (setAddressTB.getText()).substring(0, index);
//            
//            int index2 = same.lastIndexOf(":");
//            String parent = same.substring(0, index2);
//            m_RTDBAccess.getChildren("parent", "B001", parent);
//            setAddressTB.setText(same);
//        }
//        
//        /*
//         * Si la liste est vide
//         */
//        else if (childrenListBox.getItemCount() == 0)
//        {
//        	String lastItem = setAddressTB.getText();
//        	int i = lastItem.lastIndexOf(":");
//        	String addressLI = lastItem.substring(0, i);
//        	m_RTDBAccess.getChildren("CLI", "B001", addressLI);
//        }
//        /*
//         * Si aucun item n'est s�lectionn�
//         */
//        else
//        {
//        	/*
//        	 * On r�cup�re la fin du premier item et on l'ajoute � l'adresse
//        	 */
//        	String firstItem = childrenListBox.getItemText(0);
//            int index = firstItem.lastIndexOf(":");
//            String endFI = firstItem.substring(index, firstItem.length());        	
//            String addressFI = (setAddressTB.getText()) + endFI;
//            
//            /*
//             * On r�cup�re 2 fois la premi�re partie de l'addresse (le parent)
//             */
//            int i = addressFI.lastIndexOf(":");
//            String same = addressFI.substring(0, i);
//            
//            int index2 = same.lastIndexOf(":");
//            String parent = same.substring(0, index2);
//            m_RTDBAccess.getChildren("parent", "B001", parent);
//            setAddressTB.setText(parent);
//        }
    }
    
    @Override
    public void terminate() {
    	logger.log(Level.FINE, "terminate Begin");
        try {
            m_RTDBAccess.terminate();
        } catch (final IllegalStatePresenterException e) {
            e.printStackTrace();
        }
//        clearHMI();
        logger.log(Level.FINE, "terminate End");
    }

    @Override
    public void setPresenter(HypervisorPresenterClientAbstract<?> presenter) {
    	/*
    	 * =================================
    	 */
       
    }

//	@UiHandler("classListBox")
	void onClassListBoxChange(ChangeEvent event) {
//		int selIndex = classListBox.getSelectedIndex();
//        if (selIndex >=0) {
//            String v = classListBox.getItemText(selIndex);
//            classInfo.setCaptionHTML(v + " info");
//            m_RTDBAccess.getClassInfo("classinfo",  "B001", v);
//            
//        	classNameTB.setText("");
//            aliasTB.setText("");
//            fullPathTB.setText("");
//            nbAttTB.setText("");
//            
//            childrenListBox.clear();
//            
//            attributeListBox.clear();
//            formulaTB.setText("");
//            typedValueTB.setText("");
//
//            m_RTDBAccess.getAttributes("attributes", "B001", v);
//        }
	}
//	@UiHandler("instanceListBox")
	void onInstanceListBoxChange(ChangeEvent event) {
//		int selIndex = instanceListBox.getSelectedIndex();
//        if (selIndex >=0) {
//            String v = instanceListBox.getItemText(selIndex);
//            m_RTDBAccess.getClassId("classID", "B001", v);
//            m_RTDBAccess.getUserClassId("classinfo",  "B001", v);
//            
//            m_RTDBAccess.getClassName("className", "B001", v);
//            m_RTDBAccess.getAlias("alias", "B001", v);
//            m_RTDBAccess.getFullPath("fullpath", "B001", v);
//            
//            m_RTDBAccess.getChildren("children", "B001", v);
//            
//            m_RTDBAccess.getAttributes("attributes", "B001", v);
//        }
	}
//	@UiHandler("attributeListBox")
	void onAttributeListBoxChange(ChangeEvent event) {
//		int selIndex = attributeListBox.getSelectedIndex();
//        if (selIndex >=0) {
//        	String attribute;
//        	int index = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).indexOf(">");
//        	
//        	if (index >= 0)
//        	{
//        		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex()).substring(0, index-1);
//        	}
//        	else
//        	{
//        		attribute = attributeListBox.getItemText(attributeListBox.getSelectedIndex());
//        	}
//        	
//        	String dbaddresses = setAddressTB.getText() + attribute;
//            m_RTDBAccess.getAttributeFormulas("attributeFormula", "B001", dbaddresses);
//        }
	}
	
//	@UiHandler("childrenListBox")
	void onChange(ChangeEvent event) {
//		int selIndex = childrenListBox.getSelectedIndex();
//        if (selIndex >=0) {
//            String v = childrenListBox.getItemText(selIndex);
//            m_RTDBAccess.getFullPath("Fp", "B001", v);
//            
//            m_RTDBAccess.getAttributes("attributes", "B001", v);
//        }
	}
//	@UiHandler("childrenListBox")
	void onDBC(DoubleClickEvent event) {
//		int selIndex = childrenListBox.getSelectedIndex();
//        if (selIndex >=0) {
//            String v = childrenListBox.getItemText(selIndex);
//            m_RTDBAccess.getChildren("child", "B001", v);
//        }
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
//		childrenListBox.clear();
//		if (instances != null && instances.length > 0) {
//			java.util.Arrays.sort(instances);
//            for(String e : instances) {
//            	childrenListBox.addItem(e);
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
	}

	@Override
	public void setGetFullPathResult(String clientKey, String fullPath, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		fullPathTB.setText(fullPath);
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
//        setAddressTB.setText(fullPath.substring(1, (fullPath.length() - 1)));
	}

	@Override
	public void setGetAliasResult(String clientKey, String alias, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		aliasTB.setText(alias);
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
	}
	
	@Override
	public void setQueryByNameResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		queryTB.setText("");
//		queryAddressTB.setText("");
//		queryListBox.clear();
//		if (instances != null && instances.length > 0) {
//			java.util.Arrays.sort(instances);
//            for(String e : instances) {
//            	queryListBox.addItem(e);
//        }
//		}
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
	}

	@Override
	public void setGetAttributesResult(String clientKey, String[] attributes, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		attributeListBox.clear();
//		if (attributes != null && attributes.length > 0) {
//			java.util.Arrays.sort(attributes);
//			nbAttTB.setText(Integer.toString(attributes.length));
//            for(String e : attributes) {
//                attributeListBox.addItem(e);
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
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
//		sizeTB.setText(Long.toString(dbsize));
//        nbClassesTB.setText(Integer.toString(nbClasses));
//        nbFormulaTB.setText(Integer.toString(nbFormula));
//        nbInstancesTB.setText(Integer.toString(nbInstances));
//        scsPathTB.setText(scsPath);
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
//		if (formulas != null && formulas.length > 0) {
//            for(String e : formulas) {
//            	boolean bool = e.isEmpty();
//            	if(bool) {
//            		formulaTB.setText("No formula associated");
//            	}
//            	else
//            	{
//            	formulaTB.setText(e);
//            	}
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
	}

	@Override
	public void setSetAttributeFormulaResult(String clientKey, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		setFormulaTB.setText("");
	}

	@Override
	public void setGetCEOperResult(String clientKey, int[] ceModes, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
//		if (ceModes != null && ceModes.length > 0) {
//            for(int e : ceModes) {
//            	CEMode.setText("CE Operation Indicator : "+ Integer.toString(e));
//            }
//        }
//        if (errorCode != 0) {
//			messageLabel.setText(errorMessage);
//		} else {
//			messageLabel.setText("");
//		}
	}

	@Override
	public void setControlCEResult(String clientKey, int errorCode, String errorMessage) {
		// TODO Auto-generated method stub
		
	}
}
