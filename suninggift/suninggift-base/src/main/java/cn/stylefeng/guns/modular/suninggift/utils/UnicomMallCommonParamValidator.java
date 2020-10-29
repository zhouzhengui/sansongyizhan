package cn.stylefeng.guns.modular.suninggift.utils;

import cn.stylefeng.guns.modular.suninggift.enums.EncryptAlgorithmEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.enums.UnicomMallOutInterfaceMethodEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

/**
 * 公共参数校验类
 */
public class UnicomMallCommonParamValidator {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 校验公共请求参数
	 * @param jsonBody
	 * @return
	 */
	public static MessagerVo validateCommonParam(String jsonBody) {
		MessagerVo messagerVo = new MessagerVo();
		
		if (CommonUtil.isEmpty(jsonBody)) {
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_REQUESTPARAM.getCode());
            messagerVo.setSubMsg("请求参数为空");
            return messagerVo;
        }

        UnicomOutBizRequest outBizRequest = JSON.parseObject(jsonBody, UnicomOutBizRequest.class);
        if (CommonUtil.isEmpty(outBizRequest.getApp_key())) {
            messagerVo.setSubCode("20002");
            messagerVo.setSubMsg("请求参数app_key为空");
            return messagerVo;
        }

        //校验方法是否合法
        String resultMsg = validateMethod(outBizRequest.getMethod());
        if (!CommonUtil.isEmpty(resultMsg)) {
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_METHOD.getCode());
            messagerVo.setSubMsg(resultMsg);
            return messagerVo;
        }

        //校验请求时间是否在合法范围之内
        resultMsg = validateTimestamp(outBizRequest.getTimestamp());
        if (!CommonUtil.isEmpty(resultMsg)) {
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_TIME.getCode());
            messagerVo.setSubMsg(resultMsg);
            return messagerVo;
        }

        /**
         * 签名
         */
        if (CommonUtil.isEmpty(outBizRequest.getSign())) {
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_SIGN.getCode());
            messagerVo.setSubMsg("请求签名sign不能为空");
            return messagerVo;
        }

        //校验算法
        resultMsg = validateSign(outBizRequest.getSign_method());
        if (!CommonUtil.isEmpty(resultMsg)) {
            messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_SIGN_TYPE.getCode());
            messagerVo.setSubMsg(resultMsg);
            return messagerVo;
        }

        return null;
		
	}

	/**
	 * 校验算法
	 * @param sign_method
	 * @return
	 */
	private static String validateSign(String sign_method) {
		if (CommonUtil.isEmpty(sign_method)) {
            return "签名算法不能为空";
        }

        EncryptAlgorithmEnum encryptAlgorithmEnum = EncryptAlgorithmEnum.getEncryptAlgorithmEnum(sign_method);
        if (encryptAlgorithmEnum == null) {
            return "签名算法不正确";
        }

        return null;
	}

	/**
	 * 校验请求时间是否在合法范围之内
	 * @param timestamp
	 * @return
	 */
	private static String validateTimestamp(String timestamp) {
		if (CommonUtil.isEmpty(timestamp)) {
            return "请求时间不能为空";
        }

        try {
            Date requestTime = sdf.parse(timestamp);
            if (System.currentTimeMillis() - requestTime.getTime() > 30L * 60 * 1000) {
                return "请求时间已过期";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "请求时间格式不正确";
        }

        return null;
	}

	/**
	 * 校验方法是否合法
	 * @param method
	 * @return
	 */
	private static String validateMethod(String method) {
		if (CommonUtil.isEmpty(method)) {
            return "接口名称不能为空";
        }

        UnicomMallOutInterfaceMethodEnum unicomMallInterfaceMethod = UnicomMallOutInterfaceMethodEnum
            .getUnicomMallInterfaceMethod(method);

        if (unicomMallInterfaceMethod == null) {
            return "接口名称不存在";
        }

        return null;
	}
	
	

}
