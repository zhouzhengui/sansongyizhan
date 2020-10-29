package cn.stylefeng.guns.modular.suninggift.component.validatorhandler.impl;

import cn.stylefeng.guns.modular.suninggift.component.basevalidator.UnicomMallValidatorFacade;
import cn.stylefeng.guns.modular.suninggift.component.validatorhandler.UnicomMallValidatorTemplate;
import cn.stylefeng.guns.modular.suninggift.enums.EncryptAlgorithmEnum;
import cn.stylefeng.guns.modular.suninggift.enums.ResponseStatusEnum;
import cn.stylefeng.guns.modular.suninggift.enums.UnicomMallOutInterfaceMethodEnum;
import cn.stylefeng.guns.modular.suninggift.model.MessagerVo;
import cn.stylefeng.guns.modular.suninggift.model.UnicomOutBizRequest;
import cn.stylefeng.guns.modular.suninggift.utils.CommonUtil;
import cn.stylefeng.guns.modular.suninggift.utils.SignUtil;
import cn.stylefeng.guns.sys.modular.consts.service.SysConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SuNingUnicomMallMD5ValidatorHandlerV1Impl extends UnicomMallValidatorTemplate {
	private static final String charsetCarrier = "UTF-8";
	private static final String signTypeCarrier = "MD5";

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private UnicomMallValidatorFacade unicomMallValidatorFacade;

	@Override
	public boolean isValidateSign() {
		return true;
	}

	@Override
	public MessagerVo validateSign(UnicomOutBizRequest outBizRequest) {
		MessagerVo messagerVo = new MessagerVo();
		
		//判断outBizRequest是否为空
		if(CommonUtil.isEmpty(outBizRequest.toString())) {
			messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR.getCode());
			messagerVo.setSubMsg("验签内容为空");
			return messagerVo;
		}

		//获取商户接入编码(out_sys_no)
		String outSysNo = outBizRequest.getApp_key();
		if(CommonUtil.isEmpty(outSysNo)) {
			messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_OUT_SYS_NO.getCode());
			messagerVo.setSubMsg("验签接入编码(app_key)为空");
			return messagerVo;
		}
		
		//获取MD5密钥
		String md5Key = sysConfigService.getByCode("su_ning_md5_key");
		if(CommonUtil.isEmpty(md5Key)) {
			messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_CHECK_SIGN.getCode());
			messagerVo.setSubMsg("商户的MD5公钥为空");
			return messagerVo;
		}
		
		//解签加签的内容
		String signMethod = CommonUtil.isEmpty(outBizRequest.getSign_method()) ? signTypeCarrier : outBizRequest.getSign_method();

		//请求加签
		String signReq = outBizRequest.getSign();
		
		//封装加签
		Map<String ,String> params = JSONObject.parseObject(JSON.toJSONString(outBizRequest) ,Map.class);
		try {
			params.remove("sign");
			String sign =  SignUtil.signTopRequest(params, md5Key , signMethod);
			log.info("==加签==" + sign);
			
			if(signReq.equals(sign)) {
				messagerVo.setSubCode(ResponseStatusEnum.SUCCESS.getCode());
				messagerVo.setSubMsg(ResponseStatusEnum.SUCCESS.getMsg());
				messagerVo.setData(outBizRequest);
				return messagerVo;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			log.error("解签出现异常",e);
		}
		
		messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR_CHECK_SIGN.getCode());
		messagerVo.setSubMsg("请求内容验签不通过");
		return messagerVo;
	}

	@Override
	public boolean isDecrypt() {
		return false;
	}

	@Override
	public MessagerVo decrypt(UnicomOutBizRequest outBizRequest) {
		
		return null;
	}

	@Override
	public String decryptType() {
		return EncryptAlgorithmEnum.MD5.getAlgorithmName();
	}

	@Override
	public MessagerVo validator(UnicomOutBizRequest outBizRequest) {
		String method = outBizRequest.getMethod();
		
		UnicomMallOutInterfaceMethodEnum unicomMallInterfaceMethod = UnicomMallOutInterfaceMethodEnum.getUnicomMallInterfaceMethod(method);
		if(unicomMallInterfaceMethod != null) {
			return unicomMallValidatorFacade.validateUnicomCardHandle(outBizRequest);
		}
		
		MessagerVo messagerVo = new MessagerVo();
		messagerVo.setSubCode(ResponseStatusEnum.CONTROLLER_ERROR.getCode());
		messagerVo.setSubMsg("验证参数未找到");
		return messagerVo;
	}

}
