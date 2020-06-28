package top.simba1949.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

/**
 * @author SIMBA1949
 * @date 2020/6/26 16:19
 */
public class IniAuthenticationTest {
    public static void main(String[] args) {
        // 1.创建安全管理器对象
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 2.给安全管理器对象设置 realm
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
        defaultSecurityManager.setRealm(iniRealm);
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
    }
}
