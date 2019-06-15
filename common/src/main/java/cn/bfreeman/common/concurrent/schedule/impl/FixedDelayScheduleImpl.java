package cn.bfreeman.common.concurrent.schedule.impl;

import cn.bfreeman.common.concurrent.schedule.Schedule;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author : lhr
 * @Date : 17:49 2018/11/10
 */
@Slf4j
public class FixedDelayScheduleImpl implements Schedule {
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
                .namingPattern(poolTag + "_fixed_delay_schedule-%d").daemon(true)
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
        //固定延迟调度
        executorService.scheduleWithFixedDelay(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("Delay调度任务执行异常", e);
            }
        }, initialDelay, period, timeUnit);
    }
}
