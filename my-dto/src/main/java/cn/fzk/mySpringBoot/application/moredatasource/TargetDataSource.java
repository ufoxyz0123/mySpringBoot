package cn.fzk.mySpringBoot.application.moredatasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/4/20.
 * 在方法上使用，用于指定使用哪个数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TargetDataSource {
    String value() default "primary";
}
