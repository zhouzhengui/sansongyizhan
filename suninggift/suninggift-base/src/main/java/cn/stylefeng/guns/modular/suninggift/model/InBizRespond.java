package cn.stylefeng.guns.modular.suninggift.model;

import lombok.Data;

import java.io.Serializable;

/**
 * （对接外部支付工程）
 * 响应参数封装类
 */
@Data
public class InBizRespond<T> implements Serializable {

	private String flowNo; 

	private String time; // 应答时间 yyyyMMddHH24miss 格式 

	private String sign; // 商户请求签名
	
	private String entrance;
	
	private String code;
	
	private String message;
	
	private String bizContent;

    private T t;

    public InBizRespond(String code, String message){
    	this.code = code;
    	this.message = message;
	}

	public InBizRespond(){}
}
