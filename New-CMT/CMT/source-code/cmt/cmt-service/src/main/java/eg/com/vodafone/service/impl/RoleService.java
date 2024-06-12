package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.RoleDao;
import eg.com.vodafone.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/12/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Transactional(readOnly = false)
    public void addRole(Role role)
    {
        roleDao.saveRole(role);
    }

    @Transactional(readOnly = false)
    public void updateRole(Role role)
    {
        roleDao.updateRole(role);
    }

    @Transactional(readOnly = false)
    public void deleteRole(int roleId)
    {
        roleDao.deleteRole(roleId);
    }

    public List<Role> getRolesList()
    {
        return roleDao.findAllRoles();
    }

    public Role findRoleById(String roleName)
    {
        return roleDao.findRole(roleName);
    }
}
