package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.SCADAgenPager_i.ParameterName;

public class SCADAgenPager extends SimplePager {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private int fastForwardRows = 0;
	public void setFastForwardRows(int fastForwardRows) {
		this.fastForwardRows = fastForwardRows;
	}
	
	private int fastBackwardRows = 0;
	public void setFastBackwardRows(int fastBackwardRows) {
		this.fastBackwardRows = fastBackwardRows;
	}

    public int getFastForwardPages() {
    	int pageSize = getPageSize();
    	return pageSize > 0 ? fastForwardRows / pageSize : 0;
    }
    
    public int getFastBackwardPages() {
    	int pageSize = getPageSize();
    	return pageSize > 0 ? fastBackwardRows / pageSize : 0;
    }
    
    public void fastForwardPage() {
    	setPage(getPage() + getFastForwardPages());
    }
    
    public void fastBackwardPage() {
    	setPage(getPage() - getFastBackwardPages());
    }

	@Override
	protected void onRangeOrRowCountChanged() {
		String function = "onRangeOrRowCountChanged";
		super.onRangeOrRowCountChanged();
		
		logger.begin(function);
		
		if (null != buttonOperation) {
			{
				String hasType = ParameterName.HasPreviousPage.toString();
				boolean hasPreviousPage = hasPreviousPage();
				logger.debug(function, "hasType[{}] hasPreviousPage[{}]", hasType, hasPreviousPage);
				buttonOperation.buttonOperation(hasType, hasPreviousPage);
			}
			{
				String hasType = ParameterName.HasNextPage.toString();
				boolean hasNextPage = hasNextPage();
				logger.debug(function, "hasType[{}] hasNextPage[{}]", hasType, hasNextPage);
				buttonOperation.buttonOperation(hasType, hasNextPage);
			}
			{
				String hasType = ParameterName.HasFastBackwardPage.toString();
				boolean hasFastBackwardPage = hasPreviousPages(getFastBackwardPages());
				logger.debug(function, "hasType[{}] hasFastBackwardPage[{}]", hasType, hasFastBackwardPage);
				buttonOperation.buttonOperation(hasType, hasFastBackwardPage);
			}
			{
				String hasType = ParameterName.HasFastForwardPage.toString();
				boolean hasFastForwardPage = hasNextPages(getFastForwardPages());
				logger.debug(function, "hasType[{}] hasFastForwardPage[{}]", hasType, hasFastForwardPage);
				buttonOperation.buttonOperation(hasType, hasFastForwardPage);
			}

		} else {
			logger.debug(function, "buttonOperation IS NULL");
		}
		
		logger.end(function);
	}

	/**
	 * Get the text to display in the pager that reflects the state
	 * of the pager.
	 * 
	 * @return the text
	 */
	protected String createText() {
		String function = "createText";
		
		logger.begin(function);

		// Default text is 1 based.
		HasRows display = getDisplay();
		Range range = display.getVisibleRange();
		int pageStart = range.getStart() + 1;
		int pageSize = range.getLength();
		int dataSize = display.getRowCount();
		int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
		endIndex = Math.max(pageStart, endIndex);
		boolean exact = display.isRowCountExact();

		String strPage = null;
		if (null != createText) {
			strPage = createText.CreateText(pageStart, endIndex, exact, dataSize);
			
			createText.pageStart(pageStart);
			createText.endIndex(endIndex);
			createText.exact(exact, dataSize);
			
		} else {

			logger.debug(function, "createText IS NULL");

		}

		logger.debug(function, "strPage[{}]", strPage);

		logger.end(function);
		
		return strPage;
	}
	
	private CreateText_i createText = null;
	public void setCreateText(CreateText_i createText) { this.createText = createText; }
	
	private ButtonOperation_i buttonOperation = null;
	public void setButtonOperation(ButtonOperation_i buttonOperation) { this.buttonOperation = buttonOperation; }
}
