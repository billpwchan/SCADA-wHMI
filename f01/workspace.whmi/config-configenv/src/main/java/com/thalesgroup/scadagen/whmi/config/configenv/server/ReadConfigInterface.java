package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.util.ArrayList;

import com.thalesgroup.scadagen.whmi.config.config.shared.Config;

public interface ReadConfigInterface {
	ArrayList<String> getTags(String path);
	ArrayList<Config> getConfigs(String path, String tag);
}
