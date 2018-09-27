package cn.fzk.mySpringBoot.application.ehcache;

/**
 * Created by Administrator on 2018/5/30.
 */
/*@Configuration
@EnableCaching*///标注启动缓存.
public class CacheConfiguration {
    /*
      * 据shared与否的设置,
      * Spring分别通过CacheManager.create()
      * 或new CacheManager()方式来创建一个ehcache基地.
      * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
      */
   /* @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("conf/ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }*/
    /**
     *  ehcache 主要的管理器
     * @param bean
     * @return
     */
   /* @Bean(name = "ehCacheManager")
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean){
        return new EhCacheCacheManager(bean.getObject());
    }*/
}
