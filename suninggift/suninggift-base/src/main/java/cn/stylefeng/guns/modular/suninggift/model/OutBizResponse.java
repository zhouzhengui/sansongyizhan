package cn.stylefeng.guns.modular.suninggift.model;

import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 外部接口响应结果封装
 *
 */
public class OutBizResponse {
	
	private String code; //响应编码
	
	private String msg; //响应内容
	
	private String subCode; //报错编码
	
	private String subMsg; //报错内容
	
	private String signType; //签名算法类型RSA2/RSA
	
	private String sign; //商户响应签名
	
	@JsonIgnore
	private String success;
	
	@JsonIgnore
	private String fail;
	
	private JSONObject bizContent;
	
	private JSONObject outContent;
	
	public OutBizResponse() {
		super();
	}
	
	public OutBizResponse(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public OutBizResponse(String code, String msg, String subCode, String subMsg) {
		super();
		this.code = code;
		this.msg = msg;
		this.subCode = subCode;
		this.subMsg = subMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public JSONObject getBizContent() {
		return bizContent;
	}

	public void setBizContent(JSONObject bizContent) {
		this.bizContent = bizContent;
	}

	public JSONObject getOutContent() {
		return outContent;
	}

	public void setOutContent(JSONObject outContent) {
		this.outContent = outContent;
	}
	
	public boolean isSuccess(){
		ResponseStatusEnum responseStatusEnum = ResponseStatusEnum.byCode(code);
		return responseStatusEnum == ResponseStatusEnum.SUCCESS;
	}

}
