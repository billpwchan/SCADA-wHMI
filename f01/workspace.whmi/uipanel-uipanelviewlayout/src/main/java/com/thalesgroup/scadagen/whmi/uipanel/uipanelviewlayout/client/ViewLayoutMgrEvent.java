package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client;

import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;

public interface ViewLayoutMgrEvent {
	
	public enum ViewLayoutMode 
	{
		Panel
		, Image
	}
	
	public enum ViewLayoutAction 
	{
		SingleLayout(1)
		, VDoubleLayout(2)
		, HDoubleLayout(2);

		private int _value;
		
		ViewLayoutAction(int value){
			this._value = value;
		}
		public int getValue() {
			return _value;
		}
//		public static ViewLayoutAction fromint(int i){
//	        for (ViewLayoutAction b : ViewLayoutAction .values()) {
//	            if (b.getValue() == i) { return b; }
//	        }
//	        return null;
//		}
	}

	/**
	 * This should be called by ViewLayoutMgr only.
	 * Update the UI layout
	 * @param taskLaunch
	 * @param viewId
	 */
	void setTaskLaunch(UITaskLaunch taskLaunch, int viewId);
	/**
	 * This should be called by ViewLayoutMgr only.
	 * @param viewLayoutMode
	 * @param viewLayoutAction
	 */
	void setLayout(ViewLayoutMode viewLayoutMode, ViewLayoutAction viewLayoutAction);
	
	/**
	 * This should be called by ViewLayoutMgr only.
	 * @param viewIdActivate
	 */
	void setActivateView(int viewIdActivate);
	
	/**
	 * This should be called by ViewLayoutMgr only.
	 * Update the Split Button UI
	 */
	void setSplitButton();
}

