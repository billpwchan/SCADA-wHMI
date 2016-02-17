package com.thalesgroup.scadagen.whmi.config.configenv.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.thalesgroup.scadagen.whmi.config.config.shared.Config;
import com.thalesgroup.scadagen.whmi.config.config.shared.Configs;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ConfigService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ConfigServiceImpl extends RemoteServiceServlet implements ConfigService {

	public Configs configServer(String module, String xmlFile, String tag) {
		
		System.out.println(" **** configServer ["+xmlFile+"] tag["+tag+"]");

		Configs configs = new Configs();
		
		configs.setHeader("XmlFile", xmlFile);
		configs.setHeader("CreateDateTimeLabel", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		System.out.println(" **** configServer configs.getXmlFile()["+configs.getHeader("XmlFile")+"] configs.getCreateDateTimeLabel()["+configs.getHeader("CreateDateTimeLabel")+"]");
		
		String path = getServletContext().getRealPath("/") + "/" + module + "/config/"+xmlFile;
		
		System.out.println(" **** configServer path to read["+path+"]");
		
		ReadConfigXML readConfig = new ReadConfigXML();
		
		ArrayList<Config> cfgs = readConfig.getConfigs(path, tag);
		
		for(Config cfg: cfgs) {
			configs.setConfig(cfg);
		}
		
		return configs;
	}

}
