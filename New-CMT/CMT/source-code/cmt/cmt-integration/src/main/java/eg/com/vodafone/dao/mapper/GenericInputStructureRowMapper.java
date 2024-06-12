package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.GenericInputStructure;
import eg.com.vodafone.model.GenericTextInputStructure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/13/13
 * Time   : 8:43 PM
 */
public class GenericInputStructureRowMapper implements RowMapper<GenericInputStructure> {

    @Override
    public GenericInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        GenericInputStructure genericInputStructure = new GenericInputStructure();
        genericInputStructure.setExtractionParameter(resultSet.getString("DELEMITER"));
        genericInputStructure.setExtractionMethod(GenericInputStructure.DELIMITER_EXTRACTOR);
        genericInputStructure.setUseHeaders("Y".equals(resultSet.getString("USE_HEADERS")));
        genericInputStructure.setIgnoredLinesCount(resultSet.getInt("IGNORE_LINES"));
        return genericInputStructure;
    }
}
