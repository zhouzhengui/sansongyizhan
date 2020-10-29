package cn.stylefeng.guns.modular.suninggift.model;

import lombok.Data;

/**
 * （对接外部支付工程）
 * 内部接口请求参数封装类
 */
@Data
public class OutBizRequest {

	private String flowNo;//请求流水

	private String method; //方法
	
	private String signType; // 签名算法类型

	private String sign; // 商户请求签名

	private String version; //版本号

}
