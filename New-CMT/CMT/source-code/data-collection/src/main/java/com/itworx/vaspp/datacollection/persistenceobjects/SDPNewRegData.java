package com.itworx.vaspp.datacollection.persistenceobjects;
import java.util.*;
public class SDPNewRegData extends PersistenceObject{
	public Date dateTime;
	public double totalInterSucc;
	public double totalInterFail;
	public double firstInterSucc;
	public double firstInterFail;
	public double interInterSucc;
	public double interInterFail;
	public double finalInterSucc;
	public double finalInterFail;
	public double regInterSucc;
	public double regInterFail;
	public double totalThrput;
	public double firatThrput;
	public double interThrput;
	public double finalThrput;
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public double getTotalInterSucc() {
		return totalInterSucc;
	}
	public void setTotalInterSucc(double totalInterSucc) {
		this.totalInterSucc = totalInterSucc;
	}
	public double getTotalInterFail() {
		return totalInterFail;
	}
	public void setTotalInterFail(double totalInterFail) {
		this.totalInterFail = totalInterFail;
	}
	public double getFirstInterSucc() {
		return firstInterSucc;
	}
	public void setFirstInterSucc(double firstInterSucc) {
		this.firstInterSucc = firstInterSucc;
	}
	public double getFirstInterFail() {
		return firstInterFail;
	}
	public void setFirstInterFail(double firstInterFail) {
		this.firstInterFail = firstInterFail;
	}
	public double getInterInterSucc() {
		return interInterSucc;
	}
	public void setInterInterSucc(double interInterSucc) {
		this.interInterSucc = interInterSucc;
	}
	public double getInterInterFail() {
		return interInterFail;
	}
	public void setInterInterFail(double interInterFail) {
		this.interInterFail = interInterFail;
	}
	public double getFinalInterSucc() {
		return finalInterSucc;
	}
	public void setFinalInterSucc(double finalInterSucc) {
		this.finalInterSucc = finalInterSucc;
	}
	public double getFinalInterFail() {
		return finalInterFail;
	}
	public void setFinalInterFail(double finalInterFail) {
		this.finalInterFail = finalInterFail;
	}
	public double getRegInterSucc() {
		return regInterSucc;
	}
	public void setRegInterSucc(double regInterSucc) {
		this.regInterSucc = regInterSucc;
	}
	public double getRegInterFail() {
		return regInterFail;
	}
	public void setRegInterFail(double regInterFail) {
		this.regInterFail = regInterFail;
	}
	public double getTotalThrput() {
		return totalThrput;
	}
	public void setTotalThrput(double totalThrput) {
		this.totalThrput = totalThrput;
	}
	public double getFiratThrput() {
		return firatThrput;
	}
	public void setFiratThrput(double firatThrput) {
		this.firatThrput = firatThrput;
	}
	public double getInterThrput() {
		return interThrput;
	}
	public void setInterThrput(double interThrput) {
		this.interThrput = interThrput;
	}
	public double getFinalThrput() {
		return finalThrput;
	}
	public void setFinalThrput(double finalThrput) {
		this.finalThrput = finalThrput;
	}
	public SDPNewRegData()
	{
		
	}
}
