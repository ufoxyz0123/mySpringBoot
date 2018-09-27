package cn.fzk.mySpringBoot.application;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fzk on 2018/4/14.
 */
@Configuration
@ConditionalOnClass(Mongo.class)
/**
 * 表明该@Configuration仅仅在一定条件下才会被加载，这里的条件是Mongo.class位于类路径上
 * */
@EnableConfigurationProperties(MongoProperties.class)
/**
 * 将Spring Boot的配置文件（application.yml）中的spring.data.mongodb.*属性映射为MongoProperties并注入到MongoAutoConfiguration中注册为spring 容器中
 * */
/**
 * @ConditionalOnMissingBean(name = “redisTemplate”)这个注解的意思是如果容器中不存在name指定的bean则创建bean注入，否则不执行
 * */
public class MyEnvironmentAware2  {
    @Autowired
    private MongoProperties properties;
}
