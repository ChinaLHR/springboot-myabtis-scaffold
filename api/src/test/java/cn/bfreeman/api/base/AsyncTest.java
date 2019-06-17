package cn.bfreeman.api.base;

import cn.bfreeman.api.ApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author lhr
 * @date 2019/6/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class AsyncTest {

    @Resource
    private AsyncService asyncService;

    @Test
    public void testAsyncService() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            asyncService.doDelayPrintf();
        }
    }

}
