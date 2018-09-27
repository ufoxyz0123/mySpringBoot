package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.param.RoleParam;
import cn.fzk.mySpringBoot.service.PermissionService;
import cn.fzk.mySpringBoot.service.RoleService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/8/24.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    RoleService roleService;
    @Resource
    PermissionService permissionService;
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    /**
    * @author fzk
    * @date 2018/8/24 15:55
    * @param
    * @return
    *  初始化
    */
    @RequestMapping("/init")
    public ModelAndView init(){
        logger.info("角色管理初始化");
        ModelAndView modelAndView = new ModelAndView();
        if(SecurityUtils.getSubject().hasRole("admin")){
            modelAndView.setViewName("role/list");
        }
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/24 16:20
    * @param
    * @return
    * 分页查询
    */
    @RequestMapping("/findPage")
    public PageResModel findPage(RoleParam pageParam){
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(SecurityUtils.getSubject().hasRole("admin")){
            resModel = roleService.findPage(pageParam);
        }else {
            resModel.setCode(ResModel.RespCode.NO_PRIVILEGE.getCode());
            resModel.setDesc(ResModel.RespCode.NO_PRIVILEGE.getDesc());
            logger.error("分页查询角色错误，具体错误：没有权限");
        }
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/27 9:28
    * @param
    * @return
    * 角色修改状态
    */
    @RequestMapping("/roleChangeStatus")
    public ResModel roleChangeStatus(RoleParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(param.getRoleId() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的id为空");
            logger.error("角色修改状态错误，具体错误：传入的id为空");
            return resModel;
        }
        if(param.getAvailable() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的状态为空");
            logger.error("角色修改状态错误，具体错误：传入的状态为空");
            return resModel;
        }
        resModel = roleService.update(param);
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/27 14:50
    * @param
    * @return
    * 跳转角色查看详情与修改
    */
    @RequestMapping("/roleEditAndView")
    public ModelAndView view(Long rid){
        logger.info(" 角色查看详情与修改，参数param为->"+ JSON.toJSONString(rid));
        ModelAndView modelAndView = new ModelAndView("role/view");
        List<SysPermission> roleMenus = null;
        if(null != rid){
            RoleParam roleParam = new RoleParam();
            roleParam.setRoleId(rid);
            ResModel resModel= roleService.findById(roleParam);
            SysRole role = (SysRole)resModel.getRows();
            roleMenus = role.getPermissions();
            modelAndView.addObject("editAndViewRole",role);
        }
        List<SysPermission> allMenus =  permissionService.getAll();
        JSONArray arr = roleService.buildRoleMenuTreeData(allMenus, roleMenus);
        modelAndView.addObject("permissionList",arr);
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/28 15:36
    * @param
    * @return
    * 角色新增与修改
    */
    @RequestMapping("/doRoleEditAndView")
    public ResModel doRoleEditAndView(RoleParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(StringUtils.isBlank(param.getRole())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的角色为空");
            logger.error("角色新增与修改错误，具体错误：传入的角色为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getDescription())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的角色描述为空");
            logger.error("角色新增与修改错误，具体错误：传入的角色描述为空");
            return resModel;
        }
        if(param.getRoleId() == null){
            resModel = roleService.save(param);
        }else{
            resModel = roleService.update(param);
        }
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/28 17:27
    * @param
    * @return
    * 删除角色
    */
    @RequestMapping("/delRole")
    public ResModel delRole(RoleParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(param.getRoleId() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的id为空");
            logger.error("删除角色异常，具体异常：传入的id为空");
            return resModel;
        }
        resModel = roleService.delRole(param);
        return resModel;
    }
}
