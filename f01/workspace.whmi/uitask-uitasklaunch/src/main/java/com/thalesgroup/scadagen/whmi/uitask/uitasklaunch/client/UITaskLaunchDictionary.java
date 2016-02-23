package com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client;

import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;

public class UITaskLaunchDictionary extends UITaskDictionary {
	
	private static Logger logger = Logger.getLogger(UITaskLaunchDictionary.class.getName());
	
	public enum TaskLaunchType {
		MENU("MENU")
		, PANEL("PANEL")
		, IMAGE("IMAGE")
		, UNKNOW("UNKNOW")
	    ;

	    private final String text;

	    /**
	     * @param text
	     */
	    private TaskLaunchType(final String text) {
	        this.text = text;
	    }
	    
	    public static TaskLaunchType findByString(String text){
	        for(TaskLaunchType v : values()){
	            if( v.text.equals(text)){
	                return v;
	            }
	        }
	        return null;
	    }
	    
	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	public UITaskLaunchDictionary() {
		super();
	}
	
	public UITaskLaunchDictionary(Task task) {
		
		logger.log(java.util.logging.Level.FINE, "TaskLaunch Task Begin");
		
		setHeader(			task.getHeader());
		
		setFirst(			getElementFirst(getHeader()));
		setLast(			getElementLast(getHeader()));

		setName(			task.getName());
				
		setTitle(			task.getTitle());
		
		setType(			task.getType());
		setOrder(			task.getOrder());	
		
		setLevel(			String.valueOf(letterCounter(getHeader(), '|')));		
		
		setEnable(			task.getEnable());
		setVisible(			task.getVisibile());
		setLocCat(			task.getLocCat());
		setFunCat(			task.getFunCat());
		
		setUiPanel(			task.getUiPanel());
		setUiScreen(		task.getUiScreen());
		setUiPath(			task.getUiPath());
		
		logger.log(java.util.logging.Level.FINE, "TaskLaunch Task End");
	}

	public UITaskLaunchDictionary(UITaskLaunchDictionary uitaskLaunch) {
		
		super(uitaskLaunch);
		
		logger.log(java.util.logging.Level.FINE, "TaskLaunch TaskLaunch Begin");
		
		for ( String string : strings) {
			setValue(string, uitaskLaunch.getValue(string));
		}
		
		logger.log(java.util.logging.Level.FINE, "TaskLaunch TaskLaunch End");
	}
	
	public static final String Header			= "Header";
	public static final String First			= "First";
	public static final String FirstWithSpace	= "FirstWithSpace";
	public static final String Last				= "Last";
	public static final String LastWithSpace	= "LastWithSpace";
	public static final String Name				= "Name";
	public static final String NameWithSpace	= "NameWithSpace";
	public static final String Title			= "Title";
	public static final String Order			= "Order";
	public static final String Level			= "Level";
	public static final String Type				= "Type";
	public static final String Enable			= "Enable";
	public static final String Visible			= "Visible";
	public static final String LocCat			= "LocCat";
	public static final String FunCat			= "FunCat";
	public static final String UiPanel			= "UiPanel";
	
	private String [] strings = new String[] {
			Header
			, First
			, FirstWithSpace
			, Last
			, LastWithSpace
			, Name
			, NameWithSpace
			, Title
			, Order
			, Level
			, Type
			, Enable
			, Visible
			, LocCat
			, FunCat
			, UiPanel
	};
	
	public static char getSplite() { return '|'; }

	public String getHeader() { return (String)getValue(Header); }
	public void setHeader(String value) { setValue(Header, value); }
	
	public String getFirst() { return (String)getValue(First); }
	public void setFirst(String value) { setValue(First, value); }
	public String getFirstWithSpace() { return (String)getValue(FirstWithSpace); }
	
	public String getLast() { return (String)getValue(Last); }
	public void setLast(String value) { setValue(Last, value); }
	public String getLastWithSpace() { return (String)getValue(LastWithSpace); }
	
	public String getName() { return (String)getValue(Name); }
	public void setName(String value) { setValue(Name, value); }
	public String getNameWithSpace() { return (String)getValue(NameWithSpace); }
	
	public String getTitle() { return (String)getValue(Title); }
	public void setTitle(String value) { setValue(Title, value); }
	
	public String getOrder() { return (String)getValue(Order); }
	public void setOrder(String value) { setValue(Order, value); }
	
	public String getLevel() { return (String)getValue(Level); }
	public void setLevel(String value) { setValue(Level, value); }
	public int getTaskLevel() { return Integer.parseInt((String)getValue(Level)); }
	
	public String getType() { return (String)getValue(Type); }
	public void setType(String value) { setValue(Type, value); }
	public TaskLaunchType getTaskLaunchType() { return TaskLaunchType.findByString((String)getValue(Type)); }
	
	public String getEnable() { return (String)getValue(Enable); }
	public void setEnable(String value) { setValue(Enable, value); }
	
	public String getVisible() { return (String)getValue(Visible); }
	public void setVisible(String value) { setValue(Visible, value); }
	
	public String getLocCat() { return (String)getValue(LocCat); }
	public void setLocCat(String value) { setValue(LocCat, value); }
	
	public String getFunCat() { return (String)getValue(FunCat); }
	public void setFunCat(String value) { setValue(FunCat, value); }
	
	public String getUiPanel() { return (String)getValue(UiPanel); }
	public void setUiPanel(String value) { setValue(UiPanel, value); }
	
	public String getElementFirst(String string) {
		
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[0];
		}
		
		return element;
	}

	public String getElementLast(String string) {
		
		String element = "";
		String strings[] = string.split("\\|");
		if (strings.length > 0) {
			element = strings[strings.length - 1];
		}
		
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
		
		return sb.toString();
	}
	
	public int letterCounter(String str, char letter) {
		
		int counter = -1;
		if (counter != -1)
			return counter;
		counter = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == letter)
				++counter;
		}
		
		return counter;
	}
	
}
