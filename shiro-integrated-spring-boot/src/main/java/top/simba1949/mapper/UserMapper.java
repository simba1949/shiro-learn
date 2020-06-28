package top.simba1949.mapper;

import org.apache.ibatis.annotations.Param;
import top.simba1949.entity.User;

import java.util.List;
import java.util.Set;

/**
 * @author SIMBA1949
 * @date 2020/6/27 21:01
 */
public interface UserMapper {
    /**
     * 插入
     * @param user
     * @return
     */
    int insert(@Param("user") User user);

    /**
     * 更新
     * @param user
     * @return
     */
    void update(@Param("user")User user);

    /**
     * 删除
     * @param id
     */
    void delete(@Param("id")Integer id);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    User get(@Param("id")Integer id);

    /**
     * 条件查询
     * @param user
     * @return
     */
    List<User> select(@Param("user")User user);

    /**
     * 根据名称获取用户
     * @param username
     * @return
     */
    User getByUsername(@Param("username") String username);

    /**
     * 根据用户id，查询所拥有的角色
     * @param userId
     * @return
     */
    Set<String> listRoles(@Param("userId")Integer userId);

    /**
     * 根据用户id，查询用户所有的资源
     * @param id
     * @return
     */
    Set<String> listPermissions(Integer id);
}
