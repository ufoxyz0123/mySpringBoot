package cn.fzk.mySpringBoot.dto.primary.impl;

import cn.fzk.mySpringBoot.dto.primary.JdbcRoleDto;
import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.param.RoleParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/6/14.
 */
@Repository
public class JdbcRoleDtoImpl implements JdbcRoleDto {
    @Resource
    private JdbcTemplate jdbcTemplate;
    /**
     * @author fzk
     * @date 2018/8/24 16:30
     * @param
     * @return
     * 根据id查询
     */
    @Override
    public SysRole findById(Long id) {
        String sql = "select * from sys_role where  role_id= ?";
        RowMapper<SysRole> rowMapper = new BeanPropertyRowMapper<SysRole>(SysRole.class);
        return jdbcTemplate.queryForObject(sql, rowMapper,id);
    }
    /**
     * @author fzk
     * @date 2018/8/24 16:30
     * @param
     * @return
     * 获得全部
     */
    @Override
    public List<SysRole> getAll() {
        String sql = "select * from sys_role WHERE available = 0";
        RowMapper<SysRole> rowMapper = new BeanPropertyRowMapper<SysRole>(SysRole.class);
        return jdbcTemplate.query(sql, rowMapper);
    }
    /**
     * @author fzk
     * @date 2018/8/24 16:31
     * @param
     * @return
     * 获得总数
     */
    @Override
    public Integer findCount(RoleParam param) {
        StringBuffer buffer = new StringBuffer("select count(*) from sys_role where ");
        if(StringUtils.isNotBlank(param.getRole())){
            buffer.append(" role like '%"+param.getRole()+"%' and ");
        }
        if(StringUtils.isNotBlank(param.getDescription())){
            buffer.append(" description like '%"+param.getDescription()+"%' and ");
        }
        if(null != param.getAvailable()){
            buffer.append(" available = "+param.getAvailable()+" and ");
        }
        buffer.append("1=1");
        String sql = buffer.toString();
        return  jdbcTemplate.queryForObject(sql, Integer.class);
    }
    /**
     * @author fzk
     * @date 2018/8/24 16:30
     * @param
     * @return
     * 分页查询
     */
    @Override
    public List<SysRole> findPage(RoleParam param) {
        StringBuffer buffer = new StringBuffer("select * from sys_role where ");
        if(StringUtils.isNotBlank(param.getRole())){
            buffer.append(" role like '%"+param.getRole()+"%' and ");
        }
        if(StringUtils.isNotBlank(param.getDescription())){
            buffer.append(" description like '%"+param.getDescription()+"%' and ");
        }
        if(null != param.getAvailable()){
            buffer.append(" available = "+param.getAvailable()+" and ");
        }
        buffer.append("1=1");
        buffer.append(" limit "+ param.getOffset()+","+(param.getOffset()+param.getLimit()));
        String sql = buffer.toString();
        return jdbcTemplate.query(sql, new RowMapper<SysRole>() {
            @Nullable
            @Override
            public SysRole mapRow(ResultSet resultSet, int i) throws SQLException {
                SysRole sysRole = new SysRole();
                sysRole.setRoleId(resultSet.getLong("role_id"));
                sysRole.setRole(resultSet.getString("role"));
                sysRole.setDescription(resultSet.getString("description"));
                sysRole.setAvailable(resultSet.getByte("available"));
                return sysRole;
            }
        });
    }
    /**
     * @author fzk
     * @date 2018/8/27 10:27
     * @param
     * @return
     * 更改用户状态
     */
    @Override
    public void updateRole(RoleParam param) {
        Long roleId = param.getRoleId();
        StringBuffer buffer = new StringBuffer("update sys_role set ");
        AtomicInteger retryCount = new AtomicInteger(0);
        if(StringUtils.isNotBlank(param.getDescription())){
            buffer.append(" description = '"+param.getDescription()+"'");
            retryCount.getAndIncrement();
        }
        if(null != param.getAvailable()){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" available = "+ param.getAvailable());
            retryCount.getAndIncrement();
        }
        buffer.append(" where role_id = "+roleId);
        String sql = buffer.toString();
        jdbcTemplate.update(sql);
        if(param.getPermissionIds() != null){
            Long rid = param.getRoleId();
            String sqlperdel = new String("delete from sys_role_permission where role_id = ?");
            jdbcTemplate.update(sqlperdel,rid);
            String sqlroleins = new String("insert into sys_role_permission values(?,?)");
            List<String> perIds = Arrays.asList(param.getPermissionIds().split(","));
            perIds.stream().forEach((String perid)->{
                jdbcTemplate.update(sqlroleins,rid,perid);
            });
        }
    }
    /**
     * @author fzk
     * @date 2018/8/28 17:31
     * @param
     * @return
     * s删除角色
     */
    @Override
    public void delRole(RoleParam param) {
        String sql = new String("delete from sys_role where role_id = ?");
        jdbcTemplate.update(sql, param.getRoleId());
        sql = new String("delete from sys_role_permission where role_id = ?");
        jdbcTemplate.update(sql, param.getRoleId());
    }
}
