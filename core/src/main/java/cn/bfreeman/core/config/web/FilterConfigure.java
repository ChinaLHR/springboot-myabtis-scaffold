package cn.bfreeman.core.config.web;

import cn.bfreeman.core.common.validate.Validator;
import cn.bfreeman.core.filter.GlobalWebExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * @author lhr
 * @date 2019/6/14
 */
@Configuration
public class FilterConfigure {

    @Resource
    private MessageSource messageSource;

    @Resource
    private Validator validator;

    @Bean(value = "exception-filter")
    public FilterRegistrationBean exceptionFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter filter = globalExceptionFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("exception-filter");
        registration.setOrder(1);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        return registration;
    }

    private Filter globalExceptionFilter() {
        GlobalWebExceptionFilter globalExceptionFilter = new GlobalWebExceptionFilter();
        // 用于解析bindException 返回相应的提示
        globalExceptionFilter.setMessageSource(messageSource);
        globalExceptionFilter.setValidator(validator);
        globalExceptionFilter.setResolveBindingException(true);
        return globalExceptionFilter;
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

}
