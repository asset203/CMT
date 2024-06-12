package eg.com.vodafone.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import eg.com.vodafone.model.User;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created with IntelliJ IDEA. User: Samaa.ElKomy Date: 3/7/13 Time: 11:24 AM To
 * change this template use File | Settings | File Templates.
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int line) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("USERNAME"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setEmail(rs.getString("EMAIL"));
        user.setId(rs.getInt("ID"));
        user.setFirstLogin(rs.getInt("FIRST_LOGIN"));
        user.setMobile(rs.getString("MOBILE"));
        user.setAuthority(rs.getString("AUTHORITY"));
        // Added by Awad
        //CMT_DashBoard Configuration
        user.setAppsToAccess(rs.getString("APPS_TO_ACCESS"));
        // End
        return user;
    }
}
