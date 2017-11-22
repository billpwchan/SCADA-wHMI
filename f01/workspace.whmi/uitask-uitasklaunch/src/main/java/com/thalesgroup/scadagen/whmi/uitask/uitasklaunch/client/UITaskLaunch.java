package com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task_i.TaskAttribute;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.TaskLaunchType;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch_i.UITaskLaunchAttribute;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UITaskLaunch extends UITaskDictionary {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UITaskLaunch.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public static char getSplite() { return UITaskLaunch_i.STR_SPLITER; }
	public static String getSplite(char spliter) { return String.valueOf(getSplite()); }
	
	public UITaskLaunch() { }

	public UITaskLaunch(Task task) {
		super.setUiScreen(task.getParameter(TaskAttribute.UIScreen.toString()));
		super.setUiPath(task.getParameter(TaskAttribute.UIPath.toString()));
		setValue(UITaskLaunchAttribute.Key.toString()		, task.getParameter(TaskAttribute.Key.toString()));
		
		setValue(UITaskLaunchAttribute.Type.toString()		, task.getParameter(TaskAttribute.Type.toString()));
		setValue(UITaskLaunchAttribute.Name.toString()		, task.getParameter(TaskAttribute.Name.toString()));
		setValue(UITaskLaunchAttribute.Title.toString()		, task.getParameter(TaskAttribute.Title.toString()));
		
		setValue(UITaskLaunchAttribute.Enable.toString()	, task.getParameter(TaskAttribute.Enable.toString()));
		setValue(UITaskLaunchAttribute.Visible.toString()	, task.getParameter(TaskAttribute.Visible.toString()));
		
		setValue(UITaskLaunchAttribute.Css.toString()		, task.getParameter(TaskAttribute.Css.toString()));
		setValue(UITaskLaunchAttribute.Tooltips.toString()	, task.getParameter(TaskAttribute.Tooltips.toString()));
		
		setValue(UITaskLaunchAttribute.UIConf.toString()	, task.getParameter(TaskAttribute.UIConf.toString()));
		
		setValue(UITaskLaunchAttribute.UICtrl.toString()	, task.getParameter(TaskAttribute.UICtrl.toString()));
		setValue(UITaskLaunchAttribute.UIView.toString()	, task.getParameter(TaskAttribute.UIView.toString()));
		setValue(UITaskLaunchAttribute.UIOpts.toString()	, task.getParameter(TaskAttribute.UIOpts.toString()));
		setValue(UITaskLaunchAttribute.UIDict.toString()	, task.getParameter(TaskAttribute.UIDict.toString()));
		setValue(UITaskLaunchAttribute.UIElem.toString()	, task.getParameter(TaskAttribute.UIElem.toString()));
		setValue(UITaskLaunchAttribute.UISvId.toString()	, task.getParameter(TaskAttribute.UISvId.toString()));
	}

	public UITaskLaunch(UITaskLaunch taskLaunch) {
		super(taskLaunch);
		for ( UITaskLaunchAttribute taskLaunchAttribute : UITaskLaunchAttribute.values() ) { 
			String key = taskLaunchAttribute.toString();
			if ( key.equals(UITaskLaunchAttribute.UIPath.toString()) 
					|| key.equals(UITaskLaunchAttribute.UIScreen.toString()) ) {
				continue;
			}
			setValue(key, taskLaunch.getValue(key));
		}
	}
	
	public boolean isEquals(Object obj) {
		final String function = "isEqual";
		logger.begin(className, function);
		boolean ret = false;
		
		if ( null != obj ) {
			if ( obj instanceof UITaskLaunch ) {
				UITaskLaunch t = (UITaskLaunch)obj;
				if ( t.getAttributeKeys().size() != this.getAttributeKeys().size() ) {
					logger.warn(className, function, "t.getAttributeKeys().size()[{}] != this.getAttributeKeys().size()[{}]", t.getAttributeKeys().size(), this.getAttributeKeys().size());
				}
				else if (t.getValueKeys().size() != this.getValueKeys().size() ) {
					logger.warn(className, function, "t.getValueKeys().size()[{}] != this.getValueKeys().size()[{}]", t.getValueKeys().size(), this.getValueKeys().size());
				}
				else {
					boolean inValid = false;
					for ( String key : t.getAttributeKeys() ) {
						if ( t.getAttribute(key) != this.getAttribute(key) ) {
							logger.warn(className, function, "t.getAttribute({})[{}] != this.getAttribute({})[{}]", new Object[]{key, t.getAttribute(key), key, this.getAttribute(key)});
							inValid = true;
							break;
						}
					}
					if ( ! inValid ) {
						for ( String key : t.getValueKeys() ) {
							if ( t.getValue(key) != this.getValue(key) ) {
								logger.warn(className, function, "t.getValue({})[{}] != this.getValue({})[{}]", new Object[]{key, t.getValue(key), key, this.getValue(key)});
								inValid = true;
								break;
							}
						}
					}
					
					if ( ! inValid ) ret = true;
				}
			}
		}
		
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}
	

	private HashMap<String, String> options = new HashMap<String, String>();
	public String getOption(String key) { return this.options.get(key); }
	public void setOption(String key, String value) { this.options.put(key, value); }
	
	public HashMap<String, String> getOptions() { 
		HashMap<String, String> options = new HashMap<String, String>();
		for ( Entry<String, String> option : this.options.entrySet() ) {
			String key = option.getKey();
			String value = option.getValue();
			options.put(key, value); 
		}
		return options; 
	}
	public void setOptions(Map<String, String> options) {
		if ( null != options ) {
			for ( Entry<String, String> option : options.entrySet() ) {
				String key = option.getKey();
				String value = option.getValue();
				this.options.put(key, value);
			}
		}
	}

	public String getHeader() { return (String) getValue(UITaskLaunchAttribute.Key.toString()); }
	public void setHeader(String key) { setValue(UITaskLaunchAttribute.Key.toString(), key); }

	public void setName(String name) { setValue(UITaskLaunchAttribute.Name.toString(), name); }
	
	public String getName() {
		String string = (String) getValue(UITaskLaunchAttribute.Name.toString());
		if (string.length() <= 0) {
			string = (String) getValue(UITaskLaunchAttribute.Key.toString());
			string = getElementLast(string);
		}
		return string;
	}

	public void setTitle(String title) { setValue(UITaskLaunchAttribute.Title.toString(), title); }
	public String getTitle() { return (String) getValue(UITaskLaunchAttribute.Title.toString()); }

	public int getTaskLevel() {	
		return letterCounter((String) getValue(UITaskLaunchAttribute.Key.toString()), UITaskLaunch_i.STR_SPLITER); 
	}
	public void setType(String type) { setValue(UITaskLaunchAttribute.Type.toString(), type); }

	public TaskLaunchType getTaskLaunchType() {
		String t = (String)getValue(UITaskLaunchAttribute.Type.toString());
		TaskLaunchType type = null;
		// Using Igrone instead of valueOf
		if ( null != t ) {
			if (0 == t.compareToIgnoreCase(TaskLaunchType.MENU.toString())) {
				type = TaskLaunchType.MENU;
			} 
			else if (0 == t.compareToIgnoreCase(TaskLaunchType.PANEL.toString())) {
				type = TaskLaunchType.PANEL;
			} 
			else if (0 == t.compareToIgnoreCase(TaskLaunchType.IMAGE.toString())) {
				type = TaskLaunchType.IMAGE;
			}
		}
		else { type = TaskLaunchType.UNKNOW; }
		return type;
	}

	private int counter = -1;
	private int letterCounter(String str, char letter) {
		if (counter != -1)
			return counter;
		counter = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == letter)
				++counter;
		}
		return counter;
	}

	private String getElementLast(String string) {
		String element = "";
		String strings[] = string.split(getSplite(getSplite()));
		if (strings.length > 0) {
			element = strings[strings.length - 1];
		}	

		return element;
	}

	public void setCss(String css) { setValue(UITaskLaunchAttribute.Css.toString(), css); }
	public String getCss() { return (String) getValue(UITaskLaunchAttribute.Css.toString()); }

	public void setTooltip(String toolTips) { setValue(UITaskLaunchAttribute.Tooltips.toString(), toolTips); }
	public String getTooltip() { return (String) getValue(UITaskLaunchAttribute.Tooltips.toString()); }
	
	public void setExecute(String execute) { setValue(UITaskLaunchAttribute.Execute.toString(), execute); }
	public String getExecute() { return (String) getValue(UITaskLaunchAttribute.Execute.toString()); }
	
	public void setUiCtrl(String uiCtrl) { setValue(UITaskLaunchAttribute.UICtrl.toString(), uiCtrl); }
	public String getUiCtrl() { return (String) getValue(UITaskLaunchAttribute.UICtrl.toString()); }
	
	public void setUiView(String uiView) { setValue(UITaskLaunchAttribute.UIView.toString(), uiView); }
	public String getUiView() { return (String) getValue(UITaskLaunchAttribute.UIView.toString()); }
	
	public void setUiOpts(String uiOpts) { setValue(UITaskLaunchAttribute.UIOpts.toString(), uiOpts); }
	public String getUiOpts() { return (String) getValue(UITaskLaunchAttribute.UIOpts.toString()); }
	
	public void setUiElem(String uiElem) { setValue(UITaskLaunchAttribute.UIElem.toString(), uiElem); }
	public String getUiElem() { return (String) getValue(UITaskLaunchAttribute.UIElem.toString()); }
	
	public void setUiDict(String uiDict) { setValue(UITaskLaunchAttribute.UIDict.toString(), uiDict); }
	public String getUiDict() { return (String) getValue(UITaskLaunchAttribute.UIDict.toString()); }
	
	public void setUiSvId(String uiSvId) { setValue(UITaskLaunchAttribute.UISvId.toString(), uiSvId); }
	public String getUiSvId() { return (String) getValue(UITaskLaunchAttribute.UISvId.toString()); }
	
	public void setUiConf(String uiConf) { setValue(UITaskLaunchAttribute.UIConf.toString(), uiConf); }
	public String getUiConf() { return (String) getValue(UITaskLaunchAttribute.UIConf.toString()); }

}
