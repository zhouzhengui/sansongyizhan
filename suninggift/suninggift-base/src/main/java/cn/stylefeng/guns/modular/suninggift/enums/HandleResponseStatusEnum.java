package cn.stylefeng.guns.modular.suninggift.enums;


/**
 *BaseFundServiceImpl等的响应编码
 *响应状态码
 */
public enum HandleResponseStatusEnum {
	
	//成功统一编码
	SUCCESS("0000", "SUCCESS"),
	
	
	//异常统一编码
	EXCEPTION("9999", "处理出现异常");
	
	private String code;
	private String msg;
	
	private HandleResponseStatusEnum(String code, String msg){
		this.code = code;
		this.msg = msg;
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
	
	public static HandleResponseStatusEnum byCode(String code){
		HandleResponseStatusEnum[] responseStatusEnumArray = HandleResponseStatusEnum.values();
		for(HandleResponseStatusEnum responseStatusEnum : responseStatusEnumArray){
			if(responseStatusEnum.getCode().equals(code)){
				return responseStatusEnum;
			}
		}
		
		return null;
	}

}
