package top.simba1949.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.simba1949.shiro.CustomerRealm;
import top.simba1949.util.EncryptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SIMBA1949
 * @date 2020/6/27 19:07
 */
@Configuration
public class ShiroConfig {

    public static  Map<String,String> srcMap = new HashMap<>(16);
    /**
     * 1.创建 ShiroFilter，负责拦截所有请求
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 配置系统受限资源
        // 配置系统公共资源
        // anon 表示不需要认证可以访问
        srcMap.put("/user/login", "anon");
        srcMap.put("/user/register", "anon");
        srcMap.put("/register.jsp", "anon");
        srcMap.put("/permission/none.jsp", "anon");
        // /** 表示拦截项目中一切资源
        // authc 请求者资源需要认证和授权
        srcMap.put("/**", "authc");
        // 设置系统登录页面
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(srcMap);

        return shiroFilterFactoryBean;
    }

    /**
     *  2.创建安全管理器
     *  需要注入自定义的 realm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("realm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 给安全管理器设置自定义 realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    /**
     * 3.创建自定义的 realm（因为将 CustomerRealm 注册为 Spring 组件，所以这里的 bean 需要指定名称注入到安全管理器中）
     * @return
     */
    @Bean("realm")
    public Realm getRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        // 为自定义 realm 使用 hash 凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 使用的算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列次数
        hashedCredentialsMatcher.setHashIterations(EncryptionUtils.HASH_ITERATIONS);

        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return customerRealm;
    }
}