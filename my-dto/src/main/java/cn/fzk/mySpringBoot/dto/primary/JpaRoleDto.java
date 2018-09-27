package cn.fzk.mySpringBoot.dto.primary;

import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2018/8/27.
 */
public interface JpaRoleDto extends CrudRepository<SysRole, Long> {
    SysRole findByRoleId(Long rid);
}
