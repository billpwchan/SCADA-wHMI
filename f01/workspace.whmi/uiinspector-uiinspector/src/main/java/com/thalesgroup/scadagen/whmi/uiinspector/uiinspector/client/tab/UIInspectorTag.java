package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.page.PageCounter;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIButtonToggle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class UIInspectorTag implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorTag.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private final String tagname				= "tag";
	
	// Static Attribute List
	private final String aioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};
	private final String dioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};
	private final String sioStaticAttibutes[]	= new String[] {PointName.label.toString(), PointName.valueTable.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.info(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.info(className, function, "this.parent[{}]", this.parent);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		
		logger.begin(className, function);
		
		this.addresses = addresses;
		
		logger.end(className, function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private void updatePager() {
		final String function = "updatePager";
		
		logger.begin(className, function);
		
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
		
		logger.end(className, function);
	}
	
	private void updateLayout() {
		final String function = "updateLayout";
		
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		int pageSize = pageCounter.pageSize;
		
		int stopper = pageCounter.pageRowCount;
		
		logger.info(className, function
				, "pageIndex[{}] pageSize[{}], stopper[{}]"
				, new Object[]{pageIndex, pageSize, stopper});
		
		for ( int i = 0 ; i < pageSize ; i++ ) {
			boolean visible = true;
			if ( i > stopper ) {
				visible = false;
			}
			widgetBoxes[i].setVisible(visible);
		}

		logger.end(className, function);
	}
	
	@Override
	public void buildWidgets(int numOfPointEachPage) {
		buildWidgets(this.addresses.length, numOfPointEachPage);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	
	private VerticalPanel[] widgetBoxes = null;
	private InlineLabel[] inlineLabels = null;
	private HorizontalPanel[] controlboxes = null;
	private void buildWidgets(int numOfWidgets, int numOfPointEachPage) {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		logger.info(className, function, "numOfWidgets[{}]", numOfWidgets);
		logger.info(className, function, "numOfPointEachPage[{}]", numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			int pageSize = pageCounter.pageSize;
			
			int stopper = pageCounter.pageRowCount;
			
			logger.info(className, function, "pageIndex[{}] pageSize[{}], stopper[{}]"
					, new Object[]{pageIndex, pageSize, stopper});
			
			widgetBoxes	= new VerticalPanel[pageSize];
			for ( int i = 0 ; i < pageSize ; i++ ) {
				widgetBoxes[i] = new VerticalPanel();
				widgetBoxes[i].addStyleName("project-gwt-panel-inspector-"+tagname+"-control");
			}
			
			updatePager();
			updateLayout();

		} else {
			logger.warn(className, function, "vpCtrls IS NULL");
		}
		
		logger.end(className, function);
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
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "clientkey[{}]", clientKey);
		
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
		
		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
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
		
		logger.end(className, function);
	}
	
	private void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);

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

//				widgetBoxes		= new VerticalPanel[numOfWidgetShow];
				inlineLabels	= new InlineLabel[numOfWidgetShow];
				controlboxes	= new HorizontalPanel[numOfWidgetShow];
				
				widgetPoints.clear();
				widgetGroups.clear();
				
				for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
					
					ComplexPanel widget = null;
					
					String dbaddress = this.addresses[x];
					String point = DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						if ( PointType.dio == pointType ) {
							widget = updateDioControl(dbaddress, y);
						} else if ( PointType.aio == pointType ) {
							widget = updateAioControl(dbaddress, y);
						} else if ( PointType.sio == pointType ) {
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

		logger.end(className, function);
	}
	
	private ComplexPanel updateDioControl(String address, int row) {
		final String function = "updateDioControl";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "address[{}] row[{}]", address, row);
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;

		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName("project-gwt-label-inspector-"+tagname+"-control");

		String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.debug(className, function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new HorizontalPanel();

		String valueTable = DatabaseHelper.getAttributeValue(address, PointName.valueTable.toString(), dbvalues);
		logger.debug(className, function, "label[{}]", valueTable);

		if ( null !=  valueTable ) {
			
			int numOfRow = 12;
			String points[] = new String[numOfRow];
			String labels[] = new String[numOfRow];
			String values[] = new String[numOfRow];
			for( int r = 0 ; r < numOfRow ; ++r ) {
					
				points[r] = DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
				points[r] = DatabaseHelper.removeDBStringWrapper(points[r]);
					
				labels[r] = DatabaseHelper.getArrayValues(valueTable, labelCol, r );
				labels[r] = DatabaseHelper.removeDBStringWrapper(labels[r]);
					
				values[r] = DatabaseHelper.getArrayValues(valueTable, valueCol, r );
				values[r] = DatabaseHelper.removeDBStringWrapper(values[r]);					
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
				
				widgetPoints.put(btnOption, new ControlPoint(PointType.dio.toString(), scsEnvId, address, values[i]));

				initCondGLAndWidget.put(address+":"+points[i]+".initCondGL", btnOption);
			}
		}
		
//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tagname+"-control");
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(className, function);
		
		return widgetBoxes[row];
	}
	
	private ComplexPanel updateAioControl(String address, int row) {
		final String function = "updateAioControl";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "address[{}] row[{}]", address, row);
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;

		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName("project-gwt-label-inspector-"+tagname+"-control");
		
		String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.debug(className, function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new HorizontalPanel();

		String valueTable = DatabaseHelper.getAttributeValue(address, PointName.valueTable.toString(), dbvalues);
		logger.debug(className, function, "valueTable[{}]", valueTable);

		if ( null !=  valueTable ) {
			
			int numOfRow = 12;
			String points[] = new String[numOfRow];
			String labels[] = new String[numOfRow];
			String values[] = new String[numOfRow];
			for( int r = 0 ; r < numOfRow ; ++r ) {
					
				points[r] = DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
				points[r] = DatabaseHelper.removeDBStringWrapper(points[r]);
					
				labels[r] = DatabaseHelper.getArrayValues(valueTable, labelCol, r );
				labels[r] = DatabaseHelper.removeDBStringWrapper(labels[r]);
					
				values[r] = DatabaseHelper.getArrayValues(valueTable, valueCol, r );
				values[r] = DatabaseHelper.removeDBStringWrapper(values[r]);					
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
				
				widgetPoints.put(btnOption, new ControlPoint(PointType.dio.toString(), scsEnvId, address, values[i]));

				initCondGLAndWidget.put(address+":"+points[i]+".initCondGL", btnOption);
			
			}
			
		}
		
//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tagname+"-control");
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(className, function);
		
		return widgetBoxes[row];
	}
		
	private ComplexPanel updateSioControl(String address, int row) {
		final String function = "updateSioControl";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "address[{}] row[{}]", address, row);
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;

		inlineLabels[row] = new InlineLabel();
		inlineLabels[row].addStyleName("project-gwt-label-inspector-"+tagname+"-control");

		String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
		label = DatabaseHelper.removeDBStringWrapper(label);
		logger.debug(className, function, "label[{}]", label);
		if ( null != label ) inlineLabels[row].setText(label);
		
		controlboxes[row] = new HorizontalPanel();

		String valueTable = DatabaseHelper.getAttributeValue(address, PointName.valueTable.toString(), dbvalues);
		logger.debug(className, function, "valueTable[{}]", valueTable);
		
		if ( null !=  valueTable ) {
			
			int numOfRow = 12;
			String points[] = new String[numOfRow];
			String labels[] = new String[numOfRow];
			String values[] = new String[numOfRow];
			for( int r = 0 ; r < numOfRow ; ++r ) {
					
				points[r] = DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
				points[r] = DatabaseHelper.removeDBStringWrapper(points[r]);
					
				labels[r] = DatabaseHelper.getArrayValues(valueTable, labelCol, r );
				labels[r] = DatabaseHelper.removeDBStringWrapper(labels[r]);
					
				values[r] = DatabaseHelper.getArrayValues(valueTable, valueCol, r );
				values[r] = DatabaseHelper.removeDBStringWrapper(values[r]);					
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
				
				widgetPoints.put(btnOption, new ControlPoint(PointType.dio.toString(), scsEnvId, address, values[i]));

				initCondGLAndWidget.put(address+":"+points[i]+".initCondGL", btnOption);
			
			}
			
		}
		
//		widgetBoxes[row] = new VerticalPanel();
//		widgetBoxes[row].addStyleName("project-gwt-panel-inspector-"+tagname+"-control");
		widgetBoxes[row].clear();
		widgetBoxes[row].add(inlineLabels[row]);
		widgetBoxes[row].add(controlboxes[row]);
		
		logger.end(className, function);
		
		return widgetBoxes[row];	
	}
	
	private final String strPrefix	= "Executing command : ";
	private final String strInit	= strPrefix+" "+"Initing Command";
	private final String strSending	= strPrefix+" "+"Sending Command";
	private final String strSucceed	= strPrefix+" "+"Succeed";
	private final String strFailed	= strPrefix+" "+"Failed";
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		
		logger.begin(className, function);
		
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
							logger.warn(className, function, "uiButtonToggle IS NULL");
						}
					} else {
						logger.warn(className, function, "value IS NULL");
					}
				}				
			}
		}
		
		updateMsgBox(dbvalues);
		
		logger.end(className, function);
	}
	
