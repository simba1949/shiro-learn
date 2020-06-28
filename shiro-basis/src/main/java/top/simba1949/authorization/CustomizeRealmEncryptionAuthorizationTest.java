package top.simba1949.authorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * 自定义 Realm 实现：将认证、授权的数据来源转为数据的实现
 * @author SIMBA1949
 * @date 2020/6/27 12:18
 */
public class CustomizeRealmEncryptionAuthorizationTest {
    public static void main(String[] args) {
        // 1.创建 SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 2.给安全管理器对象设置自定义加密的 realm
        CustomerEncryptionAuthorizationRealm customerEncryptionRealm = new CustomerEncryptionAuthorizationRealm();
        // 为自定义 realm 使用 hash 凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 使用的算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列次数
        hashedCredentialsMatcher.setHashIterations(3);
        customerEncryptionRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        defaultSecurityManager.setRealm(customerEncryptionRealm);
        // 3.SecurityUtils 全局安全工具类，设置安全管理器
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 4.从 SecurityUtils 中获取 subject
        Subject subject = SecurityUtils.getSubject();
        // 5.创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("li-bai");
        token.setPassword("123456".toCharArray());

        // 6.用户认证，即登录
        try {
            System.out.println("认证状态：" + subject.isAuthenticated());
            subject.login(token);
            System.out.println("认证状态：" + subject.isAuthenticated());
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("认证失败：用户名不存在");
        } catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("认证失败：密码错误");
        }

        // 授权测试
        if (subject.isAuthenticated()){
            System.out.println("是否拥有角色：" + subject.hasRole("admin"));
            System.out.println("是否拥有权限：" + subject.isPermitted("user:delete"));
        }

    }
}
