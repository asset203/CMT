package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class CorruptedFilesData extends PersistenceObject{
	
	public Date dateTime;
	
	public String server;
	
	public String serverNode;
	
	public long numberOfCorruptedFiles;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getNumberOfCorruptedFiles() {
		return numberOfCorruptedFiles;
	}

	public void setNumberOfCorruptedFiles(long numberOfCorruptedFiles) {
		this.numberOfCorruptedFiles = numberOfCorruptedFiles;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getServerNode() {
		return serverNode;
	}

	public void setServerNode(String serverNode) {
		this.serverNode = serverNode;
	}

}
