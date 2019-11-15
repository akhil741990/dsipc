package com.soul.dsipc.server.protocol;

public class DataNodeRegistration {

	
	private String datanodeID;
	private String storageInfo;
	private String keys;
	private String softwareVersion;
	public DataNodeRegistration(String datanodeID, String storageInfo, String keys, String softwareVersion) {
		super();
		this.datanodeID = datanodeID;
		this.storageInfo = storageInfo;
		this.keys = keys;
		this.softwareVersion = softwareVersion;
	}
	public String getDatanodeID() {
		return datanodeID;
	}
	public String getStorageInfo() {
		return storageInfo;
	}
	public String getKeys() {
		return keys;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	
}
