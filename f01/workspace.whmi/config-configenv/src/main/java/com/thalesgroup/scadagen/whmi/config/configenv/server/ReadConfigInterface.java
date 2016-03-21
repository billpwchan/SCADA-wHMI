package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.util.ArrayList;

import com.thalesgroup.scadagen.whmi.config.config.shared.Config;
import com.thalesgroup.scadagen.whmi.config.config.shared.Dictionary;

public interface ReadConfigInterface {
	ArrayList<String> getTags(String path);
	ArrayList<Dictionary> getDictionary(String path, String tag);
	ArrayList<Config> getConfigs(String path, String tag);
}
