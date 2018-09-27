package cn.fzk.mySpringBoot.param;

/**
 * Created by Administrator on 2018/7/31.
 */
public class PageParam {
    /**
     * 分页大小
     * */
    private int limit = 20;
    /**
     * 索引位置 从0开始
     * */
    private int offset = 0;
    /**
     * asc或desc
     * */
    private String order;
    /**
     * 排序字段
     * */
    private String sort;
    /**
     * 当前页
     * */
    private int page = 1;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
