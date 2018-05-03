package com.thalesgroup.scadagen.whmi.opm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;

import com.thalesgroup.hypervisor.mwt.core.util.common.Resources;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.security.server.session.MwtSessionAuthenticationStrategy;

public class OpmLoader extends MwtSessionAuthenticationStrategy {

	private static final Logger logger = LoggerFactory.getLogger(OpmLoader.class);
	private static final String scadasoftPropertiesRelativePath_ = "projectConf" + File.separator + "scadasoft.properties";

	private String newLastUpdateTime = "";
	private String lastUpdateTime = "";
	
	private String opmManagerHostname_ = null;
	private int opmManagerPort_ = 0;
	
	// Path of "mwt-opm-permissions.xml", as returned by 'Resources.getResource()'. This will be the file generated here.
	private File permissionsFile_ = null;  
	
	// Backup of the previously existing permissions file.
	private File permissionsFileBackup_ = null;
	
	// The file "mwt-opm-permissions_SCADA.xml": this is the file that is downloaded from OPM manager.
	private File downloadedFile_ = null;   

	// The "MERGE" index file, containing the list of the XML files to merge.
	private File mergeIndexFile_ = null;
				
	// The directory containing the downloaded file, the "MERGE" file and the generated permissions file.
	private File permissionsDirectory_ = null;
	
	public OpmLoader(final SessionRegistry sessionRegistry)
	{
		super(sessionRegistry);
	}	
	
	
	private void retrieveHostnameAndPort()
	{
		try
		{
			Properties scadasoftProperties = readScadasoftConfigurationFile();
			if (scadasoftProperties != null)
			{
				opmManagerHostname_ = scadasoftProperties.getProperty("scs.opm.server.host");
				String opmManagerPortStr = scadasoftProperties.getProperty("scs.opm.server.port");
			
				if (opmManagerPortStr != null)
					opmManagerPort_ = Integer.parseInt(opmManagerPortStr);
			}	
		}
		catch (IOException e)
		{
			logger.error("Could not load the \"scadasoft.properties\" configuration file: " + e.getMessage());
		}
		 
		if (opmManagerHostname_ == null)
		{
			logger.warn("Using default value for OPM Manager host.");
			opmManagerHostname_ = "localhost";
		}
		
		if (opmManagerPort_ == 0)
		{
			logger.warn("Using default value for OPM manager port.");
			opmManagerPort_ = 12080;	
		}
		
		logger.info("OPM Mananager host: \"" + opmManagerHostname_ + "\", port: " + opmManagerPort_);		
	}
	
	// Goes through the "shared.loader" path, and, for each element, searches for "scadasoft.properties" in 
	// a sub-directory "projectConf". 
	// Returns null if the file could not be found.
	
	private InputStream retrieveScadasoftConfigurationFile()
	{			
		String sharedLoaderPath = System.getProperty("shared.loader");
		if (sharedLoaderPath == null)
			throw new AuthenticationServiceException("The property \"shared.loader\" must be present in the Catalina configuration file");
		
		InputStream result = null;
		StringTokenizer st = new StringTokenizer(sharedLoaderPath, ",");
		while (result == null && st.hasMoreTokens())
		{
			String path = st.nextToken().trim();
			String expandedPath = expandProperties(path);
			
			File propertiesFile = null;
			Path propertiesFilePath = null;
			
			try 
			{
				propertiesFile = new File(expandedPath + File.separator + scadasoftPropertiesRelativePath_);
				propertiesFilePath = propertiesFile.toPath();
			}
			catch (InvalidPathException e)
			{
				// In the "shared.loader" path list, there can be wildcards. Ignore these entries.
				logger.info("Not a path entry: \"" + expandedPath + "\".");
				continue;  // To next path entry.
			}
			
			assert propertiesFile != null && propertiesFilePath != null;
			
			logger.info("shared.loader path entry: \"" + path + "\" -- \"" + expandedPath + "\" -- \"" + propertiesFilePath.toString() + "\"");
			
			if (Files.exists(propertiesFilePath))
			{
				logger.info("Found the configuration file: \"" + expandedPath + scadasoftPropertiesRelativePath_ + "\"");
				try
				{
					result = new FileInputStream(propertiesFile);
				}
				catch (FileNotFoundException e)
				{
					logger.error("File \"" + propertiesFile.getPath() + "\" has disappeared: " + e.getMessage());
					result = null;
					// Continue to next entry in 'sharedLoaderPath'.
				}
			}
		}
		
		return result;
	}
	
