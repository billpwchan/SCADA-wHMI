package com.thalesgroup.scadasoft.myba.subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.tools.DateUtils;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.alarm.attribute.Alarm4StateEnum;
import com.thalesgroup.hv.data_v1.alarm.attribute.PriorityAttributeType;
import com.thalesgroup.hv.data_v1.alarm.attribute.StateAttributeType;
import com.thalesgroup.hv.data_v1.attribute.BooleanAttributeType;
import com.thalesgroup.hv.data_v1.attribute.DateTimeAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadasoft.datatypes.ScsFieldMap;
import com.thalesgroup.scadasoft.datatypes.client.ScsData;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager.StatusMapping;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;

public class OLSAlmConverter extends OLSConverter {
	
	private static final Logger s_logger = LoggerFactory.getLogger(OLSAlmConverter.class);

	private String m_className = null;
	
    public OLSAlmConverter(Connector connector) {
        super(connector);
    }

    public OLSAlmConverter(String className, Connector connector) {
    	super(className, connector);
        m_className = className;
    }
    
    /*
     * SCADAsoft Message field encode HV fields using separator || and |;
     * 
     * Message= SCSpart||HVfield1||HVfield2||HVfield3 first field is ignore by
     * hypervisor it will be used by scsvisu
     * 
     * Each field is a key value pair separated by |; i.e. key|;value
     * 
     * a key map to an alarm field as defined in XSD datamodel
     * 
     * One specific key is hvalarmclass it contains the name of the alarm object
     * to create e.g.
     * com.thalesgroup.scadasoft.sample.data.alarm.SCADAsoftAlarmType
     * 
     * The alarmText key use a specific encoding for the value (it is a hashmap)
     * alarmText|;msgId==MSG01;;P1==param1Value;;P2==param2Value
     * 
     * example: "eqp is in
     * fault;image;1||hvalarmclass|;com.thalesgroup.scadasoft.sample.data.alarm.
     * SCADAsoftAlarmType$$alarmText|;msgId==MSG01;;P1==param1Value;;P2==
     * param2Value
     * 
     * 
     * SCADAsoft OLS fields "AlarmId" 1 1 1 0 ScsInteger "AutoId" 12 1 1 0
     * ScsLONGLONG "UserID" 1 1 1 0 ScsInteger "EquipmentName" 3 1 1 0 ScsString
     * "Value" 3 1 1 0 ScsString "ValueState" 1 1 1 0 ScsInteger
     * "AcknowledgeRequired" 1 1 1 0 ScsInteger "Severity" 1 1 1 0 ScsInteger
     * "Shelve" 1 1 1 0 ScsInteger "Hidden" 1 1 1 0 ScsInteger "Message" 3 1 1 0
     * ScsString "Theme" 1 1 1 0 ScsInteger "EquipmentDate" 5 1 1 1351185408
     * 762000 "AcquisitionDate" 5 1 1 1351185408 762000 "SCSTime" 5 1 1
     * 1351185408 762000 "FunctionalCategory" 1 1 1 0 ScsInteger
     * "GeographicalCategory" 1 1 1 0 ScsInteger "AckAutomatonPointer" 6 1 1 0 0
     * ScsPointer "Environment" 3 1 1 0 ScsString "User1" 3 1 1 0 ScsString
     * "User2" 3 1 1 0 ScsString "DssEventType" 3 1 1 0 ScsString
     * 
     * key 6523 mode 1 6523 23292800 UserId 0
     * ":SITE1:B001:F000:SYSTEM:SRV1:SRV1A" "0" 1 1 2 0 0
     * "MSG01:P1=First SCADA Server for North Group;P2=down;|First SCADA Server for North Group is down"
     * 1 1351185450 758000 1351185450 758000 1351185450 758000 4 1 0 61917528
     * "SRV1" "" "" ""
     * 
     */
    @Override
    public AbstractEntityStatusesType convert2HV(ScsFieldMap olsrow, String olsid,
            AbstractEntityStatusesType previousEntity, long curTimeStamp) {

        // map for bean editor
        Map<String, Object> attributes = new HashMap<String, Object>();

        // get fields from message
        String rawMessage = "";
        ScsData d = olsrow.get("Message");
        if (d != null) {
            rawMessage = d.getStringValue();
        }
        Map<String, String> olsmsgFields = getFieldMapFromOLS(olsrow, "Message");

        // Alarm text
        String almText = olsmsgFields.remove("alarmText");
        if (almText != null) {
            attributes.put("alarmText", getAlmMsgFromString(almText));
        } else {
            attributes.put("alarmText", getAlmMsgFromString("msgId==MSG00;;{P1}==" + rawMessage));
        }
        
        // get HV alarm class name
        // default if set in constructor, then search in message, then search in config file
        String almClass = this.m_className;
        if (almClass == null) {
        	almClass = olsmsgFields.remove("hvalarmclass");
        }
        
        if (almClass == null) {
            almClass = SCSConfManager.instance().getHVAlmClass();    
        }
        if (almClass == null) {
        	// no class found we have to exit
            return null;
        }
        
        // SCADAsoft extra fields in message
        for (Entry<String, String> entry : olsmsgFields.entrySet()) {
            attributes.put(entry.getKey(), entry.getValue());
        }

        // create alarm object
        AbstractEntityStatusesType entity = SCSConfManager.instance().createEntity(m_connector.getDataHelper(),
                almClass, olsid, attributes, curTimeStamp);

        AbstractAlarmType alarmEntity = null;
        if (entity instanceof AbstractAlarmType) {
            alarmEntity = (AbstractAlarmType) entity;

            // Alarm field
            StringAttributeType serviceOwnerIDValue = new StringAttributeType();
            serviceOwnerIDValue.setValid(true);
            serviceOwnerIDValue.setTimestamp(curTimeStamp);
            serviceOwnerIDValue.setValue(SCSConfManager.instance().getServiceOwnerID());
            alarmEntity.setServiceOwnerID(serviceOwnerIDValue);

            StringAttributeType sourceIDValue = new StringAttributeType();
            sourceIDValue.setValid(true);
            sourceIDValue.setTimestamp(curTimeStamp);
            sourceIDValue.setValue(getEqpFromOLS(olsrow));
            alarmEntity.setSourceID(sourceIDValue);

            StringAttributeType areaIDValue = new StringAttributeType();
            areaIDValue.setValid(true);
            areaIDValue.setTimestamp(curTimeStamp);
            areaIDValue.setValue(getAreaIDFromOLS(olsrow));
            alarmEntity.setAreaID(areaIDValue);

            StateAttributeType stateValue = new StateAttributeType();
            stateValue.setValid(true);
            stateValue.setTimestamp(curTimeStamp);
            stateValue.setValue(getStateFromOLS(olsrow).value());
            alarmEntity.setState(stateValue);

            PriorityAttributeType priorityValue = new PriorityAttributeType();
            priorityValue.setValid(true);
            priorityValue.setTimestamp(curTimeStamp);
            priorityValue.setValue(getPriorityFromOLS(olsrow).value());
            alarmEntity.setPriority(priorityValue);

            DateTimeAttributeType apparitionDateValue = new DateTimeAttributeType();
            apparitionDateValue.setValid(true);
            apparitionDateValue.setTimestamp(curTimeStamp);
            String scsApparitionField = getOlsFieldFromHVField(almClass, "apparitionDate");
            if (!scsApparitionField.isEmpty()) {
            	XMLGregorianCalendar cal = getDateAttFromOLSNoRemove(olsrow, scsApparitionField);
            	if (cal != null) {
            		apparitionDateValue.setValue(cal);
            	} else {
            		s_logger.warn("ApparitionDate mapping is invalid. Use default EquipmentDate value for mapping");
            		apparitionDateValue.setValue(getDateAttFromOLSNoRemove(olsrow, "EquipmentDate"));
            	}
            } else {
            	// Use EquipmentDate from SCADA as default if no scs2hv mapping is found
            	apparitionDateValue.setValue(getDateAttFromOLSNoRemove(olsrow, "EquipmentDate"));
            }
            alarmEntity.setApparitionDate(apparitionDateValue);

            DateTimeAttributeType lastUpdateDateValue = new DateTimeAttributeType();
            lastUpdateDateValue.setValid(true);
            lastUpdateDateValue.setTimestamp(curTimeStamp);
            String scsLastUpdateDateField = getOlsFieldFromHVField(almClass, "lastUpdateDate");
            if (!scsLastUpdateDateField.isEmpty()) {
            	XMLGregorianCalendar cal = getDateAttFromOLSNoRemove(olsrow, scsLastUpdateDateField);
            	if (cal != null) {
            		lastUpdateDateValue.setValue(cal);
            	} else {
            		s_logger.warn("LastUpdateDate mapping is invalid. Use default SCSTime value for mapping");
            		lastUpdateDateValue.setValue(getDateAttFromOLSNoRemove(olsrow, "SCSTime"));
            	}
            } else {
            	// Use SCSTime from SCADA as default if no scs2hv mapping is found
            	lastUpdateDateValue.setValue(getDateAttFromOLSNoRemove(olsrow, "SCSTime"));
            }
            alarmEntity.setLastUpdateDate(lastUpdateDateValue);
            
            // Remove scsApparitionField and scsLastUpdateDateField from olsrow
            if (!scsApparitionField.isEmpty())
            	olsrow.remove(scsApparitionField);
            if (!scsLastUpdateDateField.isEmpty())
            	olsrow.remove(scsLastUpdateDateField);

            BooleanAttributeType isShelvedValue = new BooleanAttributeType();
            isShelvedValue.setValid(true);
            isShelvedValue.setTimestamp(curTimeStamp);
            isShelvedValue.setValue(getIsShelvedFromOLS(olsrow));
            alarmEntity.setIsShelved(isShelvedValue);

            BooleanAttributeType isInService = new BooleanAttributeType();
            isInService.setValid(true);
            isInService.setTimestamp(curTimeStamp);
            isInService.setValue(true);
            alarmEntity.setIsInService(isInService);

            StateAttributeType previousStateValue = new StateAttributeType();
            previousStateValue.setValid(true);
            previousStateValue.setTimestamp(curTimeStamp);
            if (previousEntity == null) {
                previousStateValue.setValue(Alarm4StateEnum.NPA.value());
            } else {
                previousStateValue.setValue(getAlarmStateFromEntity(previousEntity).value());
            }
            alarmEntity.setPreviousState(previousStateValue);

            // the following fields will be encoded in OLS Message set default value
            StringAttributeType stringValue = new StringAttributeType();
            stringValue.setValid(true);
            stringValue.setTimestamp(curTimeStamp);        	        	
            stringValue.setValue("");
            alarmEntity.setAckOperatorID(stringValue);
            alarmEntity.setComment(stringValue);
            
            fillRemainingFields(entity, olsrow, curTimeStamp);
        }

        return alarmEntity;
    }
    
