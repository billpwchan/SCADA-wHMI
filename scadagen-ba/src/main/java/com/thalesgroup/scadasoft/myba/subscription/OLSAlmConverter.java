package com.thalesgroup.scadasoft.myba.subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.tools.DateUtils;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.alarm.attribute.Alarm4StateEnum;
import com.thalesgroup.hv.data_v1.alarm.attribute.AlarmPriorityEnum;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadasoft.datatypes.ScsFieldMap;
import com.thalesgroup.scadasoft.datatypes.client.ScsData;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;

public class OLSAlmConverter extends OLSConverter {
	
	static private final Logger s_logger = LoggerFactory.getLogger(OLSConverter.class);
    private static final String LOG_PREFIX = "[OLSAlmConverter] ";

	private String m_className = null;
	
    public OLSAlmConverter(Connector connector) {
        super(connector);
    }

    public OLSAlmConverter(String className, Connector connector) {
    	super(connector);
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

        // HV standard field
        attributes.put("serviceOwnerID", SCSConfManager.instance().getServiceOwnerID());
        attributes.put("sourceID", getEqpFromOLS(olsrow));
        attributes.put("areaID", getAreaIDFromOLS(olsrow));
        attributes.put("state", getStateFromOLS(olsrow));
        attributes.put("priority", getPriorityFromOLS(olsrow));

        attributes.put("apparitionDate", getDateAttFromOLS(olsrow, "EquipmentDate"));
        attributes.put("lastUpdateDate", getDateAttFromOLS(olsrow, "SCSTime"));
        attributes.put("isShelved", getIsShelvedFromOLS(olsrow));
        attributes.put("isInService", true);
        if (previousEntity == null) {
            attributes.put("previousState", Alarm4StateEnum.NPA);
        } else {
            attributes.put("previousState", getAlarmStateFromEntity(previousEntity));
        }

        // the following fields will be encoded in OLS Message set default value
        attributes.put("ackOperatorID", "");
        attributes.put("comment", "");

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
        AbstractAlarmType alarmEntity = (entity instanceof AbstractAlarmType ? (AbstractAlarmType) entity : null);

        fillRemainingFields(entity, olsrow, curTimeStamp);

        return alarmEntity;
    }

    protected Boolean getIsShelvedFromOLS(ScsFieldMap olsrow) {
        String key = "Shelve";
        ScsData d = olsrow.remove(key);
        if (d != null) {
        	return d.getBooleanValue();
        }

        return false;
    }
    
//    @Override
//    protected void fillRemainingFields(AbstractEntityStatusesType entity, ScsFieldMap olsrow, long curTimeStamp) {
//
//    	if (entity != null) {
//            IDataHelper dh = m_connector.getDataHelper();
//            try {
//                Collection<String> knownStatuses = dh.getDeclaredStatusNames(entity.getClass());
//                
//                String curTimeStampAsString = String.valueOf(curTimeStamp);
//                
//                // create JSON object for unknown fields
//                ObjectNode extraFields = SCADAExecutor.s_json_factory.objectNode();
//                
//                // other OLS fields
//                for (java.util.Map.Entry<String, ScsData> entry : olsrow.entrySet()) {
//                    // add remaining OLS fields all method remove found field
//                    // from olrow
//                	String att = camel_caser(entry.getKey().toLowerCase());
//                	s_logger.trace("Change {} to lower case -> {}", entry.getKey(), entry.getKey().toLowerCase());
//                    if (knownStatuses.contains(att)) {
//                    	s_logger.trace("knownStatuses contains {}", att);
//                        ScsData d = entry.getValue();
//                        try {
//                            
//                            if (d.getType() == ScsData.TIME) {
//                                ScsTimeData dt = (ScsTimeData) d;
//                                Date date = new Date(dt.getMilliSec());
//                                XMLGregorianCalendar gdate = DateUtils.convertToXMLGregorianCalendar(date);
//
//                                m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, att + "/value",
//                                        DateUtils.convertToISO8601(gdate));
//                            } else {
//
//                                m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, att + "/value",
//                                        d.getStringValue());
//
//                            }
//                            m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, att + "/valid",
//                                    "true");
//                            m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, att + "/timestamp",
//                                    curTimeStampAsString);
//
//                        } catch (BeanManipulationException e) {
//                            s_logger.error("OLSConverter cannot set field {} from OLS data {}",
//                                    entry.getKey().toLowerCase(), d.getStringValue());
//                            s_logger.trace(LOG_PREFIX + e.getMessage(), e);
//                        }
//
//                    } else {
//                    	s_logger.trace("knownStatuses does not contain {}", att);
//                    	addToJSON(extraFields, entry.getKey(), entry.getValue());
//                    	s_logger.debug("OLSConverter skip field {} from OLS", entry.getKey().toLowerCase());
//                    }
//                }
                
//                if (extraFields.size() > 0 && knownStatuses.contains("data")) {
//                	try {
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "data/value", extraFields.toString());
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "data/valid", "true");
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "data/timestamp", curTimeStampAsString);
//                	} catch (BeanManipulationException e) {
//                        s_logger.error("OLSConverter cannot set field 'data' from OLS data {}", extraFields.toString());
//                        s_logger.trace(LOG_PREFIX + e.getMessage(), e);
//                    }
//                }
//                
//                if (extraFields.size() > 0 && knownStatuses.contains("listname")) {
//                	try {
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "listname/value", m_subscription.getListName());
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "listname/valid", "true");
//	                	m_connector.getDataHelper().getBeanEditor().setFromStringValue(entity, "listname/timestamp", curTimeStampAsString);
//                	} catch (BeanManipulationException e) {
//                        s_logger.error("OLSConverter cannot set field 'listname' from OLS data {}", extraFields.toString());
//                        s_logger.trace(LOG_PREFIX + e.getMessage(), e);
//                    }
//                }

