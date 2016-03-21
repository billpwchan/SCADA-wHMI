package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.history;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutHistory;

public class ViewLayoutHistoryMgr {
	
	private static Logger logger = Logger.getLogger(ViewLayoutHistoryMgr.class.getName());

	private static int HISTORY_LIMIT = 20;
	
	private int index=0;
	public int getIndex() { return index; }
	public void setIndex(int index) { this.index = index; }
	
	public int getSize() { return viewLayoutHistorys.size(); }

	private LinkedList<ViewLayoutHistory> viewLayoutHistorys;
	public ViewLayoutHistoryMgr() {
		viewLayoutHistorys = new LinkedList<ViewLayoutHistory>();
	}
	public boolean hasHistory() {
		logger.log(Level.FINE, "hasHistory Begin");
		boolean result = false;
		if ( viewLayoutHistorys.size() > 0 ) {
			result = true;
		}
		logger.log(Level.FINE, "hasPrevious result["+result+"] End");
		return result;
	}
	public void clean() {
		
		logger.log(Level.FINE, "clean Begin");
		
		this.viewLayoutHistorys.clear();
		
		logger.log(Level.FINE, "clean End");
	}
	public void add(ViewLayoutHistory history) {
		
		logger.log(Level.FINE, "add Begin");
		
		if ( viewLayoutHistorys.size() != 0 && index < viewLayoutHistorys.size() ) ++index; 
		
		this.viewLayoutHistorys.add(index, history);
		
		if ( viewLayoutHistorys.size() > HISTORY_LIMIT) {
			viewLayoutHistorys.poll();
			if ( index > 0 ) --index;
		}
		
		logger.log(Level.FINE, "add End");
	}
	public boolean hasPrevious() {
		boolean result = false;
		
		logger.log(Level.FINE, "hasPrevious Begin");
		
		if ( viewLayoutHistorys.size() > 1 && index > 0 ) {
			result = true;
		}
		
		logger.log(Level.FINE, "hasPrevious result["+result+"] End");
		
		return result;
	}
	public boolean hasNext() {
		boolean result = false;
		
		logger.log(Level.FINE, "hasNext Begin");
		
		if ( viewLayoutHistorys.size() > 1 && index+1 < viewLayoutHistorys.size() ) {
			result = true;
		}
		
		logger.log(Level.FINE, "hasNext result["+result+"] End");
		
		return result;
	}

	public ViewLayoutHistory previous() {
		
		logger.log(Level.FINE, "previous Begin");
		
		ViewLayoutHistory previous = null;
		
		if ( hasPrevious() ) {
			if ( index > 0 ) {
				previous = viewLayoutHistorys.get(--index);
			}
		}
		
		logger.log(Level.FINE, "previous End");
		
		return previous;
	}
	public ViewLayoutHistory next() {
		
		logger.log(Level.FINE, "next Begin");
		
		ViewLayoutHistory next = null;
		
		if ( hasNext() ) {
			if ( index < viewLayoutHistorys.size() ) {
				next = viewLayoutHistorys.get(++index);
			}
		}
		
		logger.log(Level.FINE, "next End");
		
		return next;
	}

	public void debug(String prefix) {
		
		logger.log(Level.SEVERE, "debug "+prefix+" Begin");
		
		logger.log(Level.SEVERE, "debug "+prefix+" taskLaunchs.length: [" + viewLayoutHistorys.size() +"]");
		logger.log(Level.SEVERE, "debug "+prefix+" index: [" + index +"]");
		
		for ( int i = 0 ; i < viewLayoutHistorys.size() ; ++i ) {
			ViewLayoutHistory viewLayoutHistory = viewLayoutHistorys.get(i);
			if ( null != viewLayoutHistory ) {
				logger.log(Level.SEVERE, "debug "+prefix+" viewLayoutHistory(" + i + ")[" + viewLayoutHistorys.size() + "]");
				viewLayoutHistory.debug("viewLayoutHistory(" + i + ")");
			} else {
				logger.log(Level.SEVERE, "debug "+prefix+" viewLayoutHistory(" + i + ") is null");
			}
		}
		
		logger.log(Level.SEVERE, "debug "+prefix+" End");
		
	}
}