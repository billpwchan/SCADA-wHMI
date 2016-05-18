package com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;

public class UITaskLaunch implements UITask_i {
	
	private static Logger logger = Logger.getLogger(UITaskLaunch.class.getName());
	
//	private String uiScreen = "";
//	private String uiPath = "";
	public String getUiScreen() {
		return this.uiScreen;
	}
	public int getTaskUiScreen() {
		return Integer.parseInt(this.uiScreen);
	}
	public void setTaskUiScreen(int uiScreen) {
		this.uiScreen = Integer.toString(uiScreen);
	}
	public void setUiScreen(String uiScreen) {
		this.uiScreen = uiScreen;
	}
	public String getUiPath() {
		return uiPath;
	}
	public void setUiPath(String uiPath) {
		this.uiPath = uiPath;
	}
	
	private final static char spliter = '|';
	public static char getSplite() { return spliter; }
	
	// TaskLaunch
	public enum TaskLaunchType {
		MENU, PANEL, IMAGE, UNKNOW
	}
	
	public UITaskLaunch() {
	
	}
	
	public UITaskLaunch(Task task) {
		
		logger.log(Level.FINE, "TaskLaunch Task Begin");
		
		this.set(task.getHeader(), task.getType(), task.getOrder(), task.getName(), task.getTitle(), task.getEnable(),
				task.getVisibile(), task.getLocCat(), task.getFunCat(), task.getUiPanel(), task.getUiScreen(),
				task.getUiPath());
		
		logger.log(Level.FINE, "TaskLaunch Task End");
	}

	public UITaskLaunch(UITaskLaunch taskLaunch) {
		
		logger.log(Level.FINE, "TaskLaunch TaskLaunch Begin");
		
		this.set(taskLaunch.getHeader(), taskLaunch.getType(), taskLaunch.getOrder(), taskLaunch.getName(), taskLaunch.getTitle(), taskLaunch.getEnable(),
				taskLaunch.getVisibile(), taskLaunch.getLocCat(), taskLaunch.getFunCat(), taskLaunch.getUiPanel(), taskLaunch.getUiScreen(),
				taskLaunch.getUiPath());
		
		logger.log(Level.FINE, "TaskLaunch TaskLaunch End");
	}

	private int counter = -1;
	private int headersCount = -1;
	private String headers[] = null;
	private String header = "";
	private String type = "", order = "", name = "", title = "", enable = "", visible = "", locCat = "", funCat = "",
			uiScreen = "", uiPath = "", uiPanel = "";
	
	private String first = "", last = "";
	public void setFirst(String first) { this.first = first; }
	public void setLast(String last) { this.last = last; }

	public void set(String header, String type, String order, String name, String title, String enable, String visible,
			String locCat, String funCat, String uiPanel, String uiScreen, String uiPath) {
		
		logger.log(Level.FINE, "set Begin");
		
		logger.log(Level.FINE, "set head["+header+"]");
		logger.log(Level.FINE, "set type["+type+"]");
		logger.log(Level.FINE, "set order["+order+"]");
		logger.log(Level.FINE, "set name["+name+"]");
		logger.log(Level.FINE, "set title["+title+"]");
		logger.log(Level.FINE, "set enable["+enable+"]");
		logger.log(Level.FINE, "set visible["+visible+"]");
		logger.log(Level.FINE, "set locCat["+locCat+"]");
		logger.log(Level.FINE, "set funCat["+funCat+"]");
		logger.log(Level.FINE, "set uiPanel["+uiPanel+"]");
		logger.log(Level.FINE, "set uiScreen["+uiScreen+"]");
		logger.log(Level.FINE, "set uiPath["+uiPath+"]");
		
		this.header = header;
		this.type = type;
		this.order = order;
		this.name = name;
		this.title = title;
		this.enable = enable;
		this.visible = visible;
		this.setLocCat(locCat);
		this.setFunCat(funCat);
		this.uiPanel = uiPanel;
		this.uiScreen = uiScreen;
		this.setUiPath(uiPath);
		
		logger.log(Level.FINE, "set End");
		
	}
	
	private String[] options;
	public String[] getOption() { return options; }
	public void setOption(String[] options) { this.options = options; }

	public String getHeader() {
		
		logger.log(Level.FINE, "getHeader Begin/End");
		
		return this.header;
	}

