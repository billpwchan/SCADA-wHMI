package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadFiles {
	
	private Logger logger					= LoggerFactory.getLogger(ReadFiles.class.getName());
	
	private List<File> files = new ArrayList<>();
	public List<File> getFiles() {
		logger.debug("Begin");
		logger.debug("this.files.size()[{}]", this.files.size());
		logger.debug("End");
		return this.files;
	}
	public void setFilePathExtension( final String module, final String folder, final String extension, final boolean recursive) {
		logger.debug("Begin");
		logger.debug("module[{}] path[{}] extension[{}] extension[{}]", new Object[]{module, folder, extension, recursive});
		String path = module + File.separator + folder;
		logger.debug("folder[{}]", folder);
		File base = new File(path);
		getFiles(base, extension, recursive);
		logger.debug("End");
	}
	private void getFiles ( final File folder, final String extension, final boolean recursive ) {
		logger.debug("Begin");
		logger.debug("folder[{}] extension[{}] recursive[{}]", new Object[]{folder.getPath(), extension, recursive});
		try {
			for ( final File fileEntry: folder.listFiles() ) {
				if ( fileEntry.isDirectory() ) {
					if ( recursive ) {
						getFiles(fileEntry, extension, recursive);
					}
				} else {
					logger.debug("fileEntry.getPath()[{}]", fileEntry.getPath());
					if ( fileEntry.getPath().endsWith(extension) ) {
						logger.debug("fileEntry.getPath()[{}] MATCH extension[{}]", fileEntry.getPath(), extension);
						this.files.add(fileEntry);
					}
				}
			}
		} catch (SecurityException e) {
			logger.warn("Error on get file: " + e.toString());
			e.printStackTrace();
		}
		logger.debug("End");
	}
}
