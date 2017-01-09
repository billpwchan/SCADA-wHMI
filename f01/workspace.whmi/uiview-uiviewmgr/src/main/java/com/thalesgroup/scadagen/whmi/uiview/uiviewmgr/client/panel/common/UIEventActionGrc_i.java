package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

public interface UIEventActionGrc_i {
	public enum UIEventActionGrcAction {
		  GetGrcList("GetGrcList")
		, GetGrcState("GetGrcState")
		, PrepareGrc("PrepareGrc")
		, AbortGrcPreparation("AbortGrcPreparation")
		, LaunchGrc("LaunchGrc")
		, AbortGrc("AbortGrc")
		, SuspendGrc("SuspendGrc")
		, ResumeGrc("ResumeGrc")
		;
		private final String text;
		private UIEventActionGrcAction(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}	
}
