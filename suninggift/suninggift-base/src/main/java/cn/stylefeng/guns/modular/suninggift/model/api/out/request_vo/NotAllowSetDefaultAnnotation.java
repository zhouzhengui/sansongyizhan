package cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标识实体类中某些字段不允许设置默认值
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAllowSetDefaultAnnotation {

}
