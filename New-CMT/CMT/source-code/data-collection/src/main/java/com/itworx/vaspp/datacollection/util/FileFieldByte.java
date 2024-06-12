/**
 * 
 */
package com.itworx.vaspp.datacollection.util;

/**
 * @author ahmad.abushady
 *
 */

/*
 * this class is an object responsible for holding data about a file with fields that
 * have a spacific file
 * it holds the bytes number, and the position of the field in the line
 * i.e. if it is the second column with 15 bytes
 * 		it will hold values Bytes=15, Position= first char of this field
 */
public class FileFieldByte {
	
	private int bytes;
	
	private int position;
	
	private boolean isNum;
	
	public FileFieldByte(){
		
	}
	
	public FileFieldByte(int byt, int pos){
		bytes = byt;
		position = pos;
		isNum = false;
	}
	
	public FileFieldByte(int byt, int pos, boolean isNo){
		bytes = byt;
		position = pos;
		isNum = isNo;
	}

	public int getBytes() {
		return bytes;
	}

	public void setBytes(int bytes) {
		this.bytes = bytes;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	public boolean getisNum() {
		return isNum;
	}

	public void setNum(boolean isNum) {
		this.isNum = isNum;
	}
}
