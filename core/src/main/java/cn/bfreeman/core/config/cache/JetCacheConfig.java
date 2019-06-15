package cn.bfreeman.core.config.cache;

import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.embedded.EmbeddedCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhr
 * @date 2019/6/15
 */
@Configuration
@EnableMethodCache(basePackages = "cn.bfreeman")
@EnableCreateCacheAnnotation
public class JetCacheConfig {

    @Bean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }

    @Bean
    public GlobalCacheConfig config(SpringConfigProvider configProvider) {
        // local缓存, 暂使用默认配置
        Map<String, CacheBuilder> localBuilders = new HashMap<>();
        EmbeddedCacheBuilder localBuilder = CaffeineCacheBuilder
                .createCaffeineCacheBuilder()
                .cachePenetrateProtect(true)
                .cacheNullValue(false)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE);
        localBuilders.put(AreaType.DEFAULT, localBuilder);

        // remote缓存, 暂使用默认配置
//        Map<String, CacheBuilder> remoteBuilders = new HashMap<>();
//        RedisCacheBuilder remoteCacheBuilder = RedisCacheBuilder.createRedisCacheBuilder()
//                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
//                .valueEncoder(JavaValueEncoder.INSTANCE)
//                .valueDecoder(JavaValueDecoder.INSTANCE)
//                .cachePenetrateProtect(true)
//                .cacheNullValue(false)
//                .jedisPool(pool);
//        remoteBuilders.put(AreaType.DEFAULT, remoteCacheBuilder);
//        remoteBuilders.put(AreaType.REMOTE, remoteCacheBuilder);

        // 多级缓存
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setConfigProvider(configProvider);
        globalCacheConfig.setLocalCacheBuilders(localBuilders);
//        globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
        // 统计间隔
        globalCacheConfig.setStatIntervalMinutes(15);

        /*
         * 在缓存key里面不包含area信息。
         * 这样就可以支持相同name的cache在不同的area里面可以保持key名统一
         * 借助该机制就可以实现不同的area下相同name的cache，如果使用同样的数据源, 可以保证数据一致性
         */
        globalCacheConfig.setAreaInCacheName(false);

        return globalCacheConfig;
    }

    /**
     * Redis 连接池配置
     */
//    @Resource
//    @Bean
//    public Pool<Jedis> pool(RedisCacheProperties redisCacheProperties) {
//        GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
//        pc.setMinIdle(redisCacheProperties.getMinIdle());
//        pc.setMaxIdle(redisCacheProperties.getMaxIdle());
//        pc.setMaxTotal(redisCacheProperties.getMaxTotal());
//        return new JedisPool(pc, redisCacheProperties.getHost(),
//                redisCacheProperties.getPort(),
//                redisCacheProperties.getTimeout(),
//                redisCacheProperties.getPassword(),
//                redisCacheProperties.getDatabase()
//        );
//    }
}
