package com.thalesgroup.config.scdm.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import com.thalesis.config.exception.AlreadyExistException;
import com.thalesis.config.exception.BadParameterException;
import com.thalesis.config.utils.ErrorManager;
import com.thalesis.config.utils.IOUtility;
import com.thalesis.config.utils.Logger;


public class ArchiveName {
	
	private static final String	CFG_FILE_EXTENSION	= ".csv";
	private boolean isFilteringOnType_ = true;
	private HashSet<String[]> listenToDcl_ = null;
	private FileWriter writer_ = null;
	private String name_ = null;
	
	public static final int	POINT_TYPE_INDEX		= 0;
	public static final int	POINT_ADDR_INDEX		= 1;
	public static final int	POINT_QUAL_INDEX		= 2;
	public static final int	POINT_TIME_INDEX		= 3;
	
	public ArchiveName() { 
		this.listenToDcl_ = new HashSet<String[]>();		
	}
	
    public ArchiveName(String path, String name) throws BadParameterException, IOException
	{
	    if ((null != name) && (!("".equals(name)))) {
	      this.name_ = name;
	      this.listenToDcl_ = new HashSet<String[]>();	      
	      File output = new File(path + File.separator + getFileName());
	      this.writer_ = new FileWriter(output);
	      
	    } else {
	    	throw new BadParameterException(ErrorManager.getMessage("exchange.extract.nomArchiveIncorrect", name));
	    }
	}

	public String getName()
	{
	   return this.name_;
	}
	
	public String getFileName()
	{
	   if (this.name_.endsWith(CFG_FILE_EXTENSION)) {
	      return this.name_;
	   }

	    return this.name_ + CFG_FILE_EXTENSION;
	}	
	
	public void addListener(String type, String value, String quality, String timestamp)
	    throws AlreadyExistException
	{
	    if (this.isFilteringOnType_) {
	    	if ("".equals(type)) {
			    this.listenToDcl_.clear();
			    this.isFilteringOnType_ = false;
			}
	    	
			String[] listener = new String[3];
			listener[0] = new String(type);
			listener[1] = new String(value);
			listener[2] = new String(quality);
			//listener[3] = new String(timestamp);
	    	
			if (!(this.listenToDcl_.add(listener))) { 
			    Logger.EXCHANGE.error("ArchiveName error : " + ErrorManager.getMessage("exchange.extract.listenerAlreadyExists", type));
			    throw new AlreadyExistException(ErrorManager.getMessage("exchange.extract.listenerAlreadyExists", type));
			}
		}
	}
	
	public String[] findListener(String type)
	{
	    if (this.isFilteringOnType_) {
	    	Iterator<String[]> it = listenToDcl_.iterator();
			while (it.hasNext()) {
				String[] runner = (String[])it.next();
				if (runner[POINT_TYPE_INDEX].equals(type))
					return runner;
			}
	    } else {
	    	return (String[])listenToDcl_.iterator().next();
	    }

	    return null;
	}
	
	public void writeHeader() throws IOException {	
		this.writer_.write("\"ID\",\"shortLabel\",\"pointLabel\",\"type\",\"label\",\"point_nom_instance\",\"geographical_cate\"\n");		
		this.writer_.flush();
	}
	
	public void writePoint(String pointID, String shortLabel, String pointLabel, String eqpType, String label, String point_nom_instance, String geographical_cat)
	   throws IOException
	{		
		this.writer_.write("\"" + pointID + "\",\"" + shortLabel + "\",\"" + pointLabel + "\",\"" + eqpType + "\",\"" + label + "\",\"" + point_nom_instance + "\",\"" + geographical_cat + "\"\n");
		this.writer_.flush();
	}
	
    public void writeBottom() throws IOException {    	
    	IOUtility.close(this.writer_);
	}
	
}
