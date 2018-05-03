package com.thalesgroup.scadasoft.opmmgt.util;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service(value = "utilService")
public class UtilService {
	
	private Date lastUpdateTime = new Date(86400000);
    
    public void setLastModifiedTime() {
    	setLastUpdateTime(new Date());
    }
    
    public void setLastUpdateTime(Date modifyDate) {
    	lastUpdateTime = modifyDate;
    }
    
    public Date getLastUpdateTime() {
    	return lastUpdateTime;
    }

}
