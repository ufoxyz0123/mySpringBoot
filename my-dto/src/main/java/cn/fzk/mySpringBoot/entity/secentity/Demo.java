package cn.fzk.mySpringBoot.entity.secentity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by fzk on 2018/4/22.
 */
@Entity
public class Demo implements Serializable{
    @Id
    @GeneratedValue//可选择生成策略注解
    private long id;
    @JSONField(serialize=false)//使用fastJson前台不返回此值，@JSONField(format="yyyy-MM-dd HH:mm:ss")可以直接对时间格式化
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
