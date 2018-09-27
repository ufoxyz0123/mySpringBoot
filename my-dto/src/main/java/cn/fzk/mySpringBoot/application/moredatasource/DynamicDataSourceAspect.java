package cn.fzk.mySpringBoot.application.moredatasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * Created by Administrator on 2018/4/20.
 * 切换数据源Advice
 */
@Aspect
@Order(3)//保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

    /*
    * @Before("@annotation(ds)")
    * 的意思是：
    * @Before：在方法执行之前进行执行：
    * @annotation(targetDataSource)：
    * 会拦截注解targetDataSource的方法，否则不拦截;
    */
    @Before("@annotation(targetDataSource)")
    public void beforeSwitchDS(JoinPoint point,TargetDataSource targetDataSource){
        //设置到动态数据源上下文中。
        DataSourceContextHolder.setDB(targetDataSource.value());
    }
    @After("@annotation(targetDataSource)")
    public void afterSwitchDS(JoinPoint point,TargetDataSource targetDataSource){
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DataSourceContextHolder.clearDB();
    }
}
