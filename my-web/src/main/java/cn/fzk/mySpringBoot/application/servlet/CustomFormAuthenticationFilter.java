package cn.fzk.mySpringBoot.application.servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/5/9.
 * 自定义的shiro验证码校验类
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if(this.isLoginRequest(request, response)) {
            if(this.isLoginSubmission(request, response)) {
                // 在这里进行验证码的校验
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpSession session = httpServletRequest.getSession();
                // 取出验证码
                String validateCode = (String) session.getAttribute("validateCode");
                // 取出页面的验证码
                // 输入的验证和session中的验证进行对比
                String randomcode = httpServletRequest.getParameter("randomcode");
                if(StringUtils.isBlank(randomcode)){
                    // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
                    httpServletRequest.setAttribute("shiroLoginFailure", "nullkaptchaValidateFailed");//自定义登录异常
                    // 拒绝访问，不再校验账号和密码
                    return true;
                }
                if (randomcode != null && validateCode != null && !randomcode.equalsIgnoreCase(validateCode)) {
                    // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
                    httpServletRequest.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");//自定义登录异常
                    // 拒绝访问，不再校验账号和密码
                    return true;
                }
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            this.saveRequestAndRedirectToLogin(request, response);
            return false;
        }
    }
}
