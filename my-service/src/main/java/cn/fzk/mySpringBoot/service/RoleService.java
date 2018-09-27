package cn.fzk.mySpringBoot.service;

import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.RoleParam;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2018/8/23.
 */
public interface RoleService {
    /**
     * 获得所有角色
     * */
     List<SysRole> getAll();
     /**
     * @author fzk
     * @date 2018/8/24 16:28
     * @param
     * @return
     * 分页查询
     */
    PageResModel findPage(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/27 9:44
    * @param
    * @return
    * 修改角色信息状态
    */
    ResModel update(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/27 15:14
    * @param
    * @return
    * 根据id查找
    */
    ResModel findById(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/28 10:10
    * @param
    * @return
    * 组装ztree可是识别的数据
    */
    JSONArray buildRoleMenuTreeData(List<SysPermission> allMenus, List<SysPermission> roleMenus);
    /**
    * @author fzk
    * @date 2018/8/28 15:39
    * @param
    * @return
    * 角色新增
    */
    ResModel save(RoleParam param);
    /**
    * @author fzk
    * @date 2018/8/28 17:28
    * @param
    * @return
    * 删除角色
    */
    ResModel delRole(RoleParam param);
}
