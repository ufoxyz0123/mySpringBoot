package cn.fzk.mySpringBoot.dto.primary;



import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import cn.fzk.mySpringBoot.param.UserParam;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */
public interface JdbcUserDto {
    UserInfo findByUserName(String userName);
    void updateUser(UserParam param);
    /**
    * @author fzk
    * @date 2018/7/31 17:37
    * @param
    * @return
    * 查询总数
    */
    Integer findCount(UserParam param);
    /**
    * @author fzk
    * @date 2018/7/31 17:37
    * @param
    * @return
    * 分页查询
    */
    List<UserInfo> findPage(UserParam param);
}
