package cn.bfreeman.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 参数化类型引用
 *
 * @author: xiang.rao  Date: 14-8-9 Time: 下午2:00
 * @version: \$Id$
 */
public abstract class ParameterizedTypeReference<T> {

    private final Type type;

    public ParameterizedTypeReference() {
        Type clazz = getClass().getGenericSuperclass();
        type = ((ParameterizedType) clazz).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }
}