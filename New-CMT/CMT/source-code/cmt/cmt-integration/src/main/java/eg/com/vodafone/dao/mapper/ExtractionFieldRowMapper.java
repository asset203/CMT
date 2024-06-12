package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.ExtractionField;
import eg.com.vodafone.model.enums.DataColumnType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/13/13
 * Time   : 9:19 PM
 */
public class ExtractionFieldRowMapper implements RowMapper<ExtractionField> {
    @Override
    public ExtractionField mapRow(ResultSet resultSet, int i) throws SQLException {
        ExtractionField field = new ExtractionField();
        field.setIdentifier(resultSet.getString("SRC_COLUMN"));
        field.setIndex(resultSet.getInt("COL_INDEX"));
        field.setActive("Y".equals(resultSet.getString("ACTIVE")));
        field.setType(DataColumnType.getDataColumnType(resultSet.getInt("TYPE_CODE")).getName());
        field.setDefaultValue(resultSet.getString("DEFAULT_VALUE"));
        field.setDateFormat(resultSet.getString("DATE_FORMAT"));
        field.setMappedColumnName(resultSet.getString("NAME"));
        field.setInputStructureId(resultSet.getString("VINPUT_STRUCT_ID"));
        return field;
    }
}
