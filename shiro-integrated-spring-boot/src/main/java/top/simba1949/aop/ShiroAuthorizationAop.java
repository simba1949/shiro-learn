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
