package com.thalesgroup.scadasoft.opmmgt.util;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service(value = "utilService")
public class UtilService {
	
	private Date lastUpdateTime = new Date();
	
	private boolean opmDumpCacheIsUpdated = false;
    
    public void setLastModifiedTime() {
    	setLastUpdateTime(new Date());
    	opmDumpCacheIsUpdated = false;
    }
    
    public void setLastUpdateTime(Date modifyDate) {
    	lastUpdateTime = modifyDate;
    }
    
    public Date getLastUpdateTime() {
    	return lastUpdateTime;
    }

    public boolean isOpmDumpCacheUpdated() {
    	return opmDumpCacheIsUpdated;
    }
    
    public void setOpmDumpCacheUpdated() {
    	opmDumpCacheIsUpdated = true;
    }

}
