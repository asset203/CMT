package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.UserDao;
import eg.com.vodafone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/7/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

  @Autowired
  private UserDao userDao;
  @Autowired
  private StandardPasswordEncoder encoder;

  @Transactional(readOnly = false)
  public List<Integer> addUser(User user) {
    user.setFirstLogin(1);
    user.setPassword(encryptWithHashAndSalt(user.getPassword()));
    return userDao.saveUser(user);
  }

  @Transactional(readOnly = false)
  public List<Integer> updateUser(User user) {
    //if empty password, get old password
    User oldUser = userDao.findUserById(user.getId());
    if (!StringUtils.hasLength(user.getPassword())) {
      user.setPassword(oldUser.getPassword());
      user.setFirstLogin(oldUser.getFirstLogin());
    } else {
      user.setPassword(encryptWithHashAndSalt(user.getPassword()));
      user.setFirstLogin(1);
    }

    return userDao.updateUser(user);
  }

  @Transactional(readOnly = false)
  public List<Integer> updateUserForFirstLogin(User user) {
    user.setPassword(encryptWithHashAndSalt(user.getPassword()));
    user.setFirstLogin(0);
    return userDao.updateUser(user);
  }

  @Transactional(readOnly = false)
  public int deleteUser(User user) {
    return userDao.deleteUser(user);
  }

  public List<User> getUserList() {
    return userDao.findAllUsers();
  }

  public User findUserById(int userId) {
    User user = userDao.findUserById(userId);
    //clearing user's password
    user.setPassword("");
    return user;
  }

  public User findUserByUserName(String username) {
    return userDao.findUserByUsername(username);
  }

  private String encryptWithHashAndSalt(String plainText) {
    return encoder.encode(plainText);
  }

  public User findUserByMobile(String mobile) {
    return userDao.findUserByMobile(mobile);
  }


  public String getUserRole(String userName) {
    return userDao.getUserRole(userName);
  }
}
