package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.test;

public class UIEventActionTest {
	
	private String dictionariesCacheName = "UIWidgetGeneric";
	private String filename = "UIViewAlmMgnFilter.opts.xml";
	private String tag = "option";
	
	public UIEventActionTest() {
	}
	
	public void test() {
		testUIOptionMgrTest(dictionariesCacheName, filename, tag);
		testUIOptionCachesTest(dictionariesCacheName, filename, tag);
		testUIEventActionMgrTest(dictionariesCacheName, filename, tag);
	}

	public void testUIOptionMgrTest(String dictionariesCacheName, String filename, String tag) {
		UIOptionMgrTest test = new UIOptionMgrTest();
		test.setDictionariesCacheName(dictionariesCacheName);
		test.setFilename(filename);
		test.setTag(tag);
		test.test();
	}
	
	public void testUIOptionCachesTest(String dictionariesCacheName, String filename, String tag) {
		UIOptionCachesTest test = new UIOptionCachesTest();
		test.setDictionariesCacheName(dictionariesCacheName);
		test.setFilename(filename);
		test.setTag(tag);
		test.test();
	}
	
	public void testUIEventActionMgrTest(String dictionariesCacheName, String filename, String tag) {
		UIEventActionMgrTest test = new UIEventActionMgrTest();
		test.setDictionariesCacheName(dictionariesCacheName);
		test.setFilename(filename);
		test.setTag(tag);
		test.test();
	}
}