	public void setName(String name) {
		
		logger.log(Level.FINE, "setName Begin/End");
		
		this.name = name;
	}

	public String getName() {
		
		logger.log(Level.FINE, "getName Begin");
		
		String string = this.name;
		if (string.length() <= 0)
			string = getElementLast(this.header);
		
		logger.log(Level.FINE, "getName End");
		
		return string;
	}

	public String getNameWithSpace() {
		
		logger.log(Level.FINE, "getNameWithSpace Begin");
		
		String string = this.name;
		if (string.length() <= 0) {
			String lastElement = getElementLast(this.header);
			string = toTitle(lastElement);
		}
		
		logger.log(Level.FINE, "getNameWithSpace End");
		
		return string;
	}
	
	public String getFirst() {
		
		logger.log(Level.FINE, "getFirst Begin");
		
		String string = getElementFirst(this.header);
		
		logger.log(Level.FINE, "getFirst End");
		
		return string;
	}
	
	public String getFirstWithSpace() {
		
		logger.log(Level.FINE, "getFirstWithSpace Begin");
		
		String string = first;
		
		if ( 0 == first.compareTo("") )  {
			string = toTitle(getElementFirst(this.header));
		}
		
		logger.log(Level.FINE, "getFirstWithSpace End");
		
		return string;
	}
//	
//	public String getLast() {
//		
//		logger.log(Level.FINE, "getLast Begin");
//		
//		String string = getElementFirst(this.header);
//		
//		logger.log(Level.FINE, "getLast End");
//		
//		return string;
//	}
//	
	public String getLastWithSpace() {
		
		logger.log(Level.FINE, "getLastWithSpace Begin");
		
		String string = last;
		
		if ( 0 == last.compareTo("") ) {
			string = toTitle(getElementLast(this.header));
		}
		
		logger.log(Level.FINE, "getLastWithSpace End");
		
		return string;
	}

	public void setTitle(String title) {
		
		logger.log(Level.FINE, "setTitle Begin/End");
		
		this.title = title;
	}

	public String getTitle() {
		
		logger.log(Level.FINE, "getTitle Begin/End");
		
		return this.title;
	}

	public String getTitleWithSpace() {
		
		logger.log(Level.FINE, "getTitleWithSpace Begin");
		
		String string = this.title;
		if (string.length() <= 0) {
			String lastElement = getElementLast(this.header);
			string = toTitle(lastElement);
		}
		
		logger.log(Level.FINE, "getTitleWithSpace End");
		
		return string;
	}

	public String getOrder() {
		
		logger.log(Level.FINE, "getOrder Begin/End");
		
		return this.order;
	}

	public int getTaskOrder() {
		
		logger.log(Level.FINE, "getTaskOrder Begin/End");
		
		return Integer.parseInt(this.order);
	}

	public String getLevel() {
		
		logger.log(Level.FINE, "getLevel Begin/End");
		
		return this.getLevel();
	}

	public int getTaskLevel() {
		
		logger.log(Level.FINE, "getTaskLevel Begin/End");
		
		return letterCounter(this.header, spliter);
	}

	public void setType(String type) {
		
		logger.log(Level.FINE, "setType Begin/End");
		
		this.type = type;
	}

	public String getType() {
		
		logger.log(Level.FINE, "getType Begin/End");
		
		return this.type;
	}

	public TaskLaunchType getTaskLaunchType() {
		
		logger.log(Level.FINE, "getTaskLaunchType Begin");
		
		TaskLaunchType type;
		if (0 == this.type.compareToIgnoreCase("M")) {
			type = TaskLaunchType.MENU;
		} else if (0 == this.type.compareToIgnoreCase("P")) {
			type = TaskLaunchType.PANEL;
		} else if (0 == this.type.compareToIgnoreCase("S")) {
			type = TaskLaunchType.IMAGE;
		} else {
			type = TaskLaunchType.UNKNOW;
		}
		
		logger.log(Level.FINE, "getTaskLaunchType End");
		
		return type;
	}

	public String getEnable() {
		
		logger.log(Level.FINE, "getEnable Begin/End");
		
		return this.enable;
	}

	public boolean isTaskEnable() {
		
		logger.log(Level.FINE, "isTaskEnable Begin/End");
		
		return (this.enable.equalsIgnoreCase("1") ? true : false);
	}

