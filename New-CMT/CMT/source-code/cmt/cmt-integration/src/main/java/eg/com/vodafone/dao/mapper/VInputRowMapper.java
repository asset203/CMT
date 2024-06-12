package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.VInput;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class VInputRowMapper implements RowMapper<VInput> {
    @Override
    public VInput mapRow(ResultSet resultSet, int i) throws SQLException {
        VInput input = new VInput();

        input.setInputId(resultSet.getLong("INPUT_ID"));
        input.setId(resultSet.getString("ID"));
        input.setInputName(resultSet.getString("INPUT_NAME"));
        input.setSystemName(resultSet.getString("SYSTEM_NAME"));
        input.setNodeName(resultSet.getString("NODE_NAME"));
        input.setNodeId(resultSet.getLong("NODE_ID"));
        input.setOriginalInputName(resultSet.getString("ORIGINAL_INPUT_NAME"));
        input.setPerNode(resultSet.getString("PER_NODE").equals("Y"));
        input.setHourlyName(resultSet.getString("HOURLY_NAME"));
        input.setAccessMethod(resultSet.getString("ACCESS_METHOD"));
        input.setServer(resultSet.getString("SERVER"));
        input.setUser(resultSet.getString("USER_NAME"));
        input.setPassword(resultSet.getString("PSWD"));
        input.setType(resultSet.getInt("INPUT_TYPE"));

        return input;
    }
}
