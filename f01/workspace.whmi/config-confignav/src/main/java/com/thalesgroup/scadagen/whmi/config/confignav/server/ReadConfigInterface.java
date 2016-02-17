package com.thalesgroup.scadagen.whmi.config.confignav.server;

import java.util.ArrayList;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;

public interface ReadConfigInterface {
	ArrayList<String> getTags(String path);
	ArrayList<Task> getTasks(String mapping, String setting, String tag);
}
