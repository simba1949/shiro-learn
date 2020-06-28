package top.simba1949.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.simba1949.entity.User;
import top.simba1949.servicer.UserService;

/**
 * @author SIMBA1949
 * @date 2020/6/27 20:32
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("register")
    public String register(String username, String password){
        User user = User.builder().username(username).password(password).build();
        userService.insert(user);
        return "redirect:/shiro/login.jsp";
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public String login(String username, String password){
        // 获取主题对象
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // 用户认证，即登录
            subject.login(token);
            // 登录成功
            return "redirect:/index.jsp";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("认证失败：用户名不存在");
        } catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("认证失败：密码错误");
        }

        return "redirect:/login.jsp";
    }

    /**
     * 登出
     * @return
     */
    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login.jsp";
    }
}