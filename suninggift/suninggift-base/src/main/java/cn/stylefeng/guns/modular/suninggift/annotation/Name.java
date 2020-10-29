package cn.stylefeng.guns.modular.suninggift.annotation;

import cn.stylefeng.guns.modular.suninggift.enums.OutApiMethodAndClassEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    OutApiMethodAndClassEnum[] value() default {};
//    String[] produces() default {};
}
