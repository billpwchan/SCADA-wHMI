package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PageCounter {
	
	private Logger logger = Logger.getLogger(PageCounter.class.getName());
		
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

		logger.log(Level.FINE, "calc numOfPoint["+numOfPoint+"] pageSize["+pageSize+"] pageCount:pageMod["+pageCount+":"+pageMod+"] pageRowBegin:pageRowEnd:pageRowCount["+pageRowBegin+":"+pageRowEnd+":"+pageRowCount+"] hasPreview:hasNext["+hasPreview+":"+hasNext+"]");

	}
}
