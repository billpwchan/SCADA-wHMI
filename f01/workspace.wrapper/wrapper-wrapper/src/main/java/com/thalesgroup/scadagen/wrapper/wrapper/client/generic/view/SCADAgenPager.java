package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view.SCADAgenPAger_i.ParameterName;

public class SCADAgenPager extends SimplePager {
	
	private static final String className = UIWidgetUtil.getClassSimpleName(SCADAgenPager.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private int fastForwardRows = 0;
	public void setFastForwardRows(int fastForwardRows) {
		this.fastForwardRows = fastForwardRows;
	}

    public int getFastForwardPages() {
    	int pageSize = getPageSize();
    	return pageSize > 0 ? fastForwardRows / pageSize : 0;
    }
    
    public void fastForwardPage() {
    	setPage(getPage() + getFastForwardPages());
    }

	@Override
	protected void onRangeOrRowCountChanged() {
		String function = "onRangeOrRowCountChanged";
		super.onRangeOrRowCountChanged();
		
		logger.begin(className, function);
		
		if (null != buttonOperation) {
			{
				String hasType = ParameterName.HasPreviousPage.toString();
				boolean hasPreviousPage = hasPreviousPage();
				logger.debug(className, function, "hasType[{}] hasPreviousPage[{}]", hasType, hasPreviousPage);
				buttonOperation.buttonOperation(hasType, hasPreviousPage);
			}
			{
				String hasType = ParameterName.HasNextPage.toString();
				boolean hasNextPage = hasNextPage();
				logger.debug(className, function, "hasType[{}] hasPreviousPage[{}]", hasType, hasNextPage);
				buttonOperation.buttonOperation(hasType, hasNextPage);
			}
			{
				String hasType = ParameterName.HasFastForwardPage.toString();
				boolean hasFastForwardPage = hasNextPages(getFastForwardPages());
				logger.debug(className, function, "hasType[{}] hasPreviousPage[{}]", hasType, hasFastForwardPage);
				buttonOperation.buttonOperation(hasType, hasFastForwardPage);
			}

		} else {
			logger.debug(className, function, "buttonOperation IS NULL");
		}
		
		logger.end(className, function);
	}

	/**
	 * Get the text to display in the pager that reflects the state
	 * of the pager.
	 * 
	 * @return the text
	 */
	protected String createText() {
		String function = "createText";
		
		logger.begin(className, function);

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

			logger.debug(className, function, "createText IS NULL");

		}

		logger.debug(className, function, "strPage[{}]", strPage);

		logger.end(className, function);
		
		return strPage;
	}
	
	private CreateText_i createText = null;
	public void setCreateText(CreateText_i createText) { this.createText = createText; }
	
	private ButtonOperation_i buttonOperation = null;
	public void setButtonOperation(ButtonOperation_i buttonOperation) { this.buttonOperation = buttonOperation; }
}
