package cn.fzk.mySpringBoot.serviceImpl;


import cn.fzk.mySpringBoot.dto.primary.JdbcRoleDto;
import cn.fzk.mySpringBoot.dto.primary.JpaRoleDto;
import cn.fzk.mySpringBoot.entity.primaryentity.SysPermission;
import cn.fzk.mySpringBoot.entity.primaryentity.SysRole;
import cn.fzk.mySpringBoot.service.RoleService;
import cn.fzk.mySpringBoot.Util.PageResModel;
import cn.fzk.mySpringBoot.Util.ResModel;
import cn.fzk.mySpringBoot.param.RoleParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2018/8/23.
 */
@Service
public class RoleServiceImpl implements RoleService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());//日志
    @Autowired
    JdbcRoleDto jdbcRoleDto;
    @Resource
    JpaRoleDto jpaRoleDto;
    /**
     * 获得所有角色
     * */
    @Override
    public List<SysRole> getAll() {
        logger.info("查询所有角色");
        return jdbcRoleDto.getAll();

    }
    /**
    * @author fzk
    * @date 2018/8/24 16:28
    * @param
    * @return
    * 分页查询
    */
    @Override
    public PageResModel findPage(RoleParam param) {
        logger.info("分页查询角色，参数param为->"+ JSON.toJSONString(param));
        PageResModel resModel = new PageResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            Integer count = jdbcRoleDto.findCount(param);
            List<SysRole> result = jdbcRoleDto.findPage(param);
            resModel.setTotal(count);
            resModel.setRows(result);
            logger.info("分页查询角色完成，查询结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("分页查询角色异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/27 9:44
     * @param
     * @return
     * 修改角色信息状态
     */
    @Override
    public ResModel update(RoleParam param) {
        logger.info("修改角色信息状态，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            jdbcRoleDto.updateRole(param);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("修改角色信息状态异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/27 15:14
     * @param
     * @return
     * 根据id查找 jpa实现
     */
    @Override
    public ResModel findById(RoleParam param) {
        logger.info("根据id查找角色，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            SysRole result = jpaRoleDto.findByRoleId(param.getRoleId());
            resModel.setRows(result);
            logger.info("根据id查找角色，结果result为->"+JSON.toJSONString(result));
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("根据id查找角色异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/28 15:39
     * @param
     * @return
     * 角色新增
     */
    @Override
    public ResModel save(RoleParam param) {
        logger.info("角色新增，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            SysRole sysRole = new SysRole();
            BeanUtils.copyProperties(param,sysRole);
            String preIds = param.getPermissionIds();
            if(StringUtils.isNotBlank(preIds)){
                List<SysPermission> list = Stream.of(preIds.split(",")).map(c->{
                    SysPermission sysPermission = new SysPermission();
                    sysPermission.setPermissionId(Long.parseLong(c));
                    return sysPermission;
                }).collect(Collectors.toList());
                sysRole.setPermissions(list);
            }
            jpaRoleDto.save(sysRole);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("角色新增异常，具体异常："+e);
        }
        return resModel;
    }
    /**
     * @author fzk
     * @date 2018/8/28 17:28
     * @param
     * @return
     * 删除角色
     */
    @Override
    public ResModel delRole(RoleParam param) {
        logger.info("删除角色，参数param为->"+JSON.toJSONString(param));
        ResModel resModel = new ResModel(ResModel.RespCode.SUCCESS.getCode(),ResModel.RespCode.SUCCESS.getDesc());
        try {
            jdbcRoleDto.delRole(param);
        }catch (Exception e){
            resModel.setCode(ResModel.RespCode.SYS_EXCEPTION.getCode());
            resModel.setDesc(ResModel.RespCode.SYS_EXCEPTION.getDesc());
            logger.error("删除角色，具体异常："+e);
        }
        return resModel;
    }

    /**
     * @author fzk
     * @date 2018/8/28 10:10
     * @param
     * @return
     * 组装ztree可是识别的数据
     */
    @Override
    public JSONArray buildRoleMenuTreeData(List<SysPermission> allMenus, List<SysPermission> roleMenus) {
        JSONArray arr = new JSONArray();
        for (SysPermission menu : allMenus) {
            JSONObject jobj = new JSONObject();
            jobj.put("id", menu.getPermissionId());
            if (null == menu.getParentId()) {
                jobj.put("pId", 0);
            } else {
                jobj.put("pId", menu.getParentId());
            }
            jobj.put("name", menu.getName());
            jobj.put("open", true);
            if(roleMenus != null){
                jobj.put("checked", roleMenus.contains(menu));
            }
            logger.info("组装ztree识别的数据参数：jobj = " +JSON.toJSONString(jobj));
            arr.add(jobj);

        }
        return arr;
    }


}
