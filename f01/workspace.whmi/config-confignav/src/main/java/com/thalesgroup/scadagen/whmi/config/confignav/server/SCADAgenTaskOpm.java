package com.thalesgroup.scadagen.whmi.config.confignav.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task;
import com.thalesgroup.scadagen.whmi.config.confignav.shared.Task_i.TaskAttribute;
import com.thalesgroup.scadagen.wrapper.wrapper.server.UIOpm_i;

/**
 * @author syau
 *
 */
public class SCADAgenTaskOpm {
	
	private Logger logger					= LoggerFactory.getLogger(SCADAgenTaskOpm.class.getName());
	
	private UIOpm_i uiOpm_i = null;
	public void setUIOpm_i(UIOpm_i uiOpm_i) {
		this.uiOpm_i = uiOpm_i;
	}
	
	/*
	* 
	*/
	public int taskOpmIsValid(Task tsk) {
		final String function = "opmChechAccess";
		
		int result = 0;
		
		if ( null != uiOpm_i ) {
			String opm = tsk.getParameter(TaskAttribute.opm.toString());
			if ( null != opm && opm.length() > 0 ) {
				String opmOperation = tsk.getParameter(TaskAttribute.opmOperation.toString());
				String opmName = tsk.getParameter(TaskAttribute.opmName.toString());
				
				logger.warn("{} opm[{}] opmOperation[{}] opmName[{}]", new Object[]{function, opm, opmOperation, opmName});
				
				boolean opmValid = opmCheckAccess(uiOpm_i, opm, opmOperation, opmName);
				
				logger.warn("{} opm[{}] opmOperation[{}] opmName[{}] opmValid[{}]", new Object[]{function, opm, opmOperation, opmName, opmValid});
				
				result = ( opmValid ? result+1 : result-1 );
				
				logger.warn("{} opm[{}] opmOperation[{}] opmName[{}] opmValid[{}] result[{}]", new Object[]{function, opm, opmOperation, opmName, opmValid, result});

			} else {
				logger.debug("{} uiOpm_i IS NULL");
			}
		}
		
		return result;
	}
	

	final int lengthOfOpmName = 4;
	final int lengthOfOpmValue = 4;
	private boolean opmCheckAccess(UIOpm_i uiOpm_i, String opm, String opmOperation, String opmName) {
		final String function = "opmCheckAccess";
		
		boolean result = false;
		if ( null != uiOpm_i ) {

			if ( null != opm && opm.length() > 0 ) {
				
				String [] opmNames = null;
				if ( null != opmName && opmName.length() > 0 ) {
					opmNames = opmName.split(SCADAgenOpmTask_i.valSpliter);
					if ( null != opmNames && opmNames.length != lengthOfOpmName ) {
						logger.warn("{} opmNames.length[{}] == lengthOfOpmName[{}] IS NOT"
								, new Object[]{function, opmNames.length, lengthOfOpmValue});
					}
				}
				
				boolean isAndOperation = false;
				
				String [] opmSets = opm.split(SCADAgenOpmTask_i.setSpliter);
				int opmSetsLength = opmSets.length;
				logger.debug("{} opmSetsLength[{}]", new Object[]{function, opmSetsLength});
				
				if ( null != opmSets && opmSetsLength > 1 ) {
					if ( null != opmOperation ) {
						if ( 0 == opmOperation.compareToIgnoreCase(SCADAgenOpmTask_i.AttributeValue.AND.toString()) ) {
							isAndOperation = true;
						}
					}
				}
				
				logger.debug("{} opmOperation[{}] isAndOperation[{}]", new Object[]{function, opmOperation, isAndOperation});
				
				for ( int i = 0 ; i < opmSetsLength ; ++i ) {
					if ( null != opmSets[i] ) {
						logger.debug("{} i[{}] opmSet[{}]", new Object[]{function, i, opmSets[i]});
						String [] opmValues = opmSets[i].split(SCADAgenOpmTask_i.valSpliter);
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
