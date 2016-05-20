package com.thalesgroup.scadasoft.gwebhmi.main.client.presenter;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.HypervisorPresenterClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.context.IPresenterContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.life.StateTransitionReturn;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.update.IPresenterUpdate;
import com.thalesgroup.scadasoft.gwebhmi.main.client.view.IMobileToolbarView;

public class MobileToolBarPresenter extends HypervisorPresenterClientAbstract<IMobileToolbarView> {

    /**
     * Description of the presenter type. Used to link the client presenter with
     * the server one.
     */
    public static final String PRESENTER_TYPE = MobileToolBarPresenter.class.getName();

    private IMobileToolbarView view_;

    public MobileToolBarPresenter(IPresenterContext context, IMobileToolbarView view) {
        super(context, view);
        view_ = view;
    }

    @Override
    public String getPresenterTypeIdentifier() {
        return PRESENTER_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize(StateTransitionReturn result) {
        super.onInitialize(result);
        if (result instanceof MobileToolbarStateTransitionReturn) {
            MobileToolbarStateTransitionReturn transitionReturn = (MobileToolbarStateTransitionReturn) result;
            view_.setNavMap(transitionReturn.getNavMap(), view_.getMainPanel());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(IPresenterUpdate presUpdate) {

    }

}
