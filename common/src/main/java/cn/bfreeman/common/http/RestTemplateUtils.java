package cn.bfreeman.common.http;

import cn.bfreeman.common.concurrent.schedule.Schedule;
import cn.bfreeman.common.concurrent.schedule.impl.FixedRateScheduleImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author : lhr
 * @Date : 14:43 2018/9/29
 * <p>
 * Http请求
 */
public class RestTemplateUtils {

    private static RestTemplate commonRestRetryTemplate;

    private static Schedule schedule;

    private static List<HttpClientConnectionManager> connectionManagers = new ArrayList<>();

    /**
     * 获取通用请求的RestTemplate
     *
     * @return
     */
    public static RestTemplate getCommonRestRetryTemplate() {
        if (Objects.isNull(commonRestRetryTemplate)) {
            HttpComponentsClientHttpRequestFactory factory = getClientHttpRequestFactory(true);
            // 连接超时
            factory.setConnectTimeout(3000);
            // 数据读取超时时间，即SocketTimeout
            // (本地压测 用户平均请求等待时间：5.959/ms,测试环境Cat Avg(ms) 0.4,Cat Max(ms) 105.3 设置为100)
            factory.setReadTimeout(6000);
            // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
            factory.setConnectionRequestTimeout(300);
            commonRestRetryTemplate = new RestTemplate(factory);
        }
        return commonRestRetryTemplate;
    }

    /**
     * 注意:DefaultMaxPerRoute与MaxTotal一致，只能针对一个dns了使用，必须为不同的dns创建不同的client
     *
     * @param isRetry
     * @return
     */
    public static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(boolean isRetry) {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30000, TimeUnit.MILLISECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(2000);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(100);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，设置为2次，没有开启
        if (isRetry) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        } else {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        }
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(CustomConnectionKeepAliveStrategy.INSTANCE);

        HttpClient httpClient = httpClientBuilder.build();

        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        runIdleConnectionMonitor(pollingConnectionManager);
        return clientHttpRequestFactory;
    }

    private static final Long IDLE_INITIALDELAY = 1000L;
    private static final Long IDLE_PERIOD = 5000L;

    /**
     * 连接回收监控
     * 如果连接在服务器端关闭，则客户端连接无法检测到连接状态的变化（并通过关闭socket 来做出适当的反应）,
     * 需要使用定时监控清理实现关闭
     * 参考：https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html  2.5 Connection eviction policy
     *
     * @param clientConnectionManager
     */
    private static void runIdleConnectionMonitor(HttpClientConnectionManager clientConnectionManager) {
        connectionManagers.add(clientConnectionManager);
        if (Objects.isNull(schedule)) {
            schedule = new FixedRateScheduleImpl();
            schedule.setPoolTag("IdleConnectionMonitor");
            schedule.init(1);
            schedule.schedule(() -> {
                connectionManagers.stream().forEach(connect->{
                    //关闭过期的链接
                    connect.closeExpiredConnections();
                    //关闭闲置超过30s的链接
                    connect.closeIdleConnections(30, TimeUnit.SECONDS);
                });
            }, IDLE_INITIALDELAY, IDLE_PERIOD, TimeUnit.MILLISECONDS);
        }


    }
}
