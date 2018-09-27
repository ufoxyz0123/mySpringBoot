package cn.fzk.mySpringBoot.application.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by fzk on 2018/4/13.
 * 过滤器只要符合规则都会过滤，不管是不是spring请求
 */
@WebFilter(filterName="myFilter",urlPatterns="/*")
@Order(1)
public class MyFilter implements Filter {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("过滤器销毁");
    }
}
