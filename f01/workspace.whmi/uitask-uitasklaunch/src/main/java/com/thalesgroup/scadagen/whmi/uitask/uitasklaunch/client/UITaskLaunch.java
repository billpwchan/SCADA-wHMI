package com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task_i.TaskAttribute;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITaskDictionary;

public class UITaskLaunch extends UITaskDictionary {

	private final static char spliter = '|';
	public static char getSplite() { return spliter; }
	
	// TaskLaunch
	public enum TaskLaunchType {
		MENU("M")
		, PANEL("P")
		, IMAGE("S")
		, UNKNOW("U")
		;
		private final String text;
		private TaskLaunchType(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public UITaskLaunch() {
	}
	
	public enum UITaskLaunchAttribute {
		Key("key")
		, Type("type")
		, Order("order")
		, Name("name")
		, Title("title")
		, Enable("enable")
		, Visible("visible")
		, LocCat("locCat")
		, FunCat("funCat")
		, UIPanel("uiPanel")
		, UIScreen("uiScreen")
		, UIPath("uiPath")
		, Css("css")
		, UIView("uiView")
		, Tooltips("tooltips")
		, Execute("execute")
		;
		private final String text;
		private UITaskLaunchAttribute(final String text) { this.text = text; }
		public boolean equalsName(String otherName) { return ( otherName == null ) ? false : text.equals(otherName); }
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() { return this.text; }
	}
	
	public UITaskLaunch(Task task) {
		super.setUiScreen(task.getParameter(TaskAttribute.UIScreen.toString()));
		super.setUiPath(task.getParameter(TaskAttribute.UIPath.toString()));
		setValue(UITaskLaunchAttribute.Key.toString()		, task.getParameter(TaskAttribute.Key.toString()));
		setValue(UITaskLaunchAttribute.Type.toString()	, task.getParameter(TaskAttribute.Type.toString()));
		setValue(UITaskLaunchAttribute.Order.toString()	, task.getParameter(TaskAttribute.Order.toString()));
		setValue(UITaskLaunchAttribute.Name.toString()	, task.getParameter(TaskAttribute.Name.toString()));
		setValue(UITaskLaunchAttribute.Title.toString()	, task.getParameter(TaskAttribute.Title.toString()));
		setValue(UITaskLaunchAttribute.Enable.toString()	, task.getParameter(TaskAttribute.Enable.toString()));
		setValue(UITaskLaunchAttribute.Visible.toString()	, task.getParameter(TaskAttribute.Visible.toString()));
		setValue(UITaskLaunchAttribute.LocCat.toString()	, task.getParameter(TaskAttribute.LocCat.toString()));
		setValue(UITaskLaunchAttribute.FunCat.toString()	, task.getParameter(TaskAttribute.FunCat.toString()));
		setValue(UITaskLaunchAttribute.UIPanel.toString()	, task.getParameter(TaskAttribute.UIPanel.toString()));
		setValue(UITaskLaunchAttribute.Css.toString()		, task.getParameter(TaskAttribute.Css.toString()));
		setValue(UITaskLaunchAttribute.UIView.toString()	, task.getParameter(TaskAttribute.UIView.toString()));
		setValue(UITaskLaunchAttribute.Tooltips.toString()	, task.getParameter(TaskAttribute.Tooltips.toString()));
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

	private Object[] options;
	public Object[] getOption() { return options; }
	public void setOption(Object[] options) { this.options = options; }

	public String getHeader() {
		return (String) getValue(UITaskLaunchAttribute.Key.toString());
	}
	public void setHeader(String key) {
		setValue(UITaskLaunchAttribute.Key.toString(), key);
	}

	public void setName(String name) {
		setValue(UITaskLaunchAttribute.Name.toString(), name);
	}
	
	public String getName() {
		String string = (String) getValue(UITaskLaunchAttribute.Name.toString());
		if (string.length() <= 0) {
			string = (String) getValue(UITaskLaunchAttribute.Key.toString());
			string = getElementLast(string);
		}
		return string;
	}

	public void setTitle(String title) {
		setValue(UITaskLaunchAttribute.Title.toString(), title);
	}

	public String getTitle() {
		return (String) getValue(UITaskLaunchAttribute.Title.toString());
	}


	public int getTaskLevel() {
		String key = (String) getValue(UITaskLaunchAttribute.Key.toString());
		return letterCounter(key, spliter);
	}

	public void setType(String type) {
		setValue(UITaskLaunchAttribute.Type.toString(), type);
	}

	public TaskLaunchType getTaskLaunchType() {
		String t = (String)getValue(UITaskLaunchAttribute.Type.toString());
		TaskLaunchType type = null;
		// Using Igrone instead of valueOf
		if ( null != t ) {
			if (0 == t.compareToIgnoreCase("M")) {
				type = TaskLaunchType.MENU;
			} else if (0 == t.compareToIgnoreCase("P")) {
				type = TaskLaunchType.PANEL;
			} else if (0 == t.compareToIgnoreCase("S")) {
				type = TaskLaunchType.IMAGE;
			}
		}
		if ( null == t ) {
			type = TaskLaunchType.UNKNOW;
		}
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
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[strings.length - 1];
		}	

		return element;
	}

	public String getUiPanel() {
		return (String) getValue(UITaskLaunchAttribute.UIPanel.toString());
	}
	public void setUiPanel(String uiPanel) {
		setValue(UITaskLaunchAttribute.UIPanel.toString(), uiPanel);
	}
	
	public void setCss(String css) {
		setValue(UITaskLaunchAttribute.Css.toString(), css);
	}
	public String getCss() {
		return (String) getValue(UITaskLaunchAttribute.Css.toString());
	}

	public void setUiView(String uiView) {
		setValue(UITaskLaunchAttribute.UIView.toString(), uiView);
	}
	public String getUiView() {
		return (String) getValue(UITaskLaunchAttribute.UIView.toString());
	}

	public void setTooltip(String toolTips) {
		setValue(UITaskLaunchAttribute.Tooltips.toString(), toolTips);
	}
	public String getTooltip() {
		return (String) getValue(UITaskLaunchAttribute.Tooltips.toString());
	}
	
	public void setExecute(String execute) {
		setValue(UITaskLaunchAttribute.Execute.toString(), execute);
	}
	public String getExecute() {
		return (String) getValue(UITaskLaunchAttribute.Execute.toString());
	}
}
