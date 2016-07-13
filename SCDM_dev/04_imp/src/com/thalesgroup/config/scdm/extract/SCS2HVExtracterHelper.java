package com.thalesgroup.config.scdm.extract;

import org.xml.sax.ContentHandler;

import com.thalesis.config.exception.InvalidOperationException;
import com.thalesis.config.exception.TechnicalException;
import com.thalesis.config.exchange.extract.ExtracterHelper;
import com.thalesis.config.storage.Resource;
import com.thalesis.config.utils.Logger;

public class SCS2HVExtracterHelper extends ExtracterHelper {


	  
	/* (non-Javadoc)
	 * @see com.thalesis.config.exchange.extract.ExtracterHelper#getExtractHandler(java.lang.String, java.lang.String)
	 */
	@Override
	public ContentHandler getExtractHandler(String extractDirectory, String root) throws TechnicalException {

		Resource resource = getResource();

		SCS2HVExtracterHandler hv2scsHandler = new SCS2HVExtracterHandler(extractDirectory, root, resource);

		SCS2HVMapSubSystemUtility subSystemUtility = null;
		try {
			subSystemUtility = new SCS2HVMapSubSystemUtility(resource);
			hv2scsHandler.setSubsytemMap(subSystemUtility);
		} catch (Exception e) {
			Logger.EXCHANGE.warn("No sub system defintion found in CDV", e);
		}
		return hv2scsHandler;
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
