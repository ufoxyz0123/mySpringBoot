package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2018/4/13.
 */
/*
 * 在CrudRepository自带常用的crud方法.
 * 这样一个基本dao就写完了.
 * 按照Spring Data的规范的规范，查询方法以find | read | get 开头，涉及查询条件时，条件的属性用条件关键字连接，要注意的是：条件属性以首字母大写
 */
public interface JpaUserDto extends CrudRepository<UserInfo, Long> {
    /**通过username查找用户信息;*/
     UserInfo findByUserName(String userName);

     UserInfo findByUid(Long uid);

}
