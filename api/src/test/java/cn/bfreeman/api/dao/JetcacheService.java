package cn.bfreeman.api.dao;

import cn.bfreeman.core.config.cache.JetCacheName;
import cn.bfreeman.core.domain.entity.UserDO;
import cn.bfreeman.core.domain.enums.SexType;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Service;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Service
public class JetcacheService {


    @Cached(
            name = "UserTest",
            cacheType = CacheType.LOCAL,
            expire = 60,
            localLimit = 100000
    )
    public UserDO getUserFromDb() throws InterruptedException {
        UserDO userDO = new UserDO();
        userDO.setName("LHR");
        userDO.setSexType(SexType.MALE);
        Thread.sleep(2000);
        return userDO;
    }
}
