package cn.stylefeng.guns.modular.suninggift.annotation;

import cn.stylefeng.guns.modular.suninggift.enums.OutApiMethodAndClassEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.model.InBizRequest;
import cn.stylefeng.guns.modular.suninggift.model.InBizRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DetectMethodAnnotation implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    //存储类-方法
    private HashMap<String, List<MethodMapping>> classMethodMap = new HashMap<>();

    /**
     * 初始化容器后解析所有包含MethodHandler注解的类中包含Name注解的方法
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取包含注解MethodHandler的类
        Map<String, Object> methodHandlerMap = applicationContext.getBeansWithAnnotation(MethodHandler.class);
        methodHandlerMap.forEach((k, v) -> {
            Class<?> clazz = v.getClass();
            Method[] methods = clazz.getDeclaredMethods();//获取所有的方法
            List<MethodMapping> methodMappings = new ArrayList<>();
            for (Method method : methods) {
                //只解析@Name注解的，并且返回值为InBizRespond的方法，方便对结果进行解析
                if (method.isAnnotationPresent(Name.class) && (method.getReturnType() == InBizRespond.class)) {
                    Name nameAnnotation = method.getAnnotation(Name.class);
                    methodMappings.add(new MethodMapping(nameAnnotation.value(), method));
                }
            }
            if (!methodMappings.isEmpty()) {
                classMethodMap.put(clazz.getName(), methodMappings);
            }
        });
    }

    /**
     * 执行
     *
     * @param request
     * @return
     */
    public InBizRespond execute(InBizRequest request) throws Exception {
        if (!classMethodMap.containsKey(this.getClass().getName())) {
            log.info("类[" + this.getClass().getName() + "]未使用注解@MethodHandler注册或未发现任何使用@Name注解的非继承方法");
            return new InBizRespond(ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getCode(), ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getMsg());
        }

        List<MethodMapping> methodMappings = classMethodMap.get(this.getClass().getName());
        for (MethodMapping methodMapping : methodMappings) {
            OutApiMethodAndClassEnum[] names = methodMapping.names;
            for(OutApiMethodAndClassEnum name : names){
                if (name.getMethod().equals(request.getMethod())) {
                    return (InBizRespond) methodMapping.method.invoke(this, request);
                }
            }
        }
        return new InBizRespond(ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getCode(), ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getMsg());

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}