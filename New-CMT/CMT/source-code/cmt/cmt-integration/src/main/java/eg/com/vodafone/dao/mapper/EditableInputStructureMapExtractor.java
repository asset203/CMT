package eg.com.vodafone.dao.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/21/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class EditableInputStructureMapExtractor implements ResultSetExtractor {
    public Map<String,Boolean> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map map = new LinkedHashMap<String,Boolean>();
        while (rs.next()) {
            String inputStructureId = rs.getString("INPUT_STRUCT_ID");
            Boolean isEditable = rs.getString("EDITABLE").equals("true");
            map.put(inputStructureId, isEditable);
        }
        return map;
    }


}
