package cn.fzk.mySpringBoot.entity.primaryentity;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 * 权限表
 */
@Entity
public class SysPermission implements Serializable {
    @Id
    @GeneratedValue
    private Long permissionId;//主键.
    private String name;//名称.

    @Column(columnDefinition="enum('menu','button')",name="resource_type")
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(name = "parent_id")
    private Long parentId; //父编号
    @Column(name = "parent_ids")
    private String parentIds; //父编号列表
    private Byte available = 0; // 是否可用,如果不可用将不会添加给用户 0可用 1不可用

    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    @JSONField(serialize=false)
    private List<SysRole> roles;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Byte getAvailable() {
        return available;
    }

    public void setAvailable(Byte available) {
        this.available = available;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysPermission that = (SysPermission) o;

        return permissionId != null ? permissionId.equals(that.permissionId) : that.permissionId == null;
    }

    @Override
    public int hashCode() {
        return permissionId != null ? permissionId.hashCode() : 0;
    }
}
