package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.BooleanAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DateTimeAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DoubleAttributeType;
import com.thalesgroup.hv.data_v1.attribute.FloatAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.LongAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;

public class HvDataWrapper extends DataAbstract {
	
	private AbstractAttributeType hvAttribute_;

	/**
     * Constructor
     * @param entityId the identifier of the hypervisor identifier
     * @param field the scadasoft dbm field
     */
    public HvDataWrapper(final String entityId, final String attributeName, final AbstractAttributeType attribute) {
    	super(entityId, attributeName, attribute.getTimestamp());
	    hvAttribute_ = attribute;
    }

	@Override
	public boolean getBooleanValue() {
		BooleanAttributeType att = (BooleanAttributeType)hvAttribute_;
		return att.isValue();
	}

	@Override
	public int getIntValue() {
		IntAttributeType att = (IntAttributeType)hvAttribute_;
		return att.getValue();
	}

	@Override
	public long getLongValue() {
		LongAttributeType att = (LongAttributeType)hvAttribute_;
		return att.getValue();
	}

	@Override
	public float getFloatValue() {
		FloatAttributeType att = (FloatAttributeType)hvAttribute_;
		return att.getValue();
	}

	@Override
	public double getDoubleValue() {
		DoubleAttributeType att = (DoubleAttributeType)hvAttribute_;
		return att.getValue();
	}

	@Override
	public String getStringValue() {
		StringAttributeType att = (StringAttributeType)hvAttribute_;
		return att.getValue();
	}

	@Override
	public Date getDateValue() {
		DateTimeAttributeType att = (DateTimeAttributeType)hvAttribute_;
		return new Date(att.getTimestamp());
	}

}
