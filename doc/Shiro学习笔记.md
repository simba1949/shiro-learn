# Shiro学习笔记

## 前言

版本说明

```properties

```

相关链接

* Shiro 官网：http://shiro.apache.org/
* Shiro maven 地址：https://mvnrepository.com/artifact/org.apache.shiro/shiro-core
* shiro-spring-boot-starter maven 地址：https://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-starter

## Shiro 架构

### Shiro 是什么？

`Apache Shiro` 是一个功能强大且易于使用的 `Java` 安全框架，它执行身份验证，授权，加密和会话管理。 使用 `Shiro` 易于理解的`API` ，您可以快速轻松地保护任何应用程序——从最小的移动应用程序到最大的Web和企业应用程序。

### 为什么要使用 Shiro

1. **易于使用** ：能够快速使用到项目中；
2. **全面** ：`Apache Shiro` 声称没有其他具有范围广度的安全框架，因此它很可能是满足安全需求的“一站式服务”；
3. **灵活** ：`Apache Shiro` 可以在任何应用程序环境中工作。尽管它可以在`Web`，`EJB`和`IoC`环境中运行，但并不需要它们。`Shiro`也不要求任何规范，甚至没有很多依赖性；
4. **具有Web功能** ：`Apache Shiro`具有出色的`Web`应用程序支持，允许您基于应用程序`URL`和`Web`协议（例如REST）创建灵活的安全策略，同时还提供一组`JSP`库来控制页面输出；
5. **可插拔** ： `Shiro`干净的`API`和设计模式使它易于与许多其他框架和应用程序集成。您会看到`Shiro`与`Spring`等框架无缝集成。

### 核心概念

1. `Subject` ：表示“当前正在执行的用户”，可以指一个人，但也指账号；
2. `SecurityManager` ：主题的“幕后”对应对象是 `SecurityManager`。主题表示当前用户的安全操作，而`SecurityManager`管理所有用户的安全操作。它是`Shiro`体系结构的核心，并充当一种“伞”对象，引用了许多内部嵌套的安全组件，这些安全组件构成了一个对象图。但是，一旦配置了`SecurityManager`及其内部对象图，通常就不理会它，应用程序开发人员几乎将所有时间都花在`Subject API`上。
3. `Realms` ：`Shiro` 从 `Realm` 获取安全数据（如用户、角色、权限），就是说 `SecurityManager` 要验证用户身份，那么它需要从 `Realm` 获取相应的用户进行比较以确定用户身份是否合法；也需要从 `Realm` 得到用户相应的角色 / 权限进行验证用户是否能进行操作；可以把 `Realm` 看成 `DataSource`，即安全数据源。

![Shiro核心概念](img/Shiro核心概念.png)

### Shiro 模块

![Shiro模块](img/Shiro模块.png)

* **`Authentication`**：身份认证 / 登录，验证用户是不是拥有相应的身份；
* **`Authorization`**：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
* **`Session Manager`**：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通 `JavaSE` 环境的，也可以是如 `Web` 环境的；
* **`Cryptography`**：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
* **`Web Support`**：`Web` 支持，可以非常容易的集成到 `Web` 环境；
* **`Caching`**：缓存，比如用户登录后，其用户信息、拥有的角色 / 权限不必每次去查，这样可以提高效率；
* **`Concurrency`**：`Shiro` 支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；
* **`Testing`**：提供测试支持；
* **`Run As`**：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；
* **`Remember Me`**：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。

### Shiro 内部架构图

![Shiro内部架构图](img/Shiro内部架构图.png)

## Shiro 实战笔记

### 加密算法

```java
package top.simba1949.encryption;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author SIMBA1949
 * @date 2020/6/27 12:52
 */
public class EncryptionMethod {
    public static void main(String[] args) {
        String password = "123456";

        // 使用 md5 setter 方法不会对密码进行加密
        Md5Hash md5HashWithoutEncryption = new Md5Hash();
        md5HashWithoutEncryption.setBytes(password.getBytes());
        System.out.println(md5HashWithoutEncryption.toHex());

        // 使用 md5，使用构造方法才会对密码进行加密
        Md5Hash md5Hash = new Md5Hash(password);
        System.out.println("使用md5构造方法：" + md5Hash.toHex());

        // 使用 md5 + salt + 散列次数
        // 盐
        String salt = "X0*7ps";
        // 散列次数
        int hashIterations = 1024;
        Md5Hash md5HashWithSaltAndHashIterations = new Md5Hash(password, salt, hashIterations);
        System.out.println("使用md5构造方法（加盐加散列次数）：" + md5HashWithSaltAndHashIterations.toHex());
    }
}
```

