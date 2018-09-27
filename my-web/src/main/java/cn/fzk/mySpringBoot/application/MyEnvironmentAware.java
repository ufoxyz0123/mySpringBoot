package cn.fzk.mySpringBoot.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by fzk on 2018/4/14.
 * 获取配置文件中内容 @Controller @Service 等被Spring管理的类都支持
 * setEnvironment 是在系统启动的时候被执行。
 */
@Configuration
public class MyEnvironmentAware implements EnvironmentAware {

    @Value("${spring.datasource.primary.url}")
    //@Value("${key:defaultVlaue}")此属性代表当没有设置key值时，使用我们设置的默认的值，并将此key：value 注入
    private String myUrl;
    @Override
    public void setEnvironment(Environment environment) {

        //打印注入的属性信息.
        System.out.println("myUrl="+myUrl);

        //通过 environment 获取到系统属性.
        //System.out.println(environment.getProperty("PATH"));

        //通过 environment 同样能获取到application.properties配置的属性.
        System.out.println(environment.getProperty("spring.datasource.secondary.url"));
    }
}
