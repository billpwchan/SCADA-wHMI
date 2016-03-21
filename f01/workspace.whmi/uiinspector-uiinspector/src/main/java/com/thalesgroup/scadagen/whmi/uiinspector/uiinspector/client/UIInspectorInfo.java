package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccessEvent;

public class UIInspectorInfo implements WrapperScsRTDBAccessEvent {
	
	private static Logger logger = Logger.getLogger(UIInspectorInfo.class.getName());
	
	public static final String strCSSStatusGreen	= "project-gwt-inlinelabel-inspector-info-status-green";
	public static final String strCSSStatusRed		= "project-gwt-inlinelabel-inspector-info-status-red";
	public static final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector-info-status-blue";
	public static final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector-info-status-grey";
	
	public UIInspectorInfo () {
		
	}
	
	private String strDot = ".";
	private String strColumn = ":";
	private String strUnderscore = "_";	
	private String strReadValue = "ReadValue";
	
	private String strB001 = "B001";

	private String strAlias = ":SITE1:B001:F001:ACCESS:DO001";
	
	private String strLabel				= "label";
	private String strValue				= "value";
	private String strValueTable		= "dal.valueTable";
	private String strValidity			= "validity"; // 0=invalid, 1=valid
	private String strValueAlarmVector	= "dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
	private String strForcedStatus		= "dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus
	
	private String attributes[]			= new String[] {strLabel, strValueTable, strValue, strValidity, strValueAlarmVector, strForcedStatus};
	
	private String strDciFault			= "dciFault";
	private String strDciIntrusion		= "dciIntrusion";
	private String strDciLocked			= "dciLocked";
	private String strDciOpenClose		= "dciOpenClose";
	
	private String points[]				= new String[] {strDciFault, strDciIntrusion, strDciLocked, strDciOpenClose};

