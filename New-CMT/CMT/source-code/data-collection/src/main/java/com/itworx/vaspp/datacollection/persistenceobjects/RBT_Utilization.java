/* 
 * File:       RBT_Utilization.java
 * Date        Author          Changes
 * 04/05/2006  Nayera Mohamed  Created
 * 
 * Persistence class for RBT Signals Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class RBT_Utilization extends PersistenceObject {

	public Date time;

	public String Transit;

	public String C7_Route;

	public String Originating_Node;

	public String Terminating_Node;

	public String SPC;

	public double SL_Count;

	public double Avg_TR_Utlz;

	public double Avg_RX_Utlz;

	public double Max_TR_Utlz;

	public double Max_RX_Utlz;

	public double Avg_Availability;

	public double Min_Availability;

	public double Avg_Quality;

	public double Min_Quality;

	public RBT_Utilization() {
	}


	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setTransit(String Transit) {
		this.Transit = Transit;
	}

	public String getTransit() {
		return Transit;
	}

	public void setC7_Route(String C7_Route) {
		this.C7_Route = C7_Route;
	}

	public String getC7_Route() {
		return C7_Route;
	}

	public void setOriginating_Node(String Originating_Node) {
		this.Originating_Node = Originating_Node;
	}

	public String getOriginating_Node() {
		return Originating_Node;
	}

	public void setTerminating_Node(String Terminating_Node) {
		this.Terminating_Node = Terminating_Node;
	}

	public String getTerminating_Node() {
		return Terminating_Node;
	}

	public void setSPC(String SPC) {
		this.SPC = SPC;
	}

	public String getSPC() {
		return SPC;
	}

	public void setAvg_TR_Utlz(double Avg_TR_Utlz) {
		this.Avg_TR_Utlz = Avg_TR_Utlz;
	}

	public double getAvg_TR_Utlz() {
		return Avg_TR_Utlz;
	}

	public void setAvg_RX_Utlz(double Avg_RX_Utlz) {
		this.Avg_RX_Utlz = Avg_RX_Utlz;
	}

	public double getAvg_RX_Utlz() {
		return Avg_RX_Utlz;
	}

	public void setMax_TR_Utlz(double Max_TR_Utlz) {
		this.Max_TR_Utlz = Max_TR_Utlz;
	}

	public double getMax_TR_Utlz() {
		return Max_TR_Utlz;
	}

	public void setMax_RX_Utlz(double Max_RX_Utlz) {
		this.Max_RX_Utlz = Max_RX_Utlz;
	}

	public double getMax_RX_Utlz() {
		return Max_RX_Utlz;
	}

	public void setAvg_Availability(double Avg_Availability) {
		this.Avg_Availability = Avg_Availability;
	}

	public double getAvg_Availability() {
		return Avg_Availability;
	}

	public void setMin_Availability(double Min_Availability) {
		this.Min_Availability = Min_Availability;
	}

	public double getMin_Availability() {
		return Min_Availability;
	}

	public void setAvg_Quality(double Avg_Quality) {
		this.Avg_Quality = Avg_Quality;
	}

	public double getAvg_Quality() {
		return Avg_Quality;
	}

	public void setMin_Quality(double Min_Quality) {
		this.Min_Quality = Min_Quality;
	}

	public double getMin_Quality() {
		return Min_Quality;
	}

	public void setSL_Count(double SL_Count) {
		this.SL_Count = SL_Count;
	}

	public double getSL_Count() {
		return SL_Count;
	}
}