### 身份认证

#### 基于 ini 身份验证

**身份验证**，即在应用中谁能证明他就是他本人。一般提供如他们的身份 ID 一些标识信息来表明他就是他本人，如提供身份证，用户名 / 密码来证明。

在 shiro 中，用户需要提供 principals （身份）和 credentials（证明）给 shiro，从而应用能验证用户身份：

**principals**：身份，即主体的标识属性，可以是任何东西，如用户名、邮箱等，唯一即可。一个主体可以有多个 principals，但只有一个 Primary principals，一般是用户名 / 密码 / 手机号。

**credentials**：证明 / 凭证，即只有主体知道的安全值，如密码 / 数字证书等。

最常见的 principals 和 credentials 组合就是用户名 / 密码了。

##### pom 依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-core -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.5.3</version>
</dependency>
```

##### shiro.ini 配置信息

```ini
[users]
li-bai=123456
du-fu=123456
```

##### 身份认证测试

```java
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
        // 4.关键对象subject主题
        Subject subject = SecurityUtils.getSubject();
        // 5.创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("li-bai");
        token.setPassword("123456".toCharArray());

        // 用户认证
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
```

##### 认证

1. 最终执行用户名比较是在：`SimpleAccountRealm` 中的 `doGetAuthenticationInfo` 方法中完成用户名的校验；
2. 最终密码校验是在：`AuthenticatingRealm` 中的 `assertCredentialsMatch` 方法中完成密码的校验；

#### 自定义 Realm 身份认证

##### pom 依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-core -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.5.3</version>
</dependency>
```

##### 自定义 realm

```java
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
        // 在 token 中获取用户名
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
```

##### 自定义 realm 测试

```java
package top.simba1949.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * 自定义 Realm 实现：将认证、授权的数据来源转为数据的实现
 * @author SIMBA1949
 * @date 2020/6/27 12:18
 */
public class CustomizeRealmAuthenticationTest {
    public static void main(String[] args) {
        // 1.创建 SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 2.给安全管理器对象设置 realm
        CustomerRealm customerRealm = new CustomerRealm();
        defaultSecurityManager.setRealm(customerRealm);
        // 3.SecurityUtils 全局安全工具类，设置安全管理器
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 4.从 SecurityUtils 中获取 subject
        Subject subject = SecurityUtils.getSubject();
        // 5.创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("li-bai");
        token.setPassword("1234561".toCharArray());

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
```

#### 自定义 Realm 加密身份认证

##### pom 依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-core -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.5.3</version>
</dependency>
```

##### 自定义加密 realm 

```java
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
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

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
```

##### 自定义加密 realm 测试

```java
package top.simba1949.authentication;

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
public class CustomizeRealmEncryptionAuthenticationTest {
    public static void main(String[] args) {
        // 1.创建 SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 2.给安全管理器对象设置自定义加密的 realm
        CustomerEncryptionRealm customerEncryptionRealm = new CustomerEncryptionRealm();
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
    }
}
```

### 授权

#### 授权方式

1. **RBAC基于角色的访问控制： ** RBAC 基于角色的访问控制是以角色为中心进行访问控制；
2. **RBAC基于资源的访问控制：** RBAC 基于资源的访问控制是以资源为中心进行访问控制；

#### Shiro 授权实现方式

1. 编程式

   ```java
   Subject subject = SecurityUtils.getSubject();
   if (subject.hasRole("admin")){
       // 有权限
   }else {
       // 无权限
   }
   ```

2. 注解式

   ```java
   import org.apache.shiro.authz.annotation.RequiresRoles;
   
   @RequiresRoles("admin")
   public void hello(){
       // 有权限
   }
   ```

3. 标签式

   ```jsp
   <shiro:hasRole name="admin">
   	<!--有权限-->
   </shiro:hasRole>
   ```

#### 自定义授权

##### 自定义 realm 的授权

```java
package top.simba1949.authorization;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author SIMBA1949
 * @date 2020/6/27 12:38
 */
