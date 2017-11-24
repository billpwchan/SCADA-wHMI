package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class SCADAgenCheckBoxCell extends AbstractEditableCell<Boolean, Boolean> {

	/**
	 * An html string representation of a checked input box.
	 */
	private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked/>");

	/**
	 * An html string representation of an unchecked input box.
	 */
	private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\"/>");

	private static final SafeHtml INPUT_CHECKED_DISABLED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked disabled=\"disabled\"/>");

	private static final SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils
			.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" disabled=\"disabled\"/>");

	private final boolean dependsOnSelection;
	private final boolean handlesSelection;

	private final String className = "SCADAgenCheckBoxCell";
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public boolean isCheckBoxDisabled_ = false;
	
	public SCADAgenCheckBoxCell(boolean dependsOnSelection, boolean handlesSelection) {
		super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
		this.dependsOnSelection = dependsOnSelection;
		this.handlesSelection = handlesSelection;
	}

	@Override
	public boolean dependsOnSelection() {
		return dependsOnSelection;
	}

	@Override
	public boolean handlesSelection() {
		return handlesSelection;
	}

	@Override
	public boolean isEditing(Context context, Element parent, Boolean value) {
		// A checkbox is never in "edit mode". There is no intermediate state
		// between checked and unchecked.
		return false;
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event,
			ValueUpdater<Boolean> valueUpdater) {
		String type = event.getType();

		boolean enterPressed = BrowserEvents.KEYDOWN.equals(type) && event.getKeyCode() == KeyCodes.KEY_ENTER;
		if (BrowserEvents.CHANGE.equals(type) || enterPressed) {
			InputElement input = parent.getFirstChild().cast();
			Boolean isChecked = input.isChecked();

			/*
			 * Toggle the value if the enter key was pressed and the cell
			 * handles selection or doesn't depend on selection. If the cell
			 * depends on selection but doesn't handle selection, then ignore
			 * the enter key and let the SelectionEventManager determine which
			 * keys will trigger a change.
			 */
			if (enterPressed && (handlesSelection() || !dependsOnSelection())) {
				isChecked = !isChecked;
				input.setChecked(isChecked);
			}

			/*
			 * Save the new value. However, if the cell depends on the
			 * selection, then do not save the value because we can get into an
			 * inconsistent state.
			 */
			if (value != isChecked && !dependsOnSelection()) {
				setViewData(context.getKey(), isChecked);
			} else {
				clearViewData(context.getKey());
			}

			if (valueUpdater != null) {
				valueUpdater.update(isChecked);
			}
		}
	}

	@Override
	  public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
	    // Get the view data.
	    Object key = context.getKey();
	    Boolean viewData = getViewData(key);
	    if (viewData != null && viewData.equals(value)) {
	      clearViewData(key);
	      viewData = null;
	    }
	    logger.debug("[SCADAgenCheckBoxCell] render Triggered!!!!");
	    logger.debug( className, "render", "isCheckBoxDisabled_:[{}]", isCheckBoxDisabled_);
	    
	    boolean checked = (viewData != null ? viewData : value);
	    boolean enabled = !isCheckBoxDisabled_;

	    if (checked && !enabled) {
	      sb.append(INPUT_CHECKED_DISABLED);
	    } else if (!checked && !enabled) {
	      sb.append(INPUT_UNCHECKED_DISABLED);
	    } else if (checked && enabled) {
	      sb.append(INPUT_CHECKED);
	    } else if (!checked && enabled) {
	      sb.append(INPUT_UNCHECKED);
	  }
	}
}
