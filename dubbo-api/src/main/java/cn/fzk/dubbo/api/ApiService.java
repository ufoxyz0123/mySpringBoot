package cn.fzk.dubbo.api;

import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;

/**
 * Created by Administrator on 2018/9/12.
 */
public interface ApiService {
    /**
     * @author fzk
     * @date 2018/5/29 13:46
     * @param username
     * @return UserInfo
     * 根据用户名查询用户，用jpa实现，shiro用
     */
    UserInfo findByUsername(String username);
}
