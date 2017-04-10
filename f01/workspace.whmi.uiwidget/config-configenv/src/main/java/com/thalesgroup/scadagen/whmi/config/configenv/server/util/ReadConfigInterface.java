package com.thalesgroup.scadagen.whmi.config.configenv.server.util;

import java.util.List;

import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary_i;

public interface ReadConfigInterface {
	List<Dictionary_i> getDictionary(String path);
	List<Dictionary_i> getDictionary(String path, String tag);
}
