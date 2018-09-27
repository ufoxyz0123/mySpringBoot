package cn.fzk.mySpringBoot.Util;

/**
 * 公共分页model封装
 *
 * @author chenjunchi
 * @since 2016-06-20
 */
public class PageResModel extends ResModel {

    /**
     * 记录总数
     */
    private int total;

    public PageResModel() {
    }

    public PageResModel(int code, String desc) {
        super(code, desc);
    }

    public PageResModel(int code, String desc, Object rows) {
        super(code, desc, rows);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
