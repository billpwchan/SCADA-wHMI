package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.HvSharedEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.HvSpecificEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.action.MxSelectionClickRequest;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.action.MxUnselectionClickRequest;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.data.AxisEntryInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.enums.MatrixAxisType;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixAxisClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixAxisContextEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixAxisDblClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixContextMenuEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.event.MatrixDoubleClickEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.presenter.GenericMatrixPresenterClient;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.selection.IMatrixSelectionManager;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.EntityMatrixSelection;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.LocationKey;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxIntersectionState;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxItemStateUpdate;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.update.MxSelectionChangedUpdate;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.GenericMatrixView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.matrix.view.MxIntersection;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.renderer.ScsMatrixRenderer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsMatrixPresenterClient;

public class ScsMatrixMultipleSelectionManager implements IMatrixSelectionManager {

	/** Generic Matrix View */
	protected GenericMatrixView view_;

	/** Generic Matrix Presenter */
	protected GenericMatrixPresenterClient presenter_;

	protected Set<LocationKey> currentSelectedLocationKeySet_ = new HashSet<LocationKey>();
	
	protected Set<String> currentSelectedAxisIdSet_ = new HashSet<String>();

	/** Whether Selection shall be updated from Server returning information */
	protected final boolean updateSelectionFromServer_;

	/** Logger */
	private static final ClientLogger logger = ClientLogger.getClientLogger();

	/**
	 * Constructor
	 */
	public ScsMatrixMultipleSelectionManager() {
		this(false);
	}

	/**
	 * Constructor
	 * 
	 * @param updateSelectionFromServer
	 *            Whether Selection shall be updated from Server returning
	 *            information
	 */
	public ScsMatrixMultipleSelectionManager(final boolean updateSelectionFromServer) {
		updateSelectionFromServer_ = updateSelectionFromServer;
	}

	protected GenericMatrixView getView() {
		return view_;
	}

