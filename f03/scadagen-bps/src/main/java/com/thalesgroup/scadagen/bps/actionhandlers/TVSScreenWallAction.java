package com.thalesgroup.scadagen.bps.actionhandlers;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.tools.bean.IBeanEditor;
import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.attribute.IntAttributeType;
import com.thalesgroup.hv.data_v1.attribute.StringAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.bps.conf.OperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.common.PropertyType;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public class TVSScreenWallAction extends AbstractActionFromOls {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TVSScreenWallAction.class);

	protected String cameraType_ = "RTSP";
	protected int paneIdx_ = 0;
	
	protected static final String STR_DEFAULT_LAYOUT_IDX = "1";

//	@Override
//	protected boolean isIncludeCorrelationId() {
//		return false;
//	}

	@Override
	public void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity,
			Map<String, AbstractAttributeType> attributeMap, String actionConfigId)
	{
		operationType_ = "com.thalesgroup.lib_tvs.data.tvs.cnf.screenwall.operation.ApplySourcesRequest";
		
		Operation operation = null;
		String hvOperColumnName = "hvOper";
		String hvidColumnName = "hvid";

		try {
			operation = OperationConfigLoader.getInstance().getOperation(actionConfigId);
		} catch (HypervisorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
    	if (operation != null) {
    		List<PropertyType> propertyList = operation.getConfigProperty();
    		for (PropertyType property: propertyList) {
    			if (property.getName().compareTo("hvOperColumnName") == 0) {
    				hvOperColumnName = property.getValue();
    			} else if (property.getName().compareTo("hvidColumnName") == 0) {
    				hvidColumnName = property.getValue();
    			} else if (property.getName().compareTo("cameraType") == 0) {
    				cameraType_ = property.getValue();
    			} else if (property.getName().compareTo("paneIdx") == 0) {
    				String str = property.getValue();
    				try {
    					paneIdx_ = Integer.parseInt(str);
    				} catch (NumberFormatException e) {
    					LOGGER.error("Invalid paneIdx value in operation configuration {}", e);
    				}
    			}
    		}
    	}
		
		super.decodeOlsData(entity);

		String csvStr = olsDataMap_.get(hvOperColumnName);
		if (csvStr != null) {
			try {
				extractDataFromCsvStr(operationConnector, csvStr, ",");
			} catch (HypervisorException e) {
				LOGGER.error("Error extract data from ols data column. {}", e);
			}
		}
	
		entityID_ = olsDataMap_.get(hvidColumnName);

		if (entityID_ == null || csvStr == null) {
			LOGGER.error("Error settting entityID or operation param");
			return;
		} else {
			super.execute(operationConnector, entity, attributeMap, actionConfigId);
		}
	}
	

	// Expected csvData format: operatorID,camerID,layoutIdx,paneIdx(optional)
	// Sample csvData: operator1,CAM001,1,0
	protected void extractDataFromCsvStr(IGenericOperationConnector operationConnector, String csvData, String delimiter) throws HypervisorException {

		try {
			IBeanEditor beanEditor = operationConnector.getTools().getOperationHelper().getBeanEditor();
			Scanner scanner = new Scanner(csvData);
			if (scanner != null) {
				if (delimiter != null && !delimiter.isEmpty()) {
					scanner.useDelimiter(delimiter);
				}
				
				if (scanner.hasNext()) {
					// first column is operatorID
					operatorID_ = scanner.next();
					LOGGER.trace("csv operatorID=[{}]", operatorID_);
					
					Class<?> sourceMappingContainerTypeClass = Class.forName("com.thalesgroup.lib_tvs.data.tvs.exchange.screenwall.attribute.SourceMappingContainerType");
					Object sourceMappingContainerTypeObj = sourceMappingContainerTypeClass.newInstance();

					while (scanner.hasNext()) {
						String cameraID = scanner.next();
						String layoutIdx = scanner.hasNext() ? scanner.next() : STR_DEFAULT_LAYOUT_IDX;
						String paneIdx = scanner.hasNext() ? scanner.next() : null;

						if (cameraID != null && layoutIdx != null) {							
							long currentTimeStamp = System.currentTimeMillis();

							Class<?> sourceTypeAttributeTypeClass = Class.forName("com.thalesgroup.lib_tvs.data.tvs.exchange.screenwall.attribute.SourceTypeAttributeType");
							Object sourceTypeAttributeTypeObj = sourceTypeAttributeTypeClass.newInstance();
							beanEditor.setValue(sourceTypeAttributeTypeObj, "value", getCameraType());
							beanEditor.setValue(sourceTypeAttributeTypeObj, "valid", true);
							beanEditor.setValue(sourceTypeAttributeTypeObj, "timestamp", currentTimeStamp);
							
							StringAttributeType sourceAtt = new StringAttributeType();
							sourceAtt.setValue(cameraID);
							sourceAtt.setValid(true);
							sourceAtt.setTimestamp(currentTimeStamp);

							Class<?> switchableSourceTypeClass = Class.forName("com.thalesgroup.lib_tvs.data.tvs.exchange.screenwall.attribute.SwitchableSourceType");
							Object switchableSourceTypeObj = switchableSourceTypeClass.newInstance();
							beanEditor.setValue(switchableSourceTypeObj, "source", sourceAtt);
							beanEditor.setValue(switchableSourceTypeObj, "type", sourceTypeAttributeTypeObj);

							IntAttributeType layoutIdxAtt = new IntAttributeType();
							layoutIdxAtt.setValue(Integer.parseInt(layoutIdx));
							layoutIdxAtt.setValid(true);
							layoutIdxAtt.setTimestamp(currentTimeStamp);
							
							IntAttributeType paneIdxAtt = new IntAttributeType();
							if (paneIdx == null) {
								paneIdxAtt.setValue(getDefaultPaneIdx());
							} else {
								paneIdxAtt.setValue(Integer.parseInt(paneIdx));
							}
							paneIdxAtt.setValid(true);
							paneIdxAtt.setTimestamp(currentTimeStamp);
							
							Class<?> sourceMappingTypeClass = Class.forName("com.thalesgroup.lib_tvs.data.tvs.exchange.screenwall.attribute.SourceMappingType");
							Object sourceMappingTypeObj = sourceMappingTypeClass.newInstance();
							beanEditor.setValue(sourceMappingTypeObj, "source", switchableSourceTypeObj);
							beanEditor.setValue(sourceMappingTypeObj, "layoutIdx", layoutIdxAtt);
							beanEditor.setValue(sourceMappingTypeObj, "paneIdx", paneIdxAtt);
							beanEditor.setValue(sourceMappingTypeObj, "valid", true);
							beanEditor.setValue(sourceMappingTypeObj, "timestamp", currentTimeStamp);
							
							beanEditor.setValue(sourceMappingContainerTypeObj, "sourceMappings", sourceMappingTypeObj);
							beanEditor.setValue(sourceMappingContainerTypeObj, "valid", true);
							beanEditor.setValue(sourceMappingContainerTypeObj, "timestamp", currentTimeStamp);
						}
					}		
					operationComplexParam_.put("sourceMappingContainer", sourceMappingContainerTypeObj);
				}
				scanner.close();
			}
		} catch (Exception e) {
			throw new HypervisorException(e);
		}
	}
	
	protected String getCameraType() {
		return cameraType_;
	}
	
	protected int getDefaultPaneIdx() {
		return paneIdx_;
	}

}
