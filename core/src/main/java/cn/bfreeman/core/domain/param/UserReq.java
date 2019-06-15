package cn.bfreeman.core.domain.param;

import cn.bfreeman.core.domain.enums.SexType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author : lhr
 * @Date : 17:31 2019/6/14
 */
@Data
public class UserReq {

    @NotBlank
    private String name;

    /**
     * 性别. -1,未知; 0,女; 1,男
     */
    @NotNull
    private SexType sexType;
}
