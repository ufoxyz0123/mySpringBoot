package cn.fzk.mySpringBoot.application.servlet;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Administrator on 2018/6/22.
 * 自定义mybatis的插件plugin
 */
/**
 * （1）首先SQLStatsInterceptor类实现了接口Interceptor；
 * （2）需要重写3个方法，核心的拦截处理方法是intercept，在这个方法中可以获取到对应的绑定的sql，在这里作为演示只是打印了SQL，如果需要可以保存起来。
 * （3）在方法上有一个很重要的注解@Intercepts，在此注解上配置的注解说明了要拦截的类（type=StatementHandler.class），拦截的方法（method="prepare"），
 *      方法中的参数（args={Connection.class,Integer.class}），也就是此拦截器会拦截StatementHandler类中的如下方法：
 *      Statement prepare(Connection connection, Integer transactionTimeout)
 *      可以拦截的类：
 *      StatementHandler (prepare, parameterize, batch, update, query)
 *      ResultSetHandler (handleResultSets, handleOutputParameters)
 *      ParameterHandler (getParameterObject, setParameters)
 *      Executor (update, query, flushStatements, commit, rollback,getTransaction, close, isClosed)
 * */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class SQLStatsInterceptor implements Interceptor {
    /**
     * 定义自己的Interceptor最重要的是要实现plugin方法和intercept方法，在plugin方法中我们可以决定是否要进行拦截进而决定要返回一个什么样的目标对象。
     * 而intercept方法就是要进行拦截的时候要执行的方法。
     * */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler= (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        System.out.println(boundSql.getSql());
        return invocation.proceed();
    }
    /**
     *plugin方法是拦截器用于封装目标对象的，通过该方法我们可以返回目标对象本身，也可以返回一个它的代理。
     * 当返回的是代理的时候我们可以对其中的方法进行拦截来调用intercept方法，当然也可以调用其他方法。
     * */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }
    /**
     * setProperties方法是用于在Mybatis配置文件中指定一些属性的。
     * */
    @Override
    public void setProperties(Properties properties) {
        String dialect = properties.getProperty("dialect");
        System.out.println("dialect="+dialect);
    }
}
