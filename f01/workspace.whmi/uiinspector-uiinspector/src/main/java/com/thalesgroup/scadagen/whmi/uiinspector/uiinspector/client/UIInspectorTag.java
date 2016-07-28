package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorPage_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.ctl.CtlMgr;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.database.Database;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.database.DatabaseEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Observer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.observer.Subject;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorTag implements UIInspectorPage_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorTag.class.getName());
	
	private final String tagname				= "tag";
	
	// Static Attribute List
	private final String aioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};
	private final String dioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};
	private final String sioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.log(Level.FINE, "setAddresses this.scsEnvId["+this.scsEnvId+"]");
		logger.log(Level.FINE, "setParent this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private void updatePager() {
		
		logger.log(Level.FINE, "updatePager Begin");
		
		pageCounter.calc(pageIndex);
				
		if ( pageCounter.hasPreview || pageCounter.hasNext ) {
			btnUp.setEnabled(pageCounter.hasPreview);
			lblPageNum.setText(String.valueOf(pageIndex+1) + " / " + String.valueOf(pageCounter.pageCount));
			btnDown.setEnabled(pageCounter.hasNext);
		} else {
			btnUp.setVisible(false);
			lblPageNum.setVisible(false);
			btnDown.setVisible(false);
		}
		
		logger.log(Level.FINE, "updatePager End");
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	
	private VerticalPanel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private HorizontalPanel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 10);
			pageCounter.calc(pageIndex);
			
			updatePager();

		} else {
			logger.log(Level.SEVERE, "buildWidgets vpCtrls IS NULL");
		}
		
		logger.log(Level.FINE, "buildWidgets End");
	}
	
	class ControlPoint {
		private HashMap<String, String> hashmap = new HashMap<String, String>();
		public ControlPoint(String point, String scsenvid, String dbaddress) {
			hashmap.put("point", point);
			hashmap.put("scsenvid", scsenvid);
			hashmap.put("dbaddress", dbaddress);
		}
		public ControlPoint(String point, String scsenvid, String dbaddress, String value) {
			this( point, scsenvid, dbaddress);
			hashmap.put("value", value);
		}
		public String getValue(String key) {
			return hashmap.get(key);
		}
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues				= new HashMap<String, String>();
	
	private HashMap<Widget, ControlPoint> widgetPoints		= new HashMap<Widget, ControlPoint>();
	private HashMap<String, Widget[]> widgetGroups			= new HashMap<String, Widget[]>();
	
	private HashMap<String, Widget> initCondGLAndWidget		= new HashMap<String, Widget>();
	private HashMap<String, String[]> addressAndInitCongGL	= new HashMap<String, String[]>();
	
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
		
		if ( "static".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( "dynamic".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesDynamic.put(clientKey, keyAndValue);
			
			updateValue(false);
		}
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValue(boolean withStatic) {
		
		logger.log(Level.FINE, "updateValue Begin");
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				HashMap<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			HashMap<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
			
			updateValueDynamic(clientKey, keyAndValue);
			
		}// End of keyAndValuesDynamic
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValueStatic Begin");

		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		if ( clientKeyStatic.equals(clientKey) ) {
			
			initCondGLAndWidget.clear();
			addressAndInitCongGL.clear();
			
			vpCtrls.clear();

			pageCounter.calc(pageIndex);
			
			updatePager();

			int rowBegin	= pageCounter.pageRowBegin;
			int rowEnd		= pageCounter.pageRowEnd;
			
			int numOfWidgetShow	= rowEnd - rowBegin;
			
			if ( numOfWidgetShow > 0 ) {

				widgetBoxes		= new VerticalPanel[numOfWidgetShow];
				inlineLabels	= new InlineLabel[numOfWidgetShow];
				controlboxes	= new HorizontalPanel[numOfWidgetShow];
				
				widgetPoints.clear();
				widgetGroups.clear();
				
				for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
					
					ComplexPanel widget = null;
					
					String dbaddress = this.addresses[x];
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						RTDB_Helper.PointType pointType = RTDB_Helper.getPointType(point);
						if ( PointType.RTDB_DIO == pointType ) {
							widget = updateDioControl(dbaddress, y);
						} else if ( PointType.RTDB_AIO == pointType ) {
							widget = updateAioControl(dbaddress, y);
						} else if ( PointType.RTDB_SIO == pointType ) {
							widget = updateSioControl(dbaddress, y);
						}
					}
					
					if ( null != widget ) {
						vpCtrls.add(widget);
					}
				}
			}
			
			createDioInitConds();
			connectDIOinitConds();		
			
		}




		logger.log(Level.FINE, "updateValueStatic End");
	}
	
	private ComplexPanel updateDioControl(String address, int row) {
		
		logger.log(Level.FINE, "updateDioControl address["+address+"] row["+row+"]");
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;

		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName("project-gwt-label-inspector-"+tagname+"-control");

		String label = null; 
		{
			String dbaddress = address + PointName.label.toString();
			if ( dbvalues.containsKey(dbaddress) ) {
				label = dbvalues.get(dbaddress);
				label = RTDB_Helper.removeDBStringWrapper(label);
				logger.log(Level.FINE, "updateValue label["+label+"]");
				inlineLabels[row].setText(label);
			}			
		}
		
		controlboxes[row] = new HorizontalPanel();

		String valueTable = null;
		{
			String dbaddress = address + PointName.valueTable.toString();
			valueTable = dbvalues.get(dbaddress);
		}

		if ( null !=  valueTable ) {
			
			int numOfRow = 12;
			String points[] = new String[numOfRow];
			String labels[] = new String[numOfRow];
			String values[] = new String[numOfRow];
			for( int r = 0 ; r < numOfRow ; ++r ) {
					
				points[r] = RTDB_Helper.getArrayValues(valueTable, dovnameCol, r );
				points[r] = RTDB_Helper.removeDBStringWrapper(points[r]);
					
				labels[r] = RTDB_Helper.getArrayValues(valueTable, labelCol, r );
				labels[r] = RTDB_Helper.removeDBStringWrapper(labels[r]);
					
				values[r] = RTDB_Helper.getArrayValues(valueTable, valueCol, r );
				values[r] = RTDB_Helper.removeDBStringWrapper(values[r]);					
			}
			
//			LinkedList<String> initCondGLList = new LinkedList<String>();
			
			// Loop Control Point
			LinkedList<UIButtonToggle> btnOptions = new LinkedList<UIButtonToggle>();
			for ( int i = 0 ; i < points.length ; ++i ) {
				
				if ( labels[i].length() == 0  ) break;
				
				UIButtonToggle btnOption = new UIButtonToggle(labels[i]);
				btnOption.addStyleName("project-gwt-button-inspector-"+tagname+"-ctrl");
				btnOption.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						onButton(event);
					}
				});
				
				btnOption.setEnabled(false);
				
				btnOptions.add(btnOption);
				
				controlboxes[row].add(btnOption);
				
				widgetGroups.put(address, btnOptions.toArray(new UIButtonToggle[0]));
				
				widgetPoints.put(btnOption, new ControlPoint(PointType.RTDB_DIO.toString(), scsEnvId, address, values[i]));

				initCondGLAndWidget.put(address+":"+points[i]+".initCondGL", btnOption);
			
			}
			
		}
		
		widgetBoxes[row] = new VerticalPanel();
		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tagname+"-control");
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		return widgetBoxes[row];
	}
	
	private ComplexPanel updateAioControl(String address, int row) {
		return null;
	}
		
	private ComplexPanel updateSioControl(String address, int row) {
		return null;	
	}
	
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValueDynamic Begin");
		
		pageCounter.calc(pageIndex);
