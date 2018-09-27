package cn.fzk.mySpringBoot.serviceImpl;

import cn.fzk.mySpringBoot.dto.primary.JdbcPermissionDto;
import cn.fzk.mySpringBoot.dto.primary.JpaPermissionDto;
import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.service.PermissionService;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.PermissionParam;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/27.
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Autowired
    JdbcPermissionDto jdbcPermissionDto;
    @Autowired
    JpaPermissionDto jpaPermissionDto;
    /**
     * 获得所有权限
     * */
    @Override
    public List<SysPermission> getAll() {
        logger.info("查询所有权限");
        return jdbcPermissionDto.getAll();
    }
    /**
     * @author fzk
     * @date 2018/8/28 16:49
     * @param
     * @return
     * 分页查询权限
     */
    @Override
    public PageResModel findPage(PermissionParam param) {
        logger.info("分页查询权限，参数param为->"+ JSON.toJSONString(param));
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            Integer count = jdbcPermissionDto.findCount(param);
            List<SysPermission> result = jdbcPermissionDto.findPage(param);
            resModel.setTotal(count);
            resModel.setRows(result);
            logger.info("分页查询权限完成，查询结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("分页查询权限异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/29 10:24
     * @param
     * @return
     * 修改权限信息、状态
     */
    @Override
    public ResModel update(PermissionParam param) {
        logger.info("修改权限信息、状态，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            jdbcPermissionDto.updatePer(param);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("修改权限信息、状态异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/29 13:27
     * @param
     * @return
     * 根据id查找权限
     */
    @Override
    public ResModel findById(PermissionParam param) {
        logger.info("根据id查找权限，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            SysPermission result = jdbcPermissionDto.findById(param.getPermissionId());
            resModel.setRows(result);
            logger.info("根据id查找权限，结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("根据id查找权限异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/29 17:15
     * @param
     * @return
     * 条件查询权限
     */
    @Override
    public ResModel findByParam(PermissionParam param) {
        logger.info("条件查询权限，参数param为->"+ JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            List<SysPermission> result = jdbcPermissionDto.findPage(param);
            resModel.setRows(result);
            logger.info("条件查询权限完成，查询结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("条件查询权限异常，具体异常："+e);
        }
        return resModel;
    }

    /**
     * @author fzk
     * @date 2018/8/29 16:03
     * @param
     * @return
     * 删除权限
     */
    @Override
    public ResModel delPer(PermissionParam param) {
        logger.info("删除权限，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            jdbcPermissionDto.delPer(param);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("删除权限，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/30 9:49
     * @param
     * @return
     * 新建权限
     */
    @Override
    public ResModel save(PermissionParam param) {
        logger.info("新建权限，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            SysPermission sysPermission = new SysPermission();
            if(param.getParentId() == null){
                param.setParentId(0L);
                param.setParentIds("0/");
            }else {
                param.setParentIds("0/"+param.getParentId());
            }
            BeanUtils.copyProperties(param,sysPermission);
            SysPermission result = jpaPermissionDto.save(sysPermission);
            logger.info("新建权限，保存结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("新建权限异常，具体异常："+e);
        }
        return resModel;
    }
}
