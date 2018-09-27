package cn.fzk.mySpringBoot.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/4/13. 异常统一处理类
 */
@ControllerAdvice//拦截全局的Controller的异常,只拦截Controller 不回拦截 Interceptor的异常
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e)  {
//      If the exception is annotated with @ResponseStatus rethrow it and let
//      the framework handle it - like the OrderNotFoundException example
//      at the start of this post.
//      AnnotationUtils is a Spring Framework utility class.
//      if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
//         throw e;
//
//      Otherwise setup and send the user to a default error-view.
        //打印异常信息：
        e.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("exception");
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        return mav;

       /*
        * 返回json数据或者String数据：
        * 那么需要在方法上加上注解：@ResponseBody
        * 添加return即可。
        *
       /*
        * 返回视图：
        * 定义一个ModelAndView即可，
        * 然后return;
        * 定义视图文件(比如：error.html,error.ftl,error.jsp);
        *
        */
    }
    /**
     *#Spring 静态资源版本映射之资源名称md5方式，防止页面缓存
     * */
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;
    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }
}
