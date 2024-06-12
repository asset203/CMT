package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.VInputStructure;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA. User: basma.alkerm Date: 3/14/13 Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class VInputStructureRowMapper implements RowMapper<VInputStructure> {

    @Override
    public VInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        VInputStructure inputStructure = new VInputStructure();
        LobHandler lobHandler = new DefaultLobHandler();
        inputStructure.setId(resultSet.getString("INPUT_STRUCT_ID"));
        inputStructure.setDirectMapping("Y".equals(resultSet.getString("DIRECT_MAPPING")));
        String sqlStatementString = lobHandler.getClobAsString(resultSet, "EXTRACTION_SQL");
        if (StringUtils.hasText(sqlStatementString)) {
            sqlStatementString = sqlStatementString.replace("''", "'");
        }
        inputStructure.setExtractionSql(sqlStatementString);
        inputStructure.setMappedTable(resultSet.getString("TABLE_NAME"));
        inputStructure.setLastCallClassName(resultSet.getString("LAST_CALL_CLASS_NAME"));
        inputStructure.setNodeColumn(resultSet.getString("NODE_COL"));
        inputStructure.setNodeColumnType(resultSet.getInt("NODE_COLUMN_TYPE"));
        inputStructure.setIdColumn(resultSet.getString("ID_COL"));
        inputStructure.setSeqName(resultSet.getString("SEQ_NAME"));
        inputStructure.setType(resultSet.getInt("TYPE"));
        inputStructure.setDateColumn(resultSet.getString("DELETE_DATE_COL"));
        inputStructure.setPartitionColumnName(resultSet.getString("PARTITION_COL_NAME"));
        inputStructure.setExtractionDateColumn(resultSet.getString("EXCTRACTION_DATE_COLUMN"));
        inputStructure.setUseUpdateEvent("Y".equals(resultSet.getString("USE_UPDATE_EVENT")));
        inputStructure.setDateFormat(resultSet.getString("EXCTRACT_DATE_FORMAT"));
        inputStructure.setExtractDate("Y".equals(resultSet.getString("EXCTRACT_DATE")));
        inputStructure.setExtractDateMonthly("Y".equals(resultSet.getString("EXCTRACT_DATE_MONTHLY")));
        inputStructure.setTruncateBeforeInsertion("Y".equals(resultSet.getString("TRUNCATE_OLD_DATA")));
        inputStructure.setIsPartitioned("Y".equals(resultSet.getString("PARTITIONE_TABLE")));
        inputStructure.setAutoFilledDateColumn(resultSet.getString("AUTO_FILLED_DATE_COL"));
        return inputStructure;
    }

}
