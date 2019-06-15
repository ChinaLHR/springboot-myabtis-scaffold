package cn.bfreeman.common.retry;

import cn.bfreeman.common.base.Func;
import cn.bfreeman.common.exception.FatalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 同步执行重试
 *
 * @author xiang.rao created on 11/5/18 5:04 PM
 * @version $Id$
 */
@Slf4j
@Component
public class SyncRetry<T> {

    /**
     * 执行一段代码, 失败后同步重试指定次数
     *
     * @param func       执行动作
     * @param retryCount 重试次数
     */
    public void execute(Func func, int retryCount) {
        Exception finalException = null;
        for (int i = 0; i < retryCount; i++) {
            try {
                func.apply();
                return;
            } catch (Exception e) {
                log.warn("第 {} 次执行失败", i + 1);
                finalException = e;
            }
        }
        throw new FatalException("同步重试%s次后, 执行失败", finalException, retryCount);
    }

    public T execute(Supplier<T> supplier, int retryCount){
        Exception finalException = null;
        for (int i = 0; i < retryCount; i++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.warn("第 {} 次执行失败", i + 1);
                finalException = e;
            }
        }
        throw new FatalException("同步重试%s次后, 执行失败", finalException, retryCount);
    }

    /**
     * 默认重试次数
     */
    private static final int RETRY_COUNT_DEFAULT = 3;

    /**
     * 按照默认重试次数执行
     *
     * @param func 执行动作
     */
    public void execute(Func func) {
        execute(func, RETRY_COUNT_DEFAULT);
    }

    /**
     * 按照默认重试次数执行
     *
     * @param supplier
     * @return
     */
    public T execute(Supplier<T> supplier){
       return execute(supplier,RETRY_COUNT_DEFAULT);
    }
}
