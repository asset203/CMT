/* 
 * File:       VInputStructure.java
 * Date        Author          Changes
 * 17/01/2006  Nayera Mohamed  Created
 * 09/05/2006  Nayera Mohamed  Converted to abstract class
 * 
 * Abstract class to be extended by any InputStructure
 */

package com.itworx.vaspp.datacollection.objects;

public abstract class VInputStructure implements Cloneable{
	private String id;
	private String lastCallClassName;
	
	private boolean directMapping = false;

	private String mappedTable;
	
	private String dateColumn;
	
	private String nodeColumn;
	
	private String idColumn;
	
	private String seqName;
	
	private DataColumn[] columns;
	
	private boolean useUpdateEvent = false;
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String getLastCallClassName() {
		return lastCallClassName;
	}

	public void setLastCallClassName(String lastCallClassName) {
		this.lastCallClassName = lastCallClassName;
	}

	public boolean isDirectMapping() {
		return directMapping;
	}

	public void setDirectMapping(boolean directMapping) {
		this.directMapping = directMapping;
	}

	public String getMappedTable() {
		return mappedTable;
	}

	public void setMappedTable(String mappedTable) {
		this.mappedTable = mappedTable;
	}

	public String getNodeColumn() {
		return nodeColumn;
	}

	public void setNodeColumn(String nodeColumn) {
		this.nodeColumn = nodeColumn;
	}

	public String getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public DataColumn[] getColumns() {
		return columns;
	}

	public void setColumns(DataColumn[] columns) {
		this.columns = columns;
	}

	public boolean isUseUpdateEvent() {
		return useUpdateEvent;
	}

	public void setUseUpdateEvent(boolean useUpdateEvent) {
		this.useUpdateEvent = useUpdateEvent;
	}

	public String getDateColumn() {
		return dateColumn;
	}

	public void setDateColumn(String dateColumn) {
		this.dateColumn = dateColumn;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		try{
			VInputStructure clonedInputStructure = (VInputStructure) super.clone();
			return clonedInputStructure; 
		}catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	
	

}