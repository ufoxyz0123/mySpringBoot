package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2018/8/30.
 */
public interface JpaPermissionDto extends CrudRepository<SysPermission, Long> {
    SysPermission save(SysPermission permission);
}
