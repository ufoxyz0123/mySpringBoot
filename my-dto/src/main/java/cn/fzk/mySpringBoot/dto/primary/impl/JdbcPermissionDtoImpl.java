package cn.fzk.mySpringBoot.dto.primary.impl;

import cn.fzk.mySpringBoot.dto.primary.JdbcPermissionDto;
import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.param.PermissionParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/8/27.
 */
@Repository
public class JdbcPermissionDtoImpl implements JdbcPermissionDto {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<SysPermission> getAll() {
        String sql = "select * from sys_permission WHERE available = 0";
        RowMapper<SysPermission> rowMapper = new BeanPropertyRowMapper<SysPermission>(SysPermission.class);
        return jdbcTemplate.query(sql, rowMapper);
    }
    /**
     * @author fzk
     * @date 2018/8/28 16:53
     * @param
     * @return
     * 获得总数
     */
    @Override
    public Integer findCount(PermissionParam param) {
        StringBuffer buffer = new StringBuffer("select count(*) from sys_permission where ");
        if(StringUtils.isNotBlank(param.getName())){
            buffer.append(" name like '%"+param.getName()+"%' and ");
        }
        if(StringUtils.isNotBlank(param.getResourceType())){
            buffer.append(" resource_type = '"+param.getResourceType()+"' and ");
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
    public List<SysPermission> findPage(PermissionParam param) {
        StringBuffer buffer = new StringBuffer("select * from sys_permission where ");
        if(StringUtils.isNotBlank(param.getName())){
            buffer.append(" name like '%"+param.getName()+"%' and ");
        }
        if(StringUtils.isNotBlank(param.getResourceType())){
            buffer.append(" resource_type = '"+param.getResourceType()+"' and ");
        }
        if(null != param.getParentId()){
            buffer.append(" parent_id = "+param.getParentId()+" and ");
        }
        buffer.append("1=1");
        if(param.getIsPage() == 0){
            buffer.append(" limit "+ param.getOffset()+","+(param.getOffset()+param.getLimit()));
        }
        String sql = buffer.toString();
        return jdbcTemplate.query(sql, new RowMapper<SysPermission>() {
            @Nullable
            @Override
            public SysPermission mapRow(ResultSet resultSet, int i) throws SQLException {
                SysPermission sysPermission = new SysPermission();
                sysPermission.setPermissionId(resultSet.getLong("permission_id"));
                sysPermission.setName(resultSet.getString("name"));
                sysPermission.setPermission(resultSet.getString("permission"));
                sysPermission.setResourceType(resultSet.getString("resource_type"));
                sysPermission.setUrl(resultSet.getString("url"));
                sysPermission.setAvailable(resultSet.getByte("available"));
                sysPermission.setParentId(resultSet.getLong("parent_id"));
                return sysPermission;
            }
        });
    }
    /**
     * @author fzk
     * @date 2018/8/29 10:24
     * @param
     * @return
     * 修改权限信息、状态
     */
    @Override
    public void updatePer(PermissionParam param) {
        Long permissionId = param.getPermissionId();
        StringBuffer buffer = new StringBuffer("update sys_permission set ");
        AtomicInteger retryCount = new AtomicInteger(0);
        if(null != param.getAvailable()){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" available = "+ param.getAvailable());
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getName())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" name = '"+ param.getName()+"'");
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getPermission())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" permission = '"+ param.getPermission()+"'");
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getUrl())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" url = '"+ param.getUrl()+"'");
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getResourceType())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" resource_type = '"+ param.getResourceType()+"'");
            retryCount.getAndIncrement();
        }
        buffer.append(" where permission_id = "+permissionId);
        String sql = buffer.toString();
        jdbcTemplate.update(sql);
    }
    /**
     * @author fzk
     * @date 2018/8/29 13:29
     * @param
     * @return
     * 根据id查找
     */
    @Override
    public SysPermission findById(Long id) {
        String sql = new String("select * from sys_permission where permission_id = ?");
        RowMapper<SysPermission> rowMapper = new BeanPropertyRowMapper<SysPermission>(SysPermission.class);
        return jdbcTemplate.queryForObject(sql, rowMapper,id);
    }
    /**
     * @author fzk
     * @date 2018/8/29 16:03
     * @param
     * @return
     * 删除权限
     */
    @Override
    public void delPer(PermissionParam param) {
        String sql = new String("delete from sys_permission where permission_id = ?");
        jdbcTemplate.update(sql, param.getPermissionId());
        sql = new String("delete from sys_role_permission where permission_id = ?");
        jdbcTemplate.update(sql, param.getPermissionId());
    }
}
