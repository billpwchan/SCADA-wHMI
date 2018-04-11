package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.page;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class PageCounter {
	
	private final String cls = this.getClass().getName();
	private final String className = UIWidgetUtil.getClassSimpleName(cls);
	private UILogger logger = UILoggerFactory.getInstance().getLogger(UIWidgetUtil.getClassName(cls));
		
	public int numOfPoint	= 0;
	public int pageSize		= 0;
	
	public int pageCount	= 0;
	public int pageMod		= 0;
	
	public int pageRowBegin	= 0;
	public int pageRowEnd	= 0;
	public int pageRowCount	= 0;
	
	public boolean hasPreview 	= false;
	public boolean hasNext		= false;
	
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