//		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		for ( int x = rowBegin ; x < rowEnd ; ++x ) {
			String dbaddress = this.addresses[x];
			
			String [] initCondGLs = addressAndInitCongGL.get(dbaddress);
			if ( null != initCondGLs ) {
				for ( String initCondGL : initCondGLs ) {
					String value = dbvalues.get(initCondGL);
					if ( null != value ) {
						UIButtonToggle widget = (UIButtonToggle)initCondGLAndWidget.get(initCondGL);
						if ( null != widget ) {
							if ( value.equals("1") ) {
								widget.setEnabled(true);
							} else {
								widget.setEnabled(false);
							}
						} else {
							logger.log(Level.SEVERE, "updateValueDynamic uiButtonToggle IS NULL");
						}
					} else {
						logger.log(Level.SEVERE, "updateValueDynamic value IS NULL");
					}
				}				
			}

		}
		
		logger.log(Level.FINE, "updateValueDynamic End");
	}
	
//	private LinkedList<String> controldios = new LinkedList<String>();
	
	private CtlMgr ctlMgr = null;
	private Observer observer = null;
	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
			
		String[] dbaddresses = null;
		{
			ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				String dbaddress = this.addresses[i];
				
				String point = RTDB_Helper.getPoint(dbaddress);
				RTDB_Helper.PointType pointType = RTDB_Helper.getPointType(point);
				
				if ( PointType.RTDB_DIO == pointType ) {
					for ( String attribute : dioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( PointType.RTDB_AIO == pointType ) {
					for ( String attribute : aioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( PointType.RTDB_SIO == pointType ) {
					for ( String attribute : sioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				}
			}
			dbaddresses = dbaddressesArrayList.toArray(new String[0]);
		}
			
		Database database = Database.getInstance();
		
		String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		String api = "multiReadValue";
		database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
			
			@Override
			public void update(String key, String[] value) {
				Database database = Database.getInstance();
				{
					String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
					if ( 0 == clientKeyStatic.compareTo(key) ) {
							String [] dbaddresses	= database.getKeyAndAddress(key);
							String [] dbvalues		= database.getKeyAndValues(key);
						HashMap<String, String> keyAndValue = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							keyAndValue.put(dbaddresses[i], dbvalues[i]);
						}
						updateValue(key, keyAndValue);
					}
				}
			}
		});
		
		{
			ctlMgr = CtlMgr.getInstance(tagname);
			controlMgrSubject = ctlMgr.getSubject();
			
			observer = new Observer() {
				@Override
				public void setSubject(Subject subject){
					this.subject = subject;
					this.subject.attach(this);
				}
				
				@Override
				public void update() {
					if ( null != messageBoxEvent ) {
						
						String message = subject.getState();
						
						String msgPrefix	= "Executing command : ";
						String msgSending	= "Sending Command";
						String msgSucceed	= "Succeed";
						String msgFailed	= "Failed";
						if ( message.equals("COMMAND SENT") 
								|| message.equals("\"SendIntCommand sent\"") 
								|| message.equals("\"SendStringCommand sent\"") 
								|| message.equals("\"SendFloatCommand sent\"") ) {
							message = msgPrefix + msgSending;
						} else if ( message.equals("\"command executed successfully\"") ) {
							message = msgPrefix + msgSucceed;
						} else if ( message.equals("COMMAND FAILED") ) {
							message = msgPrefix + msgFailed;
						}
						
						messageBoxEvent.setMessage(message);
					}
				}
			};
			
			observer.setSubject(controlMgrSubject);			
		}
		
		logger.log(Level.FINE, "connect End");
	}
	
	void createDioInitConds() {
		
		logger.log(Level.FINE, "createDioInitConds Begin");
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;
		
		LinkedList<String> initCondGLList = new LinkedList<String>();
		for ( int x = 0 ; x < this.addresses.length ; ++x ) {
			String address = this.addresses[x];
			String valueTable = null;
			{
				String dbaddress = address + PointName.valueTable.toString();
				valueTable = dbvalues.get(dbaddress);
			}
			if ( null !=  valueTable ) {
				int numOfRow = 12;

				String points[] = new String[numOfRow];
				String labels[] = new String[numOfRow];
				String values[] = new String[numOfRow];
				for( int r = 0 ; r < numOfRow ; ++r ) {
						
					points[r] = RTDB_Helper.getArrayValues(valueTable, dovnameCol, r );
					points[r] = RTDB_Helper.removeDBStringWrapper(points[r]);
						
					labels[r] = RTDB_Helper.getArrayValues(valueTable, labelCol, r );
					labels[r] = RTDB_Helper.removeDBStringWrapper(labels[r]);
						
					values[r] = RTDB_Helper.getArrayValues(valueTable, valueCol, r );
					values[r] = RTDB_Helper.removeDBStringWrapper(values[r]);					
				}
				
				for ( int i = 0 ; i < points.length ; ++i ) {
					
					if ( labels[i].length() == 0  ) break;
					
					String pointaddress = address+":"+points[i];
					initCondGLList.add(pointaddress+".initCondGL");
				}

				addressAndInitCongGL.put(address, initCondGLList.toArray(new String[0]));
			} else {
				logger.log(Level.SEVERE, "createDioInitConds valueTable IS NULL");
			}
			
		}
		
		logger.log(Level.FINE, "createDioInitConds End");
	}
	
	void connectDIOinitConds() {
		
		// Read dynamic
		{
			logger.log(Level.FINE, "multiReadValue Begin");
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String address : addressAndInitCongGL.keySet() ) {
					for ( String initCondGL : addressAndInitCongGL.get(address) ) {
						dbaddressesArrayList.add(initCondGL);
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
//			logger.log(Level.SEVERE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
//			for(int i = 0; i < dbaddresses.length; ++i ) {
//				logger.log(Level.SEVERE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
//			}
			
			Database database = Database.getInstance();
			database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
			logger.log(Level.FINE, "multiReadValue End");
		}
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		logger.log(Level.FINE, "disconnect End");
	}
	
	private Button btnExecute = null;
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls  = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setText("Execute");
		btnExecute.addStyleName("project-gwt-button-inspector-"+tagname+"-execute");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.log(Level.FINE, "getMainPanel onClick Begin");
				
				onButton(event);
				
				logger.log(Level.FINE, "getMainPanel onClick End");
			}
		});
