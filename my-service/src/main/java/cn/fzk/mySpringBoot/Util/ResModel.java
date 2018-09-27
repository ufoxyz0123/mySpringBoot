package cn.fzk.mySpringBoot.Util;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/24.
 */
public class ResModel implements Serializable{

    public enum RespCode {

        SUCCESS(0, "操作成功"),
        SYS_EXCEPTION(-1, "系统异常"),
        NO_PRIVILEGE(-2, "没有权限"),
        PARAM_EXCEPTION(-3, "参数异常"),
        TIP_CONFIRM(-4,"确认提示信息"),
        TIP_WARN(-5, "警告提示信息"),
        LOGIN_WARN(-6, "登录提示信息"),
        AUDITED(1, "审核通过"),
        PENDING(2, "待审核");

        private final int    code;

        private final String desc;

        RespCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public boolean isSuccess() {
        return RespCode.SUCCESS.getCode() == code;
    }

    /**
     * 响应码
     */
    private int    code = RespCode.SUCCESS.getCode();

    /**
     * 响应码描述
     */
    private String desc = RespCode.SUCCESS.getDesc();

    /**
     * 具体数据
     */
    private Object rows;

    public ResModel() {
    }

    public ResModel(Object rows) {
        this.rows = rows;
    }

    public ResModel(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ResModel(int code, String desc, Object rows) {
        this.code = code;
        this.desc = desc;
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
