/* 
 * File:       DataColumn.java
 * Date        Author          Changes
 * 21/01/2006  Nayera Mohamed  Created
 * 30/01/2006  Nayera Mohamed  Updated to include index
 * 
 * Represent Column of input data from XML configuration
 */

package com.itworx.vaspp.datacollection.objects;

public class DataColumn {
	private String name;

	private String type;

	private int index;
	
	private String srcColumn;

	public DataColumn() {

	}

	public DataColumn(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getSrcColumn() {
		return srcColumn;
	}

	public void setSrcColumn(String srcColumn) {
		this.srcColumn = srcColumn;
	}
	
}