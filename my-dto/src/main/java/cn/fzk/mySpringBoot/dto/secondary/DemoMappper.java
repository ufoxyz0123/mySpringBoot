package cn.fzk.mySpringBoot.dto.secondary;

import cn.fzk.mySpringBoot.entity.secentity.Demo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fzk on 2018/4/22.
 * * @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
 * @Options注解中的工作就比较有意思，我们在插入记录时，一般是定义主键自增(auto_increment)，
 * KeyProperty是实体类中定义的主键字段名，KeyColumn是表中主键对应的字段；
 * 但是在某些情况下，我们插入一条记录后，还想得到这条记录的自增主键ID，useGeneratedKeys=true就是定义数据库返回主键ID的
 * useCache = true表示本次查询结果被缓存以提高下次查询速度，flushCache = false表示下次查询时不刷新缓存，timeout = 10000表示查询结果缓存10000秒。
 */
@Mapper
@Repository(value = "demoMappper")
public interface DemoMappper {
    public List<Demo> likeName(String name);
}
