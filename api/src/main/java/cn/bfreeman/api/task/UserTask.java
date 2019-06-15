package cn.bfreeman.api.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Slf4j
@Component
public class UserTask {

    @Scheduled(cron="0/5 * *  * * ? ")
    public void printfUser(){
        log.info("user id:{}", RandomUtils.nextInt(1,100));
    }

}
