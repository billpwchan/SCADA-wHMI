package com.thalesgroup.scadasoft.myba.subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.alarm.attribute.Alarm4StateEnum;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadasoft.datatypes.ScsFieldMap;
import com.thalesgroup.scadasoft.datatypes.client.ScsData;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;

public class OLSAlmConverter extends OLSConverter {

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

}
