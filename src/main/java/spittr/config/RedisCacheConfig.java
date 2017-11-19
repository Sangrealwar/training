package spittr.config;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 功能：Redis缓存
 * 条件：
 * Created by wq on 2017/3/8.
 */
//@Configuration
//@EnableCaching   //启用Cache 相当于 <cache:annotatio   n-driven/>
public class RedisCacheConfig{

    @Bean
    public RedisConnectionFactory redisConnectionFactory()
    {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName("127.0.0.1");
        redisConnectionFactory.setPort(6379);
//        jedisConnFactory.setUsePool(true);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * redis 的缓存Manager
     * @param redisTemplate
     * @return
     */
//    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(3000); // Sets the default expire time (in seconds)
        return cacheManager;
    }
}