	public String getVisibile() {
		
		logger.log(Level.FINE, "getVisibile Begin/End");
		
		return this.visible;
	}

	public boolean isTaskVisible() {
		
		logger.log(Level.FINE, "isTaskVisible Begin/End");
		
		return (visible.compareTo("1") == 1);
	}

	public String getLocCat() {
		
		logger.log(Level.FINE, "getLocCat Begin/End");
		
		return locCat;
	}

	public void setLocCat(String locCat) {
		
		logger.log(Level.FINE, "setLocCat Begin/End");
		
		this.locCat = locCat;
	}

	public String getFunCat() {
		
		logger.log(Level.FINE, "getFunCat Begin/End");
		
		return funCat;
	}

	public void setFunCat(String funCat) {
		
		logger.log(Level.FINE, "setFunCat Begin/End");
		
		this.funCat = funCat;
	}
	
	private void splitHeader(){
		logger.log(Level.FINE, "splitHeader Begin");
		if ( null == headers ) {
			headers = header.split("\\|");
			headersCount = headers.length;
		}
		logger.log(Level.FINE, "splitHeader End");
	}
	
	public String getHeaderElement(int index) {
		logger.log(Level.FINE, "getHeaderElement Begin");
		String element = "";
		splitHeader();
		if ( index > 0 && index < headersCount )
			element = headers[index];
		logger.log(Level.FINE, "getHeaderElement End");
		return element;
	}

	public int letterCounter(String str, char letter) {
		
		logger.log(Level.FINE, "letterCounter Begin");
		
		if (counter != -1)
			return counter;
		counter = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == letter)
				++counter;
		}
		
		logger.log(Level.FINE, "letterCounter End");
		
		return counter;
	}
	
	public String getElementFirst(String string) {
		
		logger.log(Level.FINE, "getElementLast Begin");
		
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[0];
		}	
		
		// Window.alert("getElementLast string["+string+"] >
		// element["+element+"]");
		
		logger.log(Level.FINE, "getElementLast End");
		
		return element;
	}

	public String getElementLast(String string) {
		
		logger.log(Level.FINE, "getElementLast Begin");
		
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[strings.length - 1];
		}	
		
		// Window.alert("getElementLast string["+string+"] >
		// element["+element+"]");
		
		logger.log(Level.FINE, "getElementLast End");
		
		return element;
	}

	public String getElement(String string, int index) {
		
		logger.log(Level.FINE, "getElement Begin");
		
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0 && strings.length > index ) {
			element = strings[index];
		}
		
		logger.log(Level.FINE, "getElement End");
		
		// Window.alert("getElement string["+string+"] > element["+element+"]");
		return element;
	}

	public String toTitle(String string) {
		
		logger.log(Level.FINE, "toTitle Begin");
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			boolean doubleUpperCase = false;
			if (i != 0 && Character.isUpperCase(c) && i != string.length() - 1) {
				if (i + 1 < string.length()) {
					char c2 = string.charAt(i + 1);
					if (Character.isUpperCase(c2)) {
						doubleUpperCase = true;
					}
				}
				if (!doubleUpperCase) {
					sb.append(' ');
				}
			}
			sb.append(c);
		}
		// Window.alert("string["+string+"] >
		// sb.toString()["+sb.toString()+"]");
		
		logger.log(Level.FINE, "toTitle End");
		
		return sb.toString();
	}

	public String getUiPanel() {
		
		logger.log(Level.FINE, "getUiPanel Begin/End");
		
		return uiPanel;
	}

	public void setUiPanel(String uiPanel) {
		
		logger.log(Level.FINE, "setUiPanel Begin/End");
		
		this.uiPanel = uiPanel;
	}
	
	public void debug() {
		
		logger.log(Level.FINE, "debug Begin");
		
		String str  = "";
		
		if ( this.getTaskLaunchType() == TaskLaunchType.IMAGE ) {
			str = " Header[" + this.getHeader() + "]";
		}
		
		logger.log(Level.FINE, "debug UIPanel[" + this.getUiPanel() + "] UIPath["+this.getUiPath()+"] Type["+this.getType()+"] "+str);
		
		logger.log(Level.FINE, "debug End");
	}
}
