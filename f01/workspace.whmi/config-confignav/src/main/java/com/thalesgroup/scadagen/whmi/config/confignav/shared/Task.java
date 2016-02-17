package com.thalesgroup.scadagen.whmi.config.confignav.shared;

public class Task implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1367068029420283265L;

	public final static char spliter = '|';

	public Task() {

	}

	public enum TaskType {
		MENU, PANEL, IMAGE, UNKNOW
	}

	public Task(Task task) {
		this.set(task.getHeader(), task.getType(), task.getOrder(), task.getName(), task.getTitle(), task.getEnable(),
				task.getVisibile(), task.getLocCat(), task.getFunCat(), task.getUiPanel(), task.getUiScreen(),
				task.getUiPath());
	}

	private int counter = -1;
	private String header = "";
	private String type = "", order = "", name = "", title = "", enable = "", visible = "", locCat = "", funCat = "",
			uiScreen = "", uiPath = "", uiPanel = "";

	public void set(String head, String type, String order, String name, String title, String enable, String visible,
			String locCat, String funCat, String uiPanel, String uiScreen, String uiPath) {
		this.header = head;
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
	}

	public String getHeader() {
		return this.header;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		String string = this.name;
		if (string.length() <= 0)
			string = getElementLast(this.header);
		return string;
	}

	public String getNameWithSpace() {
		String string = this.name;
		if (string.length() <= 0) {
			String lastElement = getElementLast(this.header);
			string = toTitle(lastElement);
		}
		return string;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public String getTitleWithSpace() {
		String string = this.title;
		if (string.length() <= 0) {
			String lastElement = getElementLast(this.header);
			string = toTitle(lastElement);
		}
		return string;
	}

	public String getOrder() {
		return this.order;
	}

	public int getTaskOrder() {
		return Integer.parseInt(this.order);
	}

	public int getTaskLevel() {
		return letterCounter(this.header, spliter);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public TaskType getTaskType() {
		if (0 == this.type.compareToIgnoreCase("M")) {
			return TaskType.MENU;
		} else if (0 == this.type.compareToIgnoreCase("P")) {
			return TaskType.PANEL;
		} else if (0 == this.type.compareToIgnoreCase("S")) {
			return TaskType.IMAGE;
		} else {
			return TaskType.UNKNOW;
		}
	}

	public String getEnable() {
		return this.enable;
	}

	public boolean isTaskEnable() {
		return (this.enable.equalsIgnoreCase("1") ? true : false);
	}

	public String getVisibile() {
		return this.visible;
	}

	public boolean isTaskVisible() {
		return (visible.compareTo("1") == 1);
	}

	public String getLocCat() {
		return locCat;
	}

	public void setLocCat(String locCat) {
		this.locCat = locCat;
	}

	public String getFunCat() {
		return funCat;
	}

	public void setFunCat(String funCat) {
		this.funCat = funCat;
	}

	public int letterCounter(String str, char letter) {
		if (counter != -1)
			return counter;
		counter = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == letter)
				++counter;
		}
		return counter;
	}

	public String getElementLast(String string) {
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[strings.length - 1];
		}
		// Window.alert("getElementLast string["+string+"] >
		// element["+element+"]");
		return element;
	}

	public String getElement(String string, int index) {
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[index];
		}
		// Window.alert("getElement string["+string+"] > element["+element+"]");
		return element;
	}

	public String toTitle(String string) {
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
		return sb.toString();
	}

	public String getUiScreen() {
		return this.uiScreen;
	}

	public int getTaskUiScreen() {
		return Integer.parseInt(this.uiScreen);
	}

	public void setTaskUiScreen(int uiScreen) {
		this.uiScreen = Integer.toString(uiScreen);
	}

	public String getUiPath() {
		return uiPath;
	}

	public void setUiPath(String uiPath) {
		this.uiPath = uiPath;
	}

	public String getUiPanel() {
		return uiPanel;
	}

	public void setUiPanel(String uiPanel) {
		this.uiPanel = uiPanel;
	}
}
