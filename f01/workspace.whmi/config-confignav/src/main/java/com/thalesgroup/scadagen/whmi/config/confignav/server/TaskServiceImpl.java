package com.thalesgroup.scadagen.whmi.config.confignav.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskService;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;

import java.util.ArrayList;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {

	public Tasks taskServer(String module, String strMapping, String strSetting, String profile, String location, int targetLevel, String targetHeader) {
		Tasks tasks = new Tasks();
		tasks.setParentLevel(targetLevel);
		tasks.setParentHeader(targetHeader);
		
		System.out.println("taskServer strMapping["+strMapping+"] strSetting["+strSetting+"]");
		
		System.out.println("taskServer profile["+profile+"] location["+location+"] targetLevel["+targetLevel+"] targetHeader["+targetHeader+"]");
		
		String tag = "option";
		String mapping = getServletContext().getRealPath("/") + "/" + module + "/config/" + strMapping;
		String setting = getServletContext().getRealPath("/") + "/" + module + "/config/" + strSetting;
		ReadConfigXML configs = new ReadConfigXML();
		
		System.out.println("taskServer mapping["+mapping+"] setting["+setting+"] tag["+tag+"]");
		ArrayList<Task> tsks = configs.getTasks(mapping, setting, tag);
		
		for(Task tsk: tsks) {
			tasks.add(tsk);
		}
		
		return tasks;
	}

}