	// Expands the reference to properties in a string. For instance:
	// "${catalina.home}/conf/hypervisor-configuration" will be returned as 
	// "D:\dev\SCSTraining\scspaths\WEBAPP\apache-tomcat-8.0.23\conf/hypervisor-configuration".
	//
	// Properties names must be enclosed within curly braces.
	//
	// There can be several properties in the source string, but they cannot be nested.
	// If a property does not exist, it is not expanded.
	
	private String expandProperties(String path)
	{
		StringBuilder result = new StringBuilder();
		int indexAfterClosingBrace = 0;
		int indexDollar = path.indexOf('$', indexAfterClosingBrace);		
		
		while (indexDollar != -1
				&& indexDollar < path.length() - 2 
				&& path.charAt(indexDollar + 1) == '{'
				&& indexAfterClosingBrace != -1
				&& indexAfterClosingBrace < path.length() - 1) 
		{
			int indexOpeningBrace = indexDollar + 1;
			int indexClosingBrace = path.indexOf('}', indexDollar + 1);
			if (indexClosingBrace != -1)
			{
				String propertyName = path.substring(indexOpeningBrace + 1, indexClosingBrace);
				String expandedProperty = propertyName.isEmpty() ? null : System.getProperty(propertyName);
				if (expandedProperty == null)
					result.append("${" + propertyName + "}");
				else 
					result.append(expandedProperty);
				
				indexAfterClosingBrace = indexClosingBrace + 1;
				indexDollar = path.indexOf('$', indexAfterClosingBrace);
			}
			else {
				// Opening brace without a closing brace.
				// Copy the string to result and quit.
				result.append(path.substring(indexDollar));
				indexAfterClosingBrace = -1;
			}
		}
		
		if (indexAfterClosingBrace != -1 && indexAfterClosingBrace < path.length())
			result.append(path.substring(indexAfterClosingBrace));
		
		return result.toString();
	}
	
	// Throws an IOException if the properties file could be found but could not be read.
	// Returns null if the properties file could not be found.
	
	private Properties readScadasoftConfigurationFile() throws IOException
	{		
		InputStream inputStream = retrieveScadasoftConfigurationFile();
		if (inputStream == null)
		{
			logger.error("Configuration file \"" 
					+ scadasoftPropertiesRelativePath_ +
					"\" could not be found. Check the property \"shared.loader\" in \"catalina.properties\"");
			return null;
		}
		
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;
	}
	
	private void retrievePermissionsFilesPath()
	{
		if (permissionsFile_ != null)
		{
			// Already retrieved.
			assert permissionsFileBackup_ != null && downloadedFile_ != null && permissionsDirectory_ != null;
			return;
		}
		
		URL configFile = Resources.getResource("opm/mwt-opm-permission.xml");
		
		// If the OPM file is not already present, the Resources.getResource() returns
		// null.
		if (configFile == null)
			throw new AuthenticationServiceException("OPM configuration file \"mwt-opm-permission.xml\" must always be present.");
		
		String permissionsFilePath = configFile.getPath();
		permissionsFile_ = new File(permissionsFilePath);
		permissionsFileBackup_ = new File(permissionsFilePath + ".backup");
		permissionsDirectory_ = permissionsFile_.getParentFile();
		
		if (permissionsDirectory_ == null)
			throw new AuthenticationServiceException("Cannot retrieve the parent directory of \"" + permissionsFilePath + "\"");
		
		downloadedFile_ = new File(permissionsDirectory_ + File.separator + "mwt-opm-permission_SCADA.xml");
		mergeIndexFile_ = new File(permissionsDirectory_ + File.separator + "MERGE");
		
		logger.info("Permissions directory: \"" + permissionsDirectory_.getAbsolutePath() + "\"");		
		logger.info("Permissions file: \"" + permissionsFile_.getAbsolutePath() + "\"");
		logger.info("Permissions file backup: \"" + permissionsFileBackup_.getAbsolutePath() + "\"");
		logger.info("Downloaded file: \"" + downloadedFile_.getAbsolutePath() + "\"");
		logger.info("Index file: \"" + mergeIndexFile_.getAbsolutePath() + "\"");
	}	
	