	private WrapperScsRTDBAccess wrapperScsRTDBAccess;
	public void createReadRequest() {
		
		logger.log(Level.FINE, "createReadRequest Begin");
		
		wrapperScsRTDBAccess = new WrapperScsRTDBAccess(this);
		
		logger.log(Level.FINE, "createReadRequest End");
	}
	
	
	public void setReadRequestCache () {
		
		logger.log(Level.FINE, "setReadRequestCache Begin");
		
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciFault + strDot + strLabel,			new String[]{"Failure"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciIntrusion + strDot + strLabel,		new String[]{"instrusion"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciLocked + strDot + strLabel,			new String[]{"lock state"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciOpenClose + strDot + strLabel,		new String[]{"State"});
		
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciFault + strColumn + strValueTable,		new String[]{"[[1,1836216174,973106273,825241670,1128481082,978539333,329,1627409920,24940,65536,1852375040,1969317408],[normal,in fault,,,,,,,,,,],[1,1818296398,97,256,544106752,1819631974,116,-1241513984,5391653,131177,4259840,21117489],[N,A,,,,,,,,,,],[0,1,1713401449,1953264993,0,632684544,1761628741,512,822100224,82490,0,1090519040]]"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciIntrusion + strColumn + strValueTable,	new String[]{"[[1,1836216174,1124101217,978539333,842024009,1630419509,364,1946177024,25960,65536,1920204800,1701867625],[normal,tripped,,,,,,,,,,],[1,1752432718,101,256,1769108480,1684369520,5424384,981467257,1163151699,80433,4259840,20000816],[N,A,,,,,,,,,,],[0,1,1885958772,6579568,2030064325,1396342784,826627145,314,805323008,78128,0,1140850688]]"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciLocked + strColumn + strValueTable,		new String[]{"[[1,1869377141,1684368227,1946177024,25960,65536,256,1694518784,100,65536,1869348864,1684368227],[unlocked,locked,,,,,,,,,,],[1,1684340814,0,256,1668246528,6579563,4259840,20000816,0,65536,5111808,23146042],[N,N,,,,,,,,,,],[0,1,1801678700,25701,805323008,78128,0,256,973098496,90414,0,1694498816]]"});
		wrapperScsRTDBAccess.cachePut(strAlias + strColumn + strDciOpenClose + strColumn + strValueTable,	new String[]{"[[1,1769369453,26478,1694518784,100,65536,512,1677743104,0,65536,1886322688,1684368997],[moving,opened,closed,discordant,,,,,,,,],[2,6553684,0,256,1701867264,6579566,5111808,23146042,0,65536,5111808,33554432],[T,N,N,A,,,,,,,,],[0,1,1852141679,25701,973098496,90414,0,256,19968,131072,65536,1818427392]]]</code>111808,33554432],[T,N,N,A,,,,,,,,],[0,1,1852141679,25701,973098496,90414,0,256,19968,131072,65536,1818427392]]]</code>1808,23146042,0,65536,5111808,33554432],[T,N,N,A,,,,,,,,],[0,1,1852141679,25701,973098496,90414,0,256,19968,131072,65536,1818427392]]"});		
		
					
		wrapperScsRTDBAccess.readValueRequestCache(
											 strReadValue + strUnderscore + strDciFault + strUnderscore + strLabel
											 , strB001
											 , strAlias + strColumn + strDciFault + strDot + strLabel);
		wrapperScsRTDBAccess.readValueRequestCache(
						  					  strReadValue + strUnderscore + strDciFault + strUnderscore + strValueTable
						  					 , strB001
											, strAlias + strColumn + strDciFault + strColumn + strValueTable);
		
		
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strLabel
											, strB001
											, strAlias + strColumn + strDciIntrusion + strDot + strLabel);
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strValueTable
											, strB001
											, strAlias + strColumn + strDciIntrusion + strColumn + strValueTable);
		
		
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strLabel
											, strB001
											, strAlias + strColumn + strDciLocked + strDot + strLabel);
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strValueTable
											, strB001
											, strAlias + strColumn + strDciLocked + strColumn + strValueTable);
		
		
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strLabel
											, strB001
											, strAlias + strColumn + strDciOpenClose + strDot + strLabel);
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strValueTable
											, strB001
											, strAlias + strColumn + strDciOpenClose + strColumn + strValueTable);

		logger.log(Level.FINE, "setReadRequestCache End");
	}
	
	public void setReadRequest (int index) {
		
		logger.log(Level.FINE, "setReadRequest Begin");
		logger.log(Level.FINE, "setReadRequest index["+index+"]");
		
		int i = 3;
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciFault + strUnderscore + strLabel
//											, strB001
//											, strAlias + strColumn + strDciFault + strDot + strLabel);
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciFault + strUnderscore + strValueTable
//											, strB001
//											, strAlias + strColumn + strDciFault + strColumn + strValueTable);		
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciFault + strUnderscore + strValue
											, strB001
											, strAlias + strColumn + strDciFault + strDot + strValue);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciFault + strUnderscore + strValidity
											, strB001
											, strAlias + strColumn + strDciFault + strDot + strValidity);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciFault + strUnderscore + strValueAlarmVector
											, strB001
											, strAlias + strColumn + strDciFault + strColumn + strValueAlarmVector);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciFault + strUnderscore + strForcedStatus
											, strB001
											, strAlias + strColumn + strDciFault + strColumn + strForcedStatus);

		
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strLabel
//											, strB001
//											, strAlias + strColumn + strDciIntrusion + strDot + strLabel);
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strValueTable
//											, strB001
//											, strAlias + strColumn + strDciIntrusion + strColumn + strValueTable);		
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strValue
											, strB001
											, strAlias + strColumn + strDciIntrusion + strDot + strValue);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strValidity
											, strB001
											, strAlias + strColumn + strDciIntrusion + strDot + strValidity);	
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strValueAlarmVector
											, strB001
											, strAlias + strColumn + strDciIntrusion + strColumn + strValueAlarmVector);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciIntrusion + strUnderscore + strForcedStatus
											, strB001
											, strAlias + strColumn + strDciIntrusion + strColumn + strForcedStatus);

		
		
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strLabel
//											, strB001
//											, strAlias + strColumn + strDciLocked + strDot + strLabel);
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strValueTable
//											, strB001
//											, strAlias + strColumn + strDciLocked + strColumn + strValueTable);		
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strValue
											, strB001
											, strAlias + strColumn + strDciLocked + strDot + strValue);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strValidity
											, strB001
											, strAlias + strColumn + strDciLocked + strDot + strValidity);	
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strValueAlarmVector
											, strB001
											, strAlias + strColumn + strDciLocked + strColumn + strValueAlarmVector);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciLocked + strUnderscore + strForcedStatus
											, strB001
											, strAlias + strColumn + strDciLocked + strColumn + strForcedStatus);

		
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strLabel
//											, strB001
//											, strAlias + strColumn + strDciOpenClose + strDot + strLabel);
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strValueTable
//											, strB001
//											, strAlias + strColumn + strDciOpenClose + strColumn + strValueTable);		
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strValue
											, strB001
											, strAlias + strColumn + strDciOpenClose + strDot + strValue);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strValidity
											, strB001
											, strAlias + strColumn + strDciOpenClose + strDot + strValidity);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strValueAlarmVector
											, strB001
											, strAlias + strColumn + strDciOpenClose + strColumn + strValueAlarmVector);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strDciOpenClose + strUnderscore + strForcedStatus
											, strB001
											, strAlias + strColumn + strDciOpenClose + strColumn + strForcedStatus);

		
		logger.log(Level.FINE, "setReadRequest End");
	}
	
	
	private String strLabelValue[];
	private String strValueTableValue[];
	private String strValueValue[];
	private String strValueValueName[];
	private String strValueAlarmVectorValue[];
	private String strValidityValue[];
	private String strForcedStatusValue[];
	public void initVariable() {
		logger.log(Level.FINE, "initVariable Begin");
		
		int numOfPoint = points.length;
		
		logger.log(Level.FINE, "initVariable numOfPoint["+numOfPoint+"]");
		
		strLabelValue				= new String[numOfPoint];
		strValueTableValue			= new String[numOfPoint];
		strValueValue				= new String[numOfPoint];
		strValueValueName			= new String[numOfPoint];
		strValueAlarmVectorValue	= new String[numOfPoint];
		strValidityValue			= new String[numOfPoint];
		strForcedStatusValue		= new String[numOfPoint];
		for(int i=0;i<points.length;++i) {
			strLabelValue[i]				= " - ";
			strValueTableValue[i]			= "1,normal,1,N,0,1,in fault,2,A,1";
			strValueValue[i]				= " - ";
			strValueValueName[i]			= null;
			strValueAlarmVectorValue[i]		= "-1,0,0,1,0,0";
			strValidityValue[i]				= "0";
			strForcedStatusValue[i]			= "0";
		}
		
		logger.log(Level.FINE, "initVariable End");
	}
	
	@Override
	public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
		
		logger.log(Level.SEVERE, "setReadResult Begin");

		if ( null != values && values.length > 0 ) {
			for ( String value: values ) {
				
				logger.log(Level.SEVERE, "setReadResult key["+key+"] value["+value+"] errorCode["+errorCode+"] errorMessage["+errorMessage+"]");
				
				logger.log(Level.FINE, "setReadResult BF value["+value+"]");
				
				value = value.replaceAll("\"", "");
				
				logger.log(Level.FINE, "setReadResult AF value["+value+"]");
				
				for ( int x = 0 ; x < points.length ; ++x ) {
					String point = points[x];
					for ( int y = 0 ; y < attributes.length ; ++y ) {
						String attribute = attributes[y];
						
						if ( 0 == key.compareTo(strReadValue + strUnderscore + point + strUnderscore + attribute) ) {
							if ( 0 == attribute.compareTo(strLabel) ) {
								strLabelValue[x]			= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strLabelValue["+strLabelValue[x]+"]");
							} else if ( 0 == attribute.compareTo(strValueTable) ) {
								strValueTableValue[x]		= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strValueTableValue["+strValueTableValue[x]+"]");
							} else if ( 0 == attribute.compareTo(strValue) ) {
								strValueValue[x]			= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strValueValue["+strValueValue[x]+"]");
							} else if ( 0 == attribute.compareTo(strValueAlarmVector) ) {
								strValueAlarmVectorValue[x]	= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strValueAlarmVectorValue["+strValueAlarmVectorValue[x]+"]");
							} else if ( 0 == attribute.compareTo(strValidity) ) {
								strValidityValue[x]			= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strValidityValue["+strValidityValue[x]+"]");
							} else if ( 0 == attribute.compareTo(strForcedStatus) ) {
								strForcedStatusValue[x]		= value;
								logger.log(Level.FINE, "setReadResult x["+x+"] strForcedStatusValue["+strForcedStatusValue[x]+"]");
							}
							
						}
						
					}
				}
			}
		}
		
		for ( int i=0;i<points.length;++i ) {
			lblAttibuteLabel[i].setText(strLabelValue[i]);
			
			for( int x = 0 ; x < 12 ; ++x ) {
				String v = getArrayValues(strValueTableValue[i], 4, x );
				logger.log(Level.FINE, "getArrayValues x["+x+"]");
				logger.log(Level.FINE, "getArrayValues v["+v+"] == strValueTableValue[i]["+strValueTableValue[i]+"]");
				if ( 0 == v.compareTo(strValueValue[i]) ) {
					strValueValueName[i] = getArrayValues(strValueTableValue[i], 1, x );
					break;
				}
			}
			
			logger.log(Level.SEVERE, "getArrayValues strValueValue["+strValueValue+"] == strValueValueName[i]["+strValueValueName[i]+"]");
			
			if ( null != strValueValueName[i] ) {
				txtAttribute[i].setText(strValueValueName[i]);	
			} else {
				txtAttribute[i].setText(strValueValue[i]);	
			}
			
			
//			String strColor = getColorCode(strValueAlarmVectorValue[i], strValidityValue[i], strForcedStatusValue[i]);
//			txtAttibuteColor[i].getElement().getStyle().setColor(strColor);
//			txtAttibuteColor[i].getElement().getStyle().setBackgroundColor(strColor);
			
			String strColorCSS = getColorCSS(strValueAlarmVectorValue[i], strValidityValue[i], strForcedStatusValue[i]);
			txtAttibuteColor[i].setStyleName(strColorCSS);
			
			logger.log(Level.FINE, "setReadResult i["+i+"] strLabelValue["+strLabelValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strValueTableValue["+strValueTableValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strValueValue["+strValueValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strValueAlarmVectorValue["+strValueAlarmVectorValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strValidityValue["+strValidityValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strForcedStatusValue["+strForcedStatusValue[i]+"]");
			logger.log(Level.FINE, "setReadResult i["+i+"] strColor["+strColorCSS+"]");
		}
		
		logger.log(Level.SEVERE, "setReadResult End");

	}
	
	private String getArrayValues(String string, int x, int y) {
		//String str = "[1,2,3,4,5,6,7,8,9],[1.0,1.2,1.3],[T,N,A],[1,2,3,4,5,7],[1.213123123,2.34509347530],[123],[123]";
		String str = null;
		
		
		logger.log(Level.FINE, "getArrayValues Begin");

		logger.log(Level.FINE, "getArrayValues string["+string+"]");
		logger.log(Level.FINE, "getArrayValues x["+x+"] y["+y+"]");
				
		if (null != string && string.length() > 0) {
			if (string.charAt(0) == '[')
				string = string.substring(1);
			if (string.charAt(string.length() - 1) == ']')
				string = string.substring(0, string.length() - 1);
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && x < strs.length) {
				String s = strs[x];
				
				logger.log(Level.FINE, "getArrayValues s["+s+"]");
				
				//for (String s : strs) {
					//System.out.println("s [" + s + "]");
					String str2s[] = s.split(",");
					if ( str2s.length > 0 && y < str2s.length ) {
						str = str2s[y];
						
						logger.log(Level.FINE, "getArrayValues str["+str+"]");
						
						//for (String s2 : str2s) {
						//	System.out.println("s2 [" + s2 + "]");
						//}						
					}

				//}
			} else {
				// Invalid str length or index
			}
		}
		
		logger.log(Level.FINE, "getArrayValues End");

		
		return str;
	}
	
	private String getColorCSS(String alarmVector, String validity, String forcedStatus) {
		
		logger.log(Level.FINE, "getColorCSS Begin");
		
		String colorCSS	= strCSSStatusGrey;
		int intAlarmVector	= 0;
		int intValidity		= 0;
		int intForcedStatus	= 0;
		try {
			intAlarmVector = Integer.parseInt(alarmVector.split(",")[1]);
			intValidity = Integer.parseInt(validity);
			intForcedStatus = Integer.parseInt(forcedStatus);
		} catch ( NumberFormatException e ) {
			logger.log(Level.FINE, "getColorCSS NumberFormatException["+e.toString()+"]");
		}
		
		int intMO = 2, intAI = 8, intSS = 512;
		
		// 2=MO, AI=8, 512=SS
		if ( (intForcedStatus & intMO) == intMO || (intForcedStatus & intAI) == intAI || (intForcedStatus & intSS) == intSS ) {
			colorCSS = strCSSStatusBlue;
			
		// 0=invalid, 1=valid	
		} else if ( intValidity == 0 ) {
			colorCSS = strCSSStatusGrey;
			
		// 0=normal, 1=alarm
		} else if ( intAlarmVector == 1 ) {
			colorCSS = strCSSStatusRed;
			
		// Grey
		} else {
			colorCSS = strCSSStatusGreen;
		}
		
		logger.log(Level.FINE, "getColorCSS colorCode["+colorCSS+"]");
		
		logger.log(Level.FINE, "getColorCSS End");

		return colorCSS;
	}

	private TextBox txtAttribute[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	public Panel getMainPanel() {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		int numOfPoints = points.length;
		
		txtAttribute = new TextBox[numOfPoints];
		lblAttibuteLabel = new InlineLabel[numOfPoints];
		txtAttibuteColor = new InlineLabel[numOfPoints];
		
		FlexTable flexTableAttibutes = new FlexTable();
		flexTableAttibutes.setWidth("100%");
//		flexTableAttibutes.setBorderWidth(LAYOUT_BORDER);
		for ( int i = 0; i < numOfPoints ; i ++ ) {
			
			lblAttibuteLabel[i] = new InlineLabel();
			lblAttibuteLabel[i].setWidth("100%");
			lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-info-label");
			lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
			flexTableAttibutes.setWidget(i+1, 0, lblAttibuteLabel[i]);
			txtAttribute[i] = new TextBox();
			txtAttribute[i].setWidth("95%");
			txtAttribute[i].setText("ATTRIBUTE_STATUS_"+(i+1));
			txtAttribute[i].addStyleName("project-gwt-textbox-inspector-info-value");
			txtAttribute[i].setReadOnly(true);
			txtAttribute[i].setMaxLength(16);
			flexTableAttibutes.setWidget(i+1, 1, txtAttribute[i]);
			txtAttibuteColor[i] = new InlineLabel();
			txtAttibuteColor[i].setText("R");
//			txtAttibuteColor[i].getElement().getStyle().setColor(RGB_GREEN);
//			txtAttibuteColor[i].getElement().getStyle().setBackgroundColor(RGB_GREEN);
//			txtAttibuteColor[i].getElement().getStyle().setBackgroundImage("none");
			txtAttibuteColor[i].setStyleName(strCSSStatusGrey);
			flexTableAttibutes.setWidget(i+1, 2, txtAttibuteColor[i]);
		}
		
		flexTableAttibutes.getColumnFormatter().addStyleName(0, "project-gwt-flextable-inspector-info-label-col");
		flexTableAttibutes.getColumnFormatter().addStyleName(1, "project-gwt-flextable-inspector-info-value-col");
		flexTableAttibutes.getColumnFormatter().addStyleName(2, "project-gwt-flextable-inspector-info-status-col");
//		flexTableAttibutes.getColumnFormatter().setWidth(0, "150px");
//		flexTableAttibutes.getColumnFormatter().setWidth(1, "150px");
//		flexTableAttibutes.getColumnFormatter().setWidth(2, "15px");
		
		Button btnUp = new Button();
//		btnUp.setWidth("50px");
//		btnUp.getElement().getStyle().setPadding(10, Unit.PX);
		btnUp.addStyleName("project-gwt-button-inspector-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
//		lblPageNum.setWidth("50px");
//		lblPageNum.getElement().getStyle().setPadding(10, Unit.PX);
		lblPageNum.addStyleName("project-gwt-inlinelabel-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
//		btnDown.setWidth("50px");
//		btnDown.getElement().getStyle().setPadding(10, Unit.PX);
		btnDown.addStyleName("project-gwt-button-inspector-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});	
		
		Button btnAckCurPage = new Button();
//		btnAckCurPage.getElement().getStyle().setPadding(10, Unit.PX);
		btnAckCurPage.addStyleName("project-gwt-button-inspector-info-ackpage");
		btnAckCurPage.setText("Ack. Page");
		btnAckCurPage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
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
//		bottomBar.setWidth("100%");
		bottomBar.addStyleName("project-gwt-panel-inspector-info-bottom");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnAckCurPage);

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(flexTableAttibutes);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}



}
