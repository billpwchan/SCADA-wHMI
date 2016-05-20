package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.life;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.SVInitializeTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.description.SituationViewInitClient;

public class CoccSVInitStateTransitionReturn extends
		SVInitializeTransitionReturn {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7211861036335042744L;

	/**
	 * List of Properties of Buttons
	 */
	private List<Map<String, String>> buttons_ = new ArrayList<Map<String, String>>();

	/**
	 * Camera IDs
	 */
	private Map<String, String> cameraIDs_ = new HashMap<String, String>();
	
	/**
	 * GWT Serialization Constructor
	 */
	public CoccSVInitStateTransitionReturn() {
		// Nothing to do
	}

	/**
	 * Constructor
	 * @param viewInit Situation view client configuration computed from the global configuration.
	 */
	public CoccSVInitStateTransitionReturn(final SituationViewInitClient viewInit) {
		super(viewInit);
	}

	/**
	 * Constructor
	 * @param viewInit Situation view client configuration computed from the global configuration
	 * @param buttons List of properties of buttons associated to the view
	 * @param cameraIDs Map of entity ID to Camera ID
	 * @param tunnelvideoIDs Map of entity ID to Tunnel Video ID
	 */
	public CoccSVInitStateTransitionReturn(final SituationViewInitClient viewInit,
			final List<Map<String, String>> buttons,
			final Map<String, String> cameraIDs) {
		this(viewInit);
		buttons_ = buttons;
		cameraIDs_ = cameraIDs;
	}

	/**
	 * Returns a list of buttons associated to the view
	 * @return List of properties of buttons associated to the view
	 */
	public List<Map<String, String>> getButtons() {
		return buttons_;
	}

	/**
	 * Returns a map of entity ID to camera ID
	 * @return Map of entity ID to camera ID
	 */
	public Map<String, String> getCameraIDs() {
		return cameraIDs_;
	}

}
