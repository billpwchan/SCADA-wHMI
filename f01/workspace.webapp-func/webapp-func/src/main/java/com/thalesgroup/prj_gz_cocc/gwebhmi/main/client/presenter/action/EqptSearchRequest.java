package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.presenter.action;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.action.PresenterOperatorAction;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.model.EqptQuery;

public class EqptSearchRequest extends PresenterOperatorAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -580775142541442789L;
	
	/** The query to process **/
    private EqptQuery query_ = new EqptQuery();

	public EqptSearchRequest () {
		super();
	}
	
	/**
     * Constructor
     * @param presenterId the identifier of the presenter requester
     */
    public EqptSearchRequest(final String presenterId) {
        super(presenterId);
    }
    
    /**
     * Get the query to process
     * @return the query
     */
    public EqptQuery getQuery() {
        return query_;
    }

    /**
     * Set the query to process
     * @param query the query to set
     */
    public void setQuery(final EqptQuery query) {
        query_ = query;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return new String ("lineID=" + query_.getLineId() + " stationID=" + query_.getStationId() +
    			" subsystemID=" + query_.getSubsystemId() + " eqptLabel=" + query_.getEqptLabel() + " eqptName=" + query_.getEqptName());
    }
}
