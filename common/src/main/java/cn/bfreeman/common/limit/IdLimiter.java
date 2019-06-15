package cn.bfreeman.common.limit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 查询ID后面指定pageSize大小的分页
 *
 * @author xiang.rao created on 2/4/18 12:11 PM
 * @version $Id$
 */
@ToString
@Accessors(chain = true)
@ApiModel(value = "IdLimiter", description = "分页参数")
@EqualsAndHashCode
public class IdLimiter {

    /**
     * id的起点， 查询结果中不包括该ID对应的数据
     * 该值为0时表示查询第一页
     */
    @NotNull
    @Range(min = 0L)
    @Getter
    @Setter
    @ApiModelProperty(name = "beginId",
            value = "id的起点， 查询结果中不包括该ID对应的数据, 该值为0时表示查询第1页. " +
                    "如果想查询第2页, beginId是第一页的最后一条记录的id。 查询动态时送动态ID",
            required = true)
    private Long beginId;

    public static final long BEGIN_ID_FIRST_PAGE = 0;

    public static IdLimiter build() {
        IdLimiter idLimiter = new IdLimiter();
        idLimiter.setBeginId(BEGIN_ID_FIRST_PAGE);
        idLimiter.setPageSize(PAGE_SIZE_DEFAULT);
        return idLimiter;
    }

    public static IdLimiter build(Integer pageSize) {
        IdLimiter idLimiter = new IdLimiter();
        idLimiter.setBeginId(BEGIN_ID_FIRST_PAGE);
        idLimiter.setPageSize(pageSize);
        return idLimiter;
    }

    private static final int PAGE_SIZE_DEFAULT = 10;


    /**
     * 暂时不支持前端传入， 后端会写死
     */
    @Setter
    @Max(200)
    private Integer pageSize;


    public Integer getPageSize() {
        if (Objects.isNull(this.pageSize)) {
            return PAGE_SIZE_DEFAULT;
        }
        return this.pageSize;

    }

    public boolean firstPage() {
        return BEGIN_ID_FIRST_PAGE == getBeginId();
    }
}
