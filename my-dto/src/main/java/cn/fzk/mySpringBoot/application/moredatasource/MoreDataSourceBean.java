package cn.fzk.mySpringBoot.application.moredatasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by fzk on 2018/4/19.
 * 手动将数据源实例化出来
 */
@Configuration
public class MoreDataSourceBean {

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;
    @Value("${mybatis.mapper-locations}")
    private String locations;

    @Bean(name = "primary")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary") // application.properteis中对应属性的前缀
    public DataSource primary() {
        return  DruidDataSourceBuilder.create().build();
    }
    @Bean(name = "secondary")
    @ConfigurationProperties(prefix = "spring.datasource.secondary") // application.properteis中对应属性的前缀
    public DataSource secondary() {
        return  DruidDataSourceBuilder.create().build();
    }
    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(primary());
        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap();
        dsMap.put("primary", primary());
        dsMap.put("secondary", secondary());
        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }
    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);// 指定数据源(这个必须有，否则报错)
        /*pageHelper分页插件*/
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        Interceptor interceptor = new PageInterceptor();
        interceptor.setProperties(properties);
        factoryBean.setPlugins(new Interceptor[] {interceptor});
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        factoryBean.setTypeAliasesPackage(typeAliasesPackage);// 指定基包
        // 指定mybatisxml文件路径
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(locations));

        return factoryBean.getObject();
    }
    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
