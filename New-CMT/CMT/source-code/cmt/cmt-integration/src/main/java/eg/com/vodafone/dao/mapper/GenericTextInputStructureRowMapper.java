package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.GenericTextInputStructure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericTextInputStructureRowMapper implements RowMapper<GenericTextInputStructure> {
    @Override
    public GenericTextInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        GenericTextInputStructure genericTextInputStructure = new GenericTextInputStructure();
        genericTextInputStructure.setDelimiter(resultSet.getString("DELEMITER"));
        genericTextInputStructure.setUseHeaders("Y".equals(resultSet.getString("USE_HEADERS")));
        genericTextInputStructure.setIgnoredLinesCount(resultSet.getInt("IGNORE_LINES"));
        return genericTextInputStructure;
    }
}
