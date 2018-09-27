package cn.fzk.mySpringBoot.serviceImpl;


import cn.fzk.mySpringBoot.dto.primary.JdbcRoleDto;
import cn.fzk.mySpringBoot.dto.primary.JdbcUserDto;
import cn.fzk.mySpringBoot.dto.primary.JpaUserDto;
import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.entity.primaryentity.UserInfo;
import cn.fzk.mySpringBoot.service.UserService;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.UserParam;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/4/13.
 * @Cacheable 如果没有缓存则会执行方法并将返回值缓存，如果有缓存时，不会执行方法而是直接返回缓存中的值
 *              value：缓存名，也就是cacheManager中缓存的名字，可以指定多个  key：缓存的key，默认为空，既表示使用方法的参数类型及参数值作为
 *              keycondition：触发条件，只有满足条件的情况才会加入缓存，默认为空，既表示全部都加入缓存
 * @CachePut 不管有没有缓存都会执行方法并将结果缓存起来
 * @CacheEvict 移除指定缓存 注解的方法返回值可以为void allEntries：true表示清除value中的全部缓存，默认为false
 * @CacheConfig 类级别的缓存注解，允许共享缓存名称
 */
@Service
public class UserServiceImpl implements UserService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Autowired
    private JpaUserDto jpaUserDto;
    @Autowired
    private JdbcUserDto jdbcUserDao;
    @Autowired
    private JdbcRoleDto jdbcRoleDto;
    private long  expireTime  = 1800000;
    private String prefix = "userInfo:userInfoHandle";
    /**
     * redis模板
     * redisTemplate.opsForValue();//操作字符串
     * redisTemplate.opsForHash();//操作hash
     * redisTemplate.opsForList();//操作list
     * redisTemplate.opsForSet();//操作set
     * redisTemplate.opsForZSet();//操作有序set
     * */
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @author fzk
     * @date 2018/5/29 13:49
     * @param
     * @return
     * 新增用户，注意用户添加角色
     */
    @Override
    @Transactional
    public ResModel saveUser(UserParam param){
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("新增用户，参数param为->"+ JSON.toJSONString(param));
        try {
            String slat = UUID.randomUUID().toString().replaceAll("-","");
            String cipherText_md5= new SimpleHash("MD5",param.getPassWord(),ByteSource.Util.bytes(param.getUserName()+slat),2).toHex();
            UserInfo user = new UserInfo();
            BeanUtils.copyProperties(param,user);
            user.setStatus(Byte.parseByte("0"));
            user.setSalt(slat);
            user.setPassWord(cipherText_md5);
            SysRole sysRole = jdbcRoleDto.findById(2L);
            ArrayList<SysRole> list = new ArrayList<>();
            list.add(sysRole);
            user.setRoleList(list);
            user = jpaUserDto.save(user);//保存数据.
            logger.info("新增用户完成，结果result为->"+JSON.toJSONString(user));
            redisTemplate.opsForValue().set(prefix + param.getUserName(), user, expireTime, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("新增用户异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:50
     * @param
     * @return
     * 根据id删除，jpa实现
     */
    @Override
    @Transactional
    public ResModel deleteById(UserParam param) {
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("根据id删除用户，参数param为->"+ JSON.toJSONString(param));
        try {
            jpaUserDto.deleteById(param.getUid());
            redisTemplate.opsForValue().getOperations().delete(prefix +param.getUserName());
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("根据id删除用户异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:54
     * @param
     * @return
     * 修改用户信息
     */
    @Override
    @Transactional
    public ResModel update(UserParam param) {
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("修改用户信息，参数param为->"+ JSON.toJSONString(param));
        try {
            if(StringUtils.isNotBlank(param.getRealName())){
                param.setCertification(Byte.parseByte("1"));
            }
            jdbcUserDao.updateUser(param);
            UserInfo userInfo = (UserInfo)redisTemplate.opsForValue().get(prefix + param.getUserName());
            if(null != userInfo){
                if(StringUtils.isNotBlank(param.getNickName())){
                    userInfo.setNickName(param.getNickName());
                }
                if(param.getStatus() != null){
                  userInfo.setStatus(param.getStatus());
                }
                if(StringUtils.isNotBlank(param.getRealName())){
                   userInfo.setRealName(param.getRealName());
                   userInfo.setCertification(Byte.parseByte("1"));
                }
                if(StringUtils.isNotBlank(param.getEmail())){
                   userInfo.setEmail(param.getEmail());
                }
                if(null != param.getPhone()){
                    userInfo.setPhone(param.getPhone());
                }
                redisTemplate.opsForValue().set(prefix +param, userInfo,expireTime, TimeUnit.MILLISECONDS);
            }
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("修改用户信息异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:54
     * @param
     * @return
     * 修改用户密码
     */
    @Override
    @Transactional
    public ResModel updatePassword(UserParam param,UserInfo userInfo) {
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("修改用户密码，参数param为->"+ JSON.toJSONString(param));
        try {
            String cipherText_md5=  new SimpleHash("MD5",param.getPassWord(),ByteSource.Util.bytes(userInfo.getCredentialsSalt()),2).toHex();
            param.setPassWord(cipherText_md5);
            jdbcUserDao.updateUser(param);
            UserInfo cachUser = (UserInfo)redisTemplate.opsForValue().get(prefix + param.getUserName());
            if(null != cachUser){
                cachUser.setPassWord(param.getPassWord());
                redisTemplate.opsForValue().set(prefix +param, cachUser,expireTime, TimeUnit.MILLISECONDS);
            }
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("修改用户密码异常，具体异常："+e);
        }
        return resModel;
    }

    /**
     * @author fzk
     * @date 2018/5/29 13:46
     * @param username
     * @return UserInfo
     * 根据用户名查询用户，用jpa实现，shiro用
     */
    @Override
    public UserInfo findByUsername(String username) {
        logger.info("根据用户名查询用户，参数param为->"+ JSON.toJSONString(username));
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = (UserInfo)redisTemplate.opsForValue().get(prefix + username);
            if(null == userInfo){
                userInfo = jpaUserDto.findByUserName(username);
            }
            logger.info(" 根据用户名查询用户，结果result为->"+JSON.toJSONString(userInfo));
        }catch (Exception e){
            logger.error("根据用户名查询用户异常，具体异常："+e);
        }
        return userInfo;
    }
    /**
     * @author fzk
     * @date 2018/5/29 13:49
     * @param
     * @return
     * 根据id查找用户，jpa实现
     */
    @Override
    public ResModel findById(UserParam param) {
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("根据id查找用户，参数param为->"+ JSON.toJSONString(param));
        try {
            UserInfo userInfo = (UserInfo)redisTemplate.opsForValue().get(prefix + param.getUserName());
            if(null == userInfo){
                userInfo = jpaUserDto.findByUid(param.getUid());
            }
            resModel.setRows(userInfo);
            logger.info(" 根据id查找用户，结果result为->"+JSON.toJSONString(userInfo));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("根据id查找用户异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/7/31 16:16
     * @param
     * @return
     * 分页查询用户
     */
    @Override
    public PageResModel findPage(UserParam param) {
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        logger.info("分页查询用户，参数param为->"+ JSON.toJSONString(param));
        try {
            Integer count = jdbcUserDao.findCount(param);
            List<UserInfo> result = jdbcUserDao.findPage(param);
            resModel.setTotal(count);
            resModel.setRows(result);
            logger.info("分页查询角色完成，查询结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("分页查询用户异常，具体异常："+e);
        }
        return resModel;
    }

    @Override
    //@Async  异步调用 若多个任务本身之间不存在依赖关系，可以并发执行的话，可以使用本注解将多个任务同步执行，不需要依次执行
    public void test() {
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("mykey4", "random1="+Math.random());
        System.out.println(valueOperations.get("mykey4"));
    }
}
