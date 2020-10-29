
/**  
  * Project Name:gzlp_uniline  
  * File Name:CheckUserPhoneAO.java  
  * Package Name:com.gzlplink.base.model.aom  
  * Date:2020年2月19日下午5:13:27  
  * Copyright (c) 2020, chenzhou1025@126.com All Rights Reserved.  
  *  
  */  
      
package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**  
  * ClassName:CheckUserPhoneAO <br/>  
  * Function: 三户校验实体类 <br/>  
  * Date:     2020年2月19日 下午5:13:27 <br/>  
  * @author   彭齐文  
  * @version    
  * @since    JDK 1.8  
  * @see        
  */
@Data
@Component
public class CheckUserPhoneAo implements Serializable{

	
	/**  
	  * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	  * @since JDK 1.8  
	  */
	private static final long serialVersionUID = 9197424132781319456L;

	/**
	 * 用户手机号码
	 */
	private String userPhone;

	/**
	 * 号码归属运营商
	 */
	private String operator;

	/**
	 * 接入商户编码（POT系统）
	 */
	private String clientId;

	/**
	 * 业务类型：
	 * 新用户：NEWUSER
	 * 存量升档：STOCKUSER
	 */
	private String orderType = "STOCKUSER";




}
  
