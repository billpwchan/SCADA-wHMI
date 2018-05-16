package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.history;

import java.util.LinkedList;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutHistory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class ViewLayoutHistoryMgr {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

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
		final String function = "hasHistory";
		
		logger.begin(function);
		boolean result = false;
		if ( viewLayoutHistorys.size() > 0 ) {
			result = true;
		}
		logger.info(function, "hasPrevious result[{}] End", result);
		
		logger.end(function);
		return result;
	}
	public void clean() {
		final String function = "clean";
		
		logger.begin(function);
		
		this.viewLayoutHistorys.clear();
		
		logger.end(function);
	}
	public void add(ViewLayoutHistory history) {
		add(history, true, true);
	}
	public void add(ViewLayoutHistory history, boolean removeAfterCurrentIndex, boolean removeFirstWhenFull) {
		final String function = "add";
		
		logger.begin(function);
		
		logger.info(function, "index[{}]", index);
		
		if ( viewLayoutHistorys.size() != 0 && index < viewLayoutHistorys.size() ) ++index; 
		
		this.viewLayoutHistorys.add(index, history);
		
		if ( removeAfterCurrentIndex ) {
			
			int afCurIdx = index+1;
			
			logger.info(function, "viewLayoutHistorys.sublist({},{}).clear()", afCurIdx, viewLayoutHistorys.size());
		
			this.viewLayoutHistorys.subList(afCurIdx, viewLayoutHistorys.size()).clear();
		}
		
		if ( viewLayoutHistorys.size() > HISTORY_LIMIT) {
			if ( removeFirstWhenFull ) {
				viewLayoutHistorys.removeFirst();
			} else {
				viewLayoutHistorys.removeLast();
			}
			if ( index > 0 ) --index;
		}
		
		logger.info(function, "viewLayoutHistorys.size[{}] index[{}]", viewLayoutHistorys.size(), index);
		
		logger.end(function);
	}
	public boolean hasPrevious() {
		final String function = "hasPrevious";
		
		boolean result = false;
		
		logger.begin(function);
		
		if ( viewLayoutHistorys.size() > 1 && index > 0 ) {
			result = true;
		}
		
		logger.info(function, "hasPrevious result[{}] End", result);
		
		logger.end(function);
		
		return result;
	}
	public boolean hasNext() {
		final String function = "hasNext";
		
		boolean result = false;
		
		logger.begin(function);
		
		if ( viewLayoutHistorys.size() > 1 && index+1 < viewLayoutHistorys.size() ) {
			result = true;
		}
		
		logger.end(function);
		
		return result;
	}

	public ViewLayoutHistory previous() {
		final String function = "previous";
		
		logger.begin(function);
		
		ViewLayoutHistory previous = null;
		
		if ( hasPrevious() ) {
			if ( index > 0 ) {
				previous = viewLayoutHistorys.get(--index);
			}
		}
		
		logger.end(function);
		
		return previous;
	}
	public ViewLayoutHistory next() {
		final String function = "next";
		
		logger.begin(function);
		
		ViewLayoutHistory next = null;
		
		if ( hasNext() ) {
			if ( index < viewLayoutHistorys.size() ) {
				next = viewLayoutHistorys.get(++index);
			}
		}
		
		logger.end(function);
		
		return next;
	}

	public void debug(String prefix) {
		final String function = "debug";
		
		logger.begin(function);
		
		logger.info(function, "[{}] taskLaunchs.length: [{}]", prefix, viewLayoutHistorys.size());
		logger.info(function, "[{}] index: [{}, ]", prefix, index);
		
		for ( int i = 0 ; i < viewLayoutHistorys.size() ; ++i ) {
			ViewLayoutHistory viewLayoutHistory = viewLayoutHistorys.get(i);
			if ( null != viewLayoutHistory ) {
				logger.info(function, "[{}] viewLayoutHistory({})[" + viewLayoutHistorys.size() + "]", prefix, i);
				viewLayoutHistory.debug("viewLayoutHistory("+i+")");
			} else {
				logger.info(function, "[{}] viewLayoutHistory({}) is null", prefix, i);
			}
		}
		
		logger.end(function);
		
	}
}