package eg.com.vodafone.dao.mapper;

import eg.com.vodafone.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/7/13
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class RoleRowMapper implements RowMapper<Role>{
    @Override
    public Role mapRow(ResultSet rs, int line) throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("ID"));
        role.setName(rs.getString("ROLE_NAME"));
        return role;
    }
}
