
package cn.bfreeman.common.limit;


import java.io.Serializable;

/**
 * @author: xiaofeng.ma  Date: 14-8-8 Time: 上午11:13
 * @version: \$Id$
 */
public class Limiter implements Serializable {
    private static final long serialVersionUID = 4543745377452260484L;

    /**
     * 查询结果条目上限
     */
    public static int MAX_PAGE_SIZE = 200;

    /**
     * 页号
     */
    private int pageNum;
    /**
     * 查询起始位置编号
     */
    private int offset;
    /**
     * 查询的结果集条目数
     */
    private int pageSize;

    public Limiter() {
    }

    /**
     * @param pageNum  查询起始位置编号
     * @param pageSize 查询的结果集条目数
     */
    public Limiter(int pageNum, int pageSize) {
        this(pageNum, pageSize, true);
    }

    /**
     * @param pageNum            起始位置编号
     * @param pageSize           查询的结果集条目数
     * @param isLimitMaxPageSize 是否限制查询结果集最大条目数
     */
    public Limiter(int pageNum, int pageSize, boolean isLimitMaxPageSize) {

        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException("分页参数错误! pageNum: " + pageNum + ", pageSize:" + pageSize);
        }

        this.pageNum = pageNum;
        this.pageSize = (isLimitMaxPageSize && pageSize > MAX_PAGE_SIZE) ? MAX_PAGE_SIZE : pageSize;
        this.offset = (pageNum - 1) * this.pageSize;
    }

    /**
     * 默认限制查询结果集上限
     *
     * @param pageNum 起始位置编号
     * @param rowNum  查询的结果集条目数
     * @return
     */
    public static Limiter create(int pageNum, int rowNum) {
        return new Limiter(pageNum, rowNum);
    }

    /**
     * @param pageNum            起始位置编号
     * @param rowNum             查询的结果集条目数
     * @param isLimitMaxPageSize 是否限制查询结果集最大条目数
     * @return
     */
    public static Limiter create(int pageNum, int rowNum, boolean isLimitMaxPageSize) {
        return new Limiter(pageNum, rowNum, isLimitMaxPageSize);
    }

    /**
     * 获取页号
     *
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 获取查询起始位置编号
     *
     * @return
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * 获取查询的结果集条目数
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

}
