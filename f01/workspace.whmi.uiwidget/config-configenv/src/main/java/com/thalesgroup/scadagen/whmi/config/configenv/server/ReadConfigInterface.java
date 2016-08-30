package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.util.List;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public interface ReadConfigInterface {
//	List<String> getTags(String path);
	List<Dictionary> getDictionary(String path);
	List<Dictionary> getDictionary(String path, String tag);
}
