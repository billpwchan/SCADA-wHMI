package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
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
	
	public void setFilePathExtension( final String configPath, final String folderName, final String extension) {
		logger.debug("Begin");
		logger.debug("configPath[{}] folderName[{}] extension[{}] extension[{}]", new Object[]{configPath, folderName, extension});
		String path = configPath + File.separator + folderName;
		logger.debug("folder[{}]", folderName);
		File base = new File(path);
		getFiles(base, extension);
		logger.debug("End");
	}
	
	private void getFiles (final File directory, final String fileFilter) {
		Collection<File> files = FileUtils.listFiles(directory
				, new WildcardFileFilter(fileFilter)
				, DirectoryFileFilter.DIRECTORY);
		for ( File file : files ) {
			String path = file.getPath();
			logger.debug("file path[{}] MATCH extension[{}]", path, fileFilter);
			this.files.add(file);
		}
	}
	
//	public void setFilePathExtension( final String configPath, final String folderName, final String extension, final boolean recursive) {
//	logger.debug("Begin");
//	logger.debug("configPath[{}] folderName[{}] extension[{}] extension[{}]", new Object[]{configPath, folderName, extension, recursive});
//	String path = configPath + File.separator + folderName;
//	logger.debug("folder[{}]", folderName);
//	File base = new File(path);
//	getFiles(base, extension, recursive);
//	logger.debug("End");
//}
	
//	private void getFiles ( final File folder, final String extension, final boolean recursive ) {
//		logger.debug("Begin");
//		logger.debug("folder[{}] extension[{}] recursive[{}]", new Object[]{folder.getPath(), extension, recursive});
//		try {
//			for ( final File fileEntry: folder.listFiles() ) {
//				if ( fileEntry.isDirectory() ) {
//					if ( recursive ) {
//						getFiles(fileEntry, extension, recursive);
//					}
//				} else {
//					logger.debug("fileEntry.getPath()[{}]", fileEntry.getPath());
//					if ( fileEntry.getPath().endsWith(extension) ) {
//						logger.debug("fileEntry.getPath()[{}] MATCH extension[{}]", fileEntry.getPath(), extension);
//						this.files.add(fileEntry);
//					}
//				}
//			}
//		} catch (SecurityException e) {
//			logger.warn("Error on get file: " + e.toString());
//			e.printStackTrace();
//		}
//		logger.debug("End");
//	}
}
