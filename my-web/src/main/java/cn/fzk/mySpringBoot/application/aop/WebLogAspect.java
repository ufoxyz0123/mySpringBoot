package cn.fzk.mySpringBoot.application.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by Administrator on 2018/4/24.
 */
@Aspect//定义为切面类
@Component
@Order(2)//来标识切面的优先级,值越小，优先级越高在切入点前的操作，按order的值由小到大执行,在切入点后的操作，按order的值由大到小执行
public class WebLogAspect {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    /**
     * 定义一个切入点.
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 代表任意类.
     * ~ 第三个 * 代表任意方法.
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(public * cn.fzk.mySpringBoot.controller.*.*(..))")//切点
    public void webLog(){}
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());//记录请求接受时间
        // 接收到请求，记录请求内容
        logger.info("接收到请求，记录请求内容");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //Arrays.toString(joinPoint.getArgs())//获得所有参数组装成集合
        logger.info("以下是所有参数 : ");
        //获取所有参数方法一：
        Enumeration<String> enu=request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName=(String)enu.nextElement();
            logger.info(paraName + ": "+request.getParameter(paraName));
        }
    }
    @AfterReturning("webLog()")
    public void  doAfterReturning(JoinPoint joinPoint){
       //记录整个请求处理耗时
        logger.info("耗时（毫秒） : " + (System.currentTimeMillis() - startTime.get()));
        // 处理完请求，返回内容
        logger.info("处理完请求，返回内容");
    }
}
