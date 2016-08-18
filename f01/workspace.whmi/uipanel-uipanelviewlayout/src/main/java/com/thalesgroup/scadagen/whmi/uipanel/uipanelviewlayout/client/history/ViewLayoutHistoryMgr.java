package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.history;

import java.util.LinkedList;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.ViewLayoutHistory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class ViewLayoutHistoryMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(ViewLayoutHistoryMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

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
		
		logger.begin(className, function);
		boolean result = false;
		if ( viewLayoutHistorys.size() > 0 ) {
			result = true;
		}
		logger.info(className, function, "hasPrevious result[{}] End", result);
		
		logger.end(className, function);
		return result;
	}
	public void clean() {
		final String function = "clean";
		
		logger.begin(className, function);
		
		this.viewLayoutHistorys.clear();
		
		logger.end(className, function);
	}
	public void add(ViewLayoutHistory history) {
		final String function = "add";
		
		logger.begin(className, function);
		
		if ( viewLayoutHistorys.size() != 0 && index < viewLayoutHistorys.size() ) ++index; 
		
		this.viewLayoutHistorys.add(index, history);
		
		if ( viewLayoutHistorys.size() > HISTORY_LIMIT) {
			viewLayoutHistorys.poll();
			if ( index > 0 ) --index;
		}
		
		logger.end(className, function);
	}
	public boolean hasPrevious() {
		final String function = "hasPrevious";
		
		boolean result = false;
		
		logger.begin(className, function);
		
		if ( viewLayoutHistorys.size() > 1 && index > 0 ) {
			result = true;
		}
		
		logger.info(className, function, "hasPrevious result[{}] End", result);
		
		logger.end(className, function);
		
		return result;
	}
	public boolean hasNext() {
		final String function = "hasNext";
		
		boolean result = false;
		
		logger.begin(className, function);
		
		if ( viewLayoutHistorys.size() > 1 && index+1 < viewLayoutHistorys.size() ) {
			result = true;
		}
		
		logger.end(className, function);
		
		return result;
	}

	public ViewLayoutHistory previous() {
		final String function = "previous";
		
		logger.begin(className, function);
		
		ViewLayoutHistory previous = null;
		
		if ( hasPrevious() ) {
			if ( index > 0 ) {
				previous = viewLayoutHistorys.get(--index);
			}
		}
		
		logger.end(className, function);
		
		return previous;
	}
	public ViewLayoutHistory next() {
		final String function = "next";
		
		logger.begin(className, function);
		
		ViewLayoutHistory next = null;
		
		if ( hasNext() ) {
			if ( index < viewLayoutHistorys.size() ) {
				next = viewLayoutHistorys.get(++index);
			}
		}
		
		logger.end(className, function);
		
		return next;
	}

	public void debug(String prefix) {
		final String function = "debug";
		
		logger.begin(className, function);
		
		logger.info(className, function, "[{}] taskLaunchs.length: [{}]", prefix, viewLayoutHistorys.size());
		logger.info(className, function, "[{}] index: [{}, ]", prefix, index);
		
		for ( int i = 0 ; i < viewLayoutHistorys.size() ; ++i ) {
			ViewLayoutHistory viewLayoutHistory = viewLayoutHistorys.get(i);
			if ( null != viewLayoutHistory ) {
				logger.info(className, function, "[{}] viewLayoutHistory({})[" + viewLayoutHistorys.size() + "]", prefix, i);
				viewLayoutHistory.debug("viewLayoutHistory("+i+")");
			} else {
				logger.info(className, function, "[{}] viewLayoutHistory({}) is null", prefix, i);
			}
		}
		
		logger.end(className, function);
		
	}
}