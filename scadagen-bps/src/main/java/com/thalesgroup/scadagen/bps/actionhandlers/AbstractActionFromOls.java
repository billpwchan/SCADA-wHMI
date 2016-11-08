package com.thalesgroup.scadagen.bps.actionhandlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadasoft.data.exchange.entity.alarm.ScsListType;

public abstract class AbstractActionFromOls extends AbstractAction {

	protected Map<String, String> olsDataMap_ = new HashMap<String, String>();
	
	public void decodeOlsData(AbstractEntityStatusesType entity) {

		if (entity instanceof ScsListType) {
			ScsListType olsList = (ScsListType)entity;
			String data = olsList.getData().getValue();
			try {
				ObjectMapper jsonMapper = new ObjectMapper();
				ObjectNode dataNode = (ObjectNode) jsonMapper.readTree(data);
				Iterator<String> it = dataNode.fieldNames();
				while (it.hasNext()) {
					String key = it.next();
					olsDataMap_.put(key, dataNode.get(key).textValue());
				}
			} catch (JsonProcessingException e) {
				LOGGER.error("Error processing json data. {}", e);
			} catch (IOException e) {
				LOGGER.error("Error reading json data. {}", e);
			}
		}
	}
}
