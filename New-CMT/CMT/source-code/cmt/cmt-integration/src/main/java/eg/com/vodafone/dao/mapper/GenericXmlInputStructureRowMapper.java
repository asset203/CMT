package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.GenericXmlInputStructure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/18/13
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericXmlInputStructureRowMapper implements RowMapper<GenericXmlInputStructure> {
    @Override
    public GenericXmlInputStructure mapRow(ResultSet resultSet, int i) throws SQLException {
        GenericXmlInputStructure genericXmlInputStructure = new GenericXmlInputStructure();
        genericXmlInputStructure.setSimple("Y".equals(resultSet.getString("IS_SIMPLE")));
        genericXmlInputStructure.setConverterId(resultSet.getInt("CONVERTOR_ID"));
        return genericXmlInputStructure;
    }
}
