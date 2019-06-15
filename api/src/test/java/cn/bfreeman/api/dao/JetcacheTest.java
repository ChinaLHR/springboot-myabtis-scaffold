package cn.bfreeman.api.dao;

import cn.bfreeman.api.ApiApplication;
import cn.bfreeman.core.domain.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class JetcacheTest {

    @Resource
    private JetcacheService jetcacheService;

    @Test
    public void userCacheTest() throws InterruptedException {
        log.info("startTime:{}",System.currentTimeMillis());
        UserDO userFromDb = jetcacheService.getUserFromDb();
        log.info("get From Db:{}",System.currentTimeMillis());
        UserDO userFromDb1 = jetcacheService.getUserFromDb();
        log.info("get From Cache:{}",System.currentTimeMillis());
    }
}
