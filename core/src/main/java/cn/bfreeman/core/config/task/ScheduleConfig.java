package cn.bfreeman.core.config.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Slf4j
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("ScheduleThread-");
        taskScheduler.setPoolSize(10);
        // 耗时的异步任务均以干掉。 异步任务触发reject时走调用方的线程。 按理这个逻辑应该永远不会触发
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                // 打印线程池
                String msg = String
                        .format("RejectedExecutionHandler,调度线程池执行状态"
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
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

}
