package eg.com.vodafone.model.enums;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 3/21/13
 * Time   : 2:29 PM
 */

public enum DataBaseType {
    ORACLE(1,"oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:"),
    MS_SQL_SERVER(2,"com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:microsoft:sqlserver:"),
    MY_SQL(3,"com.mysql.jdbc.Driver","jdbc:mysql:"),
    SYBASE(4,"com.sybase.jdbc2.jdbc.SybDriver","jdbc:sybase:Tds:"),
    ORACLE_RAC(5,"oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:"),
    DB2(6,"COM.ibm.db2.jdbc.app.DB2Driver","jdbc:db2://");

    private long typeCode;
    private String driverClassName;
    private String path;

    private DataBaseType(long typeCode,String driverClassName,String path){
        this.typeCode=typeCode;
        this.driverClassName=driverClassName;
        this.path = path;
    }

    public long getTypeCode()
    {
        return this.typeCode;
    }

    public static DataBaseType getDataBaseType(String driverClassName)
    {
        for(DataBaseType dbType : DataBaseType.values()){
            if(dbType.getDriverClassName().equals(driverClassName)){
                return dbType;
            }
        }
        return null;
    }
    public static DataBaseType getDataBaseType(long typeCode)
    {
        for(DataBaseType dbType : DataBaseType.values()){
            if(dbType.getTypeCode() == typeCode){
                return dbType;
            }
        }
        return null;
    }


    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
