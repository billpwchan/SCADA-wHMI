package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.opm.SCADAgenTaskOpm_i;

public class UIWidgetVerifyOPMCheckAccess extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMCheckAccess.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void checkAccess() {
		final String function = "checkAccess";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		boolean result = uiOpm_i.checkAccess(functionvalue, locationvalue, actionvalue, modevalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
		
		logger.end(className, function);
	}
	
	private void checkAccessWithProfile() {
		final String function = "checkAccessWithProfileName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithProfileName(functionvalue, locationvalue, actionvalue, modevalue, hvid, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHostName() {
		final String function = "checkAccessWithHostName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHostName(functionvalue, locationvalue, actionvalue, modevalue, hvid, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHomValue() {
		final String function = "checkAccessWithHomValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hdvflagvalue		= uiGeneric.getWidgetValue("hdvflagvalue");
		
		String keyvalue			= uiGeneric.getWidgetValue("keyvalue");
		
		int hdvflag = Integer.parseInt(hdvflagvalue);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHom(functionvalue, locationvalue, actionvalue, modevalue, hdvflag, keyvalue, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHom() {
		final String function = "checkAccessWithHom";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		String keyvalue			= uiGeneric.getWidgetValue("keyvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHom(functionvalue, locationvalue, actionvalue, modevalue, hvid, keyvalue, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private boolean checkOpms(String uiopmapivalue, String opmValue, String opmName, String opmOperation) {
		final String function = "checkOpms";
		
		boolean result = false;

		boolean isAndOperation = false;
		
		String [] opmValues = opmValue.split(SCADAgenTaskOpm_i.setSpliter);
		int opmValuesLength = opmValues.length;
		logger.debug(className, function, "opmValuesLength[{}]", opmValuesLength);
		
		if ( null != opmValues && opmValuesLength > 1 ) {
			if ( null != opmOperation ) {
				if ( 0 == opmOperation.compareToIgnoreCase(SCADAgenTaskOpm_i.AttributeValue.AND.toString()) ) {
					isAndOperation = true;
				}
			}
		}
		
		logger.debug(className, function, "opmOperation[{}] isAndOperation[{}]", opmOperation, isAndOperation);
		
		for ( int i = 0 ; i < opmValuesLength ; ++i ) {
			
			boolean bResult = checkOpm(uiopmapivalue, opmValues[i], opmName);
			
			if ( ! isAndOperation ) {
				// OR Operation
				if ( bResult ) {
					result = true;
					break;
				}
			} else {
				// AND Operation
				if ( bResult ) {
					result = true;
				} else {
					result = false;
					break;
				}
			}
		}

		return result;
	}
	
	private boolean checkOpm(String uiopmapivalue, String opmValue, String opmName) {
		final String function = "checkOpm";
		logger.begin(className, function);
		
		boolean result = false;
		
		logger.debug(className, function, "opmValue[{}] opmName[{}]", opmValue, opmName);
		
		String [] opmValues = null;
		if ( null != opmValue && ! opmValue.isEmpty() ) {
			opmValues = opmValue.split(SCADAgenTaskOpm_i.valSpliter);
		} else {
			logger.debug(className, function, "opmValue[{}] IS INVALID", opmValue);
		}
		String [] opmNames = null;
		if ( null != opmName && ! opmName.isEmpty() ) {
			opmNames = opmName.split(SCADAgenTaskOpm_i.valSpliter);
		} else {
			logger.debug(className, function, "opmName[{}] IS INVALID", opmName);
		}
		
		if ( null != opmNames && null != opmValues ) {
			if ( logger.isTraceEnabled() ) {
				for ( int i = 0 ; i < opmNames.length ; i++ ) {
					logger.trace(className, function, "opmNames({})[{}] IS INVALID", i, opmNames[i]);
				}
				for ( int i = 0 ; i < opmValues.length ; i++ ) {
					logger.trace(className, function, "opmName({})[{}] IS INVALID", i, opmValues[i]);
				}
			}			

			if ( opmNames.length > 0  ) {
				if ( opmNames.length == opmValues.length ) {
					UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
					Map<String, String> parameter = new HashMap<String, String>();
					for ( int i = 0 ; i < opmName.length() ; i++ ) {
						String key = opmNames[i];
						String value = opmValues[i];
						parameter.put(key, value);
					}
					result = uiOpm_i.checkAccess(parameter);
				} else {
					logger.warn(className, function, "opmNames.length[{}] and opmValues.length[{}] IS NOT EQUAL", opmNames.length, opmValues.length);
				}
			} else {
				logger.warn(className, function, "opmName[{}] length <= 0");
			}
		} else{
			logger.warn(className, function, "opmNames OR opmValue IS NULL");
		}

		logger.end(className, function);
		
		return result;
	}
	
	private void checkAccessMap() {
		final String function = "checkAccessMap";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");

		String opmnamevalue		= uiGeneric.getWidgetValue("opmnamevalue");
		String opmvaluevalue	= uiGeneric.getWidgetValue("opmvaluevalue");

		boolean result = checkOpm(uiopmapivalue, opmvaluevalue, opmnamevalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));

		logger.end(className, function);
	}
	
	private void checkAccessMaps() {
		final String function = "checkAccessMaps";
		logger.begin(className, function);
		
		String uiopmapivalue		= uiGeneric.getWidgetValue("uiopmapivalue");

		String opmnamevalue			= uiGeneric.getWidgetValue("opmnamevalue");
		String opmvaluevalue		= uiGeneric.getWidgetValue("opmvaluevalue");
		String opmoperationvalue	= uiGeneric.getWidgetValue("opmoperationvalue");

		boolean result = checkOpms(uiopmapivalue, opmvaluevalue, opmnamevalue, opmoperationvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));

		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "checkaccess".equals(element) ) {
			checkAccess();
		} else if ( "checkaccesswithprofilename".equals(element) ) {
			checkAccessWithProfile();
		} else if ( "checkaccesswithhostname".equals(element) ) {
			checkAccessWithHostName();
		} else if ( "checkaccesswithhom".equals(element) ) {
			checkAccessWithHom();
		} else if ( "checkaccesswithhomvalue".equals(element) ) {
			checkAccessWithHomValue();
		} else if ( "checkaccessmap".equals(element) ) {
			checkAccessMap();
		} else if ( "checkaccessmaps".equals(element) ) {
			checkAccessMaps();
		}
	}

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(className, function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				// TODO Auto-generated method stub
				
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				// TODO Auto-generated method stub
			}
		
			@Override
			public void envUp(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void envDown(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}

}
