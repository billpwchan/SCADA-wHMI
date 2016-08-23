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
	
	public enum TaskLaunchAttribute {
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
		, UIView("uiview")
		;
		private final String text;
		private TaskLaunchAttribute(final String text) { this.text = text; }
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
		
		setValue(TaskLaunchAttribute.Key.toString()		, task.getParameter(TaskAttribute.Key.toString()));
		setValue(TaskLaunchAttribute.Type.toString()	, task.getParameter(TaskAttribute.Type.toString()));
		setValue(TaskLaunchAttribute.Order.toString()	, task.getParameter(TaskAttribute.Order.toString()));
		setValue(TaskLaunchAttribute.Name.toString()	, task.getParameter(TaskAttribute.Name.toString()));
		setValue(TaskLaunchAttribute.Title.toString()	, task.getParameter(TaskAttribute.Title.toString()));
		setValue(TaskLaunchAttribute.Enable.toString()	, task.getParameter(TaskAttribute.Enable.toString()));
		setValue(TaskLaunchAttribute.Visible.toString()	, task.getParameter(TaskAttribute.Visible.toString()));
		setValue(TaskLaunchAttribute.LocCat.toString()	, task.getParameter(TaskAttribute.LocCat.toString()));
		setValue(TaskLaunchAttribute.FunCat.toString()	, task.getParameter(TaskAttribute.FunCat.toString()));
		setValue(TaskLaunchAttribute.UIPanel.toString()	, task.getParameter(TaskAttribute.UIPanel.toString()));
		setValue(TaskLaunchAttribute.Css.toString()		, task.getParameter(TaskAttribute.Css.toString()));

	}

	public UITaskLaunch(UITaskLaunch taskLaunch) {
		
		super(taskLaunch);
		
		for ( TaskLaunchAttribute taskLaunchAttribute : TaskLaunchAttribute.values() ) { 
			String key = taskLaunchAttribute.toString();
			if ( key.equals(TaskLaunchAttribute.UIPath.toString()) 
					|| key.equals(TaskLaunchAttribute.UIScreen.toString()) ) {
				continue;
			}
			setValue(key, taskLaunch.getValue(key));
		}
		
//		setValue(TaskLaunchAttribute.Key.toString(), taskLaunch.getValue(TaskLaunchAttribute.Key.toString()));
//		setValue(TaskLaunchAttribute.Type.toString(), taskLaunch.getValue(TaskLaunchAttribute.Type.toString()));
//		setValue(TaskLaunchAttribute.Order.toString(), taskLaunch.getValue(TaskLaunchAttribute.Order.toString()));
//		setValue(TaskLaunchAttribute.Name.toString(), taskLaunch.getValue(TaskLaunchAttribute.Name.toString()));
//		setValue(TaskLaunchAttribute.Title.toString(), taskLaunch.getValue(TaskLaunchAttribute.Title.toString()));
//		setValue(TaskLaunchAttribute.Enable.toString(), taskLaunch.getValue(TaskLaunchAttribute.Enable.toString()));
//		setValue(TaskLaunchAttribute.Visible.toString(), taskLaunch.getValue(TaskLaunchAttribute.Visible.toString()));
//		setValue(TaskLaunchAttribute.LocCat.toString(), taskLaunch.getValue(TaskLaunchAttribute.LocCat.toString()));
//		setValue(TaskLaunchAttribute.FunCat.toString(), taskLaunch.getValue(TaskLaunchAttribute.FunCat.toString()));
//		setValue(TaskLaunchAttribute.UIPanel.toString(), taskLaunch.getValue(TaskLaunchAttribute.UIPanel.toString()));
//		setValue(TaskLaunchAttribute.UIScreen.toString(), taskLaunch.getValue(TaskLaunchAttribute.UIScreen.toString()));
//		setValue(TaskLaunchAttribute.UIPath.toString(), taskLaunch.getValue(TaskLaunchAttribute.UIPath.toString()));
//		setValue(TaskLaunchAttribute.Css.toString(), taskLaunch.getValue(TaskLaunchAttribute.Css.toString()));

	}

	private Object[] options;
	public Object[] getOption() { return options; }
	public void setOption(Object[] options) { this.options = options; }

	public String getHeader() {

		return (String) getValue(TaskLaunchAttribute.Key.toString());
	}

	public void setName(String name) {

		setValue(TaskLaunchAttribute.Name.toString(), name);
	}

	public String getName() {

		String string = (String) getValue(TaskLaunchAttribute.Name.toString());

		if (string.length() <= 0)
			string = (String) getValue(TaskLaunchAttribute.Key.toString());
			string = getElementLast(string);

		return string;
	}

	public void setTitle(String title) {

		setValue(TaskLaunchAttribute.Title.toString(), title);
	}

	public String getTitle() {

		return (String) getValue(TaskLaunchAttribute.Title.toString());
	}


	public int getTaskLevel() {

		String key = (String) getValue(TaskLaunchAttribute.Key.toString());
		
		return letterCounter(key, spliter);
	}

	public void setType(String type) {

		setValue(TaskLaunchAttribute.Type.toString(), type);
	}

	public TaskLaunchType getTaskLaunchType() {
		
		String t = (String)getValue(TaskLaunchAttribute.Type.toString());
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
		
		return (String) getValue(TaskLaunchAttribute.UIPanel.toString());

	}

	public void setUiPanel(String uiPanel) {
		
		setValue(TaskLaunchAttribute.UIPanel.toString(), uiPanel);

	}

}
