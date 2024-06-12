/**
 * 
 */
package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

/**
 * @author ahmad.abushady
 *
 */
public class DBASegmentSummaryData extends PersistenceObject {
	
	
	public String owner;
	
	public String segmenttype;
	
	public long segmentsize;
	
	public long segmentcount;
	
	public Date date_time;

	
	public DBASegmentSummaryData() {
		// TODO Auto-generated constructor stub
	}


	public Date getDate_time() {
		return date_time;
	}


	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public long getSegmentcount() {
		return segmentcount;
	}


	public void setSegmentcount(long segmentcount) {
		this.segmentcount = segmentcount;
	}


	public long getSegmentsize() {
		return segmentsize;
	}


	public void setSegmentsize(long segmentsize) {
		this.segmentsize = segmentsize;
	}


	public String getSegmenttype() {
		return segmenttype;
	}


	public void setSegmenttype(String segmenttype) {
		this.segmenttype = segmenttype;
	}

}
