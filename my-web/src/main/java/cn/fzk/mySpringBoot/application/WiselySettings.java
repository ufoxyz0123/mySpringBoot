package cn.fzk.mySpringBoot.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by fzk on 2018/4/14.
 * 读取自定义配置文件中的配置项自动封装成实体类
 */
@Component
@PropertySource(value = "classpath:i18n/messages.properties",name= "i18n/messages.properties")
@ConfigurationProperties(prefix = "wisely")
public class WiselySettings {
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
