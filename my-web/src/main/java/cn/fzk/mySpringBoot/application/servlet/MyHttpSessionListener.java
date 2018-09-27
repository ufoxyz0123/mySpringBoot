package cn.fzk.mySpringBoot.application.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by fzk on 2018/4/13.
 */
@WebListener
public class MyHttpSessionListener implements HttpSessionListener {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.info("Session 被创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("Session销毁");
    }
}
