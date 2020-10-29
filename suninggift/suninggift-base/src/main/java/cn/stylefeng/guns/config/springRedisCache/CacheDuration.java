package cn.stylefeng.guns.config.springRedisCache;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据cacheNames(或value)分组设置缓存有效时间
 * @Cacheable(value = "cacheName")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheDuration {
    //Sets the expire time (in seconds).
    public long duration() default 1800;
}