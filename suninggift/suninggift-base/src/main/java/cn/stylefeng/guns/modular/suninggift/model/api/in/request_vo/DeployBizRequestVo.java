package cn.stylefeng.guns.modular.suninggift.model.api.in.request_vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 
 * 调拨异步通知接口请求体
 */
@Data
public class DeployBizRequestVo {

	private String method; // 接口名称

	private String format; // JSON

	private String charset; // 编码格式 UTF-8 GBK

	private String timestamp; // 请求时间，格式"yyyy-MM-dd HH:mm:ss"

	private String version; // 调用有接口版本 1.0

	private String signType; // 签名算法类型RSA2 RSA

	private String sign; // 商户请求签名

	private JSONObject bizContent;

}