//            } catch (EntityManipulationException e) {
//                s_logger.error("OLSConverter cannot get status of entity", e);
//            }
//            
//            super.fillRemainingFields(entity, olsrow, curTimeStamp);
//        }
//    }
    
//    private void addToJSON(ObjectNode extraFields, String key, ScsData value) {
//		
//        switch (value.getType()) {
//        case ScsData.UNDEFINED:
//            break;
//        case ScsData.INT:
//        	extraFields.put(key, value.getIntValue());
//            break;
//        case ScsData.FLOAT:
//        	extraFields.put(key, value.getFloatValue());
//            break;
//        case ScsData.STRING:
//        	extraFields.put(key, value.getStringValue());
//            break;
//        case ScsData.WSTRING:
//        	extraFields.put(key, value.getStringValue());
//            break;
//        case ScsData.TIME:
//        	extraFields.put(key, m_dateFormat.format(value.getDateValue()));
//            break;
//        case ScsData.POINTER:
//        	extraFields.put(key, value.getIntValue());
//            break;
//        case ScsData.BIT:
//            break;
//        case ScsData.COMPLEX:
//            break;
//        case ScsData.REF:
//        	extraFields.put(key, value.getStringValue());
//            break;
//        case ScsData.LONG:
//        	extraFields.put(key, value.getIntValue());
//            break;
//        case ScsData.DOUBLE:
//        	extraFields.put(key, value.getFloatValue());
//            break;
//        default:
//            break;
//        }
//
//    }

    @Override
    protected XMLGregorianCalendar getDateAttFromOLS(ScsFieldMap olsrow, String key) {
        ScsData d = olsrow.get(key);
        if (d != null) {
            return DateUtils.convertToXMLGregorianCalendar(d.getDateValue());
        }
        return null;
    }

    @Override
    protected String getEqpFromOLS(ScsFieldMap olsrow) {
        String key = "EquipmentName";
        ScsData d = olsrow.get(key);
        if (d != null) {
            String hvid = SCSConfManager.instance().getHVIdFromSCSId(d.getStringValue());
            if (hvid != null) {
                return hvid;
            } else {
                s_logger.error("OLSConverter cannot find HV id for SCS id {}", d.getStringValue());
                return d.getStringValue();
            }
        }
        
        return null;
    }
    
    @Override
    protected Alarm4StateEnum getStateFromOLS(ScsFieldMap olsrow) {
        // use ValueState and AcknowledgeRequired
        ScsData ar = olsrow.get("AcknowledgeRequired");
        ScsData vs = olsrow.get("ValueState");

        if (ar != null && vs != null) {
            int ackreq = ar.getIntValue();
            int state = vs.getIntValue();

            if (state == 0) {
                // not in alarm
                if (ackreq == 0) {
                    return Alarm4StateEnum.NPA;
                } else {
                    return Alarm4StateEnum.NPNA;
                }
            } else {
                // in alarm
                if (ackreq == 0) {
                    return Alarm4StateEnum.PA;
                } else {
                    return Alarm4StateEnum.PNA;
                }
            }
        }

        return Alarm4StateEnum.NPA;
    }

    @Override
    protected AlarmPriorityEnum getPriorityFromOLS(ScsFieldMap olsrow) {
        String key = "Severity";
        ScsData d = olsrow.get(key);
        if (d != null) {
            String v = SCSConfManager.instance().getValueFromMap("AlarmPriority", d.getIntValue());
            if (v != null) {
                try {
                    return AlarmPriorityEnum.valueOf(v);
                } catch (IllegalArgumentException e) {
                    s_logger.error(
                            "OLSConverter cannot convert SCS severity '{}' to valid AlarmPriorityEnum set it to CRITICAL",
                            v);
                    s_logger.error(LOG_PREFIX + e.getMessage(), e);
                    return AlarmPriorityEnum.CRITICAL;
                }
            }
        }

        return null;
    }

    @Override
    protected String getAreaIDFromOLS(ScsFieldMap olsrow) {
        String key = "GeographicalCategory";
        ScsData d = olsrow.get(key);
        if (d != null) {
            String v = SCSConfManager.instance().getValueFromMap("AreaID", d.getIntValue());
            if (v == null) {
                s_logger.error("OLSConverter cannot find Area id for SCS geocat {}", d.getIntValue());
            }
            return v;
        }

        return null;
    }
}
