package cn.stylefeng.guns.modular.suninggift.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * 外部接口请求参数封装类
 */
@Data
public class UnicomOutBizRequest implements Serializable{
	
	private static final long serialVersionUID = 1229141308158035034L;

	/**
	 * 
	 */
	private String app_key;
	
	/**
	 * 格式
	 */
	private String format;
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 签名方法
	 */
	private String sign_method;
	
	/**
	 * 目标sppkey
	 */
	private String target_appkey;
	
	/**
	 * 请求时间
	 */
	private String timestamp;
	
	/**
	 * 调用方法
	 */
	private String method;
	
	/**
	 * 请求实体
	 */
	private Object data;

}
