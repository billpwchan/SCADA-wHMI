package com.thalesgroup.scadagen.whmi.translation.translationmgr.client;

public interface TranslationEngine {
	String getMessage(String message);
	String getMessage(String msgWithPlaceHolder, Object[] msgParam);
}
