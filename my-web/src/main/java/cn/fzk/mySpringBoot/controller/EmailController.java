package cn.fzk.mySpringBoot.controller;

import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.EmailParam;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */
public class EmailController {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    //邮件管理
    @Autowired
    private JavaMailSender mailSender;
    @Resource
    private TemplateEngine templateEngine;
    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public ResModel sendMimeMessage(EmailParam param){
        param.setFrom("543518241@qq.com");
        ResModel resModel = new ResModel(0,"操作成功");
        //这个是javax.mail.internet.MimeMessage下的，不要搞错了。
        MimeMessage mimeMessage =  mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //基本设置.
            helper.setFrom(param.getFrom());//发送者.
            helper.setTo(param.getTo());//接收者.
            helper.setSubject(param.getSubject());//邮件主题.
            if(param.getIsUseTem() == 0){
                if(!StringUtil.isNullOrEmpty(param.getPic())){
                    //图片
                    FileSystemResource pic = new FileSystemResource(new File(param.getPic()));
                    //这里需要注意的是addInline函数中资源名称head需要与正文中cid:head对应起来
                    helper.addInline("pic",pic);
                    StringBuffer buffer = new StringBuffer("<body>");
                    // 添加图片
                    buffer.append(param.getText()).append("<img src='cid:pic' /></body>");
                    //邮件内容
                    helper.setText(buffer.toString(),true);
                }else{
                    //邮件内容
                    helper.setText(param.getText());
                }
            }else{
                Context context = new Context();
                //设置参数
                context.setVariable("name",param.getTo());
                //获取模板html代码
                String process = templateEngine.process("emailTem", context);
                helper.setText(process, true);
            }
            List<String> files = param.getFiles();
            if(null != files && files.size() > 0){
                //org.springframework.core.io.FileSystemResource下的:
                for(String filePath:files){
                    //附件,获取文件对象.
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    //添加附件，这里第一个参数是在邮件中显示的名称，也可以直接是head.jpg，但是一定要有文件后缀，不然就无法显示图片了。
                    helper.addAttachment(file.getFilename(),file);
                }
            }
            mailSender.send(mimeMessage);
        }catch(Exception exception){
            logger.error("发送邮件异常，具体异常：",exception);
            resModel.setCode(-1);
            resModel.setDesc(exception.getMessage());
        }
        return resModel;
    }
}
