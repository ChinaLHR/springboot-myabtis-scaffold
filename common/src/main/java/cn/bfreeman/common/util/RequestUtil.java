package cn.bfreeman.common.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author lhr
 * @date 2019/6/13
 *
 * Request处理相关的工具类
 */
public final class RequestUtil {

    /**
     * 获得真实IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotEmpty(ip)) {
            List<String> strings = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(ip);
            if (CollectionUtils.isNotEmpty(strings)) {
                return strings.get(0);
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据 HttpServletRequest 查询 header，并排除 x-access-token
     * @param request
     * @return
     */
    public static Map<String, Object> queryHeader(HttpServletRequest request) {
        Map<String, Object> headers = Maps.newHashMap();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();

            // 不打印的参数在此处过滤
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers;
    }
}
