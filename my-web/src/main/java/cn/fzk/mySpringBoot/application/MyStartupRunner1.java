package cn.fzk.mySpringBoot.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/30.
 * 服务器启动的执行
 * 实现ApplicationRunner接口也可以 两者的区别就在ApplicationRunner中run方法的参数为ApplicationArguments
 */
@Component
@Order(value=-1)//执行顺序，执行顺序，从小到大执行
public class MyStartupRunner1 implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作,想干啥就加进来111<<<<<<<<<<<<<");
    }
}
