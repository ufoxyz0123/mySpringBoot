package cn.fzk.mySpringBoot.entity.primaryentity;


import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 * 用户表
 */
@Entity
public class UserInfo implements Serializable {
    /**
     * @GeneratedValue 注解主要就是为一个实体生成一个唯一标识的主键，提供了主键的生成策略
     * strategy 属性
     * GenerationType.TABLE：使用一个特定的数据库表格来保存主键。
     * GenerationType.SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     * GenerationType.IDENTITY：主键由数据库自动生成（主要是自动增长型）
     * GenerationTypeAUTO：主键由程序控制。（默认）
     * generator属性 的值是一个字符串,默认为"",其声明了主键生成器的名称
     * */
    @Id
    @GeneratedValue
    private Long uid;//用户id;
    @Column(unique=true,name = "user_Name") //是指这个字段的值在这张表里不能重复，所有记录值都要唯一，就像主键那样
    private String userName;//账号.
    @Column(name = "nick_name")
    private String nickName;//名称（昵称或者真实姓名，不同系统不同定义）
    @Column(name = "pass_word")
    private String passWord; //密码;
    private String salt;//加密密码的盐
    private Byte status = 0;//用户状态, 0:正常状态,1：用户被锁定.
    private Long phone;//电话
    @Column(name = "real_name")
    private String realName;//真实姓名
    private String email;//电子邮箱
    private Long QQ;//QQ
    private Byte certification = 0; //是否实名认证，0否，1是
    @Column(name = "create_time")
    private Date createTime;
    @ManyToMany(fetch=FetchType.EAGER)//表示取出这条数据时，它关联的数据也同时取出放入内存中 与lazy相反
    //name ：属性指定中间表名称
    //joinColumns:定义中间表与本表的外键关系
    //JoinColumn :本表中指向中间表的外键，此处就是uid是外键
    //inverseJoinColumns:属性定义了中间表与另外一端的外键关系
    //JoinColumn：另一表中roleId是外键
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    @JSONField(serialize=false)
    private List<SysRole> roleList;// 一个用户具有多个角色
    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.userName+this.salt;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getQQ() {
        return QQ;
    }

    public void setQQ(Long QQ) {
        this.QQ = QQ;
    }

    public Byte getCertification() {
        return certification;
    }

    public void setCertification(Byte certification) {
        this.certification = certification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserParam{" +
                "userName='" + userName + '\'' +
                '}';
    }
}

