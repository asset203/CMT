/* 
 * File:       VInput.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Represent Input from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

public class VInput implements Cloneable{
	private String systemName;

	private String nodeName;

	private String id;

	private boolean perNode;

	private String inputName;
	
	private String originalInputName;
	
	public String getOriginalInputName() {
		return originalInputName;
	}

	public void setOriginalInputName(String originalInputName) {
		this.originalInputName = originalInputName;
	}


	private String hourlyName;

	private String server;

	private String[] paths;

	private String user;

	private String password;

	private String accessMethod;

	private int type;

	public static final int DB_INPUT = 0;

	public static final int EXCEL_INPUT = 1;

	public static final int TEXT_INPUT = 2;
	
	public static final int GENERIC_INPUT = 3;
	
	public static final int DIRECT_DB_INPUT = 4;
	
	public static final int DIRECT_TEXT_INPUT = 5;

	private VInputStructure[] inputStructures;

	public VInput() {
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getServer() {
		return server;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setInputStructures(VInputStructure[] inputStructures) {
		this.inputStructures = inputStructures;
	}

	public VInputStructure[] getInputStructures() {
		return inputStructures;
	}

	public VInputStructure getInputStructure() {
		if (inputStructures.length == 1) {
			return inputStructures[0];
		} else {
			return null;
		}
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	public String[] getPaths() {
		return paths;
	}

	public void setPerNode(boolean perNode) {
		this.perNode = perNode;
	}

	public boolean isPerNode() {
		return perNode;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getInputName() {
		return inputName;
	}

	public void setAccessMethod(String accessMethod) {
		this.accessMethod = accessMethod;
	}

	public String getAccessMethod() {
		return accessMethod;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getHourlyName() {
		return hourlyName;
	}

	public void setHourlyName(String hourlyName) {
		this.hourlyName = hourlyName;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		try{
			VInput clonedInput = (VInput) super.clone();
			VInputStructure[] thisInputStructures = this.getInputStructures();
			VInputStructure[] copiedInputStructures = null;
			if(thisInputStructures != null){
				copiedInputStructures = new VInputStructure[thisInputStructures.length];
				for(int i = 0;i<thisInputStructures.length;i++){
					copiedInputStructures[i] = (VInputStructure)thisInputStructures[i].clone();
				}
			}
			clonedInput.setInputStructures(copiedInputStructures);
			return clonedInput; 
		}catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}