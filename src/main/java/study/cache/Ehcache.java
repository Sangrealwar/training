package study.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.net.URI;

/**
 * 名称：Ehcache官网例子
 * 功能：
 * 条件：
 * Created by wq on 2017/11/19.
 */
public class Ehcache {

    /**
     * 内存级简单的Ehcache使用
     */
    private static void simple() {
        //初始化自带一个缓存
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();

        cacheManager.init();

        //为了类型安全，传入了Key和Value的类型
        Cache<Long, String> precConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)));

        myCache.put(1L, "缓存一个");

        String value = myCache.get(1L);

        cacheManager.removeCache("preConfigured");

        cacheManager.close();
    }

    private static void useTerracotte() {
        CacheManagerBuilder<PersistentCacheManager> clusteredCacheManagerBuilder =
                CacheManagerBuilder.newCacheManagerBuilder()
                        .with(ClusteringServiceConfigurationBuilder.cluster(URI.create("terracotta://localhost:9510/my-application"))
                                .autoCreate());
        PersistentCacheManager cacheManager = clusteredCacheManagerBuilder.build(true);

        cacheManager.close();
    }

    public static void main(String[] args) {
        useTerracotte();
    }
}
