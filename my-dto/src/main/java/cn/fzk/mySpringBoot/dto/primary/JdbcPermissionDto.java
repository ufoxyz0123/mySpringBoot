package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.param.PermissionParam;

import java.util.List;

/**
 * Created by Administrator on 2018/8/27.
 */
public interface JdbcPermissionDto {
    /**
    * @author fzk
    * @date 2018/8/27 15:49
    * @param
    * @return
    * 获得所有权限
    */
    List<SysPermission> getAll();
    /**
    * @author fzk
    * @date 2018/8/28 16:53
    * @param
    * @return
    * 获得总数
    */
    Integer findCount(PermissionParam param);
    /**
     * @author fzk
     * @date 2018/8/24 16:30
     * @param
     * @return
     * 分页查询
     */
    List<SysPermission> findPage(PermissionParam param);
    /**
     * @author fzk
     * @date 2018/8/29 10:24
     * @param
     * @return
     * 修改权限信息、状态
     */
    void updatePer(PermissionParam param);
    /**
    * @author fzk
    * @date 2018/8/29 13:29
    * @param
    * @return
    * 根据id查找
    */
    SysPermission findById(Long id);
    /**
     * @author fzk
     * @date 2018/8/29 16:03
     * @param
     * @return
     * 删除权限
     */
    void delPer(PermissionParam param);
}
