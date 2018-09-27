package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.LocaleMessageSourceService;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fzk on 2018/4/18.
 * Swagger2默认将所有的Controller中的RequestMapping方法都会暴露，然而在实际开发中，我们并不一定需要把所有API都提现在文档中查看，这种情况下，
 * 使用注解@ApiIgnore来解决，如果应用在Controller范围上，则当前Controller中的所有方法都会被忽略，如果应用在方法上，则对应用的方法忽略暴露API。
 * 注解@ApiOperation和@ApiParam可以理解为API说明，多动手尝试就很容易理解了。
 * 如果我们不使用这样注解进行说明，Swagger2也是有默认值的，没什么可说的试试就知道了。
 */
@Controller
@Api(value = "LoginController", description = "用户登录登出接口")
public class LoginController {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Resource
    private LocaleMessageSourceService localeMessageSourceService;//获得i18n的语言配置

    @RequestMapping({"/","/index"})
    public String index(){
        return"index";
    }
    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login(Map<String,Object> map){
        UserInfo userInfo = (UserInfo)SecurityUtils.getSubject().getPrincipal();
        if(userInfo != null){
            return"index";
        }
        return"login";
    }


    @RequestMapping(value="/login",method=RequestMethod.POST)
    @ApiOperation(value="用户登录", notes="用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true ,dataType = "string")
    })
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {

        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = "";
        if (exception != null) {
            //账号错误                                                     //密码错误
            if (UnknownAccountException.class.getName().equals(exception)||IncorrectCredentialsException.class.getName().equals(exception)) {
                msg = "账号/密码错误";
            }else if(LockedAccountException.class.getName().equals(exception)){
                msg = "用户被锁定";
            }else if(ExcessiveAttemptsException.class.getName().equals(exception)){
                msg ="密码输入错误次数大于5次，该账号在半小时内禁止登录！";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                msg = "验证码错误";
            }else if ("nullkaptchaValidateFailed".equals(exception)) {
                msg = "验证码不可为空";
            } else {
                msg = "其他错误"+exception;
                logger.error("登录有异常，具体异常"+exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理.
        return "login";
    }
    @RequestMapping(value = "/logout")
    public String logout(){
        return "logout";
    }
    /**
     * 更改语言
     * */
    @RequestMapping("/changeSessionLanauage")
    @ResponseBody
    public ResModel changeSessionLanauage(HttpServletRequest request, String lang, Map<String,Object> map){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        String msg3 = localeMessageSourceService.getMessage("welcomestart");//获得i18n的语言配置的对应的key的值
        try {
            if("zh".equals(lang)){
                //代码中即可通过以下方法进行语言设置
                //获取当前使用的区域解析器LocaleResolver 调用里面的方法,不管是会话还是cookie区域解析器都是一样的代码
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "CN"));
            }else if("en".equals(lang)){
                //代码中即可通过以下方法进行语言设置
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US"));
            }
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.NO_PRIVILEGE.getCode());
            resModel.setDesc(ResModel.RespCode.NO_PRIVILEGE.getDesc());
        }
        return resModel;
    }
}
