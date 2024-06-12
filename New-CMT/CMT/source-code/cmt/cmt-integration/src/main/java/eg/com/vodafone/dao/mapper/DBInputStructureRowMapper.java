package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.DBInputStructure;
import eg.com.vodafone.model.enums.DataBaseType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBInputStructureRowMapper implements RowMapper<DBInputStructure> {
    @Override
    public DBInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        DBInputStructure dbInputStructure = new DBInputStructure();
        dbInputStructure.setId(resultSet.getString("INPUT_STRUCT_ID"));
        dbInputStructure.setDbType(resultSet.getLong("DB_TYPE"));
        return dbInputStructure;
    }
}
