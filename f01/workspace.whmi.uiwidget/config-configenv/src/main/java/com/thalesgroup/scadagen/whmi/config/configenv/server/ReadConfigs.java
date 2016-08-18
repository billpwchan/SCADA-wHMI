package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadConfigs {
	List<File> files = new ArrayList<>();
	public List<File> getConfigNames() {
//		System.out.println(" **** ReadConfigs getConfigNames Begin/End");
		return this.files;
	}
	public void setConfigPathExtension( final String module, final String folder, final String extension, final boolean recursive) {
//		System.out.println(" **** ReadConfigs setConfigPathExtension Begin");
//		System.out.println(" **** ReadConfigs setConfigPathExtension module["+module+"] path["+folder+"] extension["+extension+"] extension["+recursive+"]");
		String path = module + File.separator + folder;
//		System.out.println(" **** ReadConfigs setConfigPathExtension folder["+folder+"]");
		File base = new File(path);
		getConfigNames(base, extension, recursive);
//		System.out.println(" **** ReadConfigs setConfigPathExtension End");
	}
	private void getConfigNames ( final File folder, final String extension, final boolean recursive ) {
//		System.out.println(" **** ReadConfigs getConfigNames Begin");
		System.out.println(" **** ReadConfigs getConfigNames folder["+folder.getPath()+"] extension["+extension+"] recursive["+recursive+"]");
		try {
			for ( final File fileEntry: folder.listFiles() ) {
				if ( fileEntry.isDirectory() ) {
					if ( recursive ) {
						getConfigNames(fileEntry, extension, recursive);
					}
				} else {
//					System.out.println(" **** ReadConfigs getConfigNames fileEntry.getPath()["+fileEntry.getPath()+"]");
					if ( fileEntry.getPath().endsWith(extension) ) {
//						System.out.println(" **** ReadConfigs getConfigNames fileEntry.getPath()["+fileEntry.getPath()+"] MATCH extension["+extension+"]");
						this.files.add(fileEntry);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
//		System.out.println(" **** ReadConfigs getConfigNames End");
	}
}
