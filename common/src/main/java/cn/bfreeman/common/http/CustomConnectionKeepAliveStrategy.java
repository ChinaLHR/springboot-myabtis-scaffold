package cn.bfreeman.common.http;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;

/**
 * @Author : lhr
 * @Date : 18:41 2018/11/2
 */
public class CustomConnectionKeepAliveStrategy {

    private static final long DEFAULT_TIMEOUT = 20 * 1000;

    public static final ConnectionKeepAliveStrategy INSTANCE = getConnectionKeepAliveStrategy();

    private static ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        return (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator
                    (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    if (NumberUtils.isCreatable(value)) {
                        return NumberUtils.createLong(value) * 1000;
                    }
                    return DEFAULT_TIMEOUT;
                }
            }
            //如果没有约定timeout时间，默认定长20s(Tomcat默认值)
            return DEFAULT_TIMEOUT;
        };
    }
}
