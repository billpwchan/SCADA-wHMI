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
		
		final String function = "taskServer";
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance("UIOpmSCADAgen");
		
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
		
		ArrayList<Task> tsks = configs.getTasks(mapping, setting, TaskServiceImpl_i.XML_TAG);
		
		for(Task tsk: tsks) {
			
			String opm = tsk.getParameter(TaskAttribute.opm.toString());
			if ( null != opm && opm.length() > 0 ) {
				String opmOperation = tsk.getParameter(TaskAttribute.opmOperation.toString());
				String opmName = tsk.getParameter(TaskAttribute.opmName.toString());
				
				logger.warn("{} opm[{}] opmOperation[{}] opmName[{}]", new Object[]{function, opm, opmOperation, opmName});
				
				boolean opmValid = opmCheckAccess(uiOpm_i, opm, opmOperation, opmName);
				if ( ! opmValid ) continue;
			}

			tasks.add(tsk);
		}
		
		return tasks;
	}
	
	final String setSpliter = "\\|";
	final String valSpliter = ":";
	final int lengthOfOpmName = 4;
	final int lengthOfOpmValue = 4;
	private boolean opmCheckAccess(UIOpm_i uiOpm_i, String opm, String opmOperation, String opmName) {
		final String function = "opmCheckAccess";
		
		boolean result = false;
		if ( null != uiOpm_i ) {

			if ( null != opm && opm.length() > 0 ) {
				
				String [] opmNames = null;
				if ( null != opmName && opmName.length() > 0 ) {
					opmNames = opmName.split(valSpliter);
					if ( null != opmNames && opmNames.length != lengthOfOpmName ) {
						logger.warn("{} opmNames.length[{}] == lengthOfOpmName[{}] IS NOT"
								, new Object[]{function, opmNames.length, lengthOfOpmValue});
					}
				}
				
				boolean isAndOperation = false;
				
				String [] opmSets = opm.split(setSpliter);
				int opmSetsLength = opmSets.length;
				logger.debug("{} opmSetsLength[{}]", new Object[]{function, opmSetsLength});
				
				if ( null != opmSets && opmSetsLength > 1 ) {
					if ( null != opmOperation ) {
						if ( 0 == opmOperation.compareToIgnoreCase(TaskServiceImpl_i.AttributeValue.AND.toString()) ) {
							isAndOperation = true;
						}
					}
				}
				
				logger.debug("{} opmOperation[{}] isAndOperation[{}]", new Object[]{function, opmOperation, isAndOperation});
				
				for ( int i = 0 ; i < opmSetsLength ; ++i ) {
					if ( null != opmSets[i] ) {
						logger.debug("{} i[{}] opmSet[{}]", new Object[]{function, i, opmSets[i]});
						String [] opmValues = opmSets[i].split(valSpliter);
						if ( null != opmValues ) {
							if ( opmValues.length == lengthOfOpmValue ) {
								
								if (
										   null != opmValues[0]
										&& null != opmValues[1]
										&& null != opmValues[2]
										&& null != opmValues[3]
									) {
									
									boolean bResult = false;
									if ( null == opmNames || null == opmNames[0] || null == opmNames[1] || null == opmNames[2] || null == opmNames[3] ) {
										
										bResult = uiOpm_i.checkAccess(
												  opmValues[0]
												, opmValues[1]
												, opmValues[2]
												, opmValues[3]);
										
										logger.debug("{} opmValues(0)[{}] opmValues(1)[{}] opmValues(2)[{}] opmValues(3)[{}] => bResult[{}]"
												, new Object[]{function, opmValues[0], opmValues[1], opmValues[2], opmValues[3], bResult});
										
									} else {
										
										bResult = uiOpm_i.checkAccess(
												  opmNames[0], opmValues[0]
												, opmNames[1], opmValues[1]
												, opmNames[2], opmValues[2]
												, opmNames[3], opmValues[3]);
										
										logger.debug("{} opmNames(0)[{}] opmValues(0)[{}] opmNames(1)[{}] opmValues(1)[{}] opmNames(2)[{}] opmValues(2)[{}] opmNames(3)[{}] opmValues(3)[{}] => bResult[{}]"
												, new Object[]{function, opmNames[0], opmValues[0], opmNames[1], opmValues[1], opmNames[2], opmValues[2], opmNames[3], opmValues[3], bResult});

									}
									
									if ( ! isAndOperation ) {
										// OR Operation
										if ( bResult ) {
											result = true;
											break;
										}
									} else {
										// AND Operation
										if ( bResult ) {
											result = true;
										} else {
											result = false;
											break;
										}
									}
									
								}
							} else {
								logger.warn("{} opmValues.length[{}] == lengthOfOpmName[{}] IS NOT", new Object[]{function, opmValues.length, lengthOfOpmName});
							}
						}
					} else {
						logger.debug("opmSets({}) IS NULL", i);
					}
				}
			}
		}
		
		logger.debug("result[{}]", result);
		
		return result;
	}
}
