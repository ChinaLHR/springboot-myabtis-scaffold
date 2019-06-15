package cn.bfreeman.core.domain.enums;

import cn.bfreeman.common.enums.EnumTrait;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author : lhr
 * @Date : 16:11 2019/6/14
 */
public enum SexType implements EnumTrait {

    FEMALE(0, "女"), MALE(1, "男"), UNKOWN(-1, "未知");
    @Getter
    private int code;
    @Getter
    private String text;

    private static final Map<Integer, SexType> INDEX = Maps.uniqueIndex(Arrays.asList(values()), SexType::getCode);

    SexType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static SexType codeOf(int code) {
        return INDEX.get(code);
    }
}
