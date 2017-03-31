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
			if ( null != opm ) {
				String opmOperation = tsk.getParameter(TaskAttribute.opmOperation.toString());
				String opmName = tsk.getParameter(TaskAttribute.opmName.toString());
				
				logger.warn("{} opm[{}] opmOperation[{}] opmName[{}]", new Object[]{function, opm, opmOperation, opmName});
				
				boolean opmIsValid = opmCheckAccess(uiOpm_i, opm, opmOperation, opmName);
				if ( opmIsValid ) continue;
			}

			tasks.add(tsk);
		}
		
		return tasks;
	}
	
	final String setSpliter = "\\|";
	final String valSpliter = ":";
	final int lengthOfOpmName = 4;
	final int lengthOfOpmValue = 4;
	private boolean opmCheckAccess(UIOpm_i uiOpm_i, String opm, String opmName, String opmOperation) {
		final String function = "opmCheckAccess";
		
		boolean result = false;
		if ( null != uiOpm_i ) {
			
			String opmName0 = null, opmName1 = null, opmName2 = null, opmName3 = null;
			
			if ( null != opmName ) {
				String [] opmNames = opmName.split(valSpliter);
				if ( null != opmNames ) {
					if ( opmNames.length > 1 ) {
						if ( opmNames.length == lengthOfOpmName ) {
							opmName0	= opmNames[0];
							opmName1	= opmNames[1];
							opmName2	= opmNames[2];
							opmName3	= opmNames[3];
						} else {
							logger.warn("{} opmNames.length[{}] == lengthOfOpmName[{}] IS NOT"
									, new Object[]{function, opmNames.length, lengthOfOpmValue});
						}
					}
				}
			}
			
			if ( null != opm ) {
				String [] opmSets = opm.split(setSpliter);
				
				boolean isAndOperation = false;
				
				if ( opmSets.length > 1 ) {
					if ( null != opmOperation ) {
						if ( 0 == opmOperation.compareToIgnoreCase(TaskServiceImpl_i.AttributeValue.AND.toString()) ) {
							isAndOperation = true;
						}
					}
				}
				
				for ( int i = 0 ; i < opmSets.length ; ++i ) {
					String opmSet = opmSets[i];
					logger.debug("{} opmSet[{}]", function, opmSet);
					String [] opmValues = opmSet.split(valSpliter);
					if ( null != opmValues ) {
						if ( opmValues.length > 1 ) {
							if ( opmValues.length == lengthOfOpmValue ) {
								String opmValue0	= opmValues[0];
								String opmValue1	= opmValues[1];
								String opmValue2	= opmValues[2];
								String opmValue3	= opmValues[3];
								
								if (
										   null != opmValue0
										&& null != opmValue1
										&& null != opmValue2
										&& null != opmValue3
									) {
									
									boolean bResult = false;
									if ( null == opmName || null == opmName0 || null == opmName1 || null == opmName2 || null == opmName3 ) {
										
										bResult = uiOpm_i.checkAccess(
												  opmValue0
												, opmValue1
												, opmValue2
												, opmValue3);
										
										logger.debug("{} opmValue0[{}] opmValue1[{}] opmValue2[{}] opmValue3[{}] => result[{}]"
												, new Object[]{function, opmValue0, opmValue1, opmValue2, opmValue3, result});
										
									} else {
										
										bResult = uiOpm_i.checkAccess(
												  opmName0, opmValue0
												, opmName1, opmValue1
												, opmName2, opmValue2
												, opmName3, opmValue3);
										
										logger.debug("{} opmName0[{}] opmValue0[{}] opmName1[{}] opmValue1[{}] opmName2[{}] opmValue2[{}] opmName3[{}] opmValue3[{}] => result[{}]"
												, new Object[]{function, opmName0, opmValue0, opmName1, opmValue1, opmName2, opmValue2, opmName3, opmValue3, result});

									}
									
									if ( ! isAndOperation ) {
										// OR Operation
										if ( ! bResult ) {
											result = true;
											break;
										}
									} else {
										// AND Operation
										if ( ! bResult ) {
											result = false;
											break;
										} else {
											result = true;
										}
									}
									
								}
							} else {
								logger.warn("{} opmValues.length[{}] == lengthOfOpmName[{}] IS NOT", new Object[]{function, opmValues.length, lengthOfOpmName});
							}
						}
					}
				}
			}
		}
		return result;
	}
}
