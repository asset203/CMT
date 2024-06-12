package com.itworx.vaspp.datacollection.objects;

import java.util.Properties;

public class GenericMapping {
    private Properties assocMapping = new Properties();
    private String tableName;
    private String nodeColumnName;
    private String dateColumnName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setNodeColumnName(String nodeColumnName) {
        this.nodeColumnName = nodeColumnName;
    }

    public String getNodeColumnName() {
        return nodeColumnName;
    }
    
    public void createAssoc(String resultFieldIdentifier,String outputColumnName)
    {
    	assocMapping.put(resultFieldIdentifier,outputColumnName);
    }
    
    public String getOutputColumnName(String resultFieldIdentifier)
    {
        return (String)assocMapping.get(resultFieldIdentifier);
    }

	public Properties getAssocMapping() {
		return assocMapping;
	}

	public void setAssocMapping(Properties assocMapping) {
		this.assocMapping = assocMapping;
	}

	public String getDateColumnName() {
		return dateColumnName;
	}

	public void setDateColumnName(String dateColumnName) {
		this.dateColumnName = dateColumnName;
	}
	
	public void clearAssocMapping(){
		assocMapping.clear();
	}
}
