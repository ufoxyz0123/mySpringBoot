package cn.fzk.mySpringBoot.param;



import java.util.Date;

/**
 * Created by Administrator on 2018/5/29.
 */
public class UserParam  extends PageParam{

    private Long uid;//用户id;
    private String userName;//账号.
    private String nickName;//名称（昵称或者真实姓名，不同系统不同定义）
    private String passWord; //密码;
    private String salt;//加密密码的盐
    private Byte status;//用户状态, 0:正常状态,1：用户被锁定.
    private Long phone;//电话
    private String realName;//真实姓名
    private String email;//电子邮箱
    private Long QQ;//QQ
    private Byte certification; //是否实名认证，0否，1是
    private Date createTime;
    /**
     * 开始时间
     * */
    private String createTimeBegin;
    /**
     * 结束时间
     * */
    private String createTimeEnd;
    /**
     * 角色参数
     * */
    private String roleIds;
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

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserParam{" +
                "userName='" + userName + '\'' +
                ", status=" + status +
                ", phone=" + phone +
                ", certification=" + certification +
                ", createTimeBegin='" + createTimeBegin + '\'' +
                ", createTimeEnd='" + createTimeEnd + '\'' +
                '}';
    }
}
