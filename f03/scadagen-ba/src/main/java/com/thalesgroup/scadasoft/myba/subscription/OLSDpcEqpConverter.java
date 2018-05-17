package com.thalesgroup.scadasoft.myba.subscription;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadasoft.datatypes.ScsFieldMap;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;

public class OLSDpcEqpConverter extends OLSConverter {

	private String m_className = null;
	
    public OLSDpcEqpConverter(Connector connector) {
        super(connector);
    }

    public OLSDpcEqpConverter(String className, Connector connector) {
    	super(className, connector);
        m_className = className;
    }
    
    @Override
    public AbstractEntityStatusesType convert2HV(ScsFieldMap olsrow, String olsid,
            AbstractEntityStatusesType previousEntity, long curTimeStamp) {
        if (m_className == null) {
            return null;
        }

        // map for bean editor
        Map<String, Object> attributes = new HashMap<String, Object>();

        // create alarm object
        AbstractEntityStatusesType entity = SCSConfManager.instance().createEntity(m_connector.getDataHelper(),
                m_className, olsid, attributes, curTimeStamp);

        AbstractAlarmType alarmEntity = null;
        if (entity instanceof AbstractAlarmType) {
            alarmEntity = (AbstractAlarmType) entity;

            StringAttributeType stringValue = new StringAttributeType();
            stringValue.setValue(SCSConfManager.instance().getServiceOwnerID());
            stringValue.setValid(true);
            stringValue.setTimestamp(curTimeStamp);
            alarmEntity.setServiceOwnerID(stringValue);

            String eqpName = getEqpFromOLS(olsrow);
            if (eqpName != null) {
            	StringAttributeType eqpNameAtt = new StringAttributeType();
            	eqpNameAtt.setValue(eqpName);
            	eqpNameAtt.setValid(true);
            	eqpNameAtt.setTimestamp(curTimeStamp);
            	alarmEntity.setSourceID(eqpNameAtt);
            } else {
            	alarmEntity.setSourceID(stringValue);
            }

            fillRemainingFields(entity, olsrow, curTimeStamp);
        }


        return alarmEntity;
    }
}
