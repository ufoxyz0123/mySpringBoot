package cn.fzk.mySpringBoot.application.moredatasource;

/**
 * Created by Administrator on 2018/4/20.
 * 动态数据源上下文.
 */
public class DataSourceContextHolder {
    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = "primary";
    /*
    * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
    * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
    */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    // 设置数据源名
    public static void setDB(String dbType) {
        contextHolder.set(dbType);
    }
    // 获取数据源名
    public static String getDB() {
        return (contextHolder.get());
    }
    // 清除数据源名
    public static void clearDB() {
        contextHolder.remove();
    }
}
