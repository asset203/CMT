package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.DataColumn;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataColumnRowMapper  implements RowMapper<DataColumn> {
    @Override
    public DataColumn mapRow(ResultSet resultSet, int i) throws SQLException {
        DataColumn column = new DataColumn();
        column.setInputStructureId(resultSet.getString("VINPUT_STRUCT_ID"));
        column.setActive("Y".equals(resultSet.getString("ACTIVE")));
        column.setDateFormat(resultSet.getString("DATE_FORMAT"));
        column.setDefaultValue(resultSet.getString("DEFAULT_VALUE"));
        column.setIndex(resultSet.getInt("COL_INDEX"));
        column.setName(resultSet.getString("NAME"));
        LobHandler lobHandler = new DefaultLobHandler();
        String sqlExpressionString = lobHandler.getClobAsString(resultSet,"SQL_EXSPRESSION");
        if(StringUtils.hasText(sqlExpressionString))
        {
            sqlExpressionString = sqlExpressionString.replace("''","'");
        }
        column.setSqlExpression(sqlExpressionString);
        column.setSrcColumn(resultSet.getString("SRC_COLUMN"));
        column.setTypeCode(resultSet.getInt("TYPE_CODE"));
        column.setStrSize(resultSet.getInt("STRING_SIZE"));
        
        // Added by Awad
        // CMT DashBoard Configuration 
        column.setKpiType(resultSet.getInt("KPI_TYPE"));
        // End 

        return column;
    }
}

