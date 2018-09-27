package cn.fzk.mySpringBoot.application;


import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/4/28.
 * 配置类
 */
@Configuration
@EnableSwagger2//启用Swagger2。
public class Application {
    /**
     * http服务端口
     */
    @Value("${server.http.port}")
    private Integer httpPort;
    /**
     * https服务端口
     */
    @Value("${server.port}")
    private Integer httpsPort;
    /**
     * tomcat将http重定向到https
     *
     * */
    /*@Bean
    public TomcatServletWebServerFactory servletContainer() { //springboot2 新变化

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("*//*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return connector;
    }*/
    /**
     * undertow服务器下http重定向到https
     */
    @Bean
    public ServletWebServerFactory undertowFactory() {
        UndertowServletWebServerFactory undertowFactory = new UndertowServletWebServerFactory();
        undertowFactory.addBuilderCustomizers((Undertow.Builder builder) -> {
            builder.addHttpListener(httpPort, "0.0.0.0");
            // 开启HTTP2
            builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
        });
        undertowFactory.addDeploymentInfoCustomizers(deploymentInfo -> {
            // 开启HTTP自动跳转至HTTPS
            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                    .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager(exchange -> httpsPort);
        });
        return undertowFactory;
    }
    /**
     *  开启WebSocket支持
     * */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    /**
     * 自定义mybatis的插件plugin
     * */
   /* @Bean
    public SQLStatsInterceptor sqlStatsInterceptor(){
        SQLStatsInterceptor sqlStatsInterceptor = new SQLStatsInterceptor();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        sqlStatsInterceptor.setProperties(properties);
        return sqlStatsInterceptor;
    }*/
    /**
     * 消息队列提供存放消息的对方（queue）
     * */
    @Bean
    public Queue fooQueue(){
        return new  Queue("foo");
    }

    //接收到消息处理.
    @RabbitListener//启用Rabbit队列监听foo key.
    public void onMessage(@Payload String foo){
        System.out.println(" >>> "+new Date() + ": " + foo);
    }
    /**
     * 会话区域解析器，只针对当前的会话有效，session失效，还原为默认状态
     * */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        //设置默认区域,
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }
    /**
     * 会话区域解析器，只对cookie生效，session失效
     * */
	/*@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver slr = new CookieLocaleResolver();
		//设置默认区域,
		slr.setDefaultLocale(Locale.CHINA);
		slr.setCookieMaxAge(3600);//设置cookie有效期.
		return slr;
	}*/
    /**
     * 一直使用固定的Local, 改变Local是不支持的
     * @return
     */
	/*@Bean
	public LocaleResolver localeResolver() {
		FixedLocaleResolver slr = new FixedLocaleResolver ();
		//设置默认区域,
		slr.setDefaultLocale(Locale.US);
		return slr;
	}*/
    /**
     * 将LocaleChangeInterceptor拦截器应用到处理程序映射中，它会发现当前HTTP请求中出现的特殊参数。
     * 其中的参数名称可以通过拦截器的paramName属性进行自定义。如果这种参数出现在当前请求中，拦截器就会根据参数值来改变用户的区域
     * 这个是可以和会话区域解析器以及Cookie区域解析器一起使用的，但是不能和FixedLocaleResolver一起使用，否则会抛出异常信息
     * */
    @Bean(name = "localeChangeInterceptor")
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 设置请求地址的参数,默认为：locale
        lci.setParamName(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
        return lci;
    }
    /**
     * swagger
     * */
    @SuppressWarnings("unchecked")
    @Bean
    public Docket devApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(devApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.fzk.mySpringBoot.serviceImpl"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo devApiInfo() {
        return new ApiInfoBuilder()
                .title("我的项目 APIs")
                .description("我的springboot项目api接口文档")
                .version("1.0")
                .build();
    }
}
