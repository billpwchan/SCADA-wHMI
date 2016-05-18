package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.mwt.hypervisor.mwt.core.webapp.core.ui.client.dictionary.WrapperDictionary;

public class UIInspectorInfoPanel {

	ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		HorizontalPanel hp = new HorizontalPanel();
		
		InlineLabel label = new InlineLabel();
		
		hp.add(label);
		
		String caption = WrapperDictionary.Get("info_dialog_disconnected");
		
		label.setText(caption);
		
		return hp;
	}
}
