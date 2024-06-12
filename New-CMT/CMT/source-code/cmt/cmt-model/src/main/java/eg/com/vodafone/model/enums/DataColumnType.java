package eg.com.vodafone.model.enums;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/21/13
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public enum DataColumnType {
    STRING("string",1,"VARCHAR2(1024)"),
    NUMBER("number",2,"NUMBER"),
    FLOAT("float",3,"FLOAT") ,
    DATE("date",4,"DATE"),
    UN_DEFINED("undefined",-1,"");

    private String name;
    private int typeCode;
    private String equivalentSQLType;

    private DataColumnType(String name,int code,String sqlType){
        this.name=name;
        this.typeCode=code;
        this.equivalentSQLType=sqlType;
    }

    public String getName() {
        return name;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public static  DataColumnType getDataColumnType(int typeCode){
     for( DataColumnType columnType : DataColumnType.values()){
         if(columnType.getTypeCode() == typeCode){
             return columnType;
         }
     }
      return UN_DEFINED;
    }
    public  static DataColumnType getDataColumnType(String name){
        for( DataColumnType columnType : DataColumnType.values()){
            if(columnType.name.equals( name)){
                return columnType;
            }
        }
        return UN_DEFINED;
    }
}
