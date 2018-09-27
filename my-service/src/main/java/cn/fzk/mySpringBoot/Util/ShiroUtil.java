package cn.fzk.mySpringBoot.Util;

import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisSessionDAO;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by Administrator on 2018/8/22.
 * 用于用户踢出与用户更改信息后删除旧缓存重新读取信息
 */
public class ShiroUtil {
    private static RedisSessionDAO redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);

    private ShiroUtil() {
    }

    /**
     * 获取指定用户名的Session
     * @param username
     * @return
     */
    private static Session getSessionByUsername(String username){
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        UserInfo user;
        Object attribute;
        for(Session session : sessions){
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (UserInfo) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (Objects.equals(user.getUserName(), username)) {
                return session;
            }
        }
        return null;
    }

    /**
     * 删除用户缓存信息
     * @param username 用户名
     * @param isRemoveSession 是否删除session，删除后用户需重新登录
     */
    public static void kickOutUser(String username, boolean isRemoveSession){
        Session session = getSessionByUsername(username);
        if (session == null) {
            return;
        }

        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (attribute == null) {
            return;
        }

        UserInfo user = (UserInfo) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
        if (!username.equals(user.getUserName())) {
            return;
        }

        //删除session
        if (isRemoveSession) {
            redisSessionDAO.delete(session);
        }
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        //删除cache，在访问受限接口时会重新授权
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
    }

}
