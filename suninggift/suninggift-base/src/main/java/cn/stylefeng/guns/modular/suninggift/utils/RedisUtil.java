//package cn.stylefeng.guns.modular.suninggift.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import net.logstash.logback.encoder.org.apache.commons.lang.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisConnectionUtils;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * @ClassNameRedisUtil
// * @Description TODO
// * @Author tangxiong
// * @Date 2019/10/18 17:43
// **/
//@Component
//public class RedisUtil {
//    private static final long timeout = 1;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    /**
//     * 指定缓存失效时间
//     * @param key 键
//     * @param time 时间(秒)
//     * @return
//     */
//
//    public boolean expire(String key,long time){
//        try {
//            if(time>0){
//                redisTemplate.expire(key, time, TimeUnit.SECONDS);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 判断key是否存在
//     * @param key 键
//     * @return true 存在 false不存在
//     */
//    public boolean hasKey(String key){
//        try {
//            return redisTemplate.hasKey(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }finally {
//            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
//        }
//    }
//
//    /**
//     * 删除缓存
//     * @param key 可以传一个值 或多个
//     */
//    @SuppressWarnings("unchecked")
//    public void del(String ... key){
//        if(key!=null&&key.length>0){
//            if(key.length==1){
//                redisTemplate.delete(key[0]);
//            }else{
//                redisTemplate.delete(CollectionUtils.arrayToList(key));
//            }
//        }
//    }
//
//    /**
//     * 删除缓存
//     * @param list
//     */
//    @SuppressWarnings("unchecked")
//    public void del(List<String> list){
//        redisTemplate.delete(list);
//    }
//
//    /**
//     * 普通缓存获取
//     * @param key 键
//     * @return 值
//     */
//    public Object get(String key){
//        return key==null?null:redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 普通缓存放入
//     * @param key 键
//     * @param value 值
//     * @return true成功 false失败
//     */
//    public boolean set(String key,Object value) {
//        try {
//            redisTemplate.opsForValue().set(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    /**
//     * 普通缓存放入并设置时间
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
//     * @return true成功 false 失败
//     */
//    public boolean set(String key,Object value,long time){
//        try {
//            if(time>0){
//                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
//            }else{
//                set(key, value);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * @Author
//     * @Description //以json格式保存value
//     * @Date
//     * @Param ${param}
//     * @return ${return}
//     **/
//    public <T> void saveCache(String key,T t){
//        String json = JSONObject.toJSONString(t);
//        try {
//            if(StringUtils.isEmpty(json) || "null".equals(json)) {
//            }else {
//                redisTemplate.opsForValue().set(key, json, timeout, TimeUnit.MINUTES);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }finally {
//            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
//        }
//    }
//
//    /**
//     * @Author
//     * @Description //返回泛型value数据
//     * @Date
//     * @Param ${param}
//     * @return ${return}
//     **/
//    public <T> T getCache(String key,Class<T> clazz){
//        JSONObject json = new JSONObject();
//        if(StringUtils.isEmpty(key)) {
//            return null;
//        }
//
//        String value = "";
//        try {
//            Object obj = redisTemplate.opsForValue().get(key);
//            value = ObjectUtils.toString(obj);
//            if(StringUtils.isEmpty(value)) {
//                return null;
//            }else {
//                value = obj.toString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }finally {
//            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
//        }
//
//        return json.parseObject(value, clazz);
//    }
//
//
//    public void deleteByPrex(String prex) {
//        Set<String> keys = redisTemplate.keys(prex+"*");
//        if (keys != null || keys.size() > 0) {
//            redisTemplate.delete(keys);
//        }
//    }
//
//}
