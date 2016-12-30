package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

public class EquipmentBuilderImpl implements EquipmentBuilder_i {
	public Equipment_i equipment_i = null;

	public EquipmentBuilderImpl() {
		equipment_i = new Equipment();
	}
	
	@Override
	public EquipmentBuilder_i setValue(String key, Object value) {
		equipment_i.setValue(key, value);
		return this;
	}

	@Override
	public Equipment_i build() {
		return equipment_i;
	}

}
