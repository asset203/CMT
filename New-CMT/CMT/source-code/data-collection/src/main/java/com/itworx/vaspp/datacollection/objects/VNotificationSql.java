package com.itworx.vaspp.datacollection.objects;

import java.util.List;
import java.util.ArrayList;

public class VNotificationSql {
private String id;
private String sql;
public String getSql() {
	return sql;
}
public void setSql(String sql) {
	this.sql = sql;
}
private String title;
private String validationTable;
private String [] validationNodes=null;
public String[] getValidationNodes() {
	return validationNodes;
}
public void setValidationNodes(String[] validationNodes) {
	this.validationNodes = validationNodes;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getValidationTable() {
	return validationTable;
}
public void setValidationTable(String validationTable) {
	this.validationTable = validationTable;
}

}
