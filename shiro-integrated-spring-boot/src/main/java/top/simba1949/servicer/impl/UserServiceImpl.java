package top.simba1949.servicer.impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.simba1949.entity.User;
import top.simba1949.mapper.UserMapper;
import top.simba1949.servicer.UserService;
import top.simba1949.util.EncryptionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author SIMBA1949
 * @date 2020/6/27 21:12
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(User user) {
        // 对明文密码进行加密
        String salt = EncryptionUtils.getSalt();
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, EncryptionUtils.HASH_ITERATIONS);
        user.setPassword(md5Hash.toHex());
        user.setSalt(salt);
        int id = userMapper.insert(user);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) {
        userMapper.delete(id);
    }

    @Override
    public User get(int id) {
        return userMapper.get(id);
    }

    @Override
    public List<User> select(User user) {
        return userMapper.select(user);
    }

    @Override
    public User getByUsername(String username) {
        System.out.println("UserServiceImpl : " + username);
        return userMapper.getByUsername(username);
    }

    @Override
    public Set<String> listRoles(int userId) {
        return userMapper.listRoles(userId);
    }

    @Override
    public Set<String> listPermissions(Integer id) {
        return userMapper.listPermissions(id);
    }
}
