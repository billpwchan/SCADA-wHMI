package com.thalesgroup.scadagen.whmi.config.confignav.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskService;
import com.thalesgroup.scadagen.whmi.config.confignav.server.opm.SCADAgenTaskOpm;
import com.thalesgroup.scadagen.whmi.config.confignav.server.opm.UIOpmTask_i;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.opm.UIOpm_i;

import java.util.ArrayList;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {
	
	private UILogger_i logger					= UILoggerFactory.getInstance().get(this.getClass().getName());

	public Tasks taskServer(String module, String strMapping, String strSetting, String profile, String location, int targetLevel, String targetHeader) {
		
		final String function = "taskServer";
		
		Tasks tasks = new Tasks();
		tasks.setParentLevel(targetLevel);
		tasks.setParentHeader(targetHeader);
		
		logger.debug("{} strMapping[{}] strSetting[{}]", new Object[]{function, strMapping, strSetting});
		
		logger.debug("{} profile[{}] location[{}] targetLevel[{}] targetHeader[{}]", new Object[]{function, profile, location, targetLevel, targetHeader});
		
		if ( null == module ) {
			module = getServletContext().getInitParameter(TaskServiceImpl_i.MODULE_NAME);
			logger.debug("{} properties[{}] module[{}]", new Object[]{function, TaskServiceImpl_i.MODULE_NAME, module});
		}
		
		if ( null == strMapping ) {
			strMapping = getServletContext().getInitParameter(TaskServiceImpl_i.MAPPING_NAME);
			logger.debug("{} properties[{}] strMapping[{}]", new Object[]{function, TaskServiceImpl_i.MAPPING_NAME, strMapping});
		}
		
		if ( null == strSetting ) {
			strSetting = getServletContext().getInitParameter(TaskServiceImpl_i.SETTING_NAME);
			logger.debug("{} properties[{}] module[{}]", new Object[]{function, TaskServiceImpl_i.SETTING_NAME, strSetting});
		}
		
		String rootPath = getServletContext().getRealPath(TaskServiceImpl_i.SERVLET_ROOT_PATH) + TaskServiceImpl_i.FILE_SEPARATOR + module;
		
		logger.debug("{} rootPath[{}]", function, rootPath);
		
		String mapping = rootPath + TaskServiceImpl_i.FILE_SEPARATOR + strMapping;
		String setting = rootPath + TaskServiceImpl_i.FILE_SEPARATOR + strSetting;
		
		ReadConfigXML configs = new ReadConfigXML();
		
		logger.debug("{} mapping[{}] setting[{}] tag[{}]", new Object[]{function, mapping, setting, TaskServiceImpl_i.XML_TAG});
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance("UIOpmSCADAgen");
		UIOpmTask_i taskOpm = new SCADAgenTaskOpm();
		taskOpm.setUIOpm_i(uiOpm_i);
		
		ArrayList<Task> tsks = configs.getTasks(mapping, setting, TaskServiceImpl_i.XML_TAG);
		
		for(Task tsk: tsks) {
			
			int opmResult = taskOpm.isValid(tsk);
			if ( opmResult < 0 ) {
				continue;
			}
			
			tasks.add(tsk);
		}
		
		return tasks;
	}
	

}
