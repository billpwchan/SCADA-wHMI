package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.control;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.config.configenv.client.JSONUtil;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.ScsGenericDataGridView;

public class PrintGDGPage {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private final static String STR_SPILTER = ",";
	
	ScsGenericDataGridView gridView = null;
	UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	String printDataDebugId	= null;
	String printDataColumns	= null;
	String printDataIndexs	= null;
	String printDataDivIndexs	= null;
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
	public void setTimerParameter(int printDataReceviedWait, int printDataWalkthoughWait) {
		this.printDataReceviedWait=printDataReceviedWait;
		this.printDataWalkthoughWait=printDataWalkthoughWait;
	}
	public void setDownloadParameter(String printDataAttachement) {
		this.printDataAttachement=printDataAttachement;
	}
	public void setDivIndexsParameter(String printDataDivIndexs) {
		this.printDataDivIndexs=printDataDivIndexs;
	}	
	
	private int startIndexOrg = 0, pageSizeOrg = 32;
	public void setPageSize(int startIndex, int pageSize, boolean save) {
		final String function = "setPageSize";
		logger.begin(function);
		logger.debug(function, "gridView startIndex[{}] pageSize[{}]", startIndex, pageSize);
		if ( null != gridView.getPager() ) {
			if ( null != gridView.getPager().getDisplay() ) {
				if ( save ) {
					startIndexOrg = gridView.getPager().getDisplay().getVisibleRange().getStart();
					pageSizeOrg = gridView.getPager().getDisplay().getVisibleRange().getLength();
					logger.debug(function, "gridView startIndexOrg[{}] pageSizeOrg[{}]", startIndexOrg, pageSizeOrg);
				}
				gridView.getPager().getDisplay().setVisibleRange(startIndex, pageSize);
			} else {
				logger.warn(function, "gridView.getPager().getDisplay() IS NULL");
			}
		} else {
			logger.warn(function, "gridView.getPager() IS NULL");
		}
		logger.end(function);
	}
	
	public void printCurPage() {
		final String function = "printCurPage";
		logger.begin(function);
		
		JSONArray jsonPrintDataColumns = null;
		logger.debug(function, "printDataColumns[{}]", printDataColumns);
		if ( null != printDataColumns ) {
			String strPrintDataColumns [] = printDataColumns.split(STR_SPILTER);
			
			logger.debug(function, "strPrintDataColumns.length[{}]", strPrintDataColumns.length);
			for ( int i = 0 ; i < strPrintDataColumns.length ; ++i ) {
				strPrintDataColumns[i] = TranslationMgr.getInstance().getTranslation(strPrintDataColumns[i]);
			}
			jsonPrintDataColumns = JSONUtil.convertStringsToJSONArray(strPrintDataColumns);
		}

		// Data Index
		logger.debug(function, "printDataIndexs[{}]", printDataIndexs);
		String strPrintDataIndexs [] = printDataIndexs.split(STR_SPILTER);
		
		logger.debug(function, "strPrintDataIndexs.length[{}]", strPrintDataIndexs.length);
		int intPrintDataIndexs [] = JSONUtil.convertStringToInts(strPrintDataIndexs);
		JSONArray jsonPrintDataIndexs = JSONUtil.convertIntsToJSONArray(intPrintDataIndexs);
	 
		// Data Div Index
		JSONArray jsonPrintDataDivIndexs = null;
		logger.debug(function, "printDataDivIndexs[{}]", printDataDivIndexs);
		if ( null != printDataDivIndexs ) {
			String strPrintDataDivIndexs [] = printDataDivIndexs.split(STR_SPILTER);
			
			logger.debug(function, "strPrintDataDivIndexs.length[{}]", strPrintDataDivIndexs.length);
			int intPrintDataDivIndexs [] = JSONUtil.convertStringToInts(strPrintDataDivIndexs);
			jsonPrintDataDivIndexs = JSONUtil.convertIntsToJSONArray(intPrintDataDivIndexs);
		}		
		
		logger.debug(function, "printDataDebugId[{}]", printDataDebugId);
		logger.debug(function, "printDataAttachement[{}]", printDataAttachement);
		
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("PrintDataDebugId", new JSONString(printDataDebugId));
	    jsonObject.put("PrintDataAttachement", new JSONString(printDataAttachement));
	    jsonObject.put("PrintDataColumns", jsonPrintDataColumns);
	    jsonObject.put("PrintDataIndexs", jsonPrintDataIndexs);
	    jsonObject.put("PrintDataDivIndexs", jsonPrintDataDivIndexs);


	    String jsonstring = jsonObject.toString();
	    
    	logger.debug(function, "jsonstring[{}]", jsonstring);

    	UIEventAction uiEventAction2 = new UIEventAction();
		uiEventAction2.setParameter(UIActionEventAttribute.OperationType.toString(), "action");
		uiEventAction2.setParameter(UIActionEventAttribute.OperationAction.toString(), "js");
		uiEventAction2.setParameter(ActionAttribute.OperationString1.toString(), "CallJSByGWT");
		uiEventAction2.setParameter(ActionAttribute.OperationString2.toString(), "PrintCurPageCsv");
		uiEventAction2.setParameter(ActionAttribute.OperationString3.toString(), jsonstring);
		uiEventActionProcessor_i.executeAction(uiEventAction2, null);
		logger.debug(function, "executeAction uiEventAction");
   
    	logger.end(function);
	}
	
	public void setPageSizePrint() {
		final String function = "setPageSizePrint";		
		logger.begin(function);
		
		setPageSize(printDataStart, printDataLength, true);

		logger.debug(function, "printCurPage... printDataReceviedWait[{}]", printDataReceviedWait);
		new Timer() {
			public void run() {
				logger.debug(function, "printCurPage...");
				printCurPage();
				
				logger.debug(function, "setPageSize... printDataWalkthoughWait[{}]", printDataWalkthoughWait);
				new Timer() {
					public void run() {
						logger.debug(function, "setPageSize...");
						setPageSize(startIndexOrg, pageSizeOrg, false);
					}
				}.schedule(printDataWalkthoughWait);

			}
		}.schedule(printDataReceviedWait);

		logger.end(function);
	}
}
