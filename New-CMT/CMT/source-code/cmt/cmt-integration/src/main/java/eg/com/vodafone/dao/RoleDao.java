package eg.com.vodafone.dao;

import eg.com.vodafone.dao.mapper.RoleRowMapper;
import eg.com.vodafone.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/7/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class RoleDao {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleDao(@Qualifier(value = "cmtDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Role findRole(String roleName) {
        Role role;
        try {
            role = (Role) jdbcTemplate
                    .queryForObject("SELECT  ID, ROLE_NAME FROM ROLES WHERE ROLE_NAME = ?",
                            new Object[]{roleName},
                            new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            role = null;
        }
        return role;
    }

    public void saveRole(Role role) {
        jdbcTemplate.update("INSERT INTO ROLES (ID, ROLE_NAME) VALUES(?, ?)",
                new Object[]{role.getId(), role.getName()});
    }

    public void updateRole(Role role) {
        jdbcTemplate.update("UPDATE ROLES SET ROLE_NAME = ? WHERE ID = ?",
                new Object[]{role.getName(), role.getId()});
    }

    public void deleteRole(int roleId) {
        jdbcTemplate.update("DELETE FROM ROLES WHERE ID= ?",
                new Object[]{roleId});
    }

    public List<Role> findAllRoles() {
        return jdbcTemplate.query("SELECT ID, ROLE_NAME FROM ROLES",
                new RoleRowMapper());
    }
}
