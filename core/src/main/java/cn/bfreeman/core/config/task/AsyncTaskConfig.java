package cn.bfreeman.core.config.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lhr
 * @date 2019/6/14
 * <p>
 * Spring Async 异步任务配置
 */
@Slf4j
@Component
public class AsyncTaskConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
        // 核心线程池线程个数， 线程使用完后使用队列
        threadPoolTaskExecutor.setCorePoolSize(100);
        // 最大线程池线程个数， 队列使用完后使用 maxPoolSize - corePoolSize 剩下的队列
        // 队列默认值是Integer.MAX_VALUE。 所以maxPoolSize 应该不会触发
        threadPoolTaskExecutor.setMaxPoolSize(100);
        // 耗时的异步任务均以干掉。 异步任务触发reject时走调用方的线程。 按理这个逻辑应该永远不会触发
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                // 打印线程池
                String msg = String
                        .format("RejectedExecutionHandler,异步线程池执行状态"
                                        + " Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s, queueSize:%s)!",
                                e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(),
                                e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(),
                                e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(),
                                e.isTerminating(), e.getQueue().size());
                log.error(msg);
                super.rejectedExecution(r, e);
            }
        });
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.afterPropertiesSet();


        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
