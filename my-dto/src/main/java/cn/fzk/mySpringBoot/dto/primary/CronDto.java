package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.Cron;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2018/5/8.
 */
public interface CronDto extends CrudRepository<Cron, Long> {
}
