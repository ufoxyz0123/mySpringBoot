package cn.fzk.mySpringBoot.application.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/22.
 * 用于解决shiro频繁获取session ，将session放入request中
 */
public class MyShiroSessionManager extends DefaultWebSessionManager {
    public MyShiroSessionManager(){
        super();
    }

    //重写这个方法为了减少多次从redis中读取session（自定义redisSessionDao中的doReadSession方法）
    protected Session retrieveSession(SessionKey sessionKey){
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if(sessionKey instanceof WebSessionKey){
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }
        if(request != null && sessionId != null){
            Session session =  (Session) request.getAttribute(sessionId.toString());
            if(session != null){
                return session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if(request != null && sessionId != null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }

}