	protected GenericMatrixPresenterClient getPresenter() {
		return presenter_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setView(final GenericMatrixView view) {
		view_ = view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPresenter(final GenericMatrixPresenterClient presenter) {
		presenter_ = presenter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onViewEvent(final HvSpecificEvent<? extends EventHandler> event) {
		if (event instanceof MatrixClickEvent) {
			onMxItemClick((MatrixClickEvent) event);
		} else if (event instanceof MatrixDoubleClickEvent) {
			onMxItemDblClick((MatrixDoubleClickEvent) event);
		} else if (event instanceof MatrixContextMenuEvent) {
			onMxItemContextClick((MatrixContextMenuEvent) event);
		} else if (event instanceof MatrixAxisClickEvent) {
			onMxAxisClick((MatrixAxisClickEvent) event);
		} else if (event instanceof MatrixAxisDblClickEvent) {
			onMxAxisDblClick((MatrixAxisDblClickEvent) event);
		} else if (event instanceof MatrixAxisContextEvent) {
			onMxAxisContextMenu((MatrixAxisContextEvent) event);
		} else {
			logger.error("Unexpected Event type : " + event.getClass());
		}
	}

	protected void onMxAxisContextMenu(final MatrixAxisContextEvent event) {
		logger.debug("onMxAxisContextMenu : " + event);
	}

	protected void onMxAxisDblClick(final MatrixAxisDblClickEvent event) {
		logger.debug("onMxAxisDblClick : " + event);
	}

	protected void onMxAxisClick(final MatrixAxisClickEvent matrixEvent) {
		final ClickEvent event = matrixEvent.getSourceEvent();
		final MatrixAxisType matrixAxisType = matrixEvent.getAxisType();
		final AxisEntryInfo axisEntryInfo = matrixEvent.getAxisEntryInfo();
		String axisId = axisEntryInfo.getId();
		ScsMatrixRenderer renderer = null;
		ScsMatrixPresenterClient presenter = null;
		HashSet<MxIntersectionState> stateSet = null;
		
		if (getView() != null && getView().getRenderer() != null && getView().getRenderer() instanceof ScsMatrixRenderer) {
			renderer = (ScsMatrixRenderer) getView().getRenderer();
		} else {
			logger.warn("Mismatch renderer type for ScsMatrixMultipleSelection");
			return;
		}
		
		if (getPresenter() != null && getPresenter() instanceof ScsMatrixPresenterClient) {
			presenter = (ScsMatrixPresenterClient)getPresenter();
		} else {
			logger.warn("Mismatch presenter type for ScsMatrixMultipleSelection");
			return;
		}

		logger.debug("onMxAxisClick : " + event);
		
		if (!event.isControlKeyDown()) {

			clearExistingSelection();
			
			if (matrixAxisType == MatrixAxisType.COLUMN) {
				
				Integer columnIndex = presenter.getColumnIndex(axisId);
				
				if (columnIndex != null) {				
					stateSet = renderer.getColumnSet(columnIndex);			
					currentSelectedAxisIdSet_.add(axisId);
					
					for (MxIntersectionState state: stateSet) {				
						LocationKey key = state.getLocationKey();					
						currentSelectedLocationKeySet_.add(key);		
						addSelectedLocationToView(key);		
						onClickAddSelection(state, key);
					}
				}
				
			} else if (matrixAxisType == MatrixAxisType.ROW) {
				
				Integer rowIndex = presenter.getRowIndex(axisId);				

				if (rowIndex != null) {				
					stateSet = renderer.getRowSet(rowIndex);					
					currentSelectedAxisIdSet_.add(axisId);
					
					for (MxIntersectionState state: stateSet) {					
						LocationKey key = state.getLocationKey();					
						currentSelectedLocationKeySet_.add(key);	
						addSelectedLocationToView(key);
						onClickAddSelection(state, key);
					}
				}
			}
			
		} else {

			Boolean isAxisSelected = currentSelectedAxisIdSet_.contains(axisId);
	
			if (matrixAxisType == MatrixAxisType.COLUMN) {
				
				Integer columnIndex = presenter.getColumnIndex(axisId);

				if (columnIndex != null) {
					stateSet = renderer.getColumnSet(columnIndex);				
				}				
			} else if (matrixAxisType == MatrixAxisType.ROW) {
				
				Integer rowIndex = presenter.getRowIndex(axisId);
				
				if (rowIndex != null) {				
					stateSet = renderer.getRowSet(rowIndex);
				}
			}

			if (stateSet != null) {
				for (MxIntersectionState state: stateSet) {
					
					LocationKey key = state.getLocationKey();
					
					if (!isAxisSelected) {		
						currentSelectedLocationKeySet_.add(key);
						addSelectedLocationToView(key);
						onClickAddSelection(state, key);
	
					} else {
						currentSelectedLocationKeySet_.remove(key);
						removeSelectedLocationFromView(key);
						onClickRemoveSelection(key);
					}
				}
			}
			
			if (isAxisSelected) {
				currentSelectedAxisIdSet_.remove(axisId);
			} else {
				currentSelectedAxisIdSet_.add(axisId);
			}
		}
		
		logger.debug("selected key count = " + currentSelectedLocationKeySet_.size());
		for (LocationKey k : currentSelectedLocationKeySet_) {
			logger.debug("Selected key : " + k);
		}
	}

	protected void onMxItemContextClick(final MatrixContextMenuEvent event) {
		logger.debug("onMxItemContextClick : " + event);
	}

	protected void onMxItemDblClick(final MatrixDoubleClickEvent event) {
		logger.debug("onMxItemDblClick : " + event);
	}

	protected void onMxItemClick(final MatrixClickEvent matrixEvent) {

		final MxIntersection square = matrixEvent.getSquare();
		final ClickEvent event = matrixEvent.getSourceEvent();
		final LocationKey key = square != null ? square.getLocationKey() : null;

		logger.debug("onMxItemClick on : " + key);

		final boolean currentLocationSelected = currentSelectedLocationKeySet_.contains(key);

		if (!event.isControlKeyDown()) {

			clearExistingSelection();

			currentSelectedLocationKeySet_.add(key);
			addSelectedLocationToView(key);
			onClickAddSelection(square, key);
		} else {

			if (!currentLocationSelected) {
				currentSelectedLocationKeySet_.add(key);
				addSelectedLocationToView(key);
				onClickAddSelection(square, key);
			} else {
				currentSelectedLocationKeySet_.remove(key);
				removeSelectedLocationFromView(key);
				onClickRemoveSelection(key);
			}
		}

		logger.debug("selected key count = " + currentSelectedLocationKeySet_.size());
		for (LocationKey k : currentSelectedLocationKeySet_) {
			logger.debug("Selected key : " + k);
		}
	}

	protected void clearExistingSelection() {
		for (LocationKey k : currentSelectedLocationKeySet_) {
			removeSelectedLocationFromView(k);
			onClickRemoveSelection(k);
		}
		currentSelectedLocationKeySet_.clear();
		currentSelectedAxisIdSet_.clear();
	}

	protected void onClickRemoveSelection(final LocationKey key) {
		logger.debug("onClickRemoveSelection : " + key);
		removeSelectedLocationFromView(key);

		if (getPresenter() != null) {
			final MxUnselectionClickRequest operatorAction = new MxUnselectionClickRequest();
			operatorAction.setLocationKey(key);
			getPresenter().sendMatrixActionToServer(operatorAction);
		} else {
			logger.error("presenter is NULL");
		}

		notifyUnselection();
	}

	protected void onClickAddSelection(final MxIntersection square, final LocationKey key) {
		logger.debug("onClickAddSelection : " + key);
		addSelectedLocationToView(key);

		if (square != null && square.getMxIntersectionState() != null) {
			final MxIntersectionState state = square.getMxIntersectionState();
			final EntityMatrixSelection entityToSelect = state.getEntityToSelect();

			if (entityToSelect != null) {
				final EntityMatrixSelection selection = createEntitySelection(entityToSelect);

				if (getPresenter() != null) {
					final MxSelectionClickRequest operatorAction = new MxSelectionClickRequest();
					operatorAction.setLocationKey(key);
					operatorAction.setSelectedEntity(selection);

					getPresenter().sendMatrixActionToServer(operatorAction);
				} else {
					logger.error("presenter is NULL");
				}

				notifySelection(entityToSelect);
			} else {
				logger.error("No entity to select for item=" + key);
			}
		} else {
			logger.error("No available state for item=" + key);
		}
	}
	
	protected void onClickAddSelection(final MxIntersectionState state, final LocationKey key) {
		logger.debug("onClickAddSelection : " + key);
		addSelectedLocationToView(key);

		if (state != null) {
			final EntityMatrixSelection entityToSelect = state.getEntityToSelect();

			if (entityToSelect != null) {
				final EntityMatrixSelection selection = createEntitySelection(entityToSelect);

				if (getPresenter() != null) {
					final MxSelectionClickRequest operatorAction = new MxSelectionClickRequest();
					operatorAction.setLocationKey(key);
					operatorAction.setSelectedEntity(selection);

					getPresenter().sendMatrixActionToServer(operatorAction);
				} else {
					logger.error("presenter is NULL");
				}

				notifySelection(entityToSelect);
			} else {
				logger.error("No entity to select for item=" + key);
			}
		} else {
			logger.error("No available state for item=" + key);
		}
	}

	protected EntityMatrixSelection createEntitySelection(final EntityMatrixSelection entityIdToSelect) {

		final EntityMatrixSelection selection = new EntityMatrixSelection();
		selection.setEntityClient(entityIdToSelect.getEntityClient());

		return selection;
	}

	protected void notifyUnselection() {
		// Nothing to do here
		logger.debug("notifyUnselection");
	}

	protected void notifySelection(final EntityMatrixSelection entityToSelect) {
		// Nothing to do here
		logger.debug("notifySelection : " + entityToSelect);
	}

	protected void addSelectedLocationToView(final LocationKey location) {
		if (getView() != null) {
			getView().setLocationSelected(location);
		} else {
			logger.error("view is NULL");
		}
	}

	protected void removeSelectedLocationFromView(final LocationKey location) {
		if (getView() != null) {
			getView().setLocationUnselected(location);
		} else {
			logger.error("view is NULL");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onServerSelectionChanged(final MxSelectionChangedUpdate update) {
		final LocationKey selectedLocationKey = update.getSelectedLocationKey();

		logger.debug("onServerSelectionChanged : " + selectedLocationKey);

		if (updateSelectionFromServer_) {
			currentSelectedLocationKeySet_.add(selectedLocationKey);
			addSelectedLocationToView(selectedLocationKey);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onHvSharedEvent(final HvSharedEvent<?> event) {
		logger.debug("onHvSharedEvent : " + event);
	}

	protected void checkForSelectionRemoval(final MxItemStateUpdate update) {
		final MxIntersectionState intersectionState = update.getIntersectionState();
		final LocationKey locationKey = intersectionState != null ? intersectionState.getLocationKey() : null;
		final int nbEntityToDisplay = intersectionState != null ? intersectionState.getNbEntityToDisplay() : 0;

		if (currentSelectedLocationKeySet_.contains(locationKey) && nbEntityToDisplay == 0) {
			logger.debug("Location will be unselected as it does not contain entities anymore : " + locationKey);
			currentSelectedLocationKeySet_.remove(locationKey);
			removeSelectedLocationFromView(locationKey);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMatrixItemState(final MxItemStateUpdate update) {
		if (!updateSelectionFromServer_) {
			checkForSelectionRemoval(update);
		}

		if (getView() != null) {
			getView().updateMatrixItemState(update);
		} else {
			logger.error("view is NULL");
		}
	}
	
	public Set<LocationKey> getCurrentSelectedLocationKeySet() {
		return currentSelectedLocationKeySet_;
	}
	
	public Set<String> getCurrentSelectedAxisIdSet() {
		return currentSelectedAxisIdSet_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		if (view_ != null) {
			view_ = null;
		}
		if (presenter_ != null) {
			presenter_ = null;
		}
	}
}
