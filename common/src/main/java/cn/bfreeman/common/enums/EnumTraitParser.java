package cn.bfreeman.common.enums;

import cn.bfreeman.common.exception.FatalException;
import com.google.common.base.Preconditions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : lhr
 * @Date : 17:20 2019/6/12
 */
public class EnumTraitParser {
    private static Map<Class, Method> methodCache = new HashMap<Class, Method>();

    public static <T extends EnumTrait> T codeOf(Class<T> enumTraitClz, int code) {
        Method method;
        synchronized (EnumTrait.class) {
            method = methodCache.get(enumTraitClz);
            if (method == null) {
                try {
                    method = enumTraitClz.getMethod("codeOf", int.class);
                    Preconditions.checkArgument(Modifier.isStatic(method.getModifiers()), "Static method %s#codeOf(int code) required.");
                    Class<?> returnType = method.getReturnType();
                    Preconditions.checkArgument(returnType != null && enumTraitClz.isAssignableFrom(returnType), "invalid return type of %s#codeOf", returnType.getName());
                    methodCache.put(enumTraitClz, method);
                } catch (NoSuchMethodException e) {
                    throw new FatalException("method %s#codeOf does not exist", enumTraitClz.getName(), e);
                }
            }
        }

        try {
            // invoke static method
            return (T) method.invoke(null, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
