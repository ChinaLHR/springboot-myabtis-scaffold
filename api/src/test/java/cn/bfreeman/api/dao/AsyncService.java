package cn.bfreeman.api.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Service
@Slf4j
public class AsyncService {

    @Async
    public void doDelayPrintf() throws InterruptedException {
      log.info("get random num:{}", RandomUtils.nextInt(1,100));
      Thread.sleep(5000);
    }
}
