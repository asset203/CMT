/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class DBAErrorsData extends PersistenceObject {

	public String objectTypes;
	
	public String schema;
	
	public String objectName;
	
	public long sequence;
	
	public long line;
	
	public long position;
	
	public String text;
	
	public Date date_time;
	
	/**
	 * 
	 */
	public DBAErrorsData() {
		// TODO Auto-generated constructor stub
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public long getLine() {
		return line;
	}

	public void setLine(long line) {
		this.line = line;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(String objectTypes) {
		this.objectTypes = objectTypes;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
