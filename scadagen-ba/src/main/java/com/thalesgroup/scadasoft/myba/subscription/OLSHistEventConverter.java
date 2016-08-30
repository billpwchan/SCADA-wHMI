package com.thalesgroup.scadasoft.myba.subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.DateUtils;
import com.thalesgroup.hv.data_v1.alarm.AbstractAlarmType;
import com.thalesgroup.hv.data_v1.alarm.historization.AlarmHistoDataType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.hv.data_v1.histo.operation.HistorizeRequest;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.scadasoft.datatypes.ScsFieldMap;
import com.thalesgroup.scadasoft.datatypes.client.ScsData;
import com.thalesgroup.scadasoft.hvconnector.SCADAsoftBA;
import com.thalesgroup.scadasoft.hvconnector.configuration.SCSConfManager;
import com.thalesgroup.scadasoft.hvconnector.subscription.OLSConverter;

public class OLSHistEventConverter extends OLSConverter {
    static private final Logger s_logger = LoggerFactory.getLogger(OLSHistEventConverter.class);
    private SCADAsoftBA m_ba;

    public OLSHistEventConverter(SCADAsoftBA ba, Connector connector) {
        super(connector);
        m_ba = ba;
    }

    public AlarmHistoDataType convert2Histo(ScsFieldMap olsrow, String olsid, long curTimeStamp) {
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
        String almClass = olsmsgFields.remove("hvalarmclass");
        if (almClass == null) {
            almClass = SCSConfManager.instance().getHVEvtClass();
            if (almClass == null) {
                return null;
            }
        }
        String eqpType = olsmsgFields.remove("hveqpclass");
        if (eqpType == null) {
            // TODO: need a better default
            eqpType = "com.thalesgroup.scadasoft.sample.data.equipment.DoorStatusesType";
        }

        // SCADAsoft extra fields in message
        for (Entry<String, String> entry : olsmsgFields.entrySet()) {
            attributes.put(entry.getKey(), entry.getValue());
        }

        // create alarm object
        AbstractEntityStatusesType entity = SCSConfManager.instance().createEntity(m_connector.getDataHelper(),
                almClass, olsid, attributes, curTimeStamp);
        AbstractAlarmType alarmEntity = (entity instanceof AbstractAlarmType ? (AbstractAlarmType) entity : null);

        if (alarmEntity == null) {
            return null;
        }
        fillRemainingFields(entity, olsrow, curTimeStamp);

        AlarmHistoDataType alarmData = new AlarmHistoDataType();
        alarmData.setEntityType(eqpType);
        alarmData.setAlarm(alarmEntity);

        return alarmData;
    }

    @Override
    public void invalidateEntities() {
        // nothing to do data are sent to HistoryService
    }

    @Override
    synchronized public void handleSCSNotification(List<ScsFieldMap> olsdata) {

        if (olsdata != null && !olsdata.isEmpty()) {
            // prepare query
            final Connector genericConnector = SCSConfManager.instance().getConnector();
            HistorizeRequest histoRequest = null;
            try {
                histoRequest = (HistorizeRequest) genericConnector.getOperationHelper()
                        .createOperationRequest(HistorizeRequest.class.getName(), new HashMap<String, String>());
            } catch (HypervisorConversionException e1) {
                s_logger.error(e1.getMessage(), e1);
                return;
            }

            final String operatorId = SCSConfManager.instance().getRemoteEnv();
            histoRequest.setOperatorID(operatorId);

            // use current time to mark all notification
        	long curTimeStamp = DateUtils.getCurrentTimestamp();
        	
            // add item
            long lastID = m_ba.getRedManager().getLastEventID();
            long nextLastID = 0;
            for (ScsFieldMap olsrow : olsdata) {
                ScsData d = olsrow.get("ID");
                if (d != null) {
                    String olsid = d.getStringValue();
                    if (olsid != null) {

                        long curid = getIDFromString(olsid);
                        if (curid == 0 || curid < lastID) {
                            nextLastID = curid;

                            AlarmHistoDataType currentEntity = null;

                            if (olsrow.getMode() == ScsFieldMap.CREATE) {
                                currentEntity = convert2Histo(olsrow, olsid, curTimeStamp);
                                if (currentEntity != null) {
                                    histoRequest.getHistoricalData().add(currentEntity);
                                }
                            }
                        }
                    }
                }
            }

            if (nextLastID != 0) {
                final UUID correlationID = UUID.randomUUID();
                if (m_ba.isHistorianUP()) {
                    // send query
                    m_ba.getRedManager().appendWaitingHistoRequest(correlationID, histoRequest, nextLastID);
                    try {
                        genericConnector.requestHistorizeDataOperation(correlationID, histoRequest);
                    } catch (final HypervisorException e) {
                        s_logger.error(e.getMessage(), e);
                    }
                } else {
                    // store request in cache
                    m_ba.getRedManager().appendHistoRequest(correlationID, histoRequest, nextLastID);
                }
            }
        }

    }

    private long getIDFromString(String olsid) {
        // id is calculated with listName + "." + srvName + "." + envName + "@"
        // + olskey
        String sid = olsid.substring(olsid.lastIndexOf('@') + 1);
        long id = 0;
        try {
            id = Long.parseLong(sid);
        } catch (NumberFormatException e) {
            s_logger.warn("OLSEventonverter cannot get long from {}", sid);
        }
        return id;
    }

    @Override
    public AbstractEntityStatusesType convert2HV(ScsFieldMap olsrow, String olsid,
            AbstractEntityStatusesType previousEntity, long curTimeStamp) {

        return null;
    }
}
