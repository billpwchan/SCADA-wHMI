package com.thalesgroup.scadagen.whmi.config.configenv.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {
	final String className = "ReadFiles";
	final String logPrefix = "["+className+"] ";
	private List<File> files = new ArrayList<>();
	public List<File> getFiles() {
		final String function = "getFiles";
		System.out.println(logPrefix+function+" Begin");
		System.out.println(logPrefix+function+" this.files.size()["+this.files.size()+"]");
		System.out.println(logPrefix+function+" End");
		return this.files;
	}
	public void setFilePathExtension( final String module, final String folder, final String extension, final boolean recursive) {
		final String function = "setFilePathExtension";
		System.out.println(logPrefix+function+" Begin");
		System.out.println(logPrefix+function+" module["+module+"] path["+folder+"] extension["+extension+"] extension["+recursive+"]");
		String path = module + File.separator + folder;
		System.out.println(logPrefix+function+" folder["+folder+"]");
		File base = new File(path);
		getFiles(base, extension, recursive);
		System.out.println(logPrefix+function+" End");
	}
	private void getFiles ( final File folder, final String extension, final boolean recursive ) {
		final String function = "getFiles";
		System.out.println(logPrefix+function+" Begin");
		System.out.println(logPrefix+function+" folder["+folder.getPath()+"] extension["+extension+"] recursive["+recursive+"]");
		try {
			for ( final File fileEntry: folder.listFiles() ) {
				if ( fileEntry.isDirectory() ) {
					if ( recursive ) {
						getFiles(fileEntry, extension, recursive);
					}
				} else {
					System.out.println(logPrefix+function+" fileEntry.getPath()["+fileEntry.getPath()+"]");
					if ( fileEntry.getPath().endsWith(extension) ) {
						System.out.println(logPrefix+function+" fileEntry.getPath()["+fileEntry.getPath()+"] MATCH extension["+extension+"]");
						this.files.add(fileEntry);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		System.out.println(logPrefix+function+" End");
	}
}
