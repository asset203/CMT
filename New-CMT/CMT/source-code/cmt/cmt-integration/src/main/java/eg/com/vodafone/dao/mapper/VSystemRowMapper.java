package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.VSystem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class VSystemRowMapper implements RowMapper<VSystem> {
    @Override
    public VSystem mapRow(ResultSet resultSet, int i) throws SQLException {
        VSystem system = new VSystem();
        system.setName(resultSet.getString("NAME"));
        system.setDescription(resultSet.getString("DESCRIPTION"));
        return system;
    }
}
