/**
 * 
 */
package com.thalesgroup.config.scdm.extract.hypervisor;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.xml.sax.ContentHandler;

import com.thalesis.config.exception.InvalidOperationException;
import com.thalesis.config.exception.TechnicalException;
import com.thalesis.config.exchange.SubSystemUtility;
import com.thalesis.config.exchange.extract.ExtracterHelper;
import com.thalesis.config.storage.Resource;
import com.thalesis.config.utils.CommonConst;
import com.thalesis.config.utils.Logger;

/**
 * @author T0126039
 * This Helper only give access to the extractor for Hypervisor V9.1. See Extractor Description
 * @see com.thalesis.config.exchange.extract.hypervisor.HypervisorExtractor
 */
public class HypervisorExtractorHelper extends ExtracterHelper {

	public static final String SCS_ROOT_ELEMENT                    = "root";
	public static final String SCS_ROOT_ALPHANUM_ELEMENT           = "root_alphanum";
	public static final String SCS_ID_ATTR_NAME                    = "ID";
	public static final String SCS_AREA_ID_LINK_ATTR_NAME          = "lk0";
	public static final String SCS_SUBSYSTEM_ID_ATTR_NAME          = "subsystemMask";
	public static final String SCS_INSTANCE_LINK_TOKEN             = "_link_";
	public static final String SCS_INSTANCE_LINK_ATTRIBUTE_PREFIX  = "lk";
	public static final String SCS_PROTOTYPE_ATTR_NAME             = CommonConst.COLNAME_PROTOTYPE;
	public static final String SCS_PROTOTYPE_INS_ATTR_NAME         = CommonConst.COLNAME_PROTO_INSERT;
	public static final String SYSXML_REFERENCED_ID_ELEMENT = "name";
	public static final String SYSXML_TYPE_ATTR_NAME        = "xsi:type";
	public static final String SYSXML_REFERENCE_PREFIX      = "ref-";
	public static final String INSTANCE_ROOT_ELEMENT_NAME	= "hv-conf:entitiesConfiguration";
	public static final String INSTANCE_ELEMENT_NAME 		= "hv-conf:entity";
	public static final String LOCATION_ELEMENT_NAME 		= "hv-conf:location";
	public static final String INSTANCE_ID_ATTRIBUTE_NAME 			= "id";
	public static final String HV_STATUS_CLASS_NAME 		= "HvStatus";
	public static final String HV_OPERATION_CLASS_NAME 		= "HvOperation";
	public static final String HV_PARAMETER_CLASS_NAME 		= "HvParameter";
	public static final String[] HV_CLASSES = {HV_STATUS_CLASS_NAME, HV_OPERATION_CLASS_NAME, HV_PARAMETER_CLASS_NAME};
	public static final String HV_ID_ATTR_NAME 				= "hvid";
	public static final String HV_QUALIFIED_CLASS_NAME 		= "hvqname";
	public static final String HV_X					 		= "hvx";
	public static final String HV_Y					 		= "hvy";
	public static final String BAD_IDS_OUTPUT_FILENAME 		= "badIds.bad";
	
	/**
	 * This class can be mentionned in ExportProcessDefintion.xml to be activated on generate command
	 */
	public HypervisorExtractorHelper() {
		super(HypervisorExtractor.getDescription());
	}

	/* (non-Javadoc)
	 * @see com.thalesis.config.exchange.extract.ExtracterHelper#getExtractHandler(java.lang.String, java.lang.String)
	 */
	@Override
	public ContentHandler getExtractHandler(String extractDirectory, String root) throws TechnicalException {
		HypervisorExtractor hypervisorExtractor;
		try {
			Resource resource = getResource();
			hypervisorExtractor = new HypervisorExtractor(extractDirectory, root, resource);
			hypervisorExtractor.setProtoMap(this.getPrototypteUIDFilterMap());
			SubSystemUtility subSystemUtility = this.getSubSystemUtility();
			if (subSystemUtility!=null) {
				hypervisorExtractor.setSubsytemMap(subSystemUtility.getIdsByNames());
			}
			return hypervisorExtractor;
		} catch (IOException e) {
			Logger.EXCHANGE.error("Error while instantiating Hypervisor extractor", e);
		} catch (XMLStreamException e) {
			Logger.EXCHANGE.error("Error while instantiating Hypervisor extractor", e);
		}		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.thalesis.config.exchange.extract.ExtracterHelper#getQueryExtractHandler(com.thalesis.config.storage.Resource, java.lang.String, java.lang.String)
	 */
	@Override
	public ContentHandler getQueryExtractHandler(Resource resource, String extractDirectory, String root) throws TechnicalException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.thalesis.config.exchange.extract.ExtracterHelper#setInstancesFilter(java.lang.String)
	 */
	@Override
	public void setInstancesFilter(String instancesFilterFileURI) throws TechnicalException, InvalidOperationException {
	}

}
