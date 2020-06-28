package top.simba1949.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.simba1949.entity.User;
import top.simba1949.servicer.UserService;

import java.util.Set;

/**
 * @author SIMBA1949
 * @date 2020/6/27 19:16
 */
@Component
public class CustomerRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取主身份信息，即用户名
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        User dbUser = userService.getByUsername(primaryPrincipal);
        // 获取用户拥有的所有角色
        Set<String> roles = userService.listRoles(dbUser.getId());
        // 获取用户拥有的所有资源权限
        Set<String> permissions = userService.listPermissions(dbUser.getId());

        // 根据主身份信息（用户名）获取当前角色信息和权限信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String)token.getPrincipal();
        // 从数据库中获取用户数据
        User dbUser = userService.getByUsername(principal);
        if (null != dbUser){
            // 从数据库中查询到数据
            SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(dbUser.getUsername(), dbUser.getPassword(), ByteSource.Util.bytes(dbUser.getSalt()),this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