//		btnExecute.setEnabled(false);
		
		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				onButton(event);
			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				onButton(event);
			}
		});	

		HorizontalPanel pageBar = new HorizontalPanel();

		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnUp);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(lblPageNum);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnDown);
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setWidth("100%");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnExecute);
				
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-"+tagname+"-inspector");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}
	

	private void toggleButtonGroup ( UIButtonToggle btnSource, UIButtonToggle[] btnGroup ) {
		boolean toggled = btnSource.isHightLight();
		if ( !toggled ) {
			for ( UIButtonToggle btn : btnGroup ) {
				if ( btn != btnSource )
					btn.setHightLight(false);
			}
			btnSource.setHightLight(true);
		} else {
			btnSource.setHightLight(false);
		}
	}
	private void toggleButtonGroup ( Widget btnSource, Widget[] btnGroup ) {
		toggleButtonGroup((UIButtonToggle)btnSource, (UIButtonToggle[])btnGroup);
	}
	
	private Widget[] getSelectedGroup(Widget btn) {
		Widget[] targetGroups = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( targetGroups != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( targetGroups != null ) break;
				if ( widget == btn ) {
					targetGroups = widgets;
				}
			}
		}
		return targetGroups;
	}
	
	private Widget getActivateWidget() {
		logger.log(Level.FINE, "getActivateWidget Begin");
		Widget target = null;
		for ( String address : widgetGroups.keySet() ) {
			if ( target != null ) break;
			Widget[] widgets = widgetGroups.get(address);
			for ( Widget widget : widgets ) {
				if ( target != null ) break;
				if ( null != widget ) {
					if ( widget instanceof TextBox ) {
						TextBox txt = (TextBox)widget;
						String s = txt.getText();
						if ( s.length() > 0 ) {
							target = widget;
						}
					} else if ( widget instanceof UIButtonToggle ) {
						UIButtonToggle btn = (UIButtonToggle)widget;
						boolean b = btn.isHightLight();
						if ( b ) {
							target = widget;
						}
					} else {
						logger.log(Level.FINE, "getActivateWidget widget IS NOT SUPPORTED");
					}
				} else {
					logger.log(Level.FINE, "getActivateWidget btn IS NULL");
				}
			}
		}
		logger.log(Level.FINE, "getActivateWidget End");
		return target;
	}
	
	private void onButton(ClickEvent event) {
		
		int byPassInitCond = 0;
		int byPassRetCond = 0;
		int sendAnyway = 0;
		
		Button btn = (Button)event.getSource();
		if ( btn instanceof UIButtonToggle ) {
			
			logger.log(Level.FINE, "onButton Button IS UIButtonToggle");
			
			Widget[] targetGroups = getSelectedGroup(btn);
			if ( null != targetGroups ) {
				toggleButtonGroup(btn, targetGroups);
			}
			
		} else if ( btn instanceof Button ) {
			logger.log(Level.FINE, "onButton Button IS Button");
			
			if ( btnExecute == btn ) {
				
				logger.log(Level.FINE, "onButton btn IS btnExecute");
				
				Widget widget = getActivateWidget();
				
				if ( null != widget ) {
					
					// Reset the Widget
					if ( widget instanceof UIButtonToggle ) {
						((UIButtonToggle) widget).setHightLight(false);
					} else if ( widget instanceof TextBox ) {
						((TextBox) widget).setText("");
					}
					
					// Send Control
					if ( widgetPoints.containsKey(widget) ) {
						
						ControlPoint controlPoint = widgetPoints.get(widget);
						
						if ( null != controlPoint ) {
							
							String sPoint		= controlPoint.getValue("point");
							String sScsEnvId	= controlPoint.getValue("scsenvid");
							String sDbAddress	= controlPoint.getValue("dbaddress");
							String sValue		= controlPoint.getValue("value");
							
							logger.log(Level.FINE, "onButton sPoint["+sPoint+"]");
							logger.log(Level.FINE, "onButton sScsEnvId["+sScsEnvId+"]");
							logger.log(Level.FINE, "onButton sDbAddress["+sDbAddress+"]");
							logger.log(Level.FINE, "onButton sValue["+sValue+"]");
							
							if ( 0 == sPoint.compareTo("dio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] Integer ["+Integer.parseInt(sValue)+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Integer.valueOf(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo("aio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] Float ["+Float.parseFloat(sValue)+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Float.parseFloat(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo("sio") ) {
								
								logger.log(Level.FINE, "onButton controlPoint.dbaddress["+sDbAddress+"] String ["+sValue+"]");
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, sValue, byPassInitCond, byPassRetCond, sendAnyway);

							} else {
								logger.log(Level.SEVERE, "onButton INVALID controlPoint.point");
							}
						} else {
							logger.log(Level.SEVERE, "onButton controlPoint IS NULL");
						}
					} else {
						logger.log(Level.SEVERE, "onButton controlPoint NOT CONTAIN the Key");
					}
				} else {
					logger.log(Level.SEVERE, "onButton controlPoint widget IS NULL");
				}
			} else if ( btn == btnUp || btn == btnDown ) {
				if ( btn == btnUp) {
					--pageIndex;
				} else if ( btn == btnDown ) {
					++pageIndex;
				}
				updatePager();
				updateValue(true);
			}
		}
	}

	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}

}
