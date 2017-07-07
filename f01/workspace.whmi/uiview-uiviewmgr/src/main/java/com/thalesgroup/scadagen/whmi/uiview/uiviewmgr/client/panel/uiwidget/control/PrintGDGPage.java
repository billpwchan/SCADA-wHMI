package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.control;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.config.configenv.client.JSONUtil;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

public class PrintGDGPage {
	
	private final String className = UIWidgetUtil.getClassSimpleName(PrintGDGPage.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	ScsGenericDataGridView gridView = null;
	UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	String printDataDebugId	= null;
	String printDataColumns	= null;
	String printDataIndexs	= null;
	String printDataAttachement = null;
	
	int printDataStart = 0;
	int printDataLength = 8000;
	
	int printDataReceviedWait = 5000;
	int printDataWalkthoughWait = 5000;
	
	public PrintGDGPage(ScsGenericDataGridView gridView, UIEventActionProcessor_i uiEventActionProcessor_i) {
		this.gridView=gridView;
		this.uiEventActionProcessor_i=uiEventActionProcessor_i;
	}

	public void setGDGParameter(String printDataDebugId) {
		this.printDataDebugId=printDataDebugId;
	}
	public void setPageContentParameter(String printDataColumns, String printDataIndexs) {
		this.printDataColumns=printDataColumns;
		this.printDataIndexs=printDataIndexs;
	}
	public void setPageRangeParameter(int printDataStart, int printDataLength) {
		this.printDataStart=printDataStart;
		this.printDataLength=printDataLength;
	}
	public void setTimerParameter(int printDataReceviedWait, int printDataWalkthoughWait, String printDataIndexs, String printDataAttachement) {
		this.printDataReceviedWait=printDataReceviedWait;
		this.printDataWalkthoughWait=printDataWalkthoughWait;
	}
	public void setDownloadParameter(String printDataAttachement) {
		this.printDataAttachement=printDataAttachement;
	}
	
	private int startIndexOrg = 0, pageSizeOrg = 32;
	public void setPageSize(int startIndex, int pageSize, boolean save) {
		final String function = "setPageSize";
		logger.begin(className, function);
		logger.debug(className, function, "gridView startIndex[{}] pageSize[{}]", startIndex, pageSize);
		if ( null != gridView.getPager() ) {
			if ( null != gridView.getPager().getDisplay() ) {
				if ( save ) {
					startIndexOrg = gridView.getPager().getDisplay().getVisibleRange().getStart();
					pageSizeOrg = gridView.getPager().getDisplay().getVisibleRange().getLength();
					logger.debug(className, function, "gridView startIndexOrg[{}] pageSizeOrg[{}]", startIndexOrg, pageSizeOrg);
				}
				gridView.getPager().getDisplay().setVisibleRange(startIndex, pageSize);
			} else {
				logger.warn(className, function, "gridView.getPager().getDisplay() IS NULL");
			}
		} else {
			logger.warn(className, function, "gridView.getPager() IS NULL");
		}
		logger.end(className, function);
	}
	
	public void printCurPage() {
		final String function = "printCurPage";
		logger.begin(className, function);
		String strPrintDataColumns [] = printDataColumns.split(",");
		for ( int i = 0 ; i < strPrintDataColumns.length ; ++i ) {
			strPrintDataColumns[i] = TranslationMgr.getInstance().getTranslation(strPrintDataColumns[i]);
		}
		JSONArray jsonPrintDataColumns = JSONUtil.convertStringsToJSONArray(strPrintDataColumns);
		 
		String strPrintDataIndexs [] = printDataIndexs.split(",");
		int intPrintDataIndexs [] = JSONUtil.convertStringToInts(strPrintDataIndexs);
		JSONArray jsonPrintDataIndexs = JSONUtil.convertIntsToJSONArray(intPrintDataIndexs);
	 
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("PrintDataDebugId", new JSONString(printDataDebugId));
	    jsonObject.put("PrintDataAttachement", new JSONString(printDataAttachement));
	    jsonObject.put("PrintDataColumns", jsonPrintDataColumns);
	    jsonObject.put("PrintDataIndexs", jsonPrintDataIndexs);

	    String jsonstring = jsonObject.toString();
	    
    	logger.warn(className, function, "jsonstring[{}]", jsonstring);

    	UIEventAction uiEventAction2 = new UIEventAction();
		uiEventAction2.setParameter(UIActionEventAttribute.OperationType.toString(), "action");
		uiEventAction2.setParameter(UIActionEventAttribute.OperationAction.toString(), "js");
		uiEventAction2.setParameter(ActionAttribute.OperationString1.toString(), "CallJSByGWT");
		uiEventAction2.setParameter(ActionAttribute.OperationString2.toString(), "PrintCurPageCsv");
		uiEventAction2.setParameter(ActionAttribute.OperationString3.toString(), jsonstring);
		uiEventActionProcessor_i.executeAction(uiEventAction2, null);
		logger.debug(className, function, "executeAction uiEventAction");
   
    	logger.end(className, function);
	}
	
	public void setPageSizePrint() {
		final String function = "setPageSizePrint";		
		logger.begin(className, function);
		
		setPageSize(printDataStart, printDataLength, true);

		logger.debug(className, function, "printCurPage... printDataReceviedWait[{}]", printDataReceviedWait);
		new Timer() {
			public void run() {
				logger.debug(className, function, "printCurPage...");
				printCurPage();
				
				logger.debug(className, function, "setPageSize... printDataWalkthoughWait[{}]", printDataWalkthoughWait);
				new Timer() {
					public void run() {
						logger.debug(className, function, "setPageSize...");
						setPageSize(startIndexOrg, pageSizeOrg, false);
					}
				}.schedule(printDataWalkthoughWait);

			}
		}.schedule(printDataReceviedWait);

		logger.end(className, function);
	}
}
