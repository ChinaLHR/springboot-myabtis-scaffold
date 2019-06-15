package cn.bfreeman.common.concurrent.schedule.impl;

import cn.bfreeman.common.concurrent.schedule.Schedule;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author xiang.rao created on 7/10/18 10:13 AM
 * @version $Id$
 */
@Slf4j
public class FixedRateScheduleImpl implements Schedule {

    private ScheduledExecutorService executorService;

    private String poolTag;

    @Override
    public Schedule setPoolTag(String poolTag) {
        this.poolTag = poolTag;
        return this;
    }

    @Override
    public Schedule init(int corePoolSize) {
        Preconditions.checkNotNull(poolTag, "poolTag不能为空");
        executorService = new ScheduledThreadPoolExecutor(corePoolSize, new BasicThreadFactory.Builder()
                .namingPattern(poolTag + "_fixed_rate_schedule-%d").daemon(true)
                .build());
        return this;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public void schedule(Runnable runnable, long initialDelay, Long period, TimeUnit timeUnit) {
        Preconditions.checkNotNull(executorService, "调度线程池为空");
        //严格时间调度
        executorService.scheduleAtFixedRate(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("Rate调度任务执行异常", e);
            }
        }, initialDelay, period, timeUnit);
    }
}
