package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.Date;
public class UserActionsTransactionsData extends PersistenceObject{
	 public Date date;
	 public String userAction;
	 public long userActionCount;
	 public long avgActions;
	 public String switchId;
	 	 
	 public void setDate(Date date)
	 {
		 this.date=date;
	 }
	 public Date getDate()
	 {
		 return this.date;
	 }
	 
	 public void setUserAction(String userAction)
	 {
		 this.userAction=userAction;
	 }
	 public String getUserAction()
	 {
		 return this.userAction;
	 }
	 public void setUserActionCount(long userActionCount)
	 {
		 this.userActionCount=userActionCount;
	 }
	 public long getUserActionCount()
	 {
		 return this.userActionCount;
	 }
	 public void setAvgActions(long avgActions)
	 {
		 this.avgActions=avgActions;
	 }
	 public long getAvgActions()
	 {
		 return this.avgActions;
	 }
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
}
