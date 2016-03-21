package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgr;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionaryMgrEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelOlsCounter implements DictionaryMgrEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelOlsCounter.class.getName());
	
	private int rows = 3;
	private int cols = 5;

    
	String alarmlist_counter_all					= "alarmlist_counter_all";
	String alarmlist_counter_unack					= "alarmlist_counter_unack";
    String alarmlist_counter_supercritical_all 		= "alarmlist_counter_supercritical_all";
    String alarmlist_counter_supercritical_unack 	= "alarmlist_counter_supercritical_unack";
    String alarmlist_counter_critical_all 			= "alarmlist_counter_critical_all";
    String alarmlist_counter_critical_unack 		= "alarmlist_counter_critical_unack";
    String alarmlist_counter_lesscritical_all 		= "alarmlist_counter_lesscritical_all";
    String alarmlist_counter_lesscritical_unack 	= "alarmlist_counter_lesscritical_unack";
    
    private String [] counterNames = new String[] {
    		  "", "", "", "", ""
    		, "", alarmlist_counter_unack,  alarmlist_counter_supercritical_unack, alarmlist_counter_critical_unack, alarmlist_counter_lesscritical_unack
    		, "", alarmlist_counter_all, alarmlist_counter_supercritical_all, alarmlist_counter_critical_all, alarmlist_counter_lesscritical_all
    };
    
	String strEmpty			= "";
	String strChar3			= "0";
	String strTotal			= "Total";
	String strSuperCritical	= "Super";
	String strCritical		= "Critical";
	String strLessCritical	= "Less";
	String strAck			= "All";
	String strUnack			= "UnAck";
	
    private String [] strLabels = new String[] {
    		strEmpty, strTotal, strSuperCritical, strCritical, strLessCritical
			, strUnack, strChar3, strChar3, strChar3, strChar3
			, strAck, strChar3, strChar3, strChar3, strChar3};
    
    
    private TextBox txtValues[] = null;
    
    public String [] getCounterNames() {
    	return counterNames;
    }
	
    private UINameCard uiNameCard;
	public VerticalPanel getMainPanel (UINameCard uiNameCard) {
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		txtValues = new TextBox[strLabels.length];
		
	    
	    String strCSSs [] = new String[rows*cols];
	    for ( int x = 0 ; x < rows ; ++x ) {
	    	for ( int y = 0 ; y < cols ; ++y ) {
	    		logger.log(Level.FINE, "getMainPanel Build Filter Table Loop x["+x+"] y["+y+"] [project-gwt-textbox-alarmbanner-counter-"+x+"-"+y+" ["+(x*cols)+"]");
	    		strCSSs[(x*cols)+y] = "project-gwt-textbox-alarmbanner-counter-"+x+"-"+y+"";
	    	}
	    }
		
		logger.log(Level.FINE, "getMainPanel Build Filter Table Begin");
		
	    VerticalPanel statPanel = new VerticalPanel();
	    statPanel.addStyleName("project-gwt-panel-alarmbanner-filter");
	    statPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    statPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
		FlexTable flexTableAttibutes = new FlexTable();
	    statPanel.addStyleName("project-gwt-flextable-alarmbanner-filter");
	    for ( int i = 0 ; i < rows ; ++i ) {
			logger.log(Level.FINE, "getMainPanel Build Filter Table Loop ["+i+"]");
			
			for ( int j = 0 ; j < cols ; ++j ) {
				
				logger.log(Level.FINE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] => ["+(i*cols)+j+"]");
				
				txtValues[(i*cols)+j] = new TextBox();
				txtValues[(i*cols)+j].setText(strLabels[(i*cols)+j]);
				txtValues[(i*cols)+j].setWidth("100%");
				txtValues[(i*cols)+j].setReadOnly(true);
				txtValues[(i*cols)+j].addStyleName(strCSSs[(i*cols)+j]);
				flexTableAttibutes.setWidget(i, j, txtValues[(i*cols)+j]);
				
				logger.log(Level.FINE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] strLabel["+strLabels[(i*cols)+j]+"]");
				logger.log(Level.FINE, "getMainPanel Build Filter Table Loop i["+i+"] j["+j+"] strCSS["+strCSSs[(i*cols)+j]+"]");
			}
			
			logger.log(Level.FINE, "getMainPanel Build Color Label");
	    }
	    
	    for ( int j = 0 ; j < cols ; ++j ) {
	    	flexTableAttibutes.getColumnFormatter().setWidth(j, "40px");
	    }
		
		statPanel.add(flexTableAttibutes);
		
		logger.log(Level.FINE, "getMainPanel Build Filter Table End");
		
		
		logger.log(Level.FINE, "getMainPanel Get OlsFilter Diction Being");
		
		DictionaryMgr dictionaryMgr = new DictionaryMgr();
		String module = GWT.getModuleName();
		dictionaryMgr.getDictionary(module, "UIPanelOlsCounter.xml", "header", this);
		
		
		logger.log(Level.FINE, "getMainPanel Get OlsFilter Diction End");
		
		return statPanel;
		
	}
	
	public void updateCounter (String name, int value) {
		
		logger.log(Level.FINE, " **** updateCounter Begin");
		
		logger.log(Level.FINE, " **** updateCounter name["+name+"] value["+value+"]");
		
		int index = -1;
		for( int i=0 ; i < counterNames.length; i++ ) {
			if ( 0 == name.compareTo(counterNames[i]) ) {
				index = i;
				break;
			}
		}
		
		if ( index >= 0 && index < txtValues.length && null != txtValues[index] )
			this.txtValues[index].setText(String.valueOf(value));
		
		logger.log(Level.FINE, " **** updateCounter End");
	}

	@Override
	public void ready(Dictionary dictionary) {
		logger.log(Level.FINE, "ready Begin");
		
		if ( null != dictionary ) {
			String xmlFile = (String)dictionary.getAttribute("XmlFile");
			String XmlTag = (String)dictionary.getAttribute("XmlTag");
			String CreateDateTimeLabel = (String)dictionary.getAttribute("CreateDateTimeLabel");
			
			if (0 == XmlTag.compareTo("XmlTag") ) {
				logger.log(Level.FINE, "ready dictionary xmlFile["+xmlFile+"]");
				logger.log(Level.FINE, "ready dictionary XmlTag["+XmlTag+"]");
				logger.log(Level.FINE, "ready dictionary CreateDateTimeLabel["+CreateDateTimeLabel+"]");
				
				DictionaryMgr dictionaryMgr = new DictionaryMgr();
				String module = GWT.getModuleName();
				dictionaryMgr.getDictionary(module, "UIPanelOlsCounter.xml", "option", this);
				
			} else if (0 == XmlTag.compareTo("option") ) {
				logger.log(Level.FINE, "ready dictionary xmlFile["+xmlFile+"]");
				logger.log(Level.FINE, "ready dictionary XmlTag["+XmlTag+"]");
				logger.log(Level.FINE, "ready dictionary CreateDateTimeLabel["+CreateDateTimeLabel+"]");
				
				for ( Object o : dictionary.getValueKeys() ) {
					
					logger.log(Level.FINE, "ready dictionary d1");
					
					if ( null != o ) {
						Dictionary d2 = (Dictionary) dictionary.getValue(o);
						
						logger.log(Level.FINE, "ready dictionary d1");
						
						for ( Object o2 : d2.getAttributeKeys() ) {
							
							logger.log(Level.FINE, "ready dictionary d2 getAttributeKeys");
							
							if ( null != o2 ) {
								logger.log(Level.FINE, "ready dictionary getAttributeKeys["+(String)o2+"]["+(String)d2.getValue(o2)+"]");
							} else {
								logger.log(Level.FINE, "ready dictionary getAttributeKeys o2 IS NULL");
							}
						
						}
						
						for ( Object o2 : d2.getValueKeys() ) {
							
							logger.log(Level.FINE, "ready dictionary d2 getValueKeys");
							
							if ( null != o2 ) {
								logger.log(Level.FINE, "ready dictionary getValueKeys["+(String)o2+"]["+(String)d2.getValue(o2)+"]");
							} else {
								logger.log(Level.FINE, "ready dictionary getValueKeys o2 IS NULL");
							}
							
						}
					} else {
						logger.log(Level.FINE, "ready dictionary o IS NULL");
					}
					

				}
				
			}
			

		} else {
			logger.log(Level.FINE, "ready dictionary IS NULL");
		}
		
		
		logger.log(Level.FINE, "ready End");
		
	}

	@Override
	public void failed(String xmlFile) {
		logger.log(Level.FINE, "failed xmlFile["+xmlFile+"]");
		
	}
}
