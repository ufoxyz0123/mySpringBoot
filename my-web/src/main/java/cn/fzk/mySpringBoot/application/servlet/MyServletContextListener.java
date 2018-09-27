package cn.fzk.mySpringBoot.application.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by fzk on 2018/4/13.
 * 在应用中一般普通的JavaPojo都是由Spring来管理的，所以使用autowire注解来进行注入不会产生问题，但是有两个东西是例外的，
 * 一个是 Filter，一个是Servlet，这两样东西都是由Servlet容器来维护管理的，所以如果想和其他的Bean一样使用Autowire来注入的 话，是需要做一些额外的功夫的。
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /**
         * 让不受spring管理的类具有spring自动注入的特性，可以直接注入使用
         * */
        AutowireCapableBeanFactory autowireCapableBeanFactory = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext()).getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBean(this);
        logger.info("ServletContex初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("ServletContex销毁");

    }

}
