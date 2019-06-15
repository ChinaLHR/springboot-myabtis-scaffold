package cn.bfreeman.core.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author lhr
 * @date 2019/6/13
 *
 * todo 枚举转换器
 */
@Configuration
public class WebMvcConfg  extends WebMvcConfigurationSupport {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        // 去除默认的exceptionResolvers， 改用filter去做日志的处理。
        exceptionResolvers.clear();
        // 全局异常捕获 因为使用了filter,暂时不需要处理
//        GlobalWebExceptionHandler globalExceptionHandler = new GlobalWebExceptionHandler();
//        exceptionResolvers.add(0, globalExceptionHandler);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumTraitConverterFactory());
        registry.addConverterFactory(new IntegerToEnumTraitConverterFactory());
    }


}
