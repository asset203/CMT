/* 
 * File:       VNode.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Represent Sydtem Node from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

public class VNode {
	private VInput[] inputs;

	private java.lang.String name;

	private String systemName;

	public VNode() {
	}

	public void setInputs(VInput[] inputs) {
		this.inputs = inputs;
	}

	public VInput[] getInputs() {
		return inputs;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}
}