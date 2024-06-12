package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.TextInputStructure;
import eg.com.vodafone.utils.Utils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextInputStructureRowMapper implements RowMapper<TextInputStructure> {
    @Override
    public TextInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        TextInputStructure textInputStructure = new TextInputStructure();
        textInputStructure.setParametersMap(Utils.getMapFromString(resultSet.getString("PARAMETERS_MAP")));
        textInputStructure.setConverter(resultSet.getString("CONVERTOR"));
        return textInputStructure;
    }
}
