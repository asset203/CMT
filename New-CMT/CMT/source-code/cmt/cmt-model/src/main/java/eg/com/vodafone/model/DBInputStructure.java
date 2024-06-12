package eg.com.vodafone.model;

import eg.com.vodafone.model.enums.DataBaseType;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBInputStructure  extends VInputStructure{

    private long dbType;

    public DBInputStructure() {
    }
    public DBInputStructure(VInputStructure inputStructure) {
        super(inputStructure);
    }
    public long getDbType() {
        return dbType;
    }

    public void setDbType(long dbType) {
        this.dbType = dbType;
    }
    public String getDriver(){
        return DataBaseType.getDataBaseType(this.dbType).getDriverClassName();
    }
    public String getSqlStatement(){
        return getExtractionSql();
    }
}
