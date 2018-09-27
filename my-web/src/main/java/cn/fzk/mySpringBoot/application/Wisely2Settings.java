package cn.fzk.mySpringBoot.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by fzk on 2018/4/14.
 * 读取配置文件的配置项自动封装成实体类
 */
@Component
@ConfigurationProperties(prefix = "wisely2")
public class Wisely2Settings  {
    //@URL 非法的url在启动的时候是会抛出异常信息的
    //@Max(value = 99)定义最大值只能是99
    //@Min(value = 1)定义最小值只能是1
    //@NotNull 不能为null，如果为null就抛出异常
    //@NotEmpty 当没有定义key和key的值为空字符的时候都会抛出异常信息
    private String name;
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
