package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class MonitorTSFreeSpaceData extends PersistenceObject{
	
	public Date dateTime;
	
	public String tableSpace;
		
	public long pcs;
	
	public long sizeMb;
	
	public long lrgMB;
	
	public long freeMb;
	
	public double freePrec;
	
	public long used;
	
	public double usedPrec;

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getFreeMb() {
		return freeMb;
	}

	public void setFreeMb(long freeMb) {
		this.freeMb = freeMb;
	}

	public double getFreePrec() {
		return freePrec;
	}

	public void setFreePrec(double freePrec) {
		this.freePrec = freePrec;
	}

	public long getLrgMB() {
		return lrgMB;
	}

	public void setLrgMB(long lrgMB) {
		this.lrgMB = lrgMB;
	}

	public long getPcs() {
		return pcs;
	}

	public void setPcs(long pcs) {
		this.pcs = pcs;
	}

	public long getSizeMb() {
		return sizeMb;
	}

	public void setSizeMb(long sizeMb) {
		this.sizeMb = sizeMb;
	}

	public String getTableSpace() {
		return tableSpace;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public double getUsedPrec() {
		return usedPrec;
	}

	public void setUsedPrec(double usedPrec) {
		this.usedPrec = usedPrec;
	}


}
