package cn.fzk.mySpringBoot.application;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Administrator on 2018/5/25.
 * @Conditional(WindowCondition.class) 代表当matches（）返回true的时候@Conditional所注解的类或者方法就会执行
 * @ConditionalOnBean 仅仅在当前上下文中存在某个对象时，才会实例化一个Bean。
 * @ConditionalOnClass 某个class位于类路径上，才会实例化一个Bean
 * @ConditionalOnExpression 当表达式为true的时候，才会实例化一个Bean。 如：@ConditionalOnExpression("true") @ConditionalOnExpression("${my.serviceImpl.enabled:false}")
 * @ConditionalOnMissingBean 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean
 * @ConditionalOnMissingClass 某个class类路径上不存在的时候，才会实例化一个Bean
 * @ConditionalOnNotWebApplication 不是web应用
 */
public class WindowCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // conditionContext.getEnvironment().getProperty("");  可以获得配置文件里的信息
        return conditionContext.getEnvironment().getProperty("os.name").contains("Windows");
    }
}
