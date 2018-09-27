package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.param.RoleParam;

import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */
public interface JdbcRoleDto {
    /**
    * @author fzk
    * @date 2018/8/24 16:30
    * @param
    * @return
    * 根据id查询
    */
    SysRole findById(Long id);
    /**
    * @author fzk
    * @date 2018/8/24 16:30
    * @param
    * @return
    * 获得全部
    */
    List<SysRole> getAll();
    /**
    * @author fzk
    * @date 2018/8/24 16:31
    * @param
    * @return
    * 获得总数
    */
    Integer findCount(RoleParam param);
   /**
   * @author fzk
   * @date 2018/8/24 16:30
   * @param
   * @return
   * 分页查询
   */
    List<SysRole> findPage(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/27 10:27
    * @param
    * @return
    * 更改角色状态
    */
    void updateRole(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/28 17:31
    * @param
    * @return
    * s删除角色
    */
    void delRole(RoleParam param);
}
