package cn.bfreeman.core.config.web;

import cn.bfreeman.common.enums.EnumTrait;
import cn.bfreeman.common.enums.EnumTraitParser;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;

public class StringToEnumTraitConverterFactory implements ConverterFactory<String, EnumTrait> {
    @Override
    public <T extends EnumTrait> Converter<String, T> getConverter(Class<T> aClass) {
        return new Converter<String, T>() {
            @Nullable
            @Override
            public T convert(String integer) {
                if (NumberUtils.isDigits(integer)) {
                    return EnumTraitParser.codeOf(aClass, Integer.parseInt(integer));
                }
                throw new IllegalArgumentException("EnumTrait param should be int ");


            }
        };
    }
}
