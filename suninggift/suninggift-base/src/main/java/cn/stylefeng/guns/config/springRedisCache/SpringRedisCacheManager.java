package cn.stylefeng.guns.config.springRedisCache;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Administrator on 2017/10/18.
 * 新写了一个SpringRedisCacheManager，继承自RedisCacheManager，
 * 用于对@CacheDuration解析及有效期的设置
 */
@EnableCaching
@Configuration
@Data
public class SpringRedisCacheManager implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate redisTemplate;

    private Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    @Bean
    public RedisCacheWriter writer() {
        return RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
    }

    @Value("${spring.redis.defaultExpiration}")
    private Long defaultExpiration;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void afterPropertiesSet() {
        parseCacheDuration(applicationContext);
    }
    private void parseCacheDuration(ApplicationContext applicationContext) {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getType(beanName);
            Service service = findAnnotation(clazz, Service.class);
            Component component = findAnnotation(clazz, Component.class);
            if (null == service && null == component) {
                continue;
            }
            addCacheExpires(clazz, cacheConfigurations);
        }
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
                .initialCacheNames(cacheConfigurations.keySet())
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    private void addCacheExpires(final Class clazz, final Map<String, RedisCacheConfiguration> cacheConfigurations) {
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(method);
                CacheDuration cacheDuration = findCacheDuration(clazz, method);
                Cacheable cacheable = findAnnotation(method, Cacheable.class);
                CacheConfig cacheConfig = findAnnotation(clazz, CacheConfig.class);
                Set<String> cacheNames = findCacheNames(cacheConfig, cacheable);
                for (String cacheName : cacheNames) {
                    if(cacheDuration != null){
                        cacheConfigurations.put(cacheName, RedisCacheConfiguration.defaultCacheConfig()
//                                .disableCachingNullValues()
                                .entryTtl(Duration.ofSeconds(cacheDuration.duration()))
                                .prefixKeysWith(cacheName));
                    }else{
                        cacheConfigurations.put(cacheName, RedisCacheConfiguration.defaultCacheConfig()
//                                .disableCachingNullValues()
                                .entryTtl(Duration.ofSeconds(defaultExpiration))
                                .prefixKeysWith(cacheName));
                    }
                }
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return null != findAnnotation(method, Cacheable.class);
            }
        });
    }
    /**
     * CacheDuration标注的有效期，优先使用方法上标注的有效期
     * @param clazz
     * @param method
     * @return
     */
    private CacheDuration findCacheDuration(Class clazz, Method method) {
        CacheDuration methodCacheDuration = findAnnotation(method, CacheDuration.class);
        if (null != methodCacheDuration) {
            return methodCacheDuration;
        }
        CacheDuration classCacheDuration = findAnnotation(clazz, CacheDuration.class);
        if (null != classCacheDuration) {
            return classCacheDuration;
        }
//        throw new IllegalStateException("No CacheDuration config on Class " + clazz.getName() + " and method " + method.toString());
        return null;
    }
    private Set<String> findCacheNames(CacheConfig cacheConfig, Cacheable cacheable) {
        return isEmpty(cacheable.value()) ?
                newHashSet(cacheConfig.cacheNames()) : newHashSet(cacheable.value());
    }
}
