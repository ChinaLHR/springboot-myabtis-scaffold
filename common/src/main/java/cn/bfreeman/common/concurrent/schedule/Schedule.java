package cn.bfreeman.common.concurrent.schedule;

import java.util.concurrent.TimeUnit;

/**
 * @Author : lhr
 * @Date : 18:14 2019/5/31
 */
public interface Schedule {

    Schedule setPoolTag(String poolTag);

    Schedule init(int corePoolSize);

    void shutdown();

    void schedule(Runnable runnable, long initialDelay, Long period, TimeUnit timeUnit);
}