    protected String getOlsFieldFromHVField(String hvClass, String attributeName) {
    	String olsField = "";
    	
    	StatusMapping mapping = SCSConfManager.instance().getSCSFieldFromHVField(hvClass, attributeName);
    	if (mapping != null && mapping.m_scsRelativePathList != null) {
    		List<String> scsvalueList = mapping.m_scsRelativePathList;
    		if (scsvalueList.size() > 0 && scsvalueList.get(0) != null) {
    			olsField = scsvalueList.get(0);
    		}
    	}
    	return olsField;
    }

	protected Boolean getIsShelvedFromOLS(ScsFieldMap olsrow) {
        String key = "Shelve";
        ScsData d = olsrow.remove(key);
        if (d != null) {
        	return d.getBooleanValue();
        }

        return false;
    }

	protected XMLGregorianCalendar getDateAttFromOLSNoRemove(ScsFieldMap olsrow, String key) {
		ScsData d = (ScsData)olsrow.get(key);
		if (d != null) {
			return DateUtils.convertToXMLGregorianCalendar(d.getDateValue());
		}
		return null;
	}
	
	protected XMLGregorianCalendar removeDateAttFromOLS(ScsFieldMap olsrow, String key) {
		ScsData d = (ScsData)olsrow.get(key);
		if (d != null) {
			return DateUtils.convertToXMLGregorianCalendar(d.getDateValue());
		}
		return null;
	}

}
