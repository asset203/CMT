package eg.com.vodafone.model.enums;

public enum NodeColumnType {

	/*this mean that the data collection can be configured on many nodes
	 * and the each execution inserts the particular node name in the "NODE_NAME" column
	 */
	CONFIGURABLE(1),
	
	
	/*this means that the data collection can not be configured on nodes (only on system inputs)
	 * and the provided select statement selects some value and insert it in the "NODE_NAME" column
	 */
	MAPPED(2),
	
	/*this means  that the data collection can not be configured on nodes
	 * and no "NODE_NAME" column in the output table
	 * */
	NON(3);
	
	private int typeCode;
	
	private NodeColumnType(int code){
		this.typeCode = code;
	}
	
	public int getTypeCode(){
		return this.typeCode;
	}
	
}
