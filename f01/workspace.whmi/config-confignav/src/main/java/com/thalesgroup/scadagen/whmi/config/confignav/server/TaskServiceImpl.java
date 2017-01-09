package com.thalesgroup.scadagen.whmi.config.confignav.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.thalesgroup.scadagen.whmi.config.confignav.client.TaskService;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task_i.TaskAttribute;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Tasks;
import com.thalesgroup.scadagen.wrapper.wrapper.server.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.server.UIOpm_i;

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
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance("UIOpmSCADAgen");
		
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
			
			String opmName1		= tsk.getParameter(TaskAttribute.OpmName1.toString());
			String opmValue1	= tsk.getParameter(TaskAttribute.OpmValue1.toString());
			String opmName2		= tsk.getParameter(TaskAttribute.OpmName2.toString());
			String opmValue2	= tsk.getParameter(TaskAttribute.OpmValue2.toString());
			String opmName3		= tsk.getParameter(TaskAttribute.OpmName3.toString());
			String opmValue3	= tsk.getParameter(TaskAttribute.OpmValue3.toString());
			String opmName4		= tsk.getParameter(TaskAttribute.OpmName4.toString());
			String opmValue4	= tsk.getParameter(TaskAttribute.OpmValue4.toString());
			
			logger.info("opmName1[{}] opmValue1[{}]", opmName1, opmValue1 );
			logger.info("opmName2[{}] opmValue2[{}]", opmName2, opmValue2 );
			logger.info("opmName3[{}] opmValue3[{}]", opmName3, opmValue3 );
			logger.info("opmName4[{}] opmValue4[{}]", opmName4, opmValue4 );
			
			if ( null != uiOpm_i ) {
				
				if ( 
						   null != opmName1 && ! opmName1.trim().isEmpty()
						&& null != opmValue1 && ! opmValue1.trim().isEmpty()
						&& null != opmName2 && ! opmName2.trim().isEmpty()
						&& null != opmValue2 && ! opmValue2.trim().isEmpty()
						&& null != opmName3 && ! opmName3.trim().isEmpty()
						&& null != opmValue3 && ! opmValue3.trim().isEmpty()
						&& null != opmName4 && ! opmName4.trim().isEmpty()
						&& null != opmValue4 && ! opmValue4.trim().isEmpty()
						) {
					
					boolean result = uiOpm_i.checkAccess(
							  opmName1, opmValue1
							, opmName2, opmValue2
							, opmName3, opmValue3
							, opmName4, opmValue4);
					
					if ( ! result ) continue;
				}
				
			}
			
//			String func = tsk.getParameter(TaskAttribute.FunCat.toString());
//			String geoc = tsk.getParameter(TaskAttribute.LocCat.toString());
//			String action = tsk.getParameter(TaskAttribute.ActionCat.toString());
//			String mode = tsk.getParameter(TaskAttribute.ModeCat.toString());
//			
//			logger.info("func[{}] geoc[{}] action[{}] mode[{}]", new Object[]{func, geoc, action, mode});
//			
//			if ( null != uiOpm_i ) {
//				
//				if ( null != func && ! func.trim().isEmpty() 
//						&& null != geoc && ! geoc.trim().isEmpty() 
//						&& null != action && ! action.trim().isEmpty()
//						&& null != mode && ! mode.trim().isEmpty() ) {
//					
//					boolean result = uiOpm_i.checkAccess(func, geoc, action, mode);
//				
//					logger.info("func[{}] geoc[{}] action[{}] mode[{}] => result[{}]", new Object[]{func, geoc, action, mode, result});
//					
//					if ( ! result ) continue;
//				}
//				
//			}
			
			tasks.add(tsk);
		}
		
		return tasks;
	}

}
