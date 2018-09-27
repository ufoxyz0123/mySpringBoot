package cn.fzk.mySpringBoot.dto.primary.impl;

import cn.fzk.mySpringBoot.dto.primary.JdbcUserDto;
import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import cn.fzk.mySpringBoot.param.UserParam;
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
 * Created by Administrator on 2018/4/13.
 */
@Repository
public class JdbcUserDtoImpl implements JdbcUserDto {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserInfo findByUserName(String userName) {
        String sql = "select * from user_info where user_name = ?";
        RowMapper<UserInfo> rowMapper = new BeanPropertyRowMapper<UserInfo>(UserInfo.class);
        return jdbcTemplate.queryForObject(sql, rowMapper,userName);
    }

    @Override
    public void updateUser(UserParam param) {
        Long uid = param.getUid();
        AtomicInteger retryCount = new AtomicInteger(0);
        StringBuffer buffer = new StringBuffer("update user_info set ");
        if(StringUtils.isNotBlank(param.getPassWord())){
            buffer.append("pass_word = '"+param.getPassWord()+"'");
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getNickName())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" nick_name = '"+param.getNickName()+"'");
            retryCount.getAndIncrement();
        }
        if(param.getStatus() != null){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" status = "+ param.getStatus());
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getRealName())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" real_name = '"+ param.getRealName()+"'");
            retryCount.getAndIncrement();
        }
        if(StringUtils.isNotBlank(param.getEmail())){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" email = '"+ param.getEmail()+"'");
            retryCount.getAndIncrement();
        }
        if(null != param.getPhone()){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" phone = "+ param.getPhone());
            retryCount.getAndIncrement();
        }
        if(null != param.getCertification()){
            if(retryCount.get() != 0){
                buffer.append(",");
            }
            buffer.append(" certification = "+ param.getCertification());
            retryCount.getAndIncrement();
        }
        buffer.append(" where uid = "+uid);
        String sql = buffer.toString();
        jdbcTemplate.update(sql);
        if(StringUtils.isNotBlank(param.getRoleIds())){
            String sqlroledel = new String("delete from sys_user_role where uid = ?");
            jdbcTemplate.update(sqlroledel,uid);
            String sqlroleins = new String("insert into sys_user_role values(?,?)");
            List<String> roleIds = Arrays.asList(param.getRoleIds().split(","));
            roleIds.stream().forEach((String roleid)->{
                jdbcTemplate.update(sqlroleins,uid,roleid);
            });
        }
    }
    /**
     * @author fzk
     * @date 2018/7/31 17:37
     * @param
     * @return
     * 查询总数
     */
    @Override
    public Integer findCount(UserParam param) {
        StringBuffer buffer = new StringBuffer("select count(*) from user_info where ");
        if(StringUtils.isNotBlank(param.getUserName())){
            buffer.append(" user_name like '%"+param.getUserName()+"%' and ");
        }
        if(null != param.getPhone()){
            buffer.append(" phone like '%"+param.getPhone()+"%' and ");
        }
        if(null != param.getStatus()){
            buffer.append(" status = "+param.getStatus()+" and ");
        }
        if(null != param.getCertification()){
            buffer.append(" certification = "+param.getCertification()+" and ");
        }
        if(StringUtils.isNotBlank(param.getCreateTimeBegin())){
            buffer.append(" create_time >= '"+param.getCreateTimeBegin()+"' and ");
        }
        if(StringUtils.isNotBlank(param.getCreateTimeEnd())){
            buffer.append(" create_time <= '"+param.getCreateTimeEnd()+"' and ");
        }
        buffer.append("1=1");
        String sql = buffer.toString();
        return  jdbcTemplate.queryForObject(sql, Integer.class);
    }
    /**
     * @author fzk
     * @date 2018/7/31 17:37
     * @param
     * @return
     * 分页查询
     */
    @Override
    public List<UserInfo> findPage(UserParam param) {
        StringBuffer buffer = new StringBuffer("select * from user_info where ");
        if(StringUtils.isNotBlank(param.getUserName())){
            buffer.append(" user_name like '%"+param.getUserName()+"%' and ");
        }
        if(null != param.getPhone()){
            buffer.append(" phone like '%"+param.getPhone()+"%' and ");
        }
        if(null != param.getStatus()){
            buffer.append(" status = "+param.getStatus()+" and ");
        }
        if(null != param.getCertification()){
            buffer.append(" certification = "+param.getCertification()+" and ");
        }
        if(StringUtils.isNotBlank(param.getCreateTimeBegin())){
            buffer.append(" create_time >= '"+param.getCreateTimeBegin()+"' and ");
        }
        if(StringUtils.isNotBlank(param.getCreateTimeEnd())){
            buffer.append(" create_time <= '"+param.getCreateTimeEnd()+"' and ");
        }
        buffer.append(" 1=1 ");
        buffer.append(" limit "+ param.getOffset()+","+(param.getOffset()+param.getLimit()));
        String sql = buffer.toString();
        return jdbcTemplate.query(sql, new RowMapper<UserInfo>() {
            @Nullable
            @Override
            public UserInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                UserInfo userInfo = new UserInfo();
                userInfo.setNickName(resultSet.getString("nick_name"));
                userInfo.setStatus(resultSet.getByte("status"));
                userInfo.setUid(resultSet.getLong("uid"));
                userInfo.setUserName(resultSet.getString("user_name"));
                userInfo.setRealName(resultSet.getString("real_name"));
                userInfo.setEmail(resultSet.getString("email"));
                userInfo.setCertification(resultSet.getByte("certification"));
                userInfo.setPhone(resultSet.getLong("phone"));
                userInfo.setCreateTime(resultSet.getDate("create_time"));
                return userInfo;
            }
        });
    }
}
