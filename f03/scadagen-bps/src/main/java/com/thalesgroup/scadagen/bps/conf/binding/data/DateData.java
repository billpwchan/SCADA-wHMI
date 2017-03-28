package com.thalesgroup.scadagen.bps.conf.binding.data;

import java.util.Date;

/**
 * Implementation of IData storing a date value
 */
public class DateData extends DataAbstract {

	/** Date value */
    private Date value_;

    /**
     * Constructor
     * @param entityId entity identifier
     * @param inputName input name
     * @param value Date value
     * @param timestamp the timestamp of the data
     */
    public DateData(final String entityId,
                       final String inputName,
                       final Date value,
                       final long timestamp) {
        super(entityId, inputName, timestamp);
        value_ = value;
    }

	@Override
	public boolean getBooleanValue() {
		return (value_.getTime() != 0) ? true : false;
	}

	@Override
	public int getIntValue() {
		return (int) value_.getTime();
	}

	@Override
	public long getLongValue() {
		return value_.getTime();
	}

	@Override
	public float getFloatValue() {
		return value_.getTime();
	}

	@Override
	public double getDoubleValue() {
		return value_.getTime();
	}

	@Override
	public String getStringValue() {
		// TODO Auto-generated method stub
		return value_.toString();
	}

	@Override
	public Date getDateValue() {
		return value_;
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DateData[" + getEntityId() + "][" + getInputName() + "][" + value_.toString() + "]";
    }

}
