package cn.stylefeng.guns.modular.suninggift.annotation;

import cn.stylefeng.guns.modular.suninggift.enums.OutApiMethodAndClassEnum;

import java.lang.reflect.Method;

public class MethodMapping {
    //方法注解对应的名字
    public OutApiMethodAndClassEnum[] names;

    //具体的执行方法
    public Method method;
    public MethodMapping(OutApiMethodAndClassEnum[] names, Method method) {
        this.names = names;
        this.method = method;
    }
}