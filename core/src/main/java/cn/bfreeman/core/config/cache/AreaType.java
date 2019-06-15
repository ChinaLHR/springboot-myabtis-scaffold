package cn.bfreeman.core.config.cache;

import com.alicp.jetcache.anno.CacheConsts;

public interface AreaType {

    /**
     * 远程一级缓存remote
     */
    String REMOTE = "remote";
    /**
     * 默认缓存area. 两级缓存 这里是local + remote
     */
    String DEFAULT = CacheConsts.DEFAULT_AREA;

}
