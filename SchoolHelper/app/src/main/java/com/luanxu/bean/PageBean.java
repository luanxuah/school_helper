package com.luanxu.bean;

/**
 * @author: 范建海
 * @createTime: 2017/1/18 16:58
 * @className:  PageBean
 * @description: 涉及到分页实体Bean的基类
 * @changed by:
 */
public class PageBean extends Bean {
    // 当前第几页
    protected int page;
    // 总页数
    protected int total;
    // 当前页有多少条数据
    protected int records;

    public PageBean() {}

    public PageBean(int page, int total, int records) {
        this.page = page;
        this.total = total;
        this.records = records;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "page=" + page +
                ", total=" + total +
                ", records=" + records +
                '}';
    }
}
