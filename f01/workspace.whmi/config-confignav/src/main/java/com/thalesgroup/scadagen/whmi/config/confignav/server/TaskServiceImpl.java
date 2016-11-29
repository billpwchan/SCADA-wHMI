package com.thalesgroup.scadagen.whmi.config.confignav.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskService;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {
	
	private Logger logger					= LoggerFactory.getLogger(TaskServiceImpl.class.getName());

	public Tasks taskServer(String module, String strMapping, String strSetting, String profile, String location, int targetLevel, String targetHeader) {
		Tasks tasks = new Tasks();
		tasks.setParentLevel(targetLevel);
		tasks.setParentHeader(targetHeader);
		
		logger.debug("taskServer strMapping[{}] strSetting[{}]", strMapping, strSetting);
		
		logger.debug("taskServer profile[{}] location[{}] targetLevel[{}] targetHeader[{}]", new Object[]{profile, location, targetLevel, targetHeader});
		
		if ( null == module ) {
			module = getServletContext().getInitParameter("project.navigation.module");
			logger.info("project.navigation.module module[{}]", module);
		}
		
		if ( null == strMapping ) {
			strMapping = getServletContext().getInitParameter("project.navigation.module.mapping");
			logger.info("project.navigation.module strMapping[{}]", strMapping);
		}
		
		if ( null == strSetting ) {
			strSetting = getServletContext().getInitParameter("project.navigation.module.setting");
			logger.info("project.navigation.module module[{}]", strSetting);
		}
		
		String tag = "option";
		String mapping = getServletContext().getRealPath("/") + "/" + module + "/" + strMapping;
		String setting = getServletContext().getRealPath("/") + "/" + module + "/" + strSetting;
		ReadConfigXML configs = new ReadConfigXML();
		
		logger.debug("taskServer mapping[{}] setting[{}] tag[{}]", new Object[]{mapping, setting, tag});
		
		ArrayList<Task> tsks = configs.getTasks(mapping, setting, tag);
		
		for(Task tsk: tsks) {
			tasks.add(tsk);
		}
		
		return tasks;
	}

}
