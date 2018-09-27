package cn.fzk.mySpringBoot.application;

import cn.fzk.mySpringBoot.application.servlet.MyInterceptor1;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 * 对springboot的一些配置
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurationSupport {
    @Resource
    private LocaleChangeInterceptor localeChangeInterceptor;
    /**
     * 自定义静态资源加载位置
     * 默认配置的 /** 映射到 /static （或/public、/resources、/META-INF/resources）
     * META/resources > resources > static > public
     * */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
          /*'classpath:/static/'这个参数可配多个，是可变参，代表将这个文件夹配置成addResourceHandler()中的访问地址*/
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        /**
         * 此为swagger的配置
         * */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
    /**
     * 实例化我们自定义的拦截器，然后将对像手动添加到spring拦截器链中，因此这个只能拦截spring的请求，自定义的servlet不能拦截
     * 将我们定义的拦截器添加入拦截器链
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor1()).addPathPatterns("/**");
        registry.addInterceptor(localeChangeInterceptor);
        super.addInterceptors(registry);

    }
    /**
     * setUseSuffixPatternMatch : 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配；
     * 当此参数设置为true的时候，那么/user.html，/user.aa，/user.*都能是正常访问的。
     * 当此参数设置为false的时候，那么只能访问/user或者/user/( 这个前提是setUseTrailingSlashMatch 设置为true了)。
     * setUseTrailingSlashMatch : 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认真即匹配；
     * 当此参数设置为true的会后，那么地址/user，/user/都能正常访问。
     * 当此参数设置为false的时候，那么就只能访问/user了。
     * 当以上两个参数都设置为true的时候，那么路径/user或者/user.aa，/user.*，/user/都是能正常访问的，但是类似/user.html/ 是无法访问的。
     * 当都设置为false的时候，那么就只能访问/user路径了
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false)
                .setUseTrailingSlashMatch(false);
    }
    /**
     * favorPathExtension表示支持后缀匹配，
     * 属性ignoreAcceptHeader默认为fasle，表示accept-header匹配，defaultContentType开启默认匹配。
     * 例如：请求aaa.xx，若设置<entry key="xx" value="application/xml"/> 也能匹配以xml返回。
     * 根据以上条件进行一一匹配最终，得到相关并符合的策略初始化ContentNegotiationManager
     * 关闭默认的后缀匹配规则，即使访问*.html,只要我们的配置是返回json那么就是json
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    /**
     * 使用fastjson替代自带的json
     * */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }
   /* @Bean //spring监听地址，相当于配置文件中的servlet-path
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        return new ServletRegistrationBean(dispatcherServlet,"/api*//*");
    }*/
}
