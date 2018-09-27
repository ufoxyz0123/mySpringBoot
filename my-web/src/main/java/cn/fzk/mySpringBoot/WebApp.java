package cn.fzk.mySpringBoot;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//禁掉默认的数据源，因为我们要配置多数据源
/**
 * 等价于以默认属性使用 @Configuration ， @EnableAutoConfiguration 和 @ComponentScan
 *@EnableAutoConfiguration 尝试根据你添加的jar依赖自动配置你的Spring应用,
 * 可以将@EnableAutoConfiguration或者@SpringBootApplication注解添加到一个@Configuration类上来选择自动配置
 * 发现应用了你不想要的特定自动配置类，你可以使用@EnableAutoConfiguration注解的排除属性来禁用它
 * */
@ServletComponentScan
/**
 * 自动发现（扫描）并注册为Bean
 * 有basePackages={"cn.kfit","org.kfit"}这是改变扫描路径的，注意将默认的路径也扫描，要加进去
 * 主要是为自定义过滤器，监听器，servlet等加载
 * */
//@EnableAsync 使@Async注解生效，实现异步调用
public class WebApp {


	public static void main(String[] args) {
		 /**
         * Banner.Mode.OFF:关闭;
         * Banner.Mode.CONSOLE:控制台输出，默认方式;
         * Banner.Mode.LOG:日志输出方式;
         */
		SpringApplication application = new SpringApplication(WebApp.class);
		application.setBannerMode(Banner.Mode.OFF);
		/*************************************************************************/
		ApplicationContext ctx = SpringApplication.run(WebApp.class, args);
		//可以获取所有的beans，包括系统自动实例化的
		String[] beanNames =  ctx.getBeanDefinitionNames();
		//可以获取所有的service层beans
		String[] serviceBeanNames =  ctx.getBeanNamesForAnnotation(Service.class);
		//可以获取所有的dao层beans
		String[] repositoryBeanNames =  ctx.getBeanNamesForAnnotation(Repository.class);
	}
}
