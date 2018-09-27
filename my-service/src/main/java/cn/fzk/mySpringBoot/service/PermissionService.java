package cn.fzk.mySpringBoot.service;

import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.PermissionParam;

import java.util.List;

/**
 * Created by Administrator on 2018/8/27.
 */
public interface PermissionService {
    /**
     * 获得所有权限
     * */
    List<SysPermission> getAll();
    /**
    * @author fzk
    * @date 2018/8/28 16:49
    * @param
    * @return
    * 分页查询权限
    */
    PageResModel findPage(PermissionParam param);
    /**
    * @author fzk
    * @date 2018/8/29 10:24
    * @param
    * @return
    * 修改权限信息、状态
    */
    ResModel update(PermissionParam param);
    /**
    * @author fzk
    * @date 2018/8/29 13:27
    * @param
    * @return
    * 根据id查找权限
    */
    ResModel findById(PermissionParam param);
    /**
    * @author fzk
    * @date 2018/8/29 17:15
    * @param
    * @return
    * 条件查询权限
    */
    ResModel findByParam(PermissionParam param);
    /**
     * @author fzk
     * @date 2018/8/29 16:03
     * @param
     * @return
     * 删除权限
     */
    ResModel delPer(PermissionParam param);
    /**
    * @author fzk
    * @date 2018/8/30 9:49
    * @param
    * @return
    * 新建权限
    */
    ResModel save(PermissionParam param);
}
