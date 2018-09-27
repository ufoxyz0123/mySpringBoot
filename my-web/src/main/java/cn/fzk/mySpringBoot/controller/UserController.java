package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.application.Wisely2Settings;
import cn.fzk.mySpringBoot.application.WiselySettings;
import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import cn.fzk.mySpringBoot.param.UserParam;
import cn.fzk.mySpringBoot.service.RoleService;
import cn.fzk.mySpringBoot.service.UserService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/13.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    WiselySettings wiselySettings;
    @Autowired
    Wisely2Settings wisely2Settings;
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    /**
    * @author fzk
    * @date 2018/8/22 19:43
    * @param
    * @return
    * 初始化
    */
    @RequestMapping("/init")
    public ModelAndView init(){
        logger.info("用户管理初始化");
        ModelAndView modelAndView = new ModelAndView();
        if(SecurityUtils.getSubject().hasRole("admin")){
            modelAndView.setViewName("user/list");
        }
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/22 19:43
    * @param
    * @return
    * 注册账号
    */
    @RequestMapping("/registerUser")
    public ModelAndView registerUser(){
        logger.info("跳转注册用户");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registerUser");
        return modelAndView;
    }
    /**
    * @author fzk
    * @date 2018/8/22 19:43
    * @param
    * @return
    * 查看用户详情
    */
    @RequestMapping("/editAndView")
    public ModelAndView view(Long uid,String username){
        ModelAndView modelAndView = new ModelAndView("user/view");
        logger.info(" 查看用户详情，参数param为->"+ JSON.toJSONString(uid));
        UserInfo userInfo = new UserInfo();
        if( null == uid){
            userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        }else{
            UserParam userParam = new UserParam();
            userParam.setUid(uid);
            userParam.setUserName(username);
            ResModel resModel = userService.findById(userParam);
            userInfo =(UserInfo)resModel.getRows();
        }
        List<Long> roleIds =  userInfo.getRoleList().stream().map(c->{
            return c.getRoleId();
        }).collect(Collectors.toList());
        modelAndView.addObject("editAndViewUserInfo",userInfo).addObject("roleList",roleService.getAll()).addObject("roleIds",roleIds);
        return modelAndView;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:49
     * @param
     * @return
     * 新增用户，注意用户添加角色
     */
    @RequestMapping(value = "/saveUser")
    public ResModel saveUser(UserParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 新增用户，参数param为->"+ JSON.toJSONString(param));
        if(StringUtils.isBlank(param.getUserName())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的账号为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getPassWord())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的密码为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getNickName())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的用户名称为空");
            return resModel;
        }
        resModel = userService.saveUser(param);
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/7/27 11:04
    * @param param
    * @return cn.fzk.mySpringBoot.service.Util.ResModel
    * 修改密码
    */
    @RequestMapping(value = "/updatePassword")
    public ResModel updatePassword(UserParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 修改密码，参数param为->"+ JSON.toJSONString(param));
        if(StringUtils.isBlank(param.getPassWord())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的密码为空");
            return resModel;
        }
        UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        param.setUserName(userInfo.getUserName());
        resModel = userService.updatePassword(param,userInfo);
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/15 10:51
    * @param
    * @return
    * 用户信息修改，同步必须实名制
    */
    @RequestMapping(value = "/updateUser")
    public ResModel updateUser(UserParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 用户信息修改，参数param为->"+ JSON.toJSONString(param));
        if(StringUtils.isBlank(param.getNickName())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的昵称为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getRealName())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的真实姓名为空");
            return resModel;
        }
        if(StringUtils.isBlank(param.getEmail())){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的邮箱为空");
            return resModel;
        }
        if(null == param.getPhone()){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的电话为空");
            return resModel;
        }
        if(null != param.getUid()){
            if(StringUtils.isBlank(param.getRoleIds())){
                resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
                resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的权限为空");
                return resModel;
            }
        }else {
            UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
            param.setUserName(userInfo.getUserName());
            param.setUid(userInfo.getUid());
        }
        resModel = userService.update(param);
        return resModel;
    }
    /**
    * @author fzk
    * @date 2018/8/1 17:39
    * @param
    * @return
    * 管理员修改账号状态
    */
    @RequestMapping(value = "/changeStatus")
    public ResModel changeStatus(UserParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 管理员修改账号状态，参数param为->"+ JSON.toJSONString(param));
        if(param.getUid() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：无法找到要修改的用户");
            logger.error("管理员修改账号状态异常，具体异常：无法找到要修改的用户");
            return resModel;
        }
        if(param.getStatus() == null){
            resModel.setCode(ResModel.RespCode.PARAM_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.PARAM_EXCEPTION.getDesc()+"：传入的状态为空");
            return resModel;
        }
        resModel = userService.update(param);
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:49
     * @param
     * @return
     * 根据id查找用户，jpa实现
     */
    @RequestMapping("/findById")
    public ResModel findById(Long id){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 根据id查找用户，参数param为->"+ JSON.toJSONString(id));
        if(id == null){
            resModel.setCode(-3);
            resModel.setDesc("参数异常：传入的账号为空");
            return resModel;
        }
        UserParam param = new UserParam();
        param.setUid(id);
        resModel = userService.findById(param);
        return resModel;
    }

    /**
     * @author fzk
     * @date 2018/5/29 13:50
     * @param
     * @return
     * 根据id删除，jpa实现
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResModel delete(Long id,String userName){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info(" 根据id删除参数param为->"+ JSON.toJSONString(id));
        if(id == null){
            resModel.setCode(-1);
            resModel.setDesc("参数异常：传入的id为空");
            return resModel;
        }
        UserParam param = new UserParam();
        param.setUid(id);
        param.setUserName(userName);
        resModel = userService.deleteById(param);
        return resModel;
    }

    /**
    * @author fzk
    * @date 2018/7/31 16:10
    * @param
    * @return
    * 分页列表查询
    */
    @RequestMapping("/findPage")
    @ResponseBody
    public PageResModel findPage(UserParam pageParam){
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        if(SecurityUtils.getSubject().hasRole("admin")){
            resModel = userService.findPage(pageParam);
        }else {
            resModel.setCode(ResModel.RespCode.NO_PRIVILEGE.getCode());
            resModel.setDesc(ResModel.RespCode.NO_PRIVILEGE.getDesc());
        }
        return resModel;
    }
    @RequestMapping("/test1")
    @ResponseBody
    public String test1(){
        userService.test();
        System.out.println("DemoInfoController.test1()");
        return "ok1";
    }
    @RequestMapping(value = "/getProperBean")
    public String getProperBean(){
        return "wiselySettings="+wiselySettings.toString() + "||||| wisely2Settings="+wisely2Settings.toString();
    }
}
