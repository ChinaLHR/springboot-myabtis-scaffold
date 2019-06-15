package cn.bfreeman.common.enums;

/**
 * @Author : lhr
 * @Date : 13:54 2019/6/12
 * <p>
 * 统一枚举的 数据库存储而抽象出的行为 所有的枚举实现此类
 */
public interface EnumTrait {

    int getCode();

    String getText();
}
