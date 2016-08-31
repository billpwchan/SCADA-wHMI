package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class PageCounter {
	
	private final String className = UIWidgetUtil.getClassSimpleName(PageCounter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
		
	int numOfPoint		= 0;
	int pageSize		= 0;
	
	int pageCount		= 0;
	int pageMod			= 0;
	
	int pageRowBegin	= 0;
	int pageRowEnd		= 0;
	int pageRowCount	= 0;
	
	boolean hasPreview 	= false;
	boolean hasNext		= false;
	
	public PageCounter(int numOfPoint, int pageSize) {
		
		this.numOfPoint	= numOfPoint;
		this.pageSize	= pageSize;

		this.pageCount	= this.numOfPoint / this.pageSize;
		this.pageMod	= this.numOfPoint % this.pageSize;
		if ( this.pageMod != 0 ) ++this.pageCount;
	}
	
	public void calc ( int pageIndex ) {
		final String function = "calc";
		
		logger.begin(className, function);
		
		this.pageRowBegin = pageSize * pageIndex;
		
		int i = 0;
		this.pageRowEnd = this.pageRowBegin;
		while ( i < this.pageSize && this.pageRowEnd < this.numOfPoint ) {
			++this.pageRowEnd;
			++i;
		}

		this.pageRowCount	= i; 
		
		this.hasPreview		= ( this.pageCount > 1 && pageIndex-1 >= 0 );
		this.hasNext		= ( this.pageCount > 1 && pageIndex+1 < this.pageCount );

		logger.debug(className, function, "calc numOfPoint[{}] pageSize[{}] pageCount:pageMod[{}:{}] pageRowBegin:pageRowEnd:pageRowCount[{}:{}:{}] hasPreview:hasNext[{}:{}]"
				, new Object[]{numOfPoint, pageSize, pageCount, pageMod, pageRowBegin, pageRowEnd, pageRowCount, hasPreview, hasNext});

		logger.end(className, function);
	}
}