//	private LinkedList<String> controldios = new LinkedList<String>();
	
	private CtlMgr ctlMgr = null;
	private Observer observer = null;
	private Subject controlMgrSubject = null;
	@Override
	public void connect() {
		final String function = "connect";
		
		logger.begin(className, function);
			
		String[] dbaddresses = null;
		{
			ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				String dbaddress = this.addresses[i];
				
				String point = DatabaseHelper.getPoint(dbaddress);
				PointType pointType = DatabaseHelper.getPointType(point);
				
				if ( PointType.dio == pointType ) {
					for ( String attribute : dioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( PointType.aio == pointType ) {
					for ( String attribute : aioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( PointType.sio == pointType ) {
					for ( String attribute : sioStaticAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				}
			}
			dbaddresses = dbaddressesArrayList.toArray(new String[0]);
		}
		
		String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		String api = "multiReadValue";
		database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
			
			@Override
			public void update(String key, String[] value) {
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
						
//						String message = subject.getState();
//						
//						String msgPrefix	= "Executing command : ";
//						String msgSending	= "Sending Command";
//						String msgSucceed	= "Succeed";
//						String msgFailed	= "Failed";
//						if ( message.equals("COMMAND SENT") 
//								|| message.equals("\"SendIntCommand sent\"") 
//								|| message.equals("\"SendStringCommand sent\"") 
//								|| message.equals("\"SendFloatCommand sent\"") ) {
//							message = msgPrefix + msgSending;
//						} else if ( message.equals("\"command executed successfully\"") ) {
//							message = msgPrefix + msgSucceed;
//						} else if ( message.equals("COMMAND FAILED") ) {
//							message = msgPrefix + msgFailed;
//						}
//						
//						messageBoxEvent.setMessage(message);
					}
				}
			};
			
			observer.setSubject(controlMgrSubject);			
		}
		
		logger.end(className, function);
	}
	
	void createDioInitConds() {
		final String function = "createDioInitConds";
		
		logger.begin(className, function);
		
		int dovnameCol = 0, labelCol = 1, valueCol = 2;
		
		LinkedList<String> initCondGLList = new LinkedList<String>();
		for ( int x = 0 ; x < this.addresses.length ; ++x ) {
			String address = this.addresses[x];

			String valueTable = DatabaseHelper.getAttributeValue(address, PointName.valueTable.toString(), dbvalues);
			logger.debug(className, function, "valueTable[{}]", valueTable);
			
			if ( null !=  valueTable ) {
				int numOfRow = 12;

				String points[] = new String[numOfRow];
				String labels[] = new String[numOfRow];
				String values[] = new String[numOfRow];
				for( int r = 0 ; r < numOfRow ; ++r ) {
						
					points[r] = DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
					points[r] = DatabaseHelper.removeDBStringWrapper(points[r]);
						
					labels[r] = DatabaseHelper.getArrayValues(valueTable, labelCol, r );
					labels[r] = DatabaseHelper.removeDBStringWrapper(labels[r]);
						
					values[r] = DatabaseHelper.getArrayValues(valueTable, valueCol, r );
					values[r] = DatabaseHelper.removeDBStringWrapper(values[r]);					
				}
				
				for ( int i = 0 ; i < points.length ; ++i ) {
					
					if ( labels[i].length() == 0  ) break;
					
					String pointaddress = address+":"+points[i];
					initCondGLList.add(pointaddress+".initCondGL");
				}

				addressAndInitCongGL.put(address, initCondGLList.toArray(new String[0]));
			} else {
				logger.warn(className, function, "valueTable IS NULL");
			}
			
		}
		
		logger.end(className, function);
	}
	
	void connectDIOinitConds() {
		final String function = "connectDIOinitConds";
		
		logger.begin(className, function);
		
		// Read dynamic
		{
			final String functionEmb = "multiReadValue";
			logger.begin(className, functionEmb);
			
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
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}				
			}

			database.subscribe(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
			logger.end(className, function);
		}
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		{
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
		}
		logger.beginEnd(className, function);
	}
	
	private Button btnExecute = null;
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel vpCtrls = null;
	private DockLayoutPanel basePanel = null;
	private Panel rootPanel = null;
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		vpCtrls  = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setText("Execute");
		btnExecute.addStyleName("project-gwt-button-inspector-"+tagname+"-execute");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.begin(className, function);
				
				onButton(event);
				
				logger.end(className, function);
			}
		});