	// Finds the OPM manager service, and returns a stream from its "opm/dump" method.
	
	private InputStream retrieveOpmStream()
	{
		if (this.opmManagerHostname_ == null)
			retrieveHostnameAndPort();					
		
		URL localHostServer;
		
		try
		{
			localHostServer = new URL("http://" + this.opmManagerHostname_ + ":" + this.opmManagerPort_ + "/opm/dump");
		} 
		catch (MalformedURLException e)
		{
			throw new AuthenticationServiceException("Malformed URL exception while trying to retrieve the OPM file: " + e.getMessage());
		}		
		
		InputStream result;
		
		try
		{
			result = localHostServer.openStream();
		}
		catch (IOException e)
		{
			// Do not throw an exception, use the OPM file that already exists.
			// throw new AuthenticationServiceException("Cannot retrieve the OPM file from OPM Manager: " + e.getMessage());
			return null;
		}
		
		return result;
	}
	
	 
	 // Downloads the file "mwt-opm-permissions_SCADA.xml" from the OPM Manager.
	 // 
	 // If the OPM Manager could be reached, but an error happened during the downloading, an 'AuthenticationServiceException' exception is raised.
	 //  
	 // Returns false if the OPM Manager could not be reached.
	 // Returns true if the OPM manager could be reached and the file could be downloaded.
	 	
