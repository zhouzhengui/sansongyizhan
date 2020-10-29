package cn.stylefeng.guns.modular.suninggift.enums;


/**
 * 
 *响应状态码
 */
public enum ResponseStatusEnum {
	
	//成功统一编码
	SUCCESS("10000", "SUCCESS"),
	SUCCESS_0000("0000", "升档成功"),
	SUCCESS_10001("10001", "入库成功,部分参数不全"),
	
	//公共模块控制层异常编码
	CONTROLLER_ERROR("20000", "控制器层报错信息"),
	CONTROLLER_ERROR_REQUESTPARAM("20001", "请求参数为空"),
	CONTROLLER_ERROR_OUT_SYS_NO("20002", "商户接入编码不能为空"),
	CONTROLLER_ERROR_METHOD("20003", "接口名称不存在"),
	CONTROLLER_ERROR_PARAMS("20004", "参数缺失"),
	CONTROLLER_ERROR_TIME("20005", "请求时间不能为空、请求时间已过期、请求时间格式不正确"),
	CONTROLLER_ERROR_VERSION("20006", "版本号不能为空、版本号不正确"),
	CONTROLLER_ERROR_SIGN("20007", "签名内容不能为空"),
	CONTROLLER_ERROR_CHECK_SIGN("sign-check-failure", "Illegal request"),
	CONTROLLER_ERROR_SIGN_TYPE("20009", "签名类型不能为空、签名算法不正确"),
	CONTROLLER_ERROR_DECRYPT("20010", "解密内容出现异常"),
	CONTROLLER_ERROR_BIZCONTENT("20011", "请求业务参数不能为空"),
	CONTROLLER_ERROR_NO_ALLOW_MODIFY("20012", "信息数据不能更改"),
	CONTROLLER_ERROR_STATUS("20013", "资质状态不能为空/状态非法"),
	CONTROLLER_ERROR_OPERATE("20014", "非法操作"),
	CONTROLLER_ERROR_ADD_SIGN("20015", "加签请求内容、加签响应内容为空"),
	CONTROLLER_ERROR_ADD_SIGN_ERROR("20016", "加签出现错误"),
	CONTROLLER_ERROR_PHONE("20017", "手机号码格式不正确"),
	CONTROLLER_ERROR_PARAM("20018", "非法请求参数"),
	CONTROLLER_ERROR_INVALID_VERIFY_CODE("20019", "无效验证码"),
	CONTROLLER_ERROR_VERIFY_CODE_SEND_HIGNER("20020", "验证码发送频率过快"),

	//检验省市区是否正确
	CONTROLLER_ERROR_PROVINCE_CITY_AREA("20100", "省、市、区校验出现异常"),
	CONTROLLER_ERROR_PROVINCE("20101", "省份编码和省份名称不对应/省份编码不正确/省份名称不正确"),
	CONTROLLER_ERROR_CITY("20102", "市或市编码存在,但省编码不存在/市编码和市名称不对应/市编码不正确/市名称不正确"),
	CONTROLLER_ERROR_AREA("20103", "区或区编码存在,但市编码不存在/区编码和区名称不对应/区编码不正确/区名称不正确"),
	
	//查询异常
	CONTROLLER_ERROR_PAGE_INDEX("20201", "页码必须大于0/页码必须大于0"),
	CONTROLLER_ERROR_PAGE_SIZE("20202", "每页大小不是一个整数/每页大小必须大于0/每页大小不是一个整数/每页大小不能超过100条"),
	
	
	//映射关系模块控制层异常编码
	CONTROLLER_ERROR_RELATION("24000", "映射关系控制器层报错信息"),
	
	//订单模块控制层异常编码
	CONTROLLER_ERROR_ORDER_OUT_TRADE_NO("25000", "支付订单号不能为空"),
	CONTROLLER_ERROR_ORDER_TRADE_NO("25001", "支付流水号不能为空"),
	CONTROLLER_ERROR_ORDER_TRADE_NO_EXIT("25002", "该订单号不存在"),

	//公共模块服务层异常编码
	SERVICE_ERROR("30000", "服务层报错信息"),
	SERVICE_ERROR_MERCHANT("30001", "获取商城信息数据异常"),
	SERVICE_ERROR_UNFREEZE("30002", "解冻操作失败"),
	SERVICE_ERROR_OPERATE("30004", "非法操作"),
	
	//商家服务层模块功能编码
	SERVICE_AGENCY_ONLY_ERROR("30101", "商家唯一性异常"),
	SERVICE_AGENCY_NULL_ERROR("30102", "查询数据为空"),
	
	//门店服务层模块功能编码
	SERVICE_STORE_ONLY_ERROR("30201", "门店唯一性异常"),
	SERVICE_STORE_NULL_ERROR("30202", "门店数据为空"),
	
	//店员服务层模块功能代码
	SERVICE_CLERK_PARAM_ERROR("30301", "参数异常"),
	SERVICE_CLERK_ONLY_ERROR("30302", "店员唯一性异常"),
	SERVICE_CLERK_NULL_ERROR("30303", "查询数据为空"),
	
	//店员映射关系服务层模块功能代码
	SERVICE_CLERK_RELATION_PARAM_ERROR("30401", "参数异常"),
	SERVICE_CLERK_RELATION_ONLY_ERROR("30402", "店员唯一性异常"),
	SERVICE_CLERK_RELATION_NULL_ERROR("30403", "查询数据为空"),
	
	RELATION_EXIST("31000", "关系映射已存在"),

	SERVICE_ERROR_IMAGE_FAIL("35001","文件上传失败!"),
	THREE_ACCOUNT_VERIFY_FAILD("31002","三户校验失败"),
	CLIENT_QUERY_EMPTY("31006","暂时没可用套餐，敬请期待"),
	SUBSCRIBE_FAIL("31007","订购失败"),
	//异常统一编码
	MANY_TIMES_ORDER("31008","重复申请业务"),

	EXCEPTION("40000", "处理出现异常,请联系管理员"),
	EXCEPTION_READ_TIMEOUT("40005", "接口处理超时");
	
	private String code;
	private String msg;
	
	private ResponseStatusEnum(String code, String msg){
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
	
	public static ResponseStatusEnum byCode(String code){
		ResponseStatusEnum[] responseStatusEnumArray = ResponseStatusEnum.values();
		for(ResponseStatusEnum responseStatusEnum : responseStatusEnumArray){
			if(responseStatusEnum.getCode().equals(code)){
				return responseStatusEnum;
			}
		}
		
		return null;
	}

}
