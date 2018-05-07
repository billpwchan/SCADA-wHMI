package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.loader;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.IUIPanelScreen;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;

public class LoadByTask {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	private final String strUIWidgetGeneric		= "UIWidgetGeneric";
	private final String strHeader				= "header";
	
	private final String uiPath					= ":UIGws:UIPanelScreen";
		
	private final String strUIScreenMMIPath		= "UIScreenMMI/UIScreenMMI.view.xml";
	
	private final String strUIScreenMMI			= "UIScreenMMI";
	
	private final String strUIScreenEmpty		= "UIScreenEmpty";
	
	private String singleScreenViews			= "";
	private String singleScreenCtrls			= "";
	
	public LoadByTask(final String parent) {
		final String f = "LoadByTask";
		logger.begin(className, f);
		
		final String strOptsXml = parent+"/"+className+".opts.xml";
		logger.debug(className, f, "strOptsXml[{}]", new Object[]{strOptsXml});
		
		final DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			logger.debug(className, f, "SINGLE_SCREEN_VIEWS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strOptsXml);
			singleScreenViews = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.SINGLE_SCREEN_VIEWS.toString(), strHeader);
			logger.debug(className, f, "singleScreenViews[{}]", singleScreenViews);
			
			logger.debug(className, f, "SINGLE_SCREEN_CTRLS[{}] strOptsXml[{}]", IUIPanelScreen.ParameterName.SINGLE_SCREEN_CTRLS.toString(), strOptsXml);
			singleScreenCtrls = dictionariesCache.getStringValue(strOptsXml, IUIPanelScreen.ParameterName.SINGLE_SCREEN_CTRLS.toString(), strHeader);
			logger.debug(className, f, "singleScreenCtrls[{}]", singleScreenCtrls);
		}
		
		logger.end(className, f);
	}

	private boolean inSingleScreenList(final String items, final String item) {
		final String f = "inSingleScreenList";
		boolean ret = false;
		logger.debug(className, f, "items[{}] item[{}]", new Object[]{items, item});
		if(null!=item&&!item.isEmpty()) {
			if (null!=items&&!items.isEmpty()) {
				String [] vs = items.split(",");
				for(int i=0;i<vs.length;++i){
					String v = vs[i];
					logger.debug(className, f, "v[{}] view[{}]", v, item);
					if(0==v.compareTo(item)){
						ret = true;
					}
				}
			}
		}
		logger.debug(className, f, "items[{}] item[{}] ret[{}]", new Object[]{items, item, ret});
		return ret;
	}
	
	public Map<String, Object>[] load(final UITaskLaunch task, final int numOfScreen) {
		final String f = "load";
		logger.begin(className, f);
	
		logger.debug(className, f, "Prepare numOfScreen[{}]", numOfScreen);
		
		Map<String, Object>[] params = null;
		
		if(null!=task) {
			params = new Map[numOfScreen];
			
			logger.debug(className, f, "task.getUiCtrl()[{}]", task.getUiCtrl());
			
			for ( int screen = 0 ; screen < numOfScreen ; ++screen ) {
				
				logger.debug(className, f, "Prepare screen[{}]", screen);
				
				String uiCtrl = task.getUiCtrl();
				String uiView = task.getUiView();
				String uiOpts = task.getUiOpts();
				String uiDict = task.getUiDict();
				String uiElem = task.getUiElem();
				
				logger.debug(className, f, "task uiCtrl[{}] uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]"
						, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

				if ( 
					screen > 0 
					&& 
					(
						inSingleScreenList(singleScreenCtrls, uiCtrl) 
						|| inSingleScreenList(singleScreenViews, uiView )
					)
				) {
					logger.debug(className, f, "uiCtrl[{}] uiView[{}] Is Single Screen, make another panel strUIScreenEmpty[{}]"
							, new Object[]{uiCtrl, uiView, strUIScreenEmpty});
					
					uiCtrl = strUIScreenEmpty;
					uiView = null;
					uiOpts = null;
					uiDict = null;
					uiElem = null;
					
					logger.debug(className, f, "uiCtrl[{}]", uiCtrl);
				}
				
				final Map<String, Object> param = new HashMap<String, Object>();
				param.put(IUIPanelScreen.Parameters.uiCtrl.toString(), uiCtrl);
				param.put(IUIPanelScreen.Parameters.uiView.toString(), uiView);
				param.put(IUIPanelScreen.Parameters.uiOpts.toString(), uiOpts);
				param.put(IUIPanelScreen.Parameters.uiDict.toString(), uiDict);
				param.put(IUIPanelScreen.Parameters.uiElem.toString(), uiElem);
				
				params[screen] = param;
			}
		}
		logger.end(className, f);
		return params;
	}
	
	public void init(final UINameCard uiNameCard, final String viewXMLFile, final String optsXMLFile) {
		final String f = "load";
		logger.begin(className, f);
		
		String uiCtrl = "UILayoutEntryPoint";
		
		if ( viewXMLFile.equals(strUIScreenMMIPath) ) {
			uiCtrl = strUIScreenMMI;
		}
		
		logger.debug(className, f, "uiPath[{}]", uiPath);
		logger.debug(className, f, "uiCtrl[{}]", uiCtrl);
		logger.debug(className, f, "viewXMLFile[{}]", viewXMLFile);
		logger.debug(className, f, "optsXMLFile[{}]", optsXMLFile);
		
		final UITaskLaunch uiTaskLaunch = new UITaskLaunch();
		uiTaskLaunch.setTaskUiScreen(0);
		uiTaskLaunch.setUiPath(uiPath);
		uiTaskLaunch.setUiCtrl(uiCtrl);
		uiTaskLaunch.setUiView(viewXMLFile);
		uiTaskLaunch.setUiOpts(optsXMLFile);
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(uiTaskLaunch));
		
		logger.end(className, f);
	}
}
