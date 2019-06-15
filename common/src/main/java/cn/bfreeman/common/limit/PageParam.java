package cn.bfreeman.common.limit;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

/**
 * @author xiang.rao created on 5/17/18 10:58 AM
 * @version $Id$
 */
@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 2974907617606472516L;
    /**
     * 开始number
     */
    private Integer pageNum;

    /**
     * 使用offset 做批量处理时, 查询分页大小
     */
    private static final int OFFSET_BATCH_PROCESS_QUERY_PAGE_SIZE = 200;

    /**
     * 使用offset 前端请求时, 默认分页大小
     */
    private static final int OFFSET_FRONTEND_QUERY_PAGE_SIZE = 10;

    /**
     * 每页大小
     */
    private Integer pageSize;

    public Limiter getLimiter() {
        Preconditions.checkNotNull(this.getPageNum(), "页号为空");
        Preconditions.checkNotNull(this.getPageSize(), "分页大小为空");
        Limiter limiter = new Limiter(this.getPageNum(), getPageSize());
        return limiter;

    }

    /**
     * 设置批处理时的分页参数
     */
    public void withBatchProcess() {
        this.pageSize = OFFSET_BATCH_PROCESS_QUERY_PAGE_SIZE;
        this.pageNum = 1;
    }


    public static PageParam createDefault() {
        PageParam pageParam = new PageParam();
        pageParam.setPageNum(NumberUtils.INTEGER_ONE);
        pageParam.setPageSize(OFFSET_FRONTEND_QUERY_PAGE_SIZE);
        return pageParam;
    }

    public static PageParam createDefault(Integer pageSize) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNum(NumberUtils.INTEGER_ONE);
        pageParam.setPageSize(pageSize);
        return pageParam;
    }

}
