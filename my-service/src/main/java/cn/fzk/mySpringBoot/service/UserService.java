package cn.fzk.mySpringBoot.service;


import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.UserParam;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface UserService {

    /**
    * @author fzk
    * @date 2018/5/29 13:49
    * @param
    * @return
    * 新增用户，注意用户添加角色
    */
    ResModel saveUser(UserParam param);
    /**
    * @author fzk
    * @date 2018/5/29 13:50
    * @param
    * @return
    * 根据id删除，jpa实现
    */
    ResModel deleteById(UserParam param);
    /**
    * @author fzk
    * @date 2018/5/29 13:54
    * @param
    * @return
    * 修改用户信息
    */
    ResModel update(UserParam param);
    /**
     * @author fzk
     * @date 2018/5/29 13:54
     * @param
     * @return
     * 修改用户密码
     */
    ResModel updatePassword(UserParam param,UserInfo userInfo);
    /**
     * @author fzk
     * @date 2018/5/29 13:46
     * @param username
     * @return UserInfo
     * 根据用户名查询用户，用jpa实现，shiro用
     */
    UserInfo findByUsername(String username);
    /**
     * @author fzk
     * @date 2018/5/29 13:49
     * @param
     * @return
     * 根据id查找用户，jpa实现
     */
    ResModel findById(UserParam param);
    /**
    * @author fzk
    * @date 2018/7/31 16:16
    * @param
    * @return
    * 分页查询用户
    */
    PageResModel findPage(UserParam param);
    void test();
}
