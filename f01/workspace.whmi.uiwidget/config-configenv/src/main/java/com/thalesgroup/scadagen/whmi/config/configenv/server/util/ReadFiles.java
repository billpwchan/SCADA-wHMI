package com.thalesgroup.scadagen.whmi.config.configenv.server.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class ReadFiles {
	
	private UILogger_i logger = UILoggerFactory.getInstance().get(this.getClass().getName());
	
	private List<File> files = new ArrayList<>();

	public List<File> getFiles() {
		logger.debug("getFiles this.files.size()[{}]", this.files.size());
		return this.files;
	}
	
	public void setFilePathExtension( final String configPath, final String folderName, final String extension) {
		logger.debug("setFilePathExtension configPath[{}] folderName[{}] extension[{}] extension[{}]", new Object[]{configPath, folderName, extension});
		String path = configPath + File.separator + folderName;
		logger.debug("setFilePathExtension folder[{}]", folderName);
		File base = new File(path);
		getFiles(base, extension);
	}
	
	private void getFiles (final File directory, final String fileFilter) {
		Collection<File> files = FileUtils.listFiles(directory
				, new WildcardFileFilter(fileFilter)
				, DirectoryFileFilter.DIRECTORY);
		for ( File file : files ) {
			String path = file.getPath();
			logger.debug("getFiles file path[{}] MATCH extension[{}]", path, fileFilter);
			this.files.add(file);
		}
	}

}
