package cn.fzk.mySpringBoot.entity.primaryentity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/8.
 * 定时任务类
 */
@Entity
public class Cron  implements Serializable{

    @Id
    @GeneratedValue
    private String cronId;
    /**
     * cron
     */
    private String cron;

    /**
     * 状态("0":有效   "1":无效)
     */
    private Integer status;

    public String getCronId() {
        return cronId;
    }

    public void setCronId(String cronId) {
        this.cronId = cronId;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