//		btnExecute.setEnabled(false);
		
		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logger.begin(className, function);
				
				onButton(event);
				
				logger.end(className, function);
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
				logger.begin(className, function);
				
				onButton(event);
				
				logger.end(className, function);
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
		
		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-"+tagname+"-inspector");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);

		// Auto close handle
		rootPanel = new FocusPanel();
		rootPanel.add(basePanel);
		((FocusPanel)rootPanel).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ( null != event ) {
					if ( null != uiInspectorTabClickEvent ) uiInspectorTabClickEvent.onClick(); 
				} else {
					logger.info(className, function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		logger.end(className, function);
		
	}
	
	@Override
	public Panel getMainPanel() {
		return rootPanel;
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
		final String function = "getActivateWidget";
		
		logger.begin(className, function);
		
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
						logger.warn(className, function, "widget IS NOT SUPPORTED");
					}
				} else {
					logger.warn(className, function, "btn IS NULL");
				}
			}
		}
		logger.end(className, function);
		return target;
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
		int byPassInitCond = 0;
		int byPassRetCond = 0;
		int sendAnyway = 0;
		
		Button btn = (Button)event.getSource();
		if ( btn instanceof UIButtonToggle ) {
			
			logger.info(className, function, "Button IS UIButtonToggle");
			
			Widget[] targetGroups = getSelectedGroup(btn);
			if ( null != targetGroups ) {
				toggleButtonGroup(btn, targetGroups);
			}
			
		} else if ( btn instanceof Button ) {
			logger.info(className, function, "Button IS Button");
			
			if ( btnExecute == btn ) {
				
				logger.info(className, function, "btn IS btnExecute");
				
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
							
							logger.info(className, function, "sPoint[{}]", sPoint);
							logger.info(className, function, "sScsEnvId[{}]", sScsEnvId);
							logger.info(className, function, "sDbAddress[{}]", sDbAddress);
							logger.info(className, function, "sValue[{}]", sValue);
							
							subscribe(sDbAddress+PointName.execStatus.toString());
							
							String alias = "<alias>";
							if ( ! sDbAddress.startsWith(alias) ) sDbAddress = alias + sDbAddress;
							
							if ( 0 == sPoint.compareTo("dio") ) {
								
								logger.info(className, function, "controlPoint.dbaddress[{}] Integer [{}]", sDbAddress, Integer.parseInt(sValue));
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Integer.valueOf(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo("aio") ) {
								
								logger.info(className, function, "controlPoint.dbaddress[{}] Float [{}]", sDbAddress, Float.parseFloat(sValue));
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, Float.parseFloat(sValue), byPassInitCond, byPassRetCond, sendAnyway);
								
							} else if ( 0 == sPoint.compareTo("sio") ) {
								
								logger.info(className, function, "controlPoint.dbaddress[{}] String [{}]", sDbAddress, sValue);
								
								ctlMgr.sendControl(sScsEnvId, new String[]{sDbAddress}, sValue, byPassInitCond, byPassRetCond, sendAnyway);

							} else {
								logger.warn(className, function, "INVALID controlPoint.point");
							}
						} else {
							logger.warn(className, function, "controlPoint IS NULL");
						}
					} else {
						logger.warn(className, function, "controlPoint NOT CONTAIN the Key");
					}
				} else {
					logger.warn(className, function, "controlPoint widget IS NULL");
				}
			} else if ( btn == btnUp || btn == btnDown ) {
				if ( btn == btnUp) {
					--pageIndex;
				} else if ( btn == btnDown ) {
					++pageIndex;
				}
				updatePager();
				updateLayout();
				updateValue(true);
			}
		}
		logger.end(className, function);
	}

	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}
	
	private UIInspectorTabClickEvent uiInspectorTabClickEvent = null;
	@Override
	public void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent) {
		this.uiInspectorTabClickEvent = uiInspectorTabClickEvent;
	}
	
	private void updateMsgBox(HashMap<String, String> keyAndValue) {
		final String function = "updateMsgBox";
		logger.begin(className, function);
		for ( Entry<String, String> entry : keyAndValue.entrySet() ) {
			String key = entry.getKey();
			String value = entry.getValue();
			if ( null != key && null != value ) {
				if ( key.endsWith(PointName.execStatus.toString()) ) {
					logger.debug(className, function, "key[{}] value[{}]", key, value);
					if ( null != value && value.length() > 0 ) {
						int intValue = Integer.parseInt(value);
						String msg = null;
						switch (intValue) {
						case 0: 
						case 1: msg = strInit;		break;
						case 2: msg = strSending;	break;
						case 3: msg = strSucceed;
								unsubscribe(key);
								break;
						case 4:
						case 5:
						case 6: msg = strFailed;
								unsubscribe(key);
								break;
						}
						if ( null != messageBoxEvent ) messageBoxEvent.setMessage(msg);
					}
				}
			}
		}
		logger.end(className, function);
	}
	
	private void subscribe(String address) {
		final String function = "subscribe";
		logger.begin(className, function);
		logger.info(className, function, "address[{}]", address);
		String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + address;
		logger.info(className, function, "clientKey[{}]", clientKey);
		database.subscribe(clientKey, new String[]{address}, new DatabaseEvent() {
			@Override
			public void update(String key, String[] value) {
				// TODO Auto-generated method stub
				
			}
		});
		logger.end(className, function);
	}
	private void unsubscribe(String address) {
		final String function = "unsubscribe";
		logger.begin(className, function);
		logger.info(className, function, "address[{}]", address);
		String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + address;
		logger.info(className, function, "clientKey[{}]", clientKey);
		database.unSubscribe(clientKey);
		logger.end(className, function);
	}
	
	@Override
	public void setDatabase(Database db) {
		database = db;
	}
}
