/* 
 * File:       PersistenceObject.java
 * Date        Author          Changes
 * 24/01/2006  Nayera Mohamed  Created
 * 
 * Abstract class to be extended by all persistence classes
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public abstract class PersistenceObject {
	
  public long id;
  protected String systemName;

	protected String nodeName;

	protected Date date;

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

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
  public void setId(long id)
  {
    this.id = id;
  }
  public long getId()
  {
    return id;
  }
}