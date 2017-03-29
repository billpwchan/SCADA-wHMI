package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

public class EquipmentsSorting {
	
	private final String className = UIWidgetUtil.getClassSimpleName(EquipmentsSorting.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	
	private Database database = null;
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	private String parent = null;
	private String scsEnvId = null;
	public void setParent(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
	}
	
	private List<Equipement> es = new LinkedList<Equipement>();
	public void setDBAddresses(String [] dbaddresses, String dbattribute ) {
		for ( String dbaddress : dbaddresses ) {
			es.add(new Equipement(dbaddress, dbattribute));
		}
	}
	
	private int hmiOrderFilterThreshold = -1;
	public void setThreshold(int hmiOrderFilterThreshold) {
		this.hmiOrderFilterThreshold = hmiOrderFilterThreshold;
	}
	
	private EquipmentsSortingEvent event = null;
	public void setEquipmentsSortingEvent( EquipmentsSortingEvent event) {
		this.event = event;
	}
	
	private void sort() {
		final String function = "sort";
		logger.begin(className, function);
		Collections.sort(es, new Comparator<Equipement>() {
			@Override
			public int compare(Equipement o1, Equipement o2) {
				if ( null != o1 && null != o2 ) {
					// Compare with hmiOrder only
					return ( o1.attributeValue < o2.attributeValue ? -1 : ( o1.attributeValue == o2.attributeValue ? 0 : 1 ) );
				}
				return 0;
			}
		});
		logger.end(className, function);
	}
	
	private void fillResult(String[] addresses, String [] values) {
		final String function = "fillResult";
		logger.begin(className, function);
		for ( int i = 0 ; i < addresses.length ; i++ ) {
			String alias = addresses[i];
			int attributeValue = -1;
			try {
				if ( null != values[i] ) {
					attributeValue = Integer.parseInt(values[i]);
				}
			} catch ( NumberFormatException e) {
				
			}
			for ( Equipement e : es ) {
				if ( alias.equals(e.aliasWithAttribute) ) {
					e.attributeValue = attributeValue;
					break;
				}
			}
		}
		logger.end(className, function);
	}
	
	private void responseOrdering(String[] addresses, String [] values) {
		final String function = "responseOrdering";
		logger.begin(className, function);
		
		if ( logger.isDebugEnabled() ) {
			if (addresses.length == values.length) {
				for ( int i = 0 ; i < addresses.length ; i++ ) {
					logger.debug(className, function, "i[{}] addresses[{}] values[{}]", new Object[]{i, addresses[i], values[i]});
				}
			} else {
				logger.warn(className, function, "addresses.length[{}] != values.length[{}]", addresses.length, values.length);
			}
		}
		
		fillResult(addresses, values);
		
		sort();
		
		String [] sorted = getSortedDBAddress();
		
		if ( null != event ) {
			event.onSorted(sorted);
		}

		logger.end(className, function);
	}
	
	
	private String[] getSubscribeDBAddress() {
		final String function = "getSubscribeDBAddress";
		logger.begin(className, function);
		String [] result = null;
		List<String> dbaddress = new LinkedList<String>();
		for ( Equipement e : this.es ) {
			dbaddress.add(e.aliasWithAttribute);
		}
		result = dbaddress.toArray(new String[0]);
		logger.end(className, function);
		return result;
	}
	
	private String[] getSortedDBAddress(boolean filterNegative) {
		final String function = "getSortedDBAddress";
		logger.begin(className, function);
		String [] result = null;
		List<String> dbaddress = new LinkedList<String>();
		for ( Equipement e : this.es ) {
			if ( e.attributeValue > hmiOrderFilterThreshold )
				dbaddress.add(e.alias);
		}
		result = dbaddress.toArray(new String[0]);
		logger.end(className, function);
		return result;
	}
	
	public void requestOrdering() {
		final String function = "requestOrdering";
		logger.begin(className, function);
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(className);
		clientKey.setStability(Stability.STATIC);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		
		String strApi = clientKey.getApi().toString();
		
		final String [] dbaddresses = getSubscribeDBAddress();
		
		if ( null != database ) {
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] values) {
					final String function = "responseOrdering";
					logger.begin(className, function);
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(className);
					clientKey.setStability(Stability.STATIC);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toString();

					if ( strClientKey.equalsIgnoreCase(key) ) {
						
						responseOrdering(dbaddresses, values);
						
					}
				}
			});
		} else {
			logger.warn(className, function, "database IS NUL");
		}
		
		logger.end(className, function);
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		requestOrdering();
		logger.end(className, function);
	}

}
