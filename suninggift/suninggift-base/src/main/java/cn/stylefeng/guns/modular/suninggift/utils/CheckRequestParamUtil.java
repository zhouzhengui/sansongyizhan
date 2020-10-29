package cn.stylefeng.guns.modular.suninggift.utils;


import cn.stylefeng.guns.modular.suninggift.model.CommonReqParam;

public class CheckRequestParamUtil {

	public static String checkCommonRequestParam(CommonReqParam commonReqParam) {
		
		if(commonReqParam == null) {
			return "公共参数获取实体类为空";
		}
		
		if(CommonUtil.isEmpty(commonReqParam.getOutSysNo())) {
			return "外部接入商户编码为空";
		}
		
		if(CommonUtil.isEmpty(commonReqParam.getRequestMethod())) {
			return "接口请求方法为空";
		}
		
		return "";
		
	}
	
}
