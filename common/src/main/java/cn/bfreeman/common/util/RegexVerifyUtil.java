package cn.bfreeman.common.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author : lhr
 * @Date : 18:03 2019/4/3
 * <p>
 * 正则验证工具类
 */
public class RegexVerifyUtil {

    private static Map<RegexVerifyEnum, Pattern> regexCompileMap = new HashMap<>();

    static {
        /**
         * Pattern匹配预热作为常量
         */
        for (RegexVerifyEnum regex : RegexVerifyEnum.values()) {
            regexCompileMap.put(regex, Pattern.compile(regex.getValue()));
        }
    }

    public enum RegexVerifyEnum {
        /**
         * 正则表达式：验证手机号
         */
        REGEX_MOBILE("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$"),
        /**
         * 正则表达式：验证邮箱
         */
        REGEX_EMAIL("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

        @Getter
        private String value;

        RegexVerifyEnum(String value) {
            this.value = value;
        }
    }

    /**
     * 验证正则
     *
     * @param verifyEnum 正则匹配
     * @param targetStr  要验证的字符串
     * @return
     */
    public static boolean isValid(RegexVerifyEnum verifyEnum, String targetStr) {
        Pattern pattern = regexCompileMap.get(verifyEnum);
        return pattern.matcher(targetStr).find();
    }
}
