package com.thalesgroup.scadagen.whmi.uiutil.uiutil.client;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

public class UICookies {
	
    // Cookie persists 1 month (30*24*3600*1000 ms)
    private static Long cookiePersistenceDuration = 2592000000L;
    public void setCookiePersistenceDuration(Long cookiePersistenceDuration) {
    	UICookies.cookiePersistenceDuration = cookiePersistenceDuration;
    }
    
    public static void setCookies(String name, String value) {
    	final Date expireDate = new Date(System.currentTimeMillis() + cookiePersistenceDuration);
    	Cookies.setCookie(name, value, expireDate, null, "/", false);
    }
    
    public static String getCookies(String name) {
    	String value = Cookies.getCookie(name);
    	return value;
    }

}
