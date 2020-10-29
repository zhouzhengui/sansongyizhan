package cn.stylefeng.guns.modular.suninggift.annotation;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextService implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public <T> Map<String, T> getAllImplementClassByInterfaceName(Class<T> clazz){
		return applicationContext.getBeansOfType(clazz);
	}
	
	public Object getBean(String name){
		return applicationContext.getBean(name);
	}
	
	public <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	public <T> T getBean(String name,Class<T> clazz){
		return applicationContext.getBean(name, clazz);
	}

	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz){
        return applicationContext.getBeansWithAnnotation(clazz);
   }
}