public class CustomerEncryptionAuthorizationRealm extends AuthorizingRealm {
    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户的主身份，一个用户可以有多个身份，但是只有一个主身份
        String primaryPrincipal = (String)principals.getPrimaryPrincipal();
        System.out.println("用户的主身份信息：" + primaryPrincipal);
        // 根据主身份信息（用户名）获取当前角色信息和权限信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 从数据中查询到的角色集合
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        simpleAuthorizationInfo.setRoles(roles);
        // 从数据中查询到的权限集合，基于权限字符串的访问控制：资源标识符:操作:资源类型
        Set<String> permissions = new HashSet<>();
        permissions.add("user:insert:*");
        permissions.add("user:update:1");
        permissions.add("user:delete:2");
        permissions.add("user:get:*");
        permissions.add("user:select:*");
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
```

##### 自定义 realm 测试

```java
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
```

## 与 SpringBoot 集成

### 核心 pom  依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-starter -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-starter</artifactId>
    <version>1.5.3</version>
</dependency>
```

### Shiro 常见的过滤器

|    Filter Name    |                          **Class**                           | **功能**                             |
| :---------------: | :----------------------------------------------------------: | ------------------------------------ |
|       anon        |     `org.apache.shiro.web.filter.authc.AnonymousFilter`      | url 可以匿名访问                     |
|       authc       | `org.apache.shiro.web.filter.authc.FormAuthenticationFilter` | 需要登录访问                         |
|    authcBasic     | `org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter` | url需要basic登录                     |
|    authcBearer    | `org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter` |                                      |
|      logout       |       `org.apache.shiro.web.filter.authc.LogoutFilter`       | 登出                                 |
| noSessionCreation | `org.apache.shiro.web.filter.session.NoSessionCreationFilter` | 禁止创建会话                         |
|       perms       | `org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter` | 需要指定权限才能访问                 |
|       port        |        `org.apache.shiro.web.filter.authz.PortFilter`        | 需要指定端口才能访问                 |
|       rest        | `org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter` |                                      |
|       roles       | `org.apache.shiro.web.filter.authz.RolesAuthorizationFilter` | 需要指定角色才能访问                 |
|        ssl        |        `org.apache.shiro.web.filter.authz.SslFilter`         | 需要https请求才能访问                |
|       user        |        `org.apache.shiro.web.filter.authc.UserFilter`        | 需要已登录或者“记住我”的用户才能访问 |

### 与 SpringBoot 集成基本步骤

1. 引入 `SpringBoot-Shiro` 依赖
2. 配置 `ShiroConfig` 配置类（①创建`ShiroFilterFactoryBean`，②创建安全管理器`DefaultWebSecurityManager` ，③自定义 `Realm`）
3. 自定义 `Realm`
4. 基于`环绕AOP`的`Shiro`全局权限控制

#### 引入 SpringBoot-Shiro 依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-starter -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-starter</artifactId>
    <version>1.5.3</version>
</dependency>
```

#### 配置 ShiroConfig 配置类

```java
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
```

#### 自定义 `Realm`

```java
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
```

#### 基于环绕AOP的Shiro全局权限控制

```java
package top.simba1949.aop;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shiro 全局授权控制
 * @author SIMBA1949
 * @date 2020/6/28 7:13
 */
@Aspect
@Component
public class ShiroAuthorizationAop {
    /**
     * 定义切入点，切入点为com.example.demo.aop.AopController中的所有函数
     * 通过 @Pointcut 注解声明频繁使用的切点表达式
     */
    @Pointcut(value = "execution(public * top.simba1949.controller.core.*.*(..))")
    public void aspectCenter(){

    }

    /**
     * 环绕通知接受 ProceedingJoinPoint 作为参数，它来调用被通知的方法。
     * 通知方法中可以做任何的事情，当要将控制权交给被通知的方法时，需要调用 ProceedingJoinPoint 的 proceed()方法。
     * 当你不调用 proceed()方法时，将会阻塞被通知方法的访问。
     * @param pjp
     */
    @Around("aspectCenter()")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println("around before");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String requestURI = request.getRequestURI();

        String realRequestURI = requestURI.substring(6);
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isPermitted(realRequestURI)){
            // 无权限
            try {
                // 如果没有权限，转发到没有权限的页面
                // 如若是前后端分离项目，可以发送没有权限的JSON数据
                response.sendRedirect("/shiro/permission/none.jsp");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            pjp.proceed();
            System.err.println("around after");
        }
    }
}
```

