package cn.bfreeman.core.config.web;


import cn.bfreeman.common.enums.EnumTrait;
import cn.bfreeman.common.enums.EnumTraitParser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;

public class IntegerToEnumTraitConverterFactory implements ConverterFactory<Integer, EnumTrait> {
    @Override
    public <T extends EnumTrait> Converter<Integer, T> getConverter(Class<T> aClass) {
        return new Converter<Integer, T>() {
            @Nullable
            @Override
            public T convert(Integer integer) {
                return EnumTraitParser.codeOf(aClass, integer);
            }
        };
    }
}
