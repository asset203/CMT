package com.itworx.vaspp.datacollection.objects;

import java.util.Arrays;

public class VScheduledNode extends VNode implements Cloneable{
	public String cronExpression;

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		try{
			VScheduledNode clonedNode = (VScheduledNode) super.clone();
			VInput[] thisInputs = this.getInputs();
			VInput[] copiedInputs = null;
			if(thisInputs != null){
				copiedInputs = new VInput[thisInputs.length];
				for(int i = 0;i<thisInputs.length;i++){
					copiedInputs[i] = (VInput)thisInputs[i].clone();
				}
			}
			clonedNode.setInputs(copiedInputs);
			return clonedNode; 
		}catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}
