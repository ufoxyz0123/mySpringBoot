package cn.fzk.mySpringBoot.application.servlet;

import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fzk on 2018/4/14.
 */
public class MyInterceptor1  implements HandlerInterceptor {
    //MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }
    //>>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {
        if(null != modelAndView && !modelAndView.getViewName().equals("exception")){
            UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
            if(null != userInfo){
                modelAndView.addObject("userInfo",userInfo);
            }
        }
    }
    //>>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
