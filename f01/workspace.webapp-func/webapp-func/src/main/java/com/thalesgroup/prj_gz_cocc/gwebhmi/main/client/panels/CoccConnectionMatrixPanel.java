package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.panels;


//=============================================================================
//THALES COMMUNICATIONS & SECURITY
//
//Copyright (c) THALES 2014 All rights reserved.
//This file and the information it contains are property of THALES COMMUNICATIONS &
//SECURITY and confidential. They shall not be reproduced nor disclosed to any
//person except to those having a need to know them without prior written
//consent of COMMUNICATIONS & SECURITY .
//


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.ConnectionSelectionChangeEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.panel.IMatrixPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.ConnectionMatrixPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.MatrixContext;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.IMatrixRenderer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.renderer.MxRendererConnection;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.IMatrixSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.MatrixNoSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.MatrixSingleSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.GenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MatrixCss;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.presenter.exception.IllegalStatePresenterException;

/**
* A widget implementing the Connection Matrix Panel.
*/
public class CoccConnectionMatrixPanel extends ResizeComposite implements IMatrixPanel {

 /**
  * Main panel wrapping this widget and passed to its {@link ResizeComposite} parent
  */
 private CoccCaptionPanel mainPanel_;

 /**
  * Client presenter of this Widget
  */
 private ConnectionMatrixPresenterClient[] presenters_;

 /**
  * Bus used to publish and listen to events
  */
 private EventBus eventBus_;

 /**
  * Used to remove event handler
  */
 private List<HandlerRegistration> handlerRegistrations_;

 private IMatrixSelectionManager[] selectionManagers_;

 private boolean m_isTerminated = false;
 
 /** Logger */
 private static final ClientLogger logger_ = ClientLogger.getClientLogger();

 /**
  * Constructor
  * @param configIds list of identifiers of the Matrix widget
  * @param eventBus eventBus bus used to listen to events
  */
 public CoccConnectionMatrixPanel (final String[] configIds, final EventBus eventBus, final boolean selectionEnabled) {
	 
	 MatrixContext[] contexts = new MatrixContext[configIds.length];
	 for(int i =0; i < configIds.length; i++)
		 contexts[i] = new MatrixContext(configIds[i]);
	 
	 IMatrixSelectionManager[] selectionManagers = new IMatrixSelectionManager[configIds.length];
	 for(int i =0; i < configIds.length; i++)
		 selectionManagers[i] = selectionEnabled ? new MatrixSingleSelectionManager() : new MatrixNoSelectionManager();
	
	 
	 
	 init(contexts, eventBus, selectionManagers);
 }
 
 
 /**
  * initialize
  * @param matrixContextInfo Matrix Context Information
  * @param eventBus eventBus bus used to listen to events
  * @param selectionManager Selection Manager
  */
 private void init(MatrixContext[] matrixContextInfos,
                      EventBus eventBus,
                      IMatrixSelectionManager[] selectionManagers) {
     
     eventBus_ = eventBus;
     selectionManagers_ = selectionManagers;
     handlerRegistrations_ = new ArrayList<HandlerRegistration>();
     final String captionTitle = Dictionary.getWording("matrix_caption_connection");
     mainPanel_ = new CoccCaptionPanel(captionTitle);
     initWidget(mainPanel_);
     
     LayoutPanel layoutPanel = new LayoutPanel ();
     
     mainPanel_.add(layoutPanel);
     
     presenters_ = new ConnectionMatrixPresenterClient[matrixContextInfos.length];
     
     int unitHeight = 100 / matrixContextInfos.length;
     
     for(int i=0;i<matrixContextInfos.length;i++) {
     
	     // A context
	     final MatrixContext context = new MatrixContext(matrixContextInfos[i]);
	
	     // A view
	     final IMatrixRenderer renderer = new MxRendererConnection();
	     final GenericMatrixView matrixView = new GenericMatrixView(renderer);
	     matrixView.addStyleName(MatrixCss.CSS_GM_PANEL_CONNECTION);
	
	     // A presenter
	     ConnectionMatrixPresenterClient presenter_ = new ConnectionMatrixPresenterClient(context, matrixView, eventBus_);
	     
	     presenters_[i] = presenter_;
	     Widget presenterWidget = presenter_.getView().asWidget();
	     layoutPanel.add(presenterWidget);
	     layoutPanel.setWidgetTopHeight(presenterWidget, i*unitHeight, Unit.PCT, unitHeight, Unit.PCT);
	
	     // Selection manager
	     selectionManagers[i].setPresenter(presenter_);
	     selectionManagers[i].setView(matrixView);
	     
	     presenter_.setSelectionManager(selectionManagers[i]);
	
	     // Register in order to react to selection events
	     if (eventBus_ != null) {
	         handlerRegistrations_.add(eventBus_.addHandler(ConnectionSelectionChangeEvent.TYPE, presenter_));
	     }
     }
 }
 
 public boolean isTerminated() {
     return m_isTerminated ;
 }

 /**
  * {@inheritDoc}
  */
 @Override
 public void terminate() {
     
	 m_isTerminated = true;
			 
     if (mainPanel_ != null) {
         mainPanel_.clear();
     }
     
     for (final HandlerRegistration registration : handlerRegistrations_) {
         registration.removeHandler();
     }
     handlerRegistrations_.clear();
     
     if (presenters_ != null) {
    	 for(int i=0;i<presenters_.length;i++) {
        	 
    	     
    	     try {
    	         presenters_[i].terminate();
    	     } catch (final IllegalStatePresenterException e) {
    	         logger_.error("Error while trying to terminate the ConnectionMatrixPanel.", e);
    	     }
         }
     }
     
     
     if (selectionManagers_ != null) {
	     for(int i=0;i<selectionManagers_.length;i++) {
	         selectionManagers_[i].destroy();
	     }
     }
     
     if (presenters_ != null) {
	     for(int i=0;i<presenters_.length;i++) {
	    	 presenters_[i].destroy();
	     }
     }
     
 }

 /**
  * {@inheritDoc}
  */
 @Override
 public ResizeComposite getAsComposite() {
     return this;
 }
}




