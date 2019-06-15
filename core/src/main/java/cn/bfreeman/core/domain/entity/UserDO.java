package cn.bfreeman.core.domain.entity;

import cn.bfreeman.core.domain.enums.SexType;
import lombok.Data;

import java.util.Date;

/**
 * @Author : lhr
 * @Date : 16:58 2019/6/14
 */
@Data
public class UserDO {
    private Long id;

    private String name;

    /**
     * 性别. -1,未知; 0,女; 1,男
     */
    private SexType sexType;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
