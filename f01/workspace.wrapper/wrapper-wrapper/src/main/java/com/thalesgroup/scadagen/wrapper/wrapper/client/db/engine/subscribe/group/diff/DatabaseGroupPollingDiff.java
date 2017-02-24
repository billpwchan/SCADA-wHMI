package com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.diff;

import java.util.HashMap;
import java.util.LinkedList;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.subscribe.group.DatabaseGroupPolling;

public class DatabaseGroupPollingDiff extends DatabaseGroupPolling {
	
	private final String className = UIWidgetUtil.getClassSimpleName(DatabaseGroupPollingDiff.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	@Override
	public void buildRespond(String key, String[] dbaddresses, String[] values) {
		final String function = "buildReponse";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}]", key);
		String scsEnvId = requestKeyScsEnvIds.get(key);
		
		HashMap<String, String> addressValue = new HashMap<String, String>();
		for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
			String dbaddress = dbaddresses[i];
			String value = values[i];
			addressValue.put(dbaddress, value);
		}
		
		HashMap<String, PollingRequest> rqs = requests.get(scsEnvId);
		for ( String key2 : rqs.keySet() ) {
			PollingRequest rq = rqs.get(key2);
			
			LinkedList<String> al = null;
			LinkedList<String> vl = null;
			if ( null != rq.values ) {
				for ( int i = 0 ; i < rq.dbaddresses.length ; ++i ) {
					String ca = rq.dbaddresses[i];
					String nv = addressValue.get(ca);
					String cv = rq.values[i];
					if ( null == cv || ( null != cv && !cv.equals(nv) ) ) {
						
						if ( null == al ) al = new LinkedList<String>();
						if ( null == vl ) vl = new LinkedList<String>();
						
						al.add(ca);
						vl.add(nv);
						
						rq.values[i] = nv;
					}
				}
			} else {
				for ( int i = 0 ; i < rq.dbaddresses.length ; ++i ) {
					String ca = rq.dbaddresses[i];
					String nv = addressValue.get(ca);
					
					if ( null == al ) al = new LinkedList<String>();
					if ( null == vl ) vl = new LinkedList<String>();
					
					al.add(ca);
					vl.add(nv);
				}
				rq.values = vl.toArray(new String[0]);
			}

			if ( null != al && null != vl ) {
				String [] ra = al.toArray(new String[0]);
				String [] rv = vl.toArray(new String[0]);

				rq.databaseEvent.update(rq.key, ra, rv);
			}

		}
		logger.end(className, function);
	}

}
