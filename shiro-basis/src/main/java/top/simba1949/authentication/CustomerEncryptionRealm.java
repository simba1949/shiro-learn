package top.simba1949.authentication;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author SIMBA1949
 * @date 2020/6/27 12:38
 */
public class CustomerEncryptionRealm extends AuthorizingRealm {
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
        // credentials（证书）是字符数组；
        char[] credentials = (char[]) token.getCredentials();
        String credentialsStr = new String(credentials);
        System.out.println("用户名：" + principal);
        System.out.println("用户密码：" + credentialsStr);
        // 对用户的密码输入的密码进行加密
        // 用户的盐应该从数据库中获取
        String salt = "simba";

        // 假设从 db 中获取的用户名和密码
        String dbPrincipal = "li-bai";
        String dbCredentials = "123456";
        Md5Hash dbCredentialsMd5Hash = new Md5Hash(dbCredentials, salt, 3);
        if (dbPrincipal.equalsIgnoreCase(principal)){
            // 从数据库中查询到用户名的数据
            // 参数一：表示从数据库中查询到的用户名
            // 参数二：表示从数据库中查询到的密码
            // 参数三：表示随机盐
            // 参数四：表示当前 realm 的名称，可以直接调用父类的名称即可
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(dbPrincipal, dbCredentialsMd5Hash,
                ByteSource.Util.bytes(salt), this.getName());
            return simpleAuthenticationInfo;
        }else {
            // 从数据库中没有查询到用户名的数据
            return null;
        }
    }
}
