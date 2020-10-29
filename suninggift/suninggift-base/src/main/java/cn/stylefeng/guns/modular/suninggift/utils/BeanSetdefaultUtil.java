/**  
  * Project Name:cmapi  
  * File Name:BeanSetdefaultUtil.java  
  * Package Name:com.gzlplink.cmapi.utils  
  * Date:2020年2月21日下午11:58:17  
  * Copyright (c) 2020, chenzhou1025@126.com All Rights Reserved.  
  *  
  */
package cn.stylefeng.guns.modular.suninggift.utils;

import cn.stylefeng.guns.modular.suninggift.model.api.out.request_vo.NotAllowSetDefaultAnnotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;


import cn.hutool.core.date.DateUtil;

/**  
  * ClassName:BeanSetdefaultUtil <br/>  
  * Function: 给实体属性为null的设置默认值. <br/>  
  * Date:     2020年2月21日 下午11:58:17 <br/>  
  * @author   彭齐文 
  * @version    
  * @since    JDK 1.8  
  * @see        
  */
public class BeanSetdefaultUtil {

	private final static String defaultStr = "0";
	private final static Date defaultDate = DateUtil.parse("1900-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
	private final static BigDecimal defaultDecimal = new BigDecimal(0);
	private final static Timestamp defaultTimestamp = new Timestamp(new Date().getTime());
	
	@SuppressWarnings("rawtypes")
	//赋默认值
	public static void setDefaultValue(Object object) {
		try {
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			//
			
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				Class fieldClass = field.getType();
				field.setAccessible(true); //设置访问权限
				if (!field.isAnnotationPresent(NotAllowSetDefaultAnnotation.class) && isFieldValueNull(fieldName, object)) {
					if (fieldClass == Integer.class) {
						field.set(object, defaultDecimal.intValue());
					} else if (fieldClass == Long.class) {
						field.set(object, defaultDecimal.longValue());
					} else if (fieldClass == Float.class) {
						field.set(object, defaultDecimal.doubleValue());
					} else if (fieldClass == BigDecimal.class) {
						field.set(object, defaultDecimal);
					} else if (fieldClass == Date.class) {
						field.set(object, defaultDate);
					} else if (fieldClass == String.class) {
						field.set(object, defaultStr); // 设置值
					} else if (fieldClass == Timestamp.class) {
						field.set(object, defaultTimestamp);
					}
				} else if (field.isAnnotationPresent(NotAllowSetDefaultAnnotation.class) && isStringFieldValueNull(fieldName, object, fieldClass)) {//MySQL，需要对对主键做特殊处理
					field.set(object, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	//判断字段是否为空
	private static boolean isFieldValueNull(String fieldName, Object object) throws ClassNotFoundException {
		boolean isNUll = false;
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = object.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(object, new Object[] {});
			if (value == null) {
				isNUll = true;
			}
			return isNUll;
		} catch (Exception e) {
			return isNUll;
		}
	}

	//判断主键是否为空值
	private static boolean isStringFieldValueNull(String fieldName, Object object, @SuppressWarnings("rawtypes") Class fieldClass)
			throws ClassNotFoundException {
		boolean isNUll = false;
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = object.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(object, new Object[] {});
			if (value == null) {
				isNUll = true;
			} else {
				if (fieldClass == String.class && StringUtils.isBlank((String) value)) {
					isNUll = true;
				}
			}
			return isNUll;
		} catch (Exception e) {
			return isNUll;
		}

	}
	
}
