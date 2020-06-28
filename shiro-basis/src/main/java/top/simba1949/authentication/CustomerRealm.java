package top.simba1949.authentication;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author SIMBA1949
 * @date 2020/6/27 12:20
 */
public class CustomerRealm extends AuthorizingRealm {
    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 在 token 中获取用户名，需要根据用户名去数据中查询数据中的用户信息
        String principal = (String)token.getPrincipal();
        System.out.println("用户名：" + principal);
        // 假设从 db 中获取的用户名和密码
        String dbPrincipal = "li-bai";
        String dbCredentials = "123456";
        if (dbPrincipal.equalsIgnoreCase(principal)){
            // 从数据库中查询到用户名的数据
            // 参数一：表示从数据库中查询到的用户名
            // 参数二：表示从数据库中查询到的密码
            // 参数三：表示当前 realm 的名称，可以直接调用父类的名称即可
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(dbPrincipal, dbCredentials, this.getName());
            return simpleAuthenticationInfo;
        }else {
            // 从数据库中没有查询到用户名的数据
            return null;
        }
    }
}
