/* 
 * File:       BGW_Data.java
 * Date        Author          Changes
 * 07/03/2006  Nayera Mohamed  Created
 * 
 * Persistence class for BGW Data
 */

package com.itworx.vaspp.datacollection.persistenceobjects;

import java.util.Date;

public class BGW_Data extends PersistenceObject {


	public Date time;

	// public String CPU;
	public long usr;

	public long sys;

	public long wio;

	public long idle;

	public double bread;

	public double lread;

	public double rcache;

	public double bwrit;

	public double lwrit;

	public double wcache;

	public double pread;

	public double pwrit;

	public double scall;

	public double sread;

	public double swrit;

	public double fork;

	public double exec;

	public double rchar;

	public double wchar;

	public double msg;

	public double sema;

	public double freemem;

	public double freeswap;

	public double runq_sz;

	public double runocc;

	public double swpq_sz;

	public double swpocc;

	public BGW_Data() {

	}

	// public void setCPU(String CPU)
	// {
	// this.CPU = CPU;
	// }
	//	
	//	
	// public String getCPU()
	// {
	// return CPU;
	// }

	public void setUsr(long usr) {
		this.usr = usr;
	}

	public long getUsr() {
		return usr;
		
	}

	public void setSys(long sys) {
		this.sys = sys;
	}

	public long getSys() {
		return sys;
	}

	public void setWio(long wio) {
		this.wio = wio;
	}

	public long getWio() {
		return wio;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

	public long getIdle() {
		return idle;
	}

	public void setBread(double bread) {
		this.bread = bread;
	}

	public double getBread() {
		return bread;
	}

	public void setLread(double lread) {
		this.lread = lread;
	}

	public double getLread() {
		return lread;
	}

	public void setRcache(double rcache) {
		this.rcache = rcache;
	}

	public double getRcache() {
		return rcache;
	}

	public void setBwrit(double bwrit) {
		this.bwrit = bwrit;
	}

	public double getBwrit() {
		return bwrit;
	}

	public void setLwrit(double lwrit) {
		this.lwrit = lwrit;
	}

	public double getLwrit() {
		return lwrit;
	}

	public void setWcache(double wcache) {
		this.wcache = wcache;
	}

	public double getWcache() {
		return wcache;
	}

	public void setPread(double pread) {
		this.pread = pread;
	}

	public double getPread() {
		return pread;
	}

	public void setPwrit(double pwrit) {
		this.pwrit = pwrit;
	}

	public double getPwrit() {
		return pwrit;
	}

	public void setScall(double scall) {
		this.scall = scall;
	}

	public double getScall() {
		return scall;
	}

	public void setSread(double sread) {
		this.sread = sread;
	}

	public double getSread() {
		return sread;
	}

	public void setSwrit(double swrit) {
		this.swrit = swrit;
	}

	public double getSwrit() {
		return swrit;
	}

	public void setFork(double fork) {
		this.fork = fork;
	}

	public double getFork() {
		return fork;
	}

	public void setExec(double exec) {
		this.exec = exec;
	}

	public double getExec() {
		return exec;
	}

	public void setRchar(double rchar) {
		this.rchar = rchar;
	}

	public double getRchar() {
		return rchar;
	}

	public void setWchar(double wchar) {
		this.wchar = wchar;
	}

	public double getWchar() {
		return wchar;
	}

	public void setMsg(double msg) {
		this.msg = msg;
	}

	public double getMsg() {
		return msg;
	}

	public void setSema(double sema) {
		this.sema = sema;
	}

	public double getSema() {
		return sema;
	}

	public void setFreemem(double freemem) {
		this.freemem = freemem;
	}

	public double getFreemem() {
		return freemem;
	}

	public void setFreeswap(double freeswap) {
		this.freeswap = freeswap;
	}

	public double getFreeswap() {
		return freeswap;
	}

	public void setRunq_sz(double runq_sz) {
		this.runq_sz = runq_sz;
	}

	public double getRunq_sz() {
		return runq_sz;
	}

	public void setRunocc(double runocc) {
		this.runocc = runocc;
	}

	public double getRunocc() {
		return runocc;
	}

	public void setSwpq_sz(double swpq_sz) {
		this.swpq_sz = swpq_sz;
	}

	public double getSwpq_sz() {
		return swpq_sz;
	}

	public void setSwpocc(double swpocc) {
		this.swpocc = swpocc;
	}

	public double getSwpocc() {
		return swpocc;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

}