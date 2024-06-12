package eg.com.vodafone.model.enums;

public enum InputStructureType 
{

	DB(1),
    DIRECT_DB(2),
	EXCEL(3),
	TEXT(4),
    DIRECT_TEXT(5),
	GENERIC_DB(6),
	GENERIC_TEXT(7),
	GENERIC_XML(8),
    GENERIC_INPUT(9);
	
	private int typeCode;
	
	private InputStructureType(int code){
		this.typeCode=code;
	}
	
	public int getTypeCode(){
		return this.typeCode;
	}

    public static InputStructureType getInputStructureType(int typeCode){
        for(InputStructureType type : InputStructureType.values()){
            if(type.getTypeCode() == typeCode){
                return type;
            }
        }
        return null;
    }
	
}
