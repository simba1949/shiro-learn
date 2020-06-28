package top.simba1949.servicer;

import top.simba1949.entity.User;

import java.util.List;
import java.util.Set;

/**
 * @author SIMBA1949
 * @date 2020/6/27 21:11
 */
public interface UserService {

    int insert(User user);

    void update(User user);

    void delete(int id);

    User get(int id);

    List<User> select(User user);

    User getByUsername(String username);

    Set<String> listRoles(int userId);

    Set<String> listPermissions(Integer id);
}
