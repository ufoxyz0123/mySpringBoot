package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.param.PermissionParam;
import cn.fzk.mySpringBoot.service.PermissionService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/8/22.
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Resource
    PermissionService permissionService;
    /**
     * @author fzk
     * @date 2018/8/22 19:43
     * @param
     * @return
     * 初始化
     */
    @RequestMapping("/init")
    public ModelAndView init(){
        logger.info("权限管理初始化");
        ModelAndView modelAndView = new ModelAndView();
        if(SecurityUtils.getSubject().hasRole("admin")){
            modelAndView.setViewName("permission/list");
        }
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/28 16:46
    * @param
    * @return
    * 分页查询权限
    */
    @RequestMapping("/findPage")
    public PageResModel findPage(PermissionParam param){
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(SecurityUtils.getSubject().hasRole("admin")){
            resModel = permissionService.findPage(param);
        }else {
            resModel.setCode(ResModel.RespCode.NO_PRIVILEGE.getCode());
            resModel.setDesc(ResModel.RespCode.NO_PRIVILEGE.getDesc());
            logger.error("分页查询权限错误，具体错误：没有权限");
        }
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/29 10:01
    * @param
    * @return
    * 跳转新增、详情与修改权限
    */
    @RequestMapping("/peiEditAndView")
    public ModelAndView peiEditAndView(PermissionParam param){
        logger.info(" 跳转新增、详情与修改权限，参数param为->"+ JSON.toJSONString(param));
        ModelAndView modelAndView = new ModelAndView("permission/view");
        if(null != param.getPermissionId()){
            ResModel resModel= permissionService.findById(param);
            SysPermission sysPermission = (SysPermission)resModel.getRows();
            modelAndView.addObject("editAndViewPer",sysPermission);
        }else {
            param.setParentId(0L);
            param.setIsPage(Byte.parseByte("1"));
            ResModel resModel= permissionService.findByParam(param);
            modelAndView.addObject("pers",resModel.getRows());
        }
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/29 10:13
    * @param
    * @return
    * 更改权限状态
    */
    @RequestMapping("/preChangeStatus")
    public ResModel roleChangeStatus(PermissionParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(param.getPermissionId() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的id为空");
            logger.error("更改权限状态错误，具体错误：传入的id为空");
            return resModel;
        }
        if(param.getAvailable() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的状态为空");
            logger.error("更改权限状态错误，具体错误：传入的状态为空");
            return resModel;
        }
        resModel = permissionService.update(param);
        return resModel;
    }
   /**
   * @author fzk
   * @date 2018/8/29 15:37
   * @param
   * @return
   * 新增、详情与修改权限
   */
    @RequestMapping("/doPerEditAndView")
    public ResModel doPerEditAndView(PermissionParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(StringUtils.isBlank(param.getName())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的权限名称为空");
            logger.error("新增、详情与修改权限错误，具体错误：传入的权限名称为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getPermission())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的权限为空");
            logger.error("新增、详情与修改权限错误，具体错误：传入的权限为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getUrl())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的权限路径为空");
            logger.error("新增、详情与修改权限错误，具体错误：传入的权路径限为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getResourceType())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的权限种类为空");
            logger.error("新增、详情与修改权限错误，具体错误：传入的权限种类为空");
            return resModel;
        }
        if(param.getPermissionId() == null){
            resModel = permissionService.save(param);
        }else {
            resModel = permissionService.update(param);
        }
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/29 16:03
    * @param
    * @return
    * 删除权限
    */
    @RequestMapping("/delPer")
    public ResModel delPer(PermissionParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(param.getPermissionId() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的id为空");
            logger.error("删除权限异常，具体异常：传入的id为空");
            return resModel;
        }
        resModel = permissionService.delPer(param);
        return resModel;
    }
}