	private boolean downloadOpmFile()
	{
		assert downloadedFile_ != null;
		
		InputStream in = retrieveOpmStream();
		if (in == null)
		{
			logger.warn("The OPM Manager could not be contacted. Using the local permissions file");
			return false;
		}

		try
		{
			logger.warn("Retrieving permissions file from the OPM Manager");
			Files.copy(in, downloadedFile_.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			throw new AuthenticationServiceException("Cannot retrieve the permissions file \"" + permissionsFile_.getPath() + "\" from OPM Manager: " + e.getMessage());
		}
		
		return true;
	}
	
	// Backups the previously existing permissions file, if any.
	// Throws an 'AuthenticationServiceException' if the backup could not be created.
	
	private void backupPermissionsFile()
	{
		assert permissionsFile_ != null && permissionsFileBackup_ != null;
		
		if (!Files.exists(permissionsFile_.toPath()))
		{
			// Nothing to backup.
			return;
		}
		
		try
		{
			Files.move(permissionsFile_.toPath(), permissionsFileBackup_.toPath(), StandardCopyOption.REPLACE_EXISTING);		
		}
		catch (IOException e)
		{
			throw new AuthenticationServiceException("Cannot backup the OPM file \"" + permissionsFile_.getPath() + "\" to \"" + permissionsFileBackup_.getPath() + "\": " + e.getMessage());
		}	
	}
	
	// Reverts a previously existing backup of the permissions file.
	// Returns false if the backup could not be restored.
	
	private boolean revertBackupOfPermissionsFile()
	{
		assert permissionsFile_ != null && permissionsFileBackup_ != null;
		
		if (!Files.exists(permissionsFileBackup_.toPath()))
		{
			logger.error("Backup \"" + permissionsFileBackup_.getPath() + "\" of the OPM file \"" + permissionsFile_.getPath() + "\" has been erased");
			return false;
		}
		
		try
		{
			Files.move(permissionsFileBackup_.toPath(), permissionsFile_.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			logger.error("Cannot revert the OPM file \"" + permissionsFile_.getPath() + "\" from its backup \"" + permissionsFileBackup_.getPath() + "\"");
			return false;
		}				
		
		return true;
	}
	
	
	// Merge the XML files listed in the "MERGE" file in the destination directory.
	// If there is no "MERGE" file in the destination directory, just copy the downloaded file to the permissions file.
	
	private void mergePermissionsFiles()
	{
		assert mergeIndexFile_ != null && downloadedFile_ != null && permissionsFile_ != null && permissionsDirectory_ != null;
		
		if (Files.exists(mergeIndexFile_.toPath()))
		{
			logger.info("Index file is present");
			MergeMwtFiles filesMerger = new MergeMwtFiles();
			String filenamesToMerge[] = null;
			try 
			{
				filenamesToMerge = filesMerger.readMergeIndex(mergeIndexFile_.getPath());
			}
			catch (IOException e)
			{
				throw new AuthenticationServiceException("Cannot read the index file \"" + mergeIndexFile_.getPath() + "\": " + e.getMessage());
			}
			
			assert filenamesToMerge != null;
			
			String[] filenamesToMergeWithPaths = new String[filenamesToMerge.length];
			for (int i = 0; i < filenamesToMerge.length; i++)
			{
				logger.info("Merging permissions file: \"" + filenamesToMerge[i] + "\"");
				filenamesToMergeWithPaths[i] = permissionsDirectory_.getPath() + File.separator + filenamesToMerge[i];
			}
			
			try
			{
				filesMerger.mergePermissions(filenamesToMergeWithPaths, permissionsFile_.getPath());
			}
			catch (Exception e)
			{
				throw new AuthenticationServiceException("Exception while merging the files in \"" + mergeIndexFile_.getPath() + "\": " + e.getMessage());
			}			
		}
		else {
			logger.info("Index file is not present. Copying the file downloaded from the OPM");
			try
			{
				Files.copy(downloadedFile_.toPath(), permissionsFile_.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				throw new AuthenticationServiceException("Cannot copy the OPM Manager file \"" + downloadedFile_.getPath() + "\" to the permissions file \"" + permissionsFile_.getPath() + "\""); 
			}			
		}			
	}
	

	protected boolean retrieveOpmFile()
	{
		retrievePermissionsFilesPath();
		
		if (!downloadOpmFile())
			return false;
		
		backupPermissionsFile();
		
		try 
		{
			mergePermissionsFiles();
		}
		catch (AuthenticationServiceException e)
		{
			if (!revertBackupOfPermissionsFile())
				logger.error("Exception occured while merging the permissions file, and could not recover the backup");
			throw e;
		}
		
		return true;
	}
	
	protected boolean getLastUpdateTime() {
		logger.debug("getLastUpdateTime");
		boolean ret = false;

		if (this.opmManagerHostname_ == null)
			retrieveHostnameAndPort();					

		try
		{
			URL url = new URL("http://" + this.opmManagerHostname_ + ":" + this.opmManagerPort_ + "/opm/lastUpdateTime");
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(url.openStream()));

	        String inputLine;
	        StringBuilder sb = new StringBuilder();
	        while ((inputLine = in.readLine()) != null)
	        	sb.append(inputLine);
	        newLastUpdateTime = sb.toString();
	        logger.debug("newLastUpdateTime = {}", newLastUpdateTime);
	        in.close();
	        ret = true;
		} 
		catch (MalformedURLException e)
		{
			throw new AuthenticationServiceException("Malformed URL exception while trying to retrieve the OPM file: " + e.getMessage());
		}
		catch (IOException e)
		{
			// Do not throw an exception, use the OPM file that already exists.
			// throw new AuthenticationServiceException("Cannot retrieve the OPM file from OPM Manager: " + e.getMessage());
			logger.error("Cannot retrieve the OPM file from OPM Manager: " + e.getMessage());
		}

		return ret;
	}
	
	protected boolean isOpmPermissionModified() {
		logger.debug("isOpmPermissionModified lastUpdateTime = {}", lastUpdateTime);
		if (getLastUpdateTime()) {
			if (!newLastUpdateTime.isEmpty() && newLastUpdateTime.equals(lastUpdateTime)) {
				
				logger.debug("lastUpdateTime not modified {}", lastUpdateTime);
				return false;
			}
		}

		if (!newLastUpdateTime.isEmpty()) {
			lastUpdateTime = newLastUpdateTime;
		}
		logger.debug("lastUpdateTime is modified");
		return true;
	}

	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
	{
		if (isOpmPermissionModified()) {
			if (!retrieveOpmFile()) {
				logger.warn("Could not contact the OPM server. Using the local file \"mwt-opm-permission.xml\" instead.");
			}
		}
			
		super.onAuthentication(authentication, request, response);
	}
}