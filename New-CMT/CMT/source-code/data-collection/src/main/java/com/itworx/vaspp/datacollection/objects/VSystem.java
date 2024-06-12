/* 
 * File:       VSystem.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 
 * Represent Sydtem from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

public class VSystem {
	private VNode[] nodes;

	private java.lang.String name;
	
	private String description;
	
	private boolean generic;

	private VInput[] inputs;

	public VSystem() {
	}

	public void setNodes(VNode[] nodes) {
		this.nodes = nodes;
	}

	public VNode[] getNodes() {
		return nodes;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isGeneric() {
		return generic;
	}

	public void setGeneric(boolean generic) {
		this.generic = generic;
	}

